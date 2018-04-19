package com.atherys.battlegrounds.events;

import com.atherys.battlegrounds.respawn.Respawn;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class RespawnTickEvent implements Event, Cancellable {

    private Cause cause;
    private boolean cancelled;

    private Respawn respawn;

    public RespawnTickEvent( Respawn respawn ) {
        this.respawn = respawn;
        this.cause = Cause.builder()
                .append( respawn.getPoint() )
                .append( respawn )
                .build( Sponge.getCauseStackManager().getCurrentContext() );
        this.respawn = respawn;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled( boolean cancel ) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    public Respawn getRespawn() {
        return respawn;
    }
}
