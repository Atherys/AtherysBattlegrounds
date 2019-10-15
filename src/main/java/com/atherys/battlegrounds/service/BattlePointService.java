package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class BattlePointService {

    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private TeamService teamService;

    public BattlePoint createBattlePoint(
            String id,
            String name,
            BossBarColor color,
            Location<World> location,
            double innerRadius,
            double outerRadius,
            float perTickCaptureAmount,
            Duration respawnInterval,
            Duration respawnDuration,
            Set<RespawnPoint> respawnPoints,
            Set<Award> captureAwards,
            Set<Award> tickAwards
    ) {
        BattlePoint battlePoint = new BattlePoint(id);

        battlePoint.setName(name);
        battlePoint.setColor(color);
        battlePoint.setLocation(location);
        battlePoint.setInnerRadius(innerRadius);
        battlePoint.setOuterRadius(outerRadius);
        battlePoint.setPerTickCaptureAmount(perTickCaptureAmount);
        battlePoint.setRespawnInterval(respawnInterval);
        battlePoint.setRespawnTimeout(respawnDuration);
        battlePoint.setRespawnPoints(respawnPoints);
        battlePoint.setCaptureAwards(captureAwards);
        battlePoint.setTickAwards(tickAwards);

        return battlePoint;
    }

    public Award createAward(Map<Currency, Double> currencyAwards) {
        Award award = new Award();
        award.setCurrency(currencyAwards);
        return award;
    }

    public void tickBattlePoint(BattlePoint battlePoint) {
        // TODO: Move this logic into an event handler and tie it with the BattlePointEvent.Tick event

        // Find all online players currently within the inner radius of the battle point
        Set<TeamMember> onlineTeamMembersWithinInnerRadius = Sponge.getServer()
                .getOnlinePlayers().parallelStream()
                .filter(player -> isPlayerWithinBattlePointInnerRadius(battlePoint, player))
                .map(player -> teamMemberService.getOrCreateTeamMember(player))
                .collect(Collectors.toSet());

        // determine the capturing team, and if any, increment their progress by the configured amount
        // and decrement the progress of all other non-capturing teams
        teamMemberService.determineCapturingTeam(onlineTeamMembersWithinInnerRadius).ifPresent(capturingTeam -> {

            // increment the capturing team's progress
            incrementTeamProgress(battlePoint, capturingTeam, battlePoint.getPerTickCaptureAmount());

            // the other teams get their progress decremented by the same amount
            battlePoint.getTeamProgress().keySet().forEach(otherTeam -> {
                if (otherTeam.equals(capturingTeam)) {
                    return;
                }

                // decrement the other non-capturing teams progress
                incrementTeamProgress(battlePoint, otherTeam, -battlePoint.getPerTickCaptureAmount());
            });
        });

        // determine the controlling team after having modified the progress values for the battlepoint
        Optional<Team> postTickControllingTeam = determineControllingTeam(battlePoint);

        // if the point's current controlling team the controlling team after the last progress tick are different,
        // then a change of ownership has occurred. Distribute capture awards to the new controlling team
        if (postTickControllingTeam.isPresent() && !postTickControllingTeam.get().equals(battlePoint.getControllingTeam())) {
            // TODO: Move this logic into an event handler and tie it into the BattlePointEvent.Capture event
            battlePoint.setControllingTeam(postTickControllingTeam.get());

            // distribute awards for capturing the point
            teamService.distributeCaptureAwards(battlePoint, postTickControllingTeam.get());
        } else {
            // distribute tick awards for having control of the point
            teamService.distributeTickAwards(battlePoint, battlePoint.getControllingTeam());
        }
    }

    protected void incrementTeamProgress(BattlePoint battlePoint, Team team, float amount) {
        if (battlePoint.getTeamProgress().containsKey(team)) {
            battlePoint.getTeamProgress().merge(team, amount, Float::sum);
        } else {
            battlePoint.getTeamProgress().put(team, amount);
        }
    }

    protected Optional<Team> determineControllingTeam(BattlePoint battlePoint) {
        // Find the first team with 100% progress towards capturing the point
        for (Map.Entry<Team, Float> entry : battlePoint.getTeamProgress().entrySet()) {
            if (entry.getValue() != null && entry.getValue() == 1.0f) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    protected boolean isPlayerWithinBattlePointInnerRadius(BattlePoint battlePoint, Player player) {
        if (!battlePoint.getLocation().getExtent().equals(player.getWorld())) {
            return false;
        }

        double distanceToCenterSquared = battlePoint.getLocation().getPosition().distanceSquared(player.getPosition());

        return distanceToCenterSquared <= Math.pow(battlePoint.getInnerRadius(), 2);
    }

//    private static final BattlepointService instance = new BattlepointService();
//
//    private Map<BattlePoint,ServerBossBar> bossBars = new HashMap<>();
//
//    private Task battlepointTask;
//
//    private BattlepointService() {
//        battlepointTask = Task.builder()
//                .delay(1, TimeUnit.SECONDS)
//                .interval(AtherysBattlegrounds.getConfig().BATTLEPOINTS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
//                .execute(this::battlepointsTick)
//                .submit(AtherysBattlegrounds.getInstance());
//    }
//
//    public Collection<Entity> getEntitiesWithinInnerRadius(BattlePoint battlepoint) {
//        return battlepoint.getLocation().getExtent().getNearbyEntities(battlepoint.getLocation().getPosition(), battlepoint.getInnerRadius());
//    }
//
//    public Team getControllingTeam(BattlePoint battlepoint) {
//        Team highestProgressTeam = Team.NONE;
//
//        for ( Map.Entry<Team,Float> entry : battlepoint.getAllTeamProgress().entrySet() ) {
//            if ( entry.getValue() > battlepoint.getTeamProgress(highestProgressTeam) ) highestProgressTeam = entry.getKey();
//        }
//
//        return highestProgressTeam;
//    }
//
//    public List<Player> getPlayersWithinBattlepoint(BattlePoint battlepoint) {
//        List<Player> players = new ArrayList<>();
//        getEntitiesWithinInnerRadius(battlepoint).forEach(entity -> {
//            if ( entity instanceof Player ) players.add((Player) entity);
//        });
//        return players;
//    }
//
//    public BossBar getBattlepointBossBar(BattlePoint battlepoint) {
//        ServerBossBar bossBar;
//
//        if ( bossBars.containsKey(battlepoint) ) bossBar = bossBars.get(battlepoint);
//        else {
//            Team controllingTeam = getControllingTeam(battlepoint);
//
//            bossBar = ServerBossBar.builder()
//                    .name(
//                            Text.of(
//                                    TextStyles.BOLD,
//                                    battlepoint.getName(),
//                                    " held by ",
//                                    controllingTeam.getColor(),
//                                    controllingTeam.getName()
//                            )
//                    )
//                    .percent(battlepoint.getTeamProgress(controllingTeam))
//                    .color(battlepoint.getColor())
//                    .overlay(BossBarOverlays.PROGRESS)
//                    .playEndBossMusic(false)
//                    .visible(true)
//                    .build();
//
//            bossBars.put(battlepoint, bossBar);
//        }
//
//        bossBar.removePlayers(bossBar.getPlayers());
//        bossBar.addPlayers(getPlayersWithinBattlepoint(battlepoint));
//
//        return bossBar;
//    }
//
//    private void tickBattlepoint(BattlePoint battlepoint) {
//        // get the entities within the inner radius
//        Collection<Entity> innerRadiusEntities = getEntitiesWithinInnerRadius(battlepoint);
//
//        innerRadiusEntities.forEach(entity -> {
//            if (!(entity instanceof Player)) return;
//
//            Player player = (Player) entity;
//            TeamMember tm = AtherysBattlegrounds.getInstance().getTeamMemberManager().getOrCreate(player.getUniqueId());
//
//            tm.getPrimary().ifPresent(primaryTeam -> {
//                this.captureBattlepoint(battlepoint, primaryTeam, AtherysBattlegrounds.getConfig().CAPTURE_AMOUNT);
//            });
//        });
//
//
//        BattlepointEvent.Tick tickEvent = new BattlepointEvent.Tick(battlepoint);
//        Sponge.getEventManager().post(tickEvent);
//    }
//
//    private void captureBattlepoint(BattlePoint battlepoint, Team team, float capAmount) {
//        battlepoint.getAllTeamProgress().forEach((t, progress) -> {
//            if (t.equals(team)) battlepoint.addTeamProgress(team, capAmount);
//            else battlepoint.removeTeamProgress(t, capAmount);
//        });
//
//        if ( battlepoint.getTeamProgress(team) == 1.0f ) {
//            BattlepointEvent.Capture captureEvent = new BattlepointEvent.Capture(battlepoint, team);
//            Sponge.getEventManager().post(captureEvent);
//        }
//    }
//
//    private void battlepointsTick() {
//        getAll().forEach(this::tickBattlepoint);
//    }
//
//    @Override
//    @Nonnull
//    public Optional<BattlePoint> getById(@Nonnull String id) {
//        for (BattlePoint point : getAll()) {
//            if (point.getId().equals(id)) return Optional.of(point);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    @Nonnull
//    public Collection<BattlePoint> getAll() {
//        return AtherysBattlegrounds.getConfig().BATTLEPOINTS;
//    }
//
//    public static BattlepointService getInstance() {
//        return instance;
//    }
//
//    public Task getBattlepointTask() {
//        return battlepointTask;
//    }
}
