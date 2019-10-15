package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.temporal.ChronoUnit;
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
        config.BATTLE_POINTS.forEach(pointConfig -> {
            Set<RespawnPoint> respawnPoints = pointConfig.getRespawnPoints().stream()
                    .map(respawnConfig -> respawnService.createRespawnPoint(
                            pointConfig.getLocation().getExtent(),
                            respawnConfig.getPosition(),
                            respawnConfig.getRadius()
                    ))
                    .collect(Collectors.toSet());

            BattlePoint battlePoint = battlePointService.createBattlePoint(
                    pointConfig.getId(),
                    pointConfig.getName(),
                    pointConfig.getColor(),
                    pointConfig.getLocation(),
                    pointConfig.getInnerRadius(),
                    pointConfig.getOuterRadius(),
                    pointConfig.getPerTickCaptureAmount(),
                    pointConfig.getRespawnInterval(),
                    pointConfig.getRespawnDuration(),
                    respawnPoints
            );

            battlePoints.add(battlePoint);
        });

        battlepointTask = Task.builder()
                .interval(config.TICK_INTERVAL.get(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS)
                .execute(this::tickAll)
                .submit(AtherysBattlegrounds.getInstance());
    }

    protected void tickAll() {
        battlePoints.forEach(battlePointService::tickBattlePoint);
    }

}
