package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Singleton;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class RespawnService {

    // this map represents players who are still able to request a respawn, but have not done so yet
    // the value represents the timestamp before which they can request a respawn, and after which they will no longer be able to do so
    private Map<UUID, Long> playersWithAbilityToRequestRespawn = new HashMap<>();

    // this queue represents the players who have requested a respawn and are to be respawned with the next respawn tick
    private Map<UUID, BattlePoint> playersToRespawn = new HashMap<>();

    public void tickRespawns() {
        long currentTimestamp = System.currentTimeMillis();

        // do maintenance on the map to ensure no players whose request ability has already elapsed are kept
        if (!playersWithAbilityToRequestRespawn.isEmpty()) {
            for (Map.Entry<UUID, Long> entry : playersWithAbilityToRequestRespawn.entrySet()) {
                if (entry.getValue() <= currentTimestamp) {
                    playersWithAbilityToRequestRespawn.remove(entry.getKey());
                }
            }
        }

        // respawn players who have requested to do so
        if (!playersToRespawn.isEmpty()) {
            playersToRespawn.forEach((uuid, battlepoint) -> {
                Optional<Player> anyPlayerWithThisUUID = Sponge.getServer()
                        .getOnlinePlayers().stream()
                        .filter(player -> player.getUniqueId().equals(uuid))
                        .findAny();

                anyPlayerWithThisUUID.ifPresent((player) -> {
                    this.respawnPlayer(player, battlepoint);
                });
            });

            playersToRespawn.clear();
        }
    }

    public RespawnPoint createRespawnPoint(World world, Vector3i position, double radius) {
        RespawnPoint respawnPoint = new RespawnPoint();
        respawnPoint.setLocation(new Location<>(world, position));
        respawnPoint.setRadius(radius);

        return respawnPoint;
    }

    public void beginRespawnTimeoutCounter(Player player, Duration respawnTimeout) {
        playersWithAbilityToRequestRespawn.put(
                player.getUniqueId(),
                System.currentTimeMillis() + respawnTimeout.toMillis()
        );
    }

    public boolean hasPlayerRespawnCounterElapsed(Player player) {
        return !playersWithAbilityToRequestRespawn.containsKey(player.getUniqueId());
    }

    public void queuePlayerForRespawn(Player player, BattlePoint battlePoint) {
        playersToRespawn.put(player.getUniqueId(), battlePoint);
    }

    protected void respawnPlayer(Player player, BattlePoint battlePoint) {

        // if the battlepoint has no respawn points, return
        if (battlePoint.getRespawnPoints() == null || battlePoint.getRespawnPoints().isEmpty()) {
            return;
        }

        // get random respawn point
        RespawnPoint respawnPoint = battlePoint.getRespawnPoints().get(RandomUtils.nextInt(0, battlePoint.getRespawnPoints().size()));

        // calculate a random point within its radius where to respawn the player
        Location<World> respawnLocation = calculateSafeRespawnLocation(respawnPoint);

        // teleport the player to the final respawn location
        player.setLocation(respawnLocation);
    }

    protected Location<World> calculateSafeRespawnLocation(RespawnPoint respawnPoint) {
        double a = Math.random() * 2 * Math.PI;
        double r = respawnPoint.getRadius() * Math.sqrt(Math.random());

        double x = respawnPoint.getLocation().getX() + r * Math.cos(a);
        double z = respawnPoint.getLocation().getZ() + r * Math.sin(a);

        Vector3d initialPosition = new Vector3d(
                x,
                respawnPoint.getLocation().getExtent().getHighestYAt((int) Math.round(x), (int) Math.round(z)),
                z
        );

        Location<World> initialLocation = new Location<>(respawnPoint.getLocation().getExtent(), initialPosition);

        return Sponge.getTeleportHelper()
                .getSafeLocation(initialLocation, 3, 3)
                .orElse(initialLocation);
    }

}
