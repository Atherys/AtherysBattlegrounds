package com.atherys.battlegrounds.team;

import com.atherys.battlegrounds.point.BattlePoint;

import java.util.List;
import java.util.UUID;

/**
 * Wrapper object around a Player. Uses UUID internally to keep track of its player.
 */
public class TeamMember {

    private UUID player;

    private Team primaryTeam;
    private List<Team> secondaryTeams;

    public UUID getUUID() {
        return player;
    }

    /**
     * The primary team is the one the TeamMember will be capturing {@link BattlePoint}s for.
     * @return The primary team this player is part of
     */
    public Team getPrimaryTeam() {
        return primaryTeam;
    }

    public void joinSecondaryTeam( Team team ) {
        this.secondaryTeams.add( team );
    }

    public void leaveSecondaryTeam( Team team ) {
        if ( this.secondaryTeams.contains( team ) ) secondaryTeams.remove( team );
    }

    /**
     * A secondary team will not receive any benefits from the capture of {@link BattlePoint}s.
     * @return The list of secondary teams this player is a part of
     */
    public List<Team> getSecondaryTeams() {
        return secondaryTeams;
    }
}
