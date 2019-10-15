package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.persistence.TeamMemberRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TeamMemberService {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private TeamMemberRepository teamMemberRepository;

    public TeamMemberService() {
    }

    public TeamMember getOrCreateTeamMember(Player player) {
        return teamMemberRepository.findById(player.getUniqueId()).orElseGet(() -> {
            TeamMember teamMember = new TeamMember(player.getUniqueId());
            teamMemberRepository.saveOne(teamMember);
            return teamMember;
        });
    }

    protected Optional<Team> determineCapturingTeam(Set<TeamMember> onlineTeamMembersWithinInnerRadius) {
        Map<Team, Integer> numberOfPlayersFromEachTeam = new HashMap<>();

        onlineTeamMembersWithinInnerRadius.forEach(teamMember -> {
            // if the player isn't part of a team, just return
            if (teamMember.getTeam() == null) {
                return;
            }

            if (numberOfPlayersFromEachTeam.containsKey(teamMember.getTeam())) {
                numberOfPlayersFromEachTeam.merge(teamMember.getTeam(), 1, Integer::sum);
            } else {
                numberOfPlayersFromEachTeam.put(teamMember.getTeam(), 1);
            }
        });

        Team capturingTeam = null;

        for (Map.Entry<Team, Integer> entry : numberOfPlayersFromEachTeam.entrySet()) {
            if (entry.getValue() >= config.MINIMUM_PLAYERS_REQUIRED_TO_CAPTURE_POINT) {

                // If a capturing team has already been found, and another also meets the criteria,
                // then return that no team is currently capturing until there is only one single team that meets the criteria
                // if multiple teams can be considered to be capturing the point, then nobody is capturing the point.
                if (capturingTeam != null) {
                    return Optional.empty();
                }

                capturingTeam = entry.getKey();
            }
        }

        return Optional.ofNullable(capturingTeam);
    }

    public void removeTeamMemberFromTeam(Team team, TeamMember teamMember) {
        teamMember.setTeam(null);
        teamMemberRepository.saveOne(teamMember);
    }

    public void addTeamMemberToTeam(Team team, TeamMember teamMember) {
        teamMember.setTeam(team);
        teamMemberRepository.saveOne(teamMember);
    }
}
