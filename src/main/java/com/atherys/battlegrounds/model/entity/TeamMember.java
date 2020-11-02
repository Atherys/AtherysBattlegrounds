package com.atherys.battlegrounds.model.entity;

import com.atherys.battlegrounds.model.BattleTeam;
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
    private BattleTeam team;

    private String cachedName;

    private int milestone = -1;

    private int milestonesAwarded = -1;

    protected TeamMember() {}

    public TeamMember(UUID player) {
        this.id = player;
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    public BattleTeam getTeam() {
        return team;
    }

    public void setTeam(BattleTeam team) {
        this.team = team;
    }

    public String getCachedName() {
        return cachedName;
    }

    public void setCachedName(String cachedName) {
        this.cachedName = cachedName;
    }

    public int getMilestone() {
        return milestone;
    }

    public void setMilestone(int milestone) {
        this.milestone = milestone;
    }

    public int getMilestonesAwarded() {
        return milestonesAwarded;
    }

    public void setMilestonesAwarded(int milestonesAwarded) {
        this.milestonesAwarded = milestonesAwarded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMember that = (TeamMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, cachedName);
    }
}
