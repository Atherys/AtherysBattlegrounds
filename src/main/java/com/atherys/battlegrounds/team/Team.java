package com.atherys.battlegrounds.team;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;

import java.util.UUID;

@ConfigSerializable
public class Team {

    @Setting
    private UUID uuid;

    private Text name;
    private Text description;

    public void addTeamMember ( TeamMember teamMember ) {
        teamMember.joinSecondaryTeam(this);
    }

    public void removeTeamMember( TeamMember teamMember ) {
        teamMember.joinSecondaryTeam( this );
    }

}
