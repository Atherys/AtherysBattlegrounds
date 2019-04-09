package com.atherys.battlegrounds.entity;

import com.atherys.core.db.Identifiable;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;
import java.util.UUID;

@Entity(name = "team_member")
public class TeamMember implements Identifiable<UUID> {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "primary_team_id")
    private Team primaryTeam;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_member_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams;

    public TeamMember() {
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Team getPrimaryTeam() {
        return primaryTeam;
    }

    public void setPrimaryTeam(Team primaryTeam) {
        this.primaryTeam = primaryTeam;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
