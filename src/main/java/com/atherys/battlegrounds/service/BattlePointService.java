package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.event.BattlePointEvent;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBar;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.Currency;
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
            BossBar bossBar,
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
        battlePoint.setBossBar(bossBar);
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
            battlePoint.setControllingTeam(postTickControllingTeam.get());

            // distribute awards for capturing the point
            teamService.distributeCaptureAwards(battlePoint, postTickControllingTeam.get());

            // trigger the capture event
            BattlePointEvent.Capture captureEvent = new BattlePointEvent.Capture(battlePoint, battlePoint.getControllingTeam());
            Sponge.getEventManager().post(captureEvent);
        } else {
            // distribute tick awards for having control of the point
            teamService.distributeTickAwards(battlePoint, battlePoint.getControllingTeam());

            // trigger the tick event
            BattlePointEvent.Tick tickEvent = new BattlePointEvent.Tick(battlePoint);
            Sponge.getEventManager().post(tickEvent);
        }
    }

    protected void incrementTeamProgress(BattlePoint battlePoint, Team team, float amount) {
        Float currentValue = battlePoint.getTeamProgress().getOrDefault(team, 0.0f);

        if (currentValue < 0.0f || currentValue >= 1.0f) {
            return;
        }

        battlePoint.getTeamProgress().put(team, currentValue + amount);
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

}
