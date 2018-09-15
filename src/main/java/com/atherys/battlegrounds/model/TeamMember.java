package com.atherys.battlegrounds.model;

import com.atherys.core.database.api.DBObject;
import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Wrapper object around a Player. Uses UUID internally to keep track of its player.
 */
public class TeamMember implements DBObject {

    @Expose
    private UUID player;

    @Expose
    private Team primary;

    @Expose
    private Set<Team> teams = new HashSet<>();

    public TeamMember(UUID player) {
        this.player = player;
    }

    @Override
    @Nonnull
    public UUID getUniqueId() {
        return player;
    }

    public Optional<Team> getPrimary() {
        return Optional.ofNullable(primary);
    }

    /**
     * Sets the primary team for this player. This is the team that they will be capturing Battlepoints for.
     *
     * @param team The team to set as primary
     * @return true if successful, false if the player is not part of the team ( add the team first, prior to setting as primary )
     */
    public boolean setPrimary(Team team) {
        if ( !hasTeam(team) ) return false;

        this.primary = team;
        return true;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public boolean hasTeam(Team team) {
        return teams.contains(team);
    }

    public boolean addTeam(Team team) {
        return this.teams.add(team);
    }

    public boolean removeTeam(Team team) {
        return this.teams.remove(team);
    }
}
