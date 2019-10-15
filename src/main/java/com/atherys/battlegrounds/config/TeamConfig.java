package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.text.format.TextColor;

@ConfigSerializable
public class TeamConfig {

    @Setting("id")
    private String id;

    @Setting("name")
    private String name;

    @Setting("color")
    private BossBarColor color;

    public TeamConfig() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BossBarColor getColor() {
        return color;
    }

    public void setColor(BossBarColor color) {
        this.color = color;
    }
}
