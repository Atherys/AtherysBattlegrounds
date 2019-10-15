package com.atherys.battlegrounds.config;

import com.atherys.battlegrounds.model.RespawnPoint;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashSet;
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

    @Setting("respawn-duration")
    private Duration respawnDuration = Duration.of(30, ChronoUnit.SECONDS);

    @Setting("respawn-points")
    private Set<RespawnPointConfig> respawnPoints = new HashSet<>();

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

    public Duration getRespawnDuration() {
        return respawnDuration;
    }

    public Set<RespawnPointConfig> getRespawnPoints() {
        return respawnPoints;
    }
}
