package com.atherys.battlegrounds.entity;

import com.atherys.core.db.Identifiable;
import com.flowpowered.math.vector.Vector3d;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "respawn_point")
public class RespawnPoint implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // The battlepoint this respawn point belongs to
    @ManyToOne
    @JoinColumn(name = "battlepoint_id")
    private Battlepoint battlepoint;

    private Vector3d position;

    // the radius within which players may be respawned
    private double radius;

    public RespawnPoint() {
    }

    @Nonnull
    @Override
    public Long getId() {
        return id;
    }

    public Battlepoint getBattlepoint() {
        return battlepoint;
    }

    public void setBattlepoint(Battlepoint battlepoint) {
        this.battlepoint = battlepoint;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
