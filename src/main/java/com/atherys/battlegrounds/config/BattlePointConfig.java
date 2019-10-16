package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ConfigSerializable
public class BattlePointConfig {

    @Setting("id")
    private String id;

    @Setting("name")
    private String name;

    @Setting("color")
    private BossBarColor color;

    @Setting("location")
    private Location<World> location;

    @Setting("inner-radius")
    private double innerRadius;

    @Setting("outer-radius")
    private double outerRadius;

    @Setting("capture-amount-per-tick")
    private float perTickCaptureAmount = 0.01f;

    @Setting("respawn-interval")
    private Duration respawnInterval = Duration.of(1, ChronoUnit.SECONDS);

    @Setting("respawn-timeout")
    private Duration respawnTimeout = Duration.of(30, ChronoUnit.SECONDS);

    @Setting("respawn-points")
    private List<RespawnPointConfig> respawnPoints = new ArrayList<>();

    @Setting("on-capture-awards")
    private Set<AwardConfig> onCaptureAwards = new HashSet<>();

    @Setting("on-tick-awards")
    private Set<AwardConfig> onTickAwards = new HashSet<>();

    public BattlePointConfig() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BossBarColor getColor() {
        return color;
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

    public float getPerTickCaptureAmount() {
        return perTickCaptureAmount;
    }

    public Duration getRespawnInterval() {
        return respawnInterval;
    }

    public Duration getRespawnTimeout() {
        return respawnTimeout;
    }

    public List<RespawnPointConfig> getRespawnPoints() {
        return respawnPoints;
    }

    public Set<AwardConfig> getOnCaptureAwards() {
        return onCaptureAwards;
    }

    public Set<AwardConfig> getOnTickAwards() {
        return onTickAwards;
    }
}
