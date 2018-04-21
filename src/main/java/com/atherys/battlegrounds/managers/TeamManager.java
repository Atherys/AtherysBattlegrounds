package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeamManager {

    private static TeamManager instance = new TeamManager();

    private List<Team> teams = new ArrayList<>();

    private TeamManager () {
        teams = AtherysBattlegrounds.getConfig().TEAMS;
    }

    public Optional<Team> getTeam ( UUID uuid ) {
        for ( Team team : teams ) {
            if ( team.getUUID().equals( uuid ) ) return Optional.of( team );
        }
        return Optional.empty();
    }

    public void registerTeam ( Team team ) {
        if ( !teams.contains( team ) ) this.teams.add( team );
    }

    public void removeTeam( Team team ) {
        teams.remove( team );
    }

    public TeamManager getInstance() {
        return instance;
    }

}
