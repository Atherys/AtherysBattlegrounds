package com.atherys.battlegrounds.team;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.util.UUID;

public class Team {

    private UUID uuid;

    private TextColor color;
    private Text name;
    private Text description;

    public UUID getUUID() {
        return uuid;
    }

    public TextColor getColor() {
        return color;
    }

    public Text getName() {
        return name;
    }

    public Text getDescription() {
        return description;
    }

    public void addTeamMember( TeamMember teamMember ) {
        teamMember.joinSecondaryTeam( this );
    }

    public void removeTeamMember( TeamMember teamMember ) {
        teamMember.leaveSecondaryTeam( this );
    }
}
