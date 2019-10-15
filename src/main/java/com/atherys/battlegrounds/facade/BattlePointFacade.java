package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.*;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
public class BattlePointFacade {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private BattlePointService battlePointService;

    @Inject
    private RespawnService respawnService;

    private Set<BattlePoint> battlePoints;

    private Task battlepointTask;

    public BattlePointFacade() {
    }

    public void init() {
        // create the battlepoints from the configuration
        config.BATTLE_POINTS.forEach(pointConfig -> {
            // parse the respawn point configs
            Set<RespawnPoint> respawnPoints = pointConfig.getRespawnPoints().parallelStream()
                    .map(respawnConfig -> respawnService.createRespawnPoint(
                            pointConfig.getLocation().getExtent(),
                            respawnConfig.getPosition(),
                            respawnConfig.getRadius()
                    ))
                    .collect(Collectors.toSet());

            // parse the capture awards config
            Set<Award> captureAwards = pointConfig.getOnCaptureAwards().parallelStream()
                    .map(awardConfig -> battlePointService.createAward(awardConfig.getCurrency()))
                    .collect(Collectors.toSet());

            // parse the tick awards config
            Set<Award> tickAwards = pointConfig.getOnTickAwards().parallelStream()
                    .map(awardConfig -> battlePointService.createAward(awardConfig.getCurrency()))
                    .collect(Collectors.toSet());

            // create the boss bar for the battlepoint
            BossBar battlePointBossBar = createBattlePointBossBar(pointConfig.getName(), pointConfig.getColor());

            // create the battlepoint
            BattlePoint battlePoint = battlePointService.createBattlePoint(
                    pointConfig.getId(),
                    pointConfig.getName(),
                    battlePointBossBar,
                    pointConfig.getLocation(),
                    pointConfig.getInnerRadius(),
                    pointConfig.getOuterRadius(),
                    pointConfig.getPerTickCaptureAmount(),
                    pointConfig.getRespawnInterval(),
                    pointConfig.getRespawnTimeout(),
                    respawnPoints,
                    captureAwards,
                    tickAwards
            );

            // keep it in memory for future use
            battlePoints.add(battlePoint);
        });

        // start the tick task
        battlepointTask = Task.builder()
                .interval(config.TICK_INTERVAL.get(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS)
                .execute(this::tickAll)
                .submit(AtherysBattlegrounds.getInstance());
    }

    protected void tickAll() {
        battlePoints.forEach(battlePointService::tickBattlePoint);
    }

    public void updateBattlePointBossBar(BattlePoint battlePoint) {
        // TODO
    }

    protected BossBar createBattlePointBossBar(String battlePointName, BossBarColor color) {
        return ServerBossBar.builder()
                .name(Text.of(battlePointName))
                .color(color)
                .overlay(BossBarOverlays.PROGRESS)
                .playEndBossMusic(false)
                .createFog(false)
                .darkenSky(false)
                .build();
    }

}
