package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.model.Team;

import javax.persistence.AttributeConverter;

public class TeamTypeAdapter implements AttributeConverter<Team, String> {
    @Override
    public String convertToDatabaseColumn(Team attribute) {
        return attribute.getId();
    }

    @Override
    public Team convertToEntityAttribute(String dbData) {
        // TODO: Fetch team from the persisted id via the TeamService
        return null;
    }
}
