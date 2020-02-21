package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.AtherysCore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.HashMap;
import java.util.Map;

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

        Text.Builder bank = Text.builder();

        AtherysCore.getEconomyService().flatMap(economyService -> economyService.getOrCreateAccount(team.getId())).ifPresent(account -> {
            account.getBalances().forEach((currency, balance) -> {
                bank.append(Text.of(currency.getDisplayName(), ": ", balance.doubleValue()));
            });
        });

        Text bankHoverButton = Text.builder()
                .color(TextColors.BLUE)
                .style(TextStyles.ITALIC)
                .append(Text.of("[Here]"))
                .onHover(TextActions.showText(bank.build()))
                .build();

        msg.info(source, Text.of("You are currently part of \"", team, "\". To view your team's bank, hover ",  bankHoverButton));
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
}
