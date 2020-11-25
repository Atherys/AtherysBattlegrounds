package com.atherys.battlegrounds.integration;

import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.towns.AtherysTowns;

import java.util.Set;
import java.util.stream.Collectors;

public final class AtherysTownsIntegration {

    public static Set<TeamConfig> fetchNationsAndConvertToTeamConfigs() {
        return AtherysTowns.getInstance().getNationService().getAllNations()
                .stream()
                .map(nation -> {
                    TeamConfig teamConfig = new TeamConfig();
                    teamConfig.setId(nation.getId().toString());
                    teamConfig.setName(nation.getName());
                    teamConfig.setColor(nation.getColor());

                    return teamConfig;
                })
                .collect(Collectors.toSet());
    }

}
