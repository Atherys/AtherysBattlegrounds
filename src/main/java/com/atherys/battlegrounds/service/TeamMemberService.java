package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.battlegrounds.model.entity.PlayerRanking;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.persistence.PlayerRankingRepository;
import com.atherys.battlegrounds.persistence.TeamMemberRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TeamMemberService {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private PlayerRankingRepository playerRankingRepository;

    @Inject
    private TeamMemberRepository teamMemberRepository;

    public TeamMemberService() {
    }

    public TeamMember getOrCreateTeamMember(Player player) {
        return teamMemberRepository.findById(player.getUniqueId()).orElseGet(() -> {
            TeamMember teamMember = new TeamMember(player.getUniqueId());

            PlayerRanking playerRanking = new PlayerRanking();
            playerRankingRepository.saveOne(playerRanking);

            teamMember.setRanking(playerRanking);

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

    public void switchRankings(TeamMember victimTeamMember, TeamMember attackerTeamMember) {
        PlayerRanking temp = victimTeamMember.getRanking();

        victimTeamMember.setRanking(attackerTeamMember.getRanking());
        attackerTeamMember.setRanking(temp);

        teamMemberRepository.saveOne(victimTeamMember);
        teamMemberRepository.saveOne(attackerTeamMember);
    }

    public String getTeamMemberRankName(TeamMember teamMember) {
        return teamMemberRepository.fetchTeamMemberRankName(teamMember);
    }

    public void rankPlayerLast(TeamMember teamMember) {
        teamMember.setRanking(new PlayerRanking());

        teamMemberRepository.saveOne(teamMember);
    }
}
