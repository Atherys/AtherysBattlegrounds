package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.PlayerRanking;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.AtherysCore;
import com.atherys.core.economy.Economy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Tristate;

import java.util.*;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.*;

@Singleton
public class TeamFacade {

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private TeamService teamService;

    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private BattlePointService battlePointService;

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

    public void showTeamInfo(MessageReceiver receiver, Team team) {
        List<Text> info = new ArrayList<>();

        info.add(Text.of(DARK_GRAY, "[]====[ ", team.getColor(), team.getName(), DARK_GRAY, " ]====[]"));

        Economy.getAccount(team.getId()).map(Account::getBalances).ifPresent(currencies -> currencies.forEach((currency, amount) -> {
            if (config.TEAM_CURRENCIES.contains(currency)) {
                info.add(Text.of(DARK_GREEN, currency.getPluralDisplayName(), ": ", GOLD, amount.toString()));
            }
        }));

        String points = battlePointService.getControlledPoints(team).stream()
                .map(BattlePoint::getName)
                .collect(Collectors.joining(", "));

        info.add(Text.of(DARK_GREEN, "Controlling: ", GOLD, points));

        info.forEach(receiver::sendMessage);
    }

    public void showTeamInfo(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Team team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        showTeamInfo(source, team);
    }

    public void removePlayerFromTeam(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        Team team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        source.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, "atherysbattlegrounds.teams." + team.getId(), Tristate.FALSE);

        teamMemberService.removeTeamMemberFromTeam(team, teamMember);

        msg.error(source, "You have left ", team, ".");
    }

    public void addPlayerToTeam(Player source, Team team) {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);

        teamMemberService.addTeamMemberToTeam(team, teamMember);
        source.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, "atherysbattlegrounds.teams." + team.getId(), Tristate.TRUE);

        msg.info(source, "You have joined the team ", team, ".");
    }

    public Map<String, Team> getTeamChoices() {
        Map<String, Team> choices = new HashMap<>();

        teamService.getAllTeams().forEach(team -> {
            choices.put(team.getId(), team);
        });

        return choices;
    }

    public void grantPointsOnKill(Player victim, Player killer) {
        TeamMember killerMember = teamMemberService.getOrCreateTeamMember(killer);

        if (killerMember.getTeam() == null) return;

        if (killerMember.getTeam().equals(teamMemberService.getOrCreateTeamMember(victim).getTeam())) return;

        AwardConfig award = battlePointService.getBattlePointFromLocation(victim.getLocation()).map(BattlePoint::getKillAward).orElse(config.KILL_AWARD);

        teamService.distributeAwardsToMembers(award, Collections.singleton(killer));
    }
}
