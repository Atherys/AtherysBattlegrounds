package com.atherys.battlegrounds.respawn;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class Respawn {

    private UUID player;
    private RespawnPoint point;

    private boolean ready;
    private long timestamp;

    public Respawn ( RespawnPoint point, Player player ) {
        this.point = point;
        this.player = player.getUniqueId();
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getPlayer() {
        return player;
    }

    public RespawnPoint getPoint() {
        return point;
    }

    public boolean isReady() {
        return ready;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setReady( boolean ready ) {
        this.ready = ready;
    }

    public boolean isValid() {
        return System.currentTimeMillis() - timestamp > AtherysBattlegrounds.getConfig().RESPAWN_TIMEOUT;
    }

    public void invoke( Player player ) {
        if ( player.getUniqueId() != this.player ) return;
        player.setLocation( point.getRandomPointWithin() );
    }
}
