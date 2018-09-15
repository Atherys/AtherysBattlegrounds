package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.event.BattlepointEvent;
import com.atherys.battlegrounds.model.Battlepoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.TeamMember;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBar;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BattlepointService implements CatalogRegistryModule<Battlepoint> {

    private static final BattlepointService instance = new BattlepointService();

    private Map<Battlepoint,ServerBossBar> bossBars = new HashMap<>();

    private Task battlepointTask;

    private BattlepointService() {
        battlepointTask = Task.builder()
                .delay(1, TimeUnit.SECONDS)
                .interval(AtherysBattlegrounds.getConfig().BATTLEPOINTS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                .execute(this::battlepointsTick)
                .submit(AtherysBattlegrounds.getInstance());
    }

    public Collection<Entity> getEntitiesWithinInnerRadius(Battlepoint battlepoint) {
        return battlepoint.getLocation().getExtent().getNearbyEntities(battlepoint.getLocation().getPosition(), battlepoint.getInnerRadius());
    }

    public Team getControllingTeam(Battlepoint battlepoint) {
        Team highestProgressTeam = Team.NONE;

        for ( Map.Entry<Team,Float> entry : battlepoint.getAllTeamProgress().entrySet() ) {
            if ( entry.getValue() > battlepoint.getTeamProgress(highestProgressTeam) ) highestProgressTeam = entry.getKey();
        }

        return highestProgressTeam;
    }

    public List<Player> getPlayersWithinBattlepoint(Battlepoint battlepoint) {
        List<Player> players = new ArrayList<>();
        getEntitiesWithinInnerRadius(battlepoint).forEach(entity -> {
            if ( entity instanceof Player ) players.add((Player) entity);
        });
        return players;
    }

    public BossBar getBattlepointBossBar(Battlepoint battlepoint) {
        ServerBossBar bossBar;

        if ( bossBars.containsKey(battlepoint) ) bossBar = bossBars.get(battlepoint);
        else {
            Team controllingTeam = getControllingTeam(battlepoint);

            bossBar = ServerBossBar.builder()
                    .name(
                            Text.of(
                                    TextStyles.BOLD,
                                    battlepoint.getName(),
                                    " held by ",
                                    controllingTeam.getColor(),
                                    controllingTeam.getName()
                            )
                    )
                    .percent(battlepoint.getTeamProgress(controllingTeam))
                    .overlay(BossBarOverlays.PROGRESS)
                    .playEndBossMusic(false)
                    .visible(true)
                    .build();
        }

        bossBar.removePlayers(bossBar.getPlayers());
        bossBar.addPlayers(getPlayersWithinBattlepoint(battlepoint));

        return bossBar;
    }

    private void tickBattlepoint(Battlepoint battlepoint) {
        // get the entities within the inner radius
        Collection<Entity> innerRadiusEntities = getEntitiesWithinInnerRadius(battlepoint);
        innerRadiusEntities.forEach(entity -> {
            if (!(entity instanceof Player)) return;

            Player player = (Player) entity;
            Optional<TeamMember> teamMember = AtherysBattlegrounds.getInstance().getTeamMemberManager().get(player.getUniqueId());

            teamMember.ifPresent(tm -> {
                tm.getPrimary().ifPresent(primaryTeam -> {
                    this.captureBattlepoint(battlepoint, primaryTeam, AtherysBattlegrounds.getConfig().CAPTURE_AMOUNT);
                });
            });
        });


        BattlepointEvent.Tick tickEvent = new BattlepointEvent.Tick(battlepoint);
        Sponge.getEventManager().post(tickEvent);
    }

    private void captureBattlepoint(Battlepoint battlepoint, Team team, float capAmount) {

        battlepoint.getAllTeamProgress().forEach((t, progress) -> {
            if (t.equals(team)) battlepoint.addTeamProgress(team, capAmount);
            else battlepoint.removeTeamProgress(t, capAmount);
        });

        if ( battlepoint.getTeamProgress(team) == 1.0d ) {
            BattlepointEvent.Capture captureEvent = new BattlepointEvent.Capture(battlepoint, team);
            Sponge.getEventManager().post(captureEvent);
        }
    }

    private void battlepointsTick() {
        getAll().forEach(this::tickBattlepoint);
    }

    @Override
    @Nonnull
    public Optional<Battlepoint> getById(@Nonnull String id) {
        for (Battlepoint point : getAll()) {
            if (point.getId().equals(id)) return Optional.of(point);
        }
        return Optional.empty();
    }

    @Override
    @Nonnull
    public Collection<Battlepoint> getAll() {
        return AtherysBattlegrounds.getConfig().BATTLEPOINTS;
    }

    public static BattlepointService getInstance() {
        return instance;
    }

    public Task getBattlepointTask() {
        return battlepointTask;
    }
}
