package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.persistence.TeamTypeAdapter;
import com.atherys.core.db.SpongeIdentifiable;

import javax.annotation.Nonnull;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class TeamMember implements SpongeIdentifiable {

    @Id
    private UUID id;

    @Convert(converter = TeamTypeAdapter.class)
    private Team team;

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
}
