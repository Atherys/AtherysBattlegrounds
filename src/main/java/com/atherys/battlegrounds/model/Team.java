package com.atherys.battlegrounds.model;

import org.spongepowered.api.text.format.TextColor;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Team {

    private String id;

    private String name;

    private TextColor color;

    public Team(String id) {
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

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }
}
