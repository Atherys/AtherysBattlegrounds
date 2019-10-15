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
import org.spongepowered.api.scheduler.Task;

import java.time.temporal.ChronoUnit;
import java.util.Currency;
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

            // create the battlepoint
            BattlePoint battlePoint = battlePointService.createBattlePoint(
                    pointConfig.getId(),
                    pointConfig.getName(),
                    pointConfig.getColor(),
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

}
