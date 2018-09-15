package com.atherys.battlegrounds.model;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Objects;

@ConfigSerializable
public class Team implements CatalogType {

    public static final Team NONE = new Team("none", "Nobody", TextColors.WHITE);

    @Setting
    private String id;

    @Setting
    private String name;

    @Setting
    private TextColor color;

    private Team() {}

    public Team(String id, String name, TextColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public TextColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(name, team.name) &&
                Objects.equals(color, team.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
