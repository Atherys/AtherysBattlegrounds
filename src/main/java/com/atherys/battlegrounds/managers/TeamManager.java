package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.team.Team;
import com.atherys.battlegrounds.team.TeamMember;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class TeamManager {

    private static TeamManager instance = new TeamManager();

    private List<Team> teams = new ArrayList<>();
    private Map<UUID,TeamMember> teamMembers = new HashMap<>();

    private TeamManager() {
    }

    public void registerTeam ( Team team ) {
        if ( !teams.contains( team ) ) this.teams.add( team );
    }

    public void removeTeam( Team team ) {
        teams.remove( team );
    }

    public Optional<Team> getPrimaryTeam( Player player ) {
        TeamMember member = teamMembers.get( player.getUniqueId() );
        if ( member == null ) return Optional.empty();
        return Optional.of( member.getPrimaryTeam() );
    }

    public static TeamManager getInstance() {
        return instance;
    }

}
