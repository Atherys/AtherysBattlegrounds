package com.atherys.battlegrounds.model;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BattlePoint {

    private String id;

    private String name;

    private BossBarColor color;

    private World world;

    private Vector3d position;

    private double innerRadius;

    private double outerRadius;

    private Set<RespawnPoint> respawnPoints = new HashSet<>();

    private Map<Team, Float> teamProgress = new HashMap<>();

    public BattlePoint(String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public BossBarColor getColor() {
        return color;
    }

    public World getWorld() {
        return world;
    }

    public Vector3d getPosition() {
        return position;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public Set<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }

    public Map<Team, Float> getTeamProgress() {
        return teamProgress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(BossBarColor color) {
        this.color = color;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public void setInnerRadius(double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public void setOuterRadius(double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public void setRespawnPoints(Set<RespawnPoint> respawnPoints) {
        this.respawnPoints = respawnPoints;
    }

    public void setTeamProgress(Map<Team, Float> teamProgress) {
        this.teamProgress = teamProgress;
    }
}
