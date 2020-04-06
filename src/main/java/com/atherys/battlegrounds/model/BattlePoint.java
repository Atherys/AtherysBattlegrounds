package com.atherys.battlegrounds.model;

import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.utils.ColorUtils;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.*;

public class BattlePoint implements TextRepresentable {

    private String id;

    private String name;

    private ServerBossBar bossBar;

    private Location<World> location;

    private double innerRadius;

    private double outerRadius;

    private float perTickCaptureAmount;

    private float perMemberTickCaptureAmount;

    private float maxPerTickCaptureAmount;

    private Duration respawnInterval;

    private Duration respawnTimeout;

    private List<RespawnPoint> respawnPoints = new ArrayList<>();

    private AwardConfig captureAward;

    private AwardConfig tickAward;

    private AwardConfig killAward;

    private Map<BattleTeam, Float> teamProgress = new HashMap<>();

    private BattleTeam controllingTeam;

    public BattlePoint(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location<World> getLocation() {
        return location;
    }

    public void setLocation(Location<World> location) {
        this.location = location;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public float getPerTickCaptureAmount() {
        return perTickCaptureAmount;
    }

    public void setPerTickCaptureAmount(float perTickCaptureAmount) {
        this.perTickCaptureAmount = perTickCaptureAmount;
    }

    public float getPerMemberTickCaptureAmount() {
        return perMemberTickCaptureAmount;
    }

    public void setPerMemberTickCaptureAmount(float perMemberTickCaptureAmount) {
        this.perMemberTickCaptureAmount = perMemberTickCaptureAmount;
    }

    public float getMaxPerTickCaptureAmount() {
        return maxPerTickCaptureAmount;
    }

    public void setMaxPerTickCaptureAmount(float maxPerTickCaptureAmount) {
        this.maxPerTickCaptureAmount = maxPerTickCaptureAmount;
    }

    public Duration getRespawnInterval() {
        return respawnInterval;
    }

    public void setRespawnInterval(Duration respawnInterval) {
        this.respawnInterval = respawnInterval;
    }

    public Duration getRespawnTimeout() {
        return respawnTimeout;
    }

    public void setRespawnTimeout(Duration respawnTimeout) {
        this.respawnTimeout = respawnTimeout;
    }

    public List<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }

    public void setRespawnPoints(List<RespawnPoint> respawnPoints) {
        this.respawnPoints = respawnPoints;
    }

    public AwardConfig getCaptureAward() {
        return captureAward;
    }

    public void setCaptureAward(AwardConfig captureAward) {
        this.captureAward = captureAward;
    }

    public AwardConfig getTickAward() {
        return tickAward;
    }

    public void setTickAward(AwardConfig tickAward) {
        this.tickAward = tickAward;
    }

    public AwardConfig getKillAward() {
        return killAward;
    }

    public void setKillAward(AwardConfig killAward) {
        this.killAward = killAward;
    }

    public Map<BattleTeam, Float> getTeamProgress() {
        return teamProgress;
    }

    public void setTeamProgress(Map<BattleTeam, Float> teamProgress) {
        this.teamProgress = teamProgress;
    }

    public BattleTeam getControllingTeam() {
        return controllingTeam;
    }

    public void setControllingTeam(BattleTeam controllingTeam) {
        this.controllingTeam = controllingTeam;
    }

    public ServerBossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(ServerBossBar bossBar) {
        this.bossBar = bossBar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattlePoint that = (BattlePoint) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BattlePoint{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bossBar=" + bossBar +
                ", location=" + location +
                ", innerRadius=" + innerRadius +
                ", outerRadius=" + outerRadius +
                ", perTickCaptureAmount=" + perTickCaptureAmount +
                ", respawnInterval=" + respawnInterval +
                ", respawnTimeout=" + respawnTimeout +
                ", respawnPoints=" + respawnPoints +
                ", captureAward=" + captureAward +
                ", tickAward=" + tickAward +
                ", teamProgress=" + teamProgress +
                ", controllingTeam=" + controllingTeam +
                '}';
    }

    @Override
    public Text toText() {
        return Text.of(ColorUtils.bossBarColorToTextColor(bossBar.getColor()), name, TextColors.RESET);
    }
}
