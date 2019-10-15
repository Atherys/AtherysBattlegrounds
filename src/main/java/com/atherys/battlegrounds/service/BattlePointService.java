package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.config.RespawnPointConfig;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.Set;

public class BattlePointService {// implements CatalogRegistryModule<BattlePoint> {

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
            Set<RespawnPoint> respawnPoints
    ) {
        BattlePoint battlePoint = new BattlePoint(id);
        battlePoint.setName(name);
        battlePoint.setColor(color);
        battlePoint.setLocation(location);
        battlePoint.setInnerRadius(innerRadius);
        battlePoint.setOuterRadius(outerRadius);
        battlePoint.setPerTickCaptureAmount(perTickCaptureAmount);
        battlePoint.setRespawnInterval(respawnInterval);
        battlePoint.setRespawnDuration(respawnDuration);
        battlePoint.setRespawnPoints(respawnPoints);

        return battlePoint;
    }

    public void tickBattlePoint(BattlePoint battlePoint) {
        // TODO: Find all players current within the inner radius of the battlePoint
        // TODO: If there are enough players ( a number larger than MINIMUM_PLAYERS_REQUIRED_TO_CAPTURE_POINT ) on the same team,
        // TODO: then increase the capture amount for that team by the per-tick-capture-amount configured for this point.
        // TODO: If the capture amount for the team is at 100%, then the team has captured the point
        // TODO: With each tick, the currency rewards for controlling the point should also be handed out
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
