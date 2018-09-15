package com.atherys.battlegrounds.model;

import com.atherys.core.utils.MathUtils;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.*;

@ConfigSerializable
public class Battlepoint implements CatalogType {

    @Setting("id")
    private String id;

    @Setting("name")
    private String name;

    @Setting("color")
    private BossBarColor color;

    @Setting("location")
    private Location<World> location;

    @Setting("inner_radius")
    private double innerRadius;

    @Setting("outer_radius")
    private double outerRadius;

    @Setting("respawn_points")
    private Set<RespawnPoint> respawnPoints = new HashSet<>();

    @Setting("team_progress")
    private Map<Team,Float> teamProgress = new HashMap<>();

    private Battlepoint() {}

    public Battlepoint(String id, String name, Location<World> location, BossBarColor color, double innerRadius, double outerRadius) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.color = color;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

    public Location<World> getLocation() {
        return location;
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

    public boolean hasRespawnPoint(RespawnPoint respawnPoint) {
        return respawnPoints.contains(respawnPoint);
    }

    public boolean addRespawnPoint(RespawnPoint respawnPoint) {
        return respawnPoints.add(respawnPoint);
    }

    public boolean removeRespawnPoint(RespawnPoint respawnPoint) {
        return respawnPoints.remove(respawnPoint);
    }

    public Map<Team, Float> getAllTeamProgress() {
        return teamProgress;
    }

    public float getTeamProgress(Team team) {
        return teamProgress.getOrDefault(team, 0.0f);
    }

    public void setTeamProgress(Team team, float progress) {
        teamProgress.put(team, progress);
    }

    public void addTeamProgress(Team team, float amount) {
        setTeamProgress(team, MathUtils.clamp(0.0f, 1.0f, getTeamProgress(team) + amount));
    }

    public void removeTeamProgress(Team team, float amount) {
        addTeamProgress(team, -amount);
    }

    public BossBarColor getColor() {
        return color;
    }
}
