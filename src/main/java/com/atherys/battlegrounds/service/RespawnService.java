package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.RespawnPoint;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Identifiable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RespawnService {

//    private static final RespawnService instance = new RespawnService();
//
//    public static class Respawn implements Identifiable {
//
//        private UUID player;
//
//        private RespawnPoint point;
//
//        private long timestamp;
//
//        private boolean ready;
//
//        public Respawn(UUID player, RespawnPoint point, long timestamp) {
//            this.player = player;
//            this.point = point;
//            this.timestamp = timestamp;
//        }
//
//        @Override
//        @Nonnull
//        public UUID getUniqueId() {
//            return player;
//        }
//
//        public RespawnPoint getPoint() {
//            return point;
//        }
//
//        public long getTimestamp() {
//            return timestamp;
//        }
//
//        public boolean isReady() {
//            return ready;
//        }
//
//        public void setReady(boolean state) {
//            this.ready = state;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Respawn)) return false;
//            Respawn respawn = (Respawn) o;
//            return Objects.equals(player, respawn.player);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(player);
//        }
//    }
//
//    private Map<UUID, Respawn> respawns = new HashMap<>();
//
//    private Task respawnTask;
//
//    private RespawnService() {
//        respawnTask = Task.builder()
//                .delay(1, TimeUnit.SECONDS)
//                .interval(AtherysBattlegrounds.getConfig().RESPAWN_INTERVAL, TimeUnit.SECONDS)
//                .execute(this::respawnTick)
//                .submit(AtherysBattlegrounds.getInstance());
//    }
//
//    /**
//     * Queues a new respawn for the given player to the given point. If that player already has another
//     * respawn queued, this will return false.
//     *
//     * @param player The players to be queued
//     * @param point  the point to queue for
//     * @return true if successful, false if player is already queued for respawn
//     */
//    public boolean queueRespawn(UUID player, RespawnPoint point) {
//        Respawn respawn = new Respawn(player, point, System.currentTimeMillis());
//
//        if (respawns.containsKey(player)) return false;
//
//        respawns.put(player, respawn);
//        return true;
//    }
//
//    /**
//     * If the given player has been queued for a respawn, this will set the respawn state to ready,
//     * and when the appropriate time has passed, the player will be teleported to the respawn point.
//     * <p>
//     * If this method is not called, the player's respawn will expire, and they will not be teleported.
//     *
//     * @param player The player whose respawn to confirm
//     * @return true if successful, false if the player has not been queued for a respawn
//     */
//    public boolean confirmRespawn(UUID player) {
//        if (!respawns.containsKey(player)) return false;
//
//        respawns.get(player).setReady(true);
//        return true;
//    }
//
//    private void respawnPlayer(Player player) {
//        Respawn respawn = respawns.get(player.getUniqueId());
//        if (respawn == null) return;
//
//        long currentTimestamp = System.currentTimeMillis();
//
//        if (!respawn.isReady()) {
//
//            // if the respawn is not confirmed, and it's duration has run out, clear it
//            if (currentTimestamp - respawn.getTimestamp() >= AtherysBattlegrounds.getConfig().RESPAWN_DURATION) {
//                respawns.remove(player.getUniqueId());
//            }
//
//            // return, as the respawn has not been confirmed
//        } else {
//
//            // if the respawn is confirmed, and it's duration has not run out, return
//            if (currentTimestamp - respawn.getTimestamp() < AtherysBattlegrounds.getConfig().RESPAWN_DURATION) {
//                return;
//            }
//
//            player.setLocation(respawn.getPoint().getRandomPointWithin());
//        }
//    }
//
//    private void respawnTick() {
//        respawns.forEach((uuid, respawn) -> Sponge.getServer().getPlayer(uuid).ifPresent(this::respawnPlayer));
//    }
//
//    public Task getRespawnTask() {
//        return respawnTask;
//    }
//
//    public static RespawnService getInstance() {
//        return instance;
//    }
}
