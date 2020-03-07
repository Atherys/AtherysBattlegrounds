package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.PlayerRanking;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.AtherysCore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class TeamFacade {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private TeamService teamService;

    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private BattlegroundMessagingFacade msg;

    public TeamFacade() {
    }

    public void init() {
        config.TEAMS.forEach(teamConfig -> teamService.createTeam(
                teamConfig.getId(),
                teamConfig.getName(),
                teamConfig.getColor()
        ));
    }

    public void showTeamInfo(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Team team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        msg.info(source, Text.of("You are currently part of \"", team, "\"."));
    }

    public void removePlayerFromTeam(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Team team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        teamMemberService.removeTeamMemberFromTeam(team, teamMember);

        msg.error(source, Text.of("You have left the team \"", team, "\""));
    }

    public void addPlayerToTeam(Player source, Team team) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);

        teamMemberService.addTeamMemberToTeam(team, teamMember);

        msg.info(source, Text.of("You have joined the team \"", team, "\""));
    }

    public Map<String, Team> getTeamChoices() {
        Map<String, Team> choices = new HashMap<>();

        teamService.getAllTeams().forEach(team -> {
            choices.put(team.getId(), team);
        });

        return choices;
    }

    public void rankPlayers(Player victim, Player attacker) {
        TeamMember attackerTeamMember = teamMemberService.getOrCreateTeamMember(attacker);
        TeamMember victimTeamMember = teamMemberService.getOrCreateTeamMember(victim);

        // If both players are on the same team, no rankings will be changed
        if (Objects.equals(attackerTeamMember.getTeam(), victimTeamMember.getTeam())) {
            return;
        }

        PlayerRanking victimRanking = victimTeamMember.getRanking();

        // If the attacker is higher position than their victim, switch the ranking positions
        if (victimRanking.getPosition() <= attackerTeamMember.getRanking().getPosition()) {
            teamMemberService.switchRankings(victimTeamMember, attackerTeamMember);

            PlayerRanking attackerRanking = attackerTeamMember.getRanking();

            msg.error(victim, "You have died to a player of lower rank. Your new ranking position is: ", victimRanking.getPosition());
            msg.info(attacker, "You have killed a player of higher ranking. Your new ranking position is: ", attackerRanking.getPosition());
        }
    }

    public void sendPlayerRanking(Player source) {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);

        if (teamMember.getRanking() == null) {
            teamMemberService.rankPlayerLast(teamMember);
        }

        msg.info(source, "Your current ranking position is ", teamMember.getRanking().getPosition(), ". Your rank is \"", teamMemberService.getTeamMemberRankName(teamMember),"\"");
    }

    public void sendPlayerRankList(Player source) {
        msg.info(source, "Not yet implemented");
    }
}
