package com.atherys.battlegrounds.entity;

import com.atherys.core.db.Identifiable;
import com.flowpowered.math.vector.Vector3i;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Battlepoint implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String color;

    // the center position of the battlepoint sphere
    private Vector3i position;

    // the radius of the capturing sphere, within which players can increase their team's influence on the point
    private double captureRadius;

    // the radius of the respawn sphere, within which players who die may be respawned at one of the respawn points
    private double respawnRadius;

    @OneToMany(mappedBy = "battlepoint")
    private Set<RespawnPoint> respawnPoints;

    // the team which currently holds the battlepoint
    @JoinColumn(name = "owning_team_id")
    private Team owningTeam;

    public Battlepoint() {
    }

    @Nonnull
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public double getCaptureRadius() {
        return captureRadius;
    }

    public void setCaptureRadius(double captureRadius) {
        this.captureRadius = captureRadius;
    }

    public double getRespawnRadius() {
        return respawnRadius;
    }

    public void setRespawnRadius(double respawnRadius) {
        this.respawnRadius = respawnRadius;
    }

    public Set<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }

    public void setRespawnPoints(Set<RespawnPoint> respawnPoints) {
        this.respawnPoints = respawnPoints;
    }

    public Team getOwningTeam() {
        return owningTeam;
    }

    public void setOwningTeam(Team owningTeam) {
        this.owningTeam = owningTeam;
    }
}
