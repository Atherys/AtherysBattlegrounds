package com.atherys.battlegrounds.model;

import org.spongepowered.api.text.format.TextColor;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Team {

    private String id;

    private String name;

    private TextColor color;

    private Team() {}

    @Nonnull
    public String getId() {
        return id;
    }

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
