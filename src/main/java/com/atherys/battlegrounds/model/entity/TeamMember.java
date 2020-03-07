package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.persistence.TeamConverter;
import com.atherys.core.db.SpongeIdentifiable;
import org.hibernate.annotations.Fetch;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TeamMember implements SpongeIdentifiable {

    @Id
    private UUID id;

    @Convert(converter = TeamConverter.class)
    private Team team;

    private String cachedName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PlayerRanking ranking;

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

    public String getCachedName() {
        return cachedName;
    }

    public void setCachedName(String cachedName) {
        this.cachedName = cachedName;
    }

    public PlayerRanking getRanking() {
        return ranking;
    }

    public void setRanking(PlayerRanking ranking) {
        this.ranking = ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMember that = (TeamMember) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(team, that.team) &&
                Objects.equals(cachedName, that.cachedName) &&
                Objects.equals(ranking, that.ranking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, cachedName, ranking);
    }
}
