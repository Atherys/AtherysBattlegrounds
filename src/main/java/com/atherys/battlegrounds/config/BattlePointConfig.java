package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.boss.BossBarColor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class BattlePointConfig {

    @Setting("id")
    private String id;

    @Setting("name")
    private String name;

    @Setting("color")
    private BossBarColor color;

    @Setting("location")
    private LocationConfig location;

    @ConfigSerializable
    public static class LocationConfig {

        @Setting("world")
        private String world;

        @Setting("x")
        private double x;

        @Setting("y")
        private double y;

        @Setting("z")
        private double z;

        public LocationConfig() {
        }

        public String getWorld() {
            return world;
        }

        public void setWorld(String world) {
            this.world = world;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }

    @Setting("inner-radius")
    private double innerRadius;

    @Setting("outer-radius")
    private double outerRadius;

    @Setting("capture-amount-per-tick")
    private float perTickCaptureAmount = 0.01f;

    @Setting("max-capture-amount-per-tick")
    private float maxPerTickCaptureAmount = 0.01f;

    @Setting("capture-amount-per-member-per-tick")
    private float perMemberTickCaptureAmount = 0.01f;

    @Setting("respawn-interval")
    private Duration respawnInterval = Duration.of(1, ChronoUnit.SECONDS);

    @Setting("respawn-timeout")
    private Duration respawnTimeout = Duration.of(30, ChronoUnit.SECONDS);

    @Setting("respawn-points")
    private List<RespawnPointConfig> respawnPoints = new ArrayList<>();

    @Setting("on-capture-awards")
    private AwardConfig onCaptureAward = new AwardConfig();

    @Setting("on-tick-awards")
    private AwardConfig onTickAward = new AwardConfig();

    @Setting("on-kill-awards")
    private AwardConfig onKillAward = new AwardConfig();

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

    public LocationConfig getLocation() {
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

    public float getMaxPerTickCaptureAmount() {
        return maxPerTickCaptureAmount;
    }

    public float getPerMemberTickCaptureAmount() {
        return perMemberTickCaptureAmount;
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

    public AwardConfig getOnCaptureAward() {
        return onCaptureAward;
    }

    public AwardConfig getOnTickAward() {
        return onTickAward;
    }

    public AwardConfig getOnKillAward() {
        return onKillAward;
    }
}
