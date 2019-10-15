package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.persistence.TeamTypeAdapter;
import com.atherys.core.db.SpongeIdentifiable;

import javax.annotation.Nonnull;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class TeamMember implements SpongeIdentifiable {

    @Id
    private UUID id;

    @Convert(converter = TeamTypeAdapter.class)
    private Team primary;

    @Convert(converter = TeamTypeAdapter.class)
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
