package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.utils.ColorUtils;
import com.atherys.core.utils.MathUtils;
import com.atherys.core.utils.Sound;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.effect.sound.SoundCategories;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
public class BattlePointFacade {

    @Inject
    private Logger logger;

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private BattlePointService battlePointService;

    @Inject
    private RespawnService respawnService;

    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private BattlegroundMessagingFacade msg;

    private Set<BattlePoint> battlePoints;

    private Task battlepointTask;

    public BattlePointFacade() {
    }

    public void init() {
        // create the battlepoints from the configuration
        config.BATTLE_POINTS.forEach(pointConfig -> {
            // parse the configured location
            BattlePointConfig.LocationConfig locationConfig = pointConfig.getLocation();

            Optional<World> world = Sponge.getServer().getWorld(locationConfig.getWorld());

            if (!world.isPresent()) {
                logger.error("Configured location for battlepoint " + pointConfig.getId() + " contains an invalid world. Skipping.");
                return;
            }

            Location<World> location = new Location<>(
                    world.get(),
                    locationConfig.getX(),
                    locationConfig.getY(),
                    locationConfig.getZ()
            );

            // parse the respawn point configs
            List<RespawnPoint> respawnPoints = pointConfig.getRespawnPoints().parallelStream()
                    .map(respawnConfig -> respawnService.createRespawnPoint(
                            location.getExtent(),
                            respawnConfig.getPosition(),
                            respawnConfig.getRadius()
                    ))
                    .collect(Collectors.toList());

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
                    location,
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
                .interval(config.TICK_INTERVAL.toMillis(), TimeUnit.MILLISECONDS)
                .execute(this::tickAll)
                .submit(AtherysBattlegrounds.getInstance());
    }

    protected void tickAll() {
        battlePointService.getAllBattlePoints().forEach(battlePointService::tickBattlePoint);
    }

    public void updateBattlePointBossBar(BattlePoint battlePoint) {
        // find the highest progress team and display their progress on the boss bar
        Team highestProgressTeam = null;
        float highestProgress = 0.0f;

        for (Map.Entry<Team, Float> entry : battlePoint.getTeamProgress().entrySet()) {
            if (entry.getValue() > highestProgress) {
                highestProgressTeam = entry.getKey();
                highestProgress = entry.getValue();
            }
        }

        if (highestProgressTeam != null) {
            battlePoint.getBossBar().setColor(ColorUtils.textColorToBossBarColor(highestProgressTeam.getColor()));
        }

        battlePoint.getBossBar().setPercent(MathUtils.clamp(highestProgress, 0.0f, 1.0f));
    }

    public void onPlayerMovement(Player player, Transform<World> from, Transform<World> to) {
        if (from.getPosition().equals(to.getPosition())) {
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
                .title(Text.of(battlePoint))
                .subtitle(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), "Leaving Battleground..."))
                .fadeIn(config.TITLE_FADE_TICKS)
                .stay(config.TITLE_STAY_TICKS)
                .fadeOut(config.TITLE_FADE_TICKS)
                .build();

        player.sendTitle(title);
    }

    private void showWelcomeTitleToPlayer(Player player, BattlePoint battlePoint) {
        Title title = Title.builder()
                .title(Text.of(battlePoint))
                .subtitle(Text.of(ColorUtils.bossBarColorToTextColor(battlePoint.getBossBar().getColor()), "Entering Battleground..."))
                .fadeIn(config.TITLE_FADE_TICKS)
                .stay(config.TITLE_STAY_TICKS)
                .fadeOut(config.TITLE_FADE_TICKS)
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

    public void notifyCapturedBattlePoint(BattlePoint battlePoint, Team capturingTeam) {
        msg.broadcast(Text.of("Team \"", capturingTeam, "\" has captured \"", battlePoint, "\""));

        fetchPlayersWithinBattlePointOuterRadius(battlePoint).forEach(this::playBattlePointCaptureSound);
    }

    private Set<Player> fetchPlayersWithinBattlePointOuterRadius(BattlePoint battlePoint) {
        return Sponge.getServer()
                .getOnlinePlayers().parallelStream()
                .filter(player -> battlePointService.isPlayerWithinBattlePointOuterRadius(battlePoint, player))
                .collect(Collectors.toSet());
    }

    private void playBattlePointCaptureSound(Player player) {
        Sound.playSound(
                Sound.builder(SoundTypes.ENTITY_PLAYER_LEVELUP, 1.0).soundCategory(SoundCategories.AMBIENT).build(),
                player,
                player.getPosition()
        );
    }
}
