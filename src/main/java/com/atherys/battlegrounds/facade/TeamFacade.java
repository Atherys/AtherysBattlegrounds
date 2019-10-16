package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        msg.info(source, Text.of("You are currently part of \"", team.getColor(), team.getName(), TextColors.RESET, "\"."));
    }

    public void removePlayerFromTeam(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Team team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        teamMemberService.removeTeamMemberFromTeam(team, teamMember);
    }

    public void addPlayerToTeam(Player source, String teamId) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Optional<Team> team = teamService.getTeamFromId(teamId);

        if (!team.isPresent()) {
            throw msg.exception("No such team exists!");
        }

        teamMemberService.addTeamMemberToTeam(team.get(), teamMember);
    }

    public Map<String, Team> getTeamChoices() {
        Map<String, Team> choices = new HashMap<>();

        teamService.getAllTeams().forEach(team -> {
            choices.put(team.getId(), team);
        });

        return choices;
    }
}
