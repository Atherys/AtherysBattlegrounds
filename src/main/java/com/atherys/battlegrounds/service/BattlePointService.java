package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.event.BattlePointEvent;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class BattlePointService {

    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private TeamService teamService;

    private Set<BattlePoint> battlePoints = new HashSet<>();

    public BattlePointService() {
    }

    public BattlePoint createBattlePoint(
            String id,
            String name,
            ServerBossBar bossBar,
            Location<World> location,
            double innerRadius,
            double outerRadius,
            float perTickCaptureAmount,
            Duration respawnInterval,
            Duration respawnDuration,
            List<RespawnPoint> respawnPoints,
            AwardConfig captureAward,
            AwardConfig tickAward,
            AwardConfig killAward
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
        battlePoint.setCaptureAward(captureAward);
        battlePoint.setTickAward(tickAward);
        battlePoint.setKillAward(killAward);

        battlePoints.add(battlePoint);

        return battlePoint;
    }

    public void tickBattlePoint(BattlePoint battlePoint) {
        Map<Team, Set<Player>> membersWithinInnerRadius = new HashMap<>();

        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            if (isPlayerWithinBattlePointInnerRadius(battlePoint, player)) {
                TeamMember member = teamMemberService.getOrCreateTeamMember(player);

                if (member.getTeam() == null) continue;

                if (membersWithinInnerRadius.containsKey(member.getTeam())) {
                    membersWithinInnerRadius.get(member.getTeam()).add(player);
                } else {
                    membersWithinInnerRadius.put(member.getTeam(), Sets.newHashSet(player));
                }
            }
        }

        // determine the capturing team, and if any, increment their progress by the configured amount
        // and decrement the progress of all other non-capturing teams
        teamMemberService.determineCapturingTeam(membersWithinInnerRadius).ifPresent(capturingTeam -> {

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
            teamService.distributeAward(battlePoint.getCaptureAward(), postTickControllingTeam.get());
            teamService.distributeAwardsToMembers(battlePoint.getCaptureAward(), membersWithinInnerRadius.get(postTickControllingTeam.get()));

            // trigger the capture event
            BattlePointEvent.Capture captureEvent = new BattlePointEvent.Capture(battlePoint, battlePoint.getControllingTeam());
            Sponge.getEventManager().post(captureEvent);

            return;
        } else if (battlePoint.getControllingTeam() != null) {
            // distribute tick awards for having control of the point
            teamService.distributeAward(battlePoint.getTickAward(), battlePoint.getControllingTeam());
        }

        // trigger the tick event
        BattlePointEvent.Tick tickEvent = new BattlePointEvent.Tick(battlePoint);
        Sponge.getEventManager().post(tickEvent);
    }

    protected void incrementTeamProgress(BattlePoint battlePoint, Team team, float amount) {
        Float currentValue = battlePoint.getTeamProgress().getOrDefault(team, 0.0f);

        if (currentValue + amount <= 0.0f) {
            battlePoint.getTeamProgress().put(team, 0.0f);
        } else if (currentValue + amount >= 1.0f) {
            battlePoint.getTeamProgress().put(team, 1.0f);
        } else {
            battlePoint.getTeamProgress().put(team, currentValue + amount);
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

    public boolean isPlayerWithinBattlePointInnerRadius(BattlePoint battlePoint, Player player) {
        if (!battlePoint.getLocation().getExtent().equals(player.getWorld())) {
            return false;
        }

        double distanceToCenterSquared = battlePoint.getLocation().getPosition().distanceSquared(player.getPosition());

        return distanceToCenterSquared <= Math.pow(battlePoint.getInnerRadius(), 2);
    }

    public boolean isPlayerWithinBattlePointOuterRadius(BattlePoint battlePoint, Player player) {
        return isLocationWithinBattlePointOuterRadius(battlePoint, player.getLocation());
    }

    public Set<BattlePoint> getAllBattlePoints() {
        return battlePoints;
    }

    public Set<BattlePoint> getControlledPoints(Team team) {
        return battlePoints.stream()
                .filter(battlePoint -> team.equals(battlePoint.getControllingTeam()))
                .collect(Collectors.toSet());
    }

    public Optional<BattlePoint> getBattlePointFromLocation(Location<World> location) {
        Set<BattlePoint> overlappingBPs = battlePoints.stream()
                .filter(battlePoint -> isLocationWithinBattlePointOuterRadius(battlePoint, location))
                .collect(Collectors.toSet());

        // if there's only one battle point found to overlap those coordinates ( or none ), just return it
        // otherwise, return the point with the smallest relative distance to the player
        // For further reading: https://stackoverflow.com/questions/49892268/point-within-multiple-spheres
        if (overlappingBPs.size() == 0 || overlappingBPs.size() == 1) {
            return overlappingBPs.stream().findFirst();
        } else {
            double closestRelativeDistance = 1.0;
            BattlePoint closestBP = null;

            for (BattlePoint bp : overlappingBPs) {
                double distanceSquared = bp.getLocation().getPosition().distanceSquared(location.getPosition());
                double radiusSquared = Math.pow(bp.getOuterRadius(), 2);

                double relativeDistance = distanceSquared / radiusSquared;

                if (relativeDistance < closestRelativeDistance) {
                    closestRelativeDistance = relativeDistance;
                    closestBP = bp;
                }
            }

            return Optional.ofNullable(closestBP);
        }
    }

    private boolean isLocationWithinBattlePointOuterRadius(BattlePoint battlePoint, Location<World> location) {
        if (!battlePoint.getLocation().getExtent().equals(location.getExtent())) {
            return false;
        }

        double distanceToCenterSquared = battlePoint.getLocation().getPosition().distanceSquared(location.getPosition());

        return distanceToCenterSquared <= Math.pow(battlePoint.getOuterRadius(), 2);
    }

    public void clearBattlePoints() {
        battlePoints.forEach(bp -> {
            bp.getBossBar().removePlayers(bp.getBossBar().getPlayers());

        });

        battlePoints.clear();
    }
}
