package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.BattleTeam;

import javax.persistence.AttributeConverter;

public class TeamConverter implements AttributeConverter<BattleTeam, String> {
    @Override
    public String convertToDatabaseColumn(BattleTeam attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public BattleTeam convertToEntityAttribute(String dbData) {
        return AtherysBattlegrounds.getInstance().getTeamService().getTeamFromId(dbData).orElse(null);
    }
}
