package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.utils.ColorUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.*;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.World;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
            ServerBossBar battlePointBossBar = createBattlePointBossBar(pointConfig.getName(), pointConfig.getColor());

            // create the battlepoint
            battlePointService.createBattlePoint(
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
        });

        // start the tick task
        battlepointTask = Task.builder()
                .interval(config.TICK_INTERVAL.get(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS)
                .execute(this::tickAll)
                .submit(AtherysBattlegrounds.getInstance());
    }

    protected void tickAll() {
        battlePointService.getAllBattlePoints().forEach(battlePointService::tickBattlePoint);
    }

    public void updateBattlePointBossBar(BattlePoint battlePoint) {
        battlePoint.getBossBar().setPercent(battlePoint.getTeamProgress().getOrDefault(battlePoint.getControllingTeam(), 0.0f));
    }

    public void onPlayerMovement(Player player, Transform<World> from, Transform<World> to) {
        if (from.getPosition().toInt().equals(to.getPosition().toInt())) {
            return;
        }

        Optional<BattlePoint> fromBP = battlePointService.getBattlePointFromLocation(from.getLocation());
        Optional<BattlePoint> toBP = battlePointService.getBattlePointFromLocation(to.getLocation());

        // If it's the same battlepoint, or if both are null, return
        if (fromBP.equals(toBP)) {
            return;
        }

        // If player is entering into a battle point
        if (!fromBP.isPresent() && toBP.isPresent()) {
            showWelcomeTitleToPlayer(player, toBP.get());

            // show battle point boss bar to player
            toBP.get().getBossBar().addPlayer(player);
        }

        // if player is exiting a battle point
        if (fromBP.isPresent() && !toBP.isPresent()) {
            showExitTitleToPlayer(player, fromBP.get());

            // hide battle point boss bar from player
            fromBP.get().getBossBar().removePlayer(player);
        }

        // if player is crossing the border from one battle point to another
        if (fromBP.isPresent() && toBP.isPresent()) {
            showWelcomeTitleToPlayer(player, toBP.get());

            // hide previous battle point boss bar from player
            fromBP.get().getBossBar().removePlayer(player);

            // show new battle point boss bar to player
            toBP.get().getBossBar().addPlayer(player);
        }
    }

    private void showExitTitleToPlayer(Player player, BattlePoint battlePoint) {
        Title title = Title.builder()
                .title(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), battlePoint.getName()))
                .subtitle(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), "Entering Battleground..."))
                .fadeIn(3)
                .stay(5)
                .fadeOut(3)
                .build();

        player.sendTitle(title);
    }

    private void showWelcomeTitleToPlayer(Player player, BattlePoint battlePoint) {
        Title title = Title.builder()
                .title(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), battlePoint.getName()))
                .subtitle(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), "Leaving Battleground..."))
                .fadeIn(3)
                .stay(5)
                .fadeOut(3)
                .build();

        player.sendTitle(title);
    }


    protected ServerBossBar createBattlePointBossBar(String battlePointName, BossBarColor color) {
        return ServerBossBar.builder()
                .name(Text.of(ColorUtils.bossBarColorToTextColor(color), battlePointName))
                .color(color)
                .overlay(BossBarOverlays.PROGRESS)
                .playEndBossMusic(false)
                .createFog(false)
                .darkenSky(false)
                .build();
    }

}
