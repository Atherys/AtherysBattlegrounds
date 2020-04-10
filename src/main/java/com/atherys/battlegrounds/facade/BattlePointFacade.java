package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.utils.ColorUtils;
import com.atherys.core.utils.MathUtils;
import com.atherys.core.utils.Sound;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.sound.SoundCategories;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
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

    private Task battlepointTask;

    private String timeLeft;

    private static final List<Vector3i> offsets = Arrays.asList(
            Vector3i.UNIT_X,
            Vector3i.UNIT_Z,
            Vector3i.from(1, 0, -1),
            Vector3i.from(-1, 0, 1),
            Vector3i.from(-1, 0, -1),
            Vector3i.from(-1, 0, 0),
            Vector3i.from(0, 0, -1),
            Vector3i.from(1, 0, 1)
    );

    public BattlePointFacade() {
    }

    public void init() {
        // create the battlepoints from the configuration
        config.BATTLE_POINTS.forEach(pointConfig -> {
            // parse the configured location
            BattlePointConfig.LocationConfig locationConfig = pointConfig.getLocation();

            Optional<World> world = Sponge.getServer().getWorld(locationConfig.getWorld());

            if (!world.isPresent()) {
                logger.error("Configured location for battlepoint " + pointConfig.getId() + " contains invalid world \"" + locationConfig.getWorld() + "\". Skipping.");
                return;
            }

            Location<World> location = new Location<>(
                    world.get(),
                    locationConfig.getX(),
                    locationConfig.getY(),
                    locationConfig.getZ()
            );

            BattlePointConfig.LocationConfig beaconLocationConfig = pointConfig.getBeaconLocation();

            Vector3i beaconLocation = new Vector3i(beaconLocationConfig.getX(), beaconLocationConfig.getY(), beaconLocationConfig.getZ());

            // parse the respawn point configs
            List<RespawnPoint> respawnPoints = pointConfig.getRespawnPoints().stream()
                    .map(respawnConfig -> respawnService.createRespawnPoint(
                            location.getExtent(),
                            respawnConfig.getPosition(),
                            respawnConfig.getRadius()
                    ))
                    .collect(Collectors.toList());

            // create the boss bar for the battlepoint
            ServerBossBar battlePointBossBar = createBattlePointBossBar(pointConfig.getName(), pointConfig.getColor());

            // create the battlepoint
            BattlePoint battlePoint = battlePointService.createBattlePoint(
                    pointConfig.getId(),
                    pointConfig.getName(),
                    battlePointBossBar,
                    location,
                    beaconLocation,
                    pointConfig.getInnerRadius(),
                    pointConfig.getOuterRadius(),
                    pointConfig.getPerTickCaptureAmount(),
                    pointConfig.getPerMemberTickCaptureAmount(),
                    pointConfig.getMaxPerTickCaptureAmount(),
                    pointConfig.getRespawnInterval(),
                    pointConfig.getRespawnTimeout(),
                    pointConfig.getCaptureCooldown(),
                    respawnPoints,
                    pointConfig.getOnCaptureAward(),
                    pointConfig.getOnTickAward(),
                    pointConfig.getOnKillAward()
            );

            placeBeacon(battlePoint);
        });

        // start the tick task
        battlepointTask = Task.builder()
                .interval(config.TICK_INTERVAL.toMillis(), TimeUnit.MILLISECONDS)
                .execute(this::tickAll)
                .submit(AtherysBattlegrounds.getInstance());

        timeLeft = DurationFormatUtils.formatDuration(config.WARNING_TIME.toMillis(), "m") + " minutes";
    }

    public void reload() {
        battlePointService.clearBattlePoints();
        battlepointTask.cancel();

        init();
    }

    protected void tickAll() {
        battlePointService.getAllBattlePoints().forEach(battlePointService::tickBattlePoint);
    }

    public void updateBattlePointBossBar(BattlePoint battlePoint) {
        // find the highest progress team and display their progress on the boss bar
        BattleTeam highestProgressTeam = null;
        float highestProgress = 0.0f;

        for (Map.Entry<BattleTeam, Float> entry : battlePoint.getTeamProgress().entrySet()) {
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

    public void onPlayerJoin(Player player) {
        Optional<BattlePoint> bp = battlePointService.getBattlePointFromLocation(player.getLocation());
        bp.ifPresent(battlePoint -> {
            showWelcomeTitleToPlayer(player, battlePoint);
            battlePoint.getBossBar().addPlayer(player);
        });
    }

    public void onPlayerDisconnect(Player player) {
        Optional<BattlePoint> bp = battlePointService.getBattlePointFromLocation(player.getLocation());
        bp.ifPresent(battlePoint -> battlePoint.getBossBar().removePlayer(player));
    }

    public void notifyCapturedBattlePoint(BattlePoint battlePoint, BattleTeam capturingTeam) {
        msg.broadcast(capturingTeam, " has captured ", battlePoint, ".");
        setBeaconColor(battlePoint, ColorUtils.textColorToDyeColor(battlePoint.getControllingTeam().getColor()));
        fetchPlayersWithinBattlePointOuterRadius(battlePoint).forEach(this::playBattlePointCaptureSound);
    }

    public void notifyCapturableBattlePoint(BattlePoint battlePoint) {
        msg.broadcast(battlePoint, " is now capturable!");
    }

    public void warnCapturableBattlePoint(BattlePoint battlePoint) {
        msg.broadcast(battlePoint, " will be capturable in ", timeLeft, " minutes.");
    }

    private void setBeaconColor(BattlePoint battlePoint, DyeColor color) {
        BlockState state = BlockState.builder()
                .blockType(BlockTypes.STAINED_GLASS)
                .add(Keys.DYE_COLOR, color)
                .build();

        battlePoint.getLocation().getExtent().setBlock(battlePoint.getBeaconLocation().add(0, 1, 0), state);
    }

    private void placeBeacon(BattlePoint battlePoint) {
        World world = battlePoint.getLocation().getExtent();

        world.setBlockType(battlePoint.getBeaconLocation(), BlockTypes.BEACON);
        Vector3i below = battlePoint.getBeaconLocation().sub(0, 1, 0);
        world.setBlockType(below, BlockTypes.IRON_BLOCK);
        offsets.forEach(offset -> {
            world.setBlockType(below.add(offset), BlockTypes.IRON_BLOCK);
        });

        setBeaconColor(battlePoint, DyeColors.WHITE);
    }

    private void showExitTitleToPlayer(Player player, BattlePoint battlePoint) {
        Title title = Title.builder()
                .title(Text.of(battlePoint))
                .subtitle(Text.of(battlePoint.getColor(), "Leaving Battleground..."))
                .fadeIn(config.TITLE_FADE_TICKS)
                .stay(config.TITLE_STAY_TICKS)
                .fadeOut(config.TITLE_FADE_TICKS)
                .build();

        player.sendTitle(title);
    }

    private void showWelcomeTitleToPlayer(Player player, BattlePoint battlePoint) {
        Title title = Title.builder()
                .title(Text.of(battlePoint))
                .subtitle(Text.of(battlePoint.getColor(), "Entering Battleground..."))
                .fadeIn(config.TITLE_FADE_TICKS)
                .stay(config.TITLE_STAY_TICKS)
                .fadeOut(config.TITLE_FADE_TICKS)
                .build();

        player.sendTitle(title);
    }


    private ServerBossBar createBattlePointBossBar(String battlePointName, TextColor color) {
        return ServerBossBar.builder()
                .name(Text.of(color, battlePointName))
                .color(BossBarColors.WHITE)
                .overlay(BossBarOverlays.PROGRESS)
                .playEndBossMusic(false)
                .createFog(false)
                .darkenSky(false)
                .build();
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
