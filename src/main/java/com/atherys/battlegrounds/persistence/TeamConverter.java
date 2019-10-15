package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.Team;

import javax.persistence.AttributeConverter;

public class TeamConverter implements AttributeConverter<Team, String> {
    @Override
    public String convertToDatabaseColumn(Team attribute) {
        return attribute.getId();
    }

    @Override
    public Team convertToEntityAttribute(String dbData) {
        return AtherysBattlegrounds.getInstance().getTeamService().getTeamFromId(dbData).orElse(null);
    }
}
