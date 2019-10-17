package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.persistence.TeamConverter;
import com.atherys.core.db.SpongeIdentifiable;

import javax.annotation.Nonnull;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TeamMember implements SpongeIdentifiable {

    @Id
    private UUID id;

    @Convert(converter = TeamConverter.class)
    private Team team;

    protected TeamMember() {}

    public TeamMember(UUID player) {
        this.id = player;
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMember that = (TeamMember) o;
        return id.equals(that.id) &&
                Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team);
    }
}
