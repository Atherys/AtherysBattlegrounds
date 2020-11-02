package com.atherys.battlegrounds.model;

import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.Objects;

public class BattleTeam implements TextRepresentable {

    private String id;

    private String name;

    private TextColor color;

    private Team scoreboardTeam;

    public BattleTeam(String id) {
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

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    public void setScoreboardTeam(Team scoreboardTeam) {
        this.scoreboardTeam = scoreboardTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleTeam team = (BattleTeam) o;
        return id.equals(team.id) &&
                Objects.equals(name, team.name) &&
                Objects.equals(color, team.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public Text toText() {
        return Text.of(color, name, TextColors.RESET);
    }
}
