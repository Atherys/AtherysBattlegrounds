package com.atherys.battlegrounds.event;

import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.Team;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public abstract class BattlePointEvent implements Event {

    protected Cause cause;

    private BattlePoint battlepoint;

    public BattlePointEvent(BattlePoint battlepoint) {
        this.battlepoint = battlepoint;
        this.cause = Cause.of(Sponge.getCauseStackManager().getCurrentContext(), battlepoint);
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    public BattlePoint getBattlepoint() {
        return battlepoint;
    }

    /**
     * Triggered at the end of every Battlepoint tick
     */
    public static class Tick extends BattlePointEvent {
        public Tick(BattlePoint battlepoint) {
            super(battlepoint);
        }
    }

    /**
     * Triggered at the end of every Battlepoint tick
     */
    public static class Capture extends Tick {

        private Team team;

        public Capture(BattlePoint battlepoint, Team team) {
            super(battlepoint);
            this.team = team;
            this.cause = Cause.of(Sponge.getCauseStackManager().getCurrentContext(), battlepoint, team);
        }

        public Team getTeam() {
            return team;
        }
    }

}
