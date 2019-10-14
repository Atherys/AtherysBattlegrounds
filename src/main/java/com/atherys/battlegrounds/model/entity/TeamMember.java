package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.Team;
import com.atherys.core.db.SpongeIdentifiable;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamMember implements SpongeIdentifiable {

    private UUID id;

    private Team primary;

    private Set<Team> teams = new HashSet<>();

    public TeamMember(UUID player) {
        this.id = player;
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    public Team getPrimary() {
        return primary;
    }

    public void setPrimary(Team primary) {
        this.primary = primary;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
