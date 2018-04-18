package com.atherys.battlegrounds.respawn;

import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class Respawn {

    private UUID player;
    private RespawnPoint point;

    private boolean ready;

    public Respawn ( RespawnPoint point, Player player ) {
        this.point = point;
        this.player = player.getUniqueId();
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

    public void setReady( boolean ready ) {
        this.ready = ready;
    }

    public void invoke( Player player ) {
        if ( player.getUniqueId() != this.player ) return;
        player.setLocation( point.getRandomPointWithin() );
    }
}
