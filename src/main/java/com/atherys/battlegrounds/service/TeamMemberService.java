package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.persistence.TeamMemberRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

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

    public TeamMember getOrCreateTeamMember(User user) {
        return teamMemberRepository.findById(user.getUniqueId()).orElseGet(() -> {
            TeamMember teamMember = new TeamMember(user.getUniqueId());

            teamMemberRepository.saveOne(teamMember);
            return teamMember;
        });
    }

    protected Optional<BattleTeam> determineCapturingTeam(Map<BattleTeam, Set<Player>> onlineTeamMembersWithinInnerRadius) {
        BattleTeam capturingTeam = null;

        for (Map.Entry<BattleTeam, Set<Player>> entry : onlineTeamMembersWithinInnerRadius.entrySet()) {
            if (entry.getValue().size() >= config.MINIMUM_PLAYERS_REQUIRED_TO_CAPTURE_POINT) {

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

    public void removeTeamMemberFromTeam(TeamMember teamMember) {
        teamMember.setTeam(null);
        teamMemberRepository.saveOne(teamMember);
    }

    public void addTeamMemberToTeam(BattleTeam team, TeamMember teamMember) {
        teamMember.setTeam(team);
        teamMemberRepository.saveOne(teamMember);
    }

    public void setMilestone(TeamMember teamMember, int milestone) {
        teamMember.setMilestone(milestone);
        teamMemberRepository.saveOne(teamMember);
    }

    public void setAwardedMilestones(TeamMember teamMember, int milestones) {
        teamMember.setMilestonesAwarded(milestones);
        teamMemberRepository.saveOne(teamMember);
    }
}
