package com.atherys.battlegrounds.events;

import com.atherys.battlegrounds.point.BattlePoint;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class BattlePointTickEvent implements Event, Cancellable {

    private Cause cause;
    private boolean cancelled;

    private BattlePoint point;

    public BattlePointTickEvent( BattlePoint point ) {
        this.point = point;
        this.cause = Cause.builder().append( point ).build( Sponge.getCauseStackManager().getCurrentContext() );
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

    public BattlePoint getPoint() {
        return point;
    }
}
