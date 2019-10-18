package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.core.utils.Question;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class RespawnFacade {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private BattlePointService battlePointService;

    @Inject
    private RespawnService respawnService;

    @Inject
    private BattlegroundMessagingFacade msg;

    private Task respawnTask;

    public void init() {
        respawnTask = Task.builder()
                .interval(config.RESPAWN_INTERVAL.toMillis(), TimeUnit.MILLISECONDS)
                .execute(respawnService::tickRespawns)
                .submit(AtherysBattlegrounds.getInstance());
    }

    public void offerRespawn(Player player) {
        Optional<BattlePoint> battlePoint = battlePointService.getBattlePointFromLocation(player.getLocation());

        if (!battlePoint.isPresent()) {
            return;
        }

        // ask the player if they would like to respawn
        Question.of(msg.formatInfo("You died at ", battlePoint, ". Would you like to respawn? You have ", DurationFormatUtils.formatDurationISO(battlePoint.get().getRespawnTimeout().toMillis()), " to decide."))
                .addAnswer(Question.Answer.of(Text.of("Yes"), (src) -> onPlayerAcceptRespawn(player, battlePoint.get())))
                .addAnswer(Question.Answer.of(Text.of("No"), (src) -> {
                }))
                .build()
                .pollChat(player);

        respawnService.beginRespawnTimeoutCounter(player, battlePoint.get().getRespawnTimeout());
    }

    private void onPlayerAcceptRespawn(Player player, BattlePoint battlePoint) {
        boolean respawnCounterHasElapsed = respawnService.hasPlayerRespawnCounterElapsed(player);

        if (respawnCounterHasElapsed) {
            msg.error(player, "Your respawn counter has already elapsed, you are no longer able to respawn.");
            return;
        }

        msg.info(player, Text.of("Prepare to respawn at \"", battlePoint, "\"!"));

        respawnService.queuePlayerForRespawn(player, battlePoint);
    }
}
