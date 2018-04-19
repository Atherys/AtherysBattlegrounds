package com.atherys.battlegrounds.events;

import com.atherys.battlegrounds.point.BattlePoint;
import com.atherys.battlegrounds.team.Team;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

public class BattlePointCaptureEvent implements org.spongepowered.api.event.Event {

    private Cause cause;

    private BattlePoint point;
    private Team team;

    public BattlePointCaptureEvent( BattlePoint point, Team team ) {
        this.point = point;
        this.team = team;
        this.cause = Cause.builder()
                .append( point )
                .append( team )
                .build( Sponge.getCauseStackManager().getCurrentContext() );
    }

    @Override
    public Cause getCause() {
        return cause;
    }
}
