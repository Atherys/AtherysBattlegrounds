package com.atherys.battlegrounds.event;

import com.atherys.battlegrounds.model.Battlepoint;
import com.atherys.battlegrounds.model.Team;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public abstract class BattlepointEvent implements Event {

    protected Cause cause;

    private Battlepoint battlepoint;

    public BattlepointEvent(Battlepoint battlepoint) {
        this.battlepoint = battlepoint;
        this.cause = Cause.of(Sponge.getCauseStackManager().getCurrentContext(), battlepoint);
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    public Battlepoint getBattlepoint() {
        return battlepoint;
    }

    /**
     * Triggered at the end of every Battlepoint tick
     */
    public static class Tick extends BattlepointEvent {
        public Tick(Battlepoint battlepoint) {
            super(battlepoint);
        }
    }

    /**
     * Triggered at the end of every Battlepoint tick
     */
    public static class Capture extends Tick {

        private Team team;

        public Capture(Battlepoint battlepoint, Team team) {
            super(battlepoint);
            this.team = team;
            this.cause = Cause.of(Sponge.getCauseStackManager().getCurrentContext(), battlepoint, team);
        }

        public Team getTeam() {
            return team;
        }
    }

}
