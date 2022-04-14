package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.battlegrounds.integration.AtherysTownsIntegration;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.economy.Economy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Team;
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
        Set<TeamConfig> configs = config.TEAMS;
        List<Team> scoreboardTeams;

        if (Sponge.getPluginManager().isLoaded("atherystowns")) {
            configs = AtherysTownsIntegration.fetchNationsAndConvertToTeamConfigs();
            AtherysBattlegrounds.getInstance().getLogger().info("Configs: {}", configs.size());
        }
        scoreboardTeams = configs.stream()
                .map(teamConfig -> teamService.createTeam(teamConfig.getId(), teamConfig.getName(), teamConfig.getColor()).getScoreboardTeam())
                .collect(Collectors.toList());

        teamService.setScoreboard(scoreboardTeams);
    }

    public void onPlayerJoin(Player source) {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        if (teamMember.getTeam() != null) {
            teamMember.getTeam().getScoreboardTeam().addMember(source.getTeamRepresentation());
        }
        teamService.addPlayerToScoreboard(source);
    }

    public void showTeamInfo(MessageReceiver receiver, BattleTeam team) {
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
        BattleTeam team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        showTeamInfo(source, team);
    }

    public void removePlayerFromTeam(Player player) {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(player);
        BattleTeam team = teamMember.getTeam();

        if (team == null) return;

        team.getScoreboardTeam().removeMember(player.getTeamRepresentation());
        setTeamPermission(player, team, false);
        teamMemberService.removeTeamMemberFromTeam(teamMember);
    }

    public void leaveTeam(Player source) throws CommandException {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);
        BattleTeam team = teamMember.getTeam();

        if (team == null) {
            throw msg.exception(Text.of("You are not part of a team!"));
        }

        team.getScoreboardTeam().removeMember(source.getTeamRepresentation());
        setTeamPermission(source, team, false);
        teamMemberService.removeTeamMemberFromTeam(teamMember);

        msg.error(source, "You have left ", team, ".");
    }

    private void removePlayer(Player player, TeamMember teamMember, BattleTeam team) {
        team.getScoreboardTeam().removeMember(player.getTeamRepresentation());
        setTeamPermission(player, team, false);
        teamMemberService.removeTeamMemberFromTeam(teamMember);
    }

    public void addPlayerToTeam(Player source, BattleTeam team) {
        TeamMember teamMember = teamMemberService.getOrCreateTeamMember(source);

        if (teamMember.getTeam() != null) {
            setTeamPermission(source, teamMember.getTeam(), false);
        }

        teamMemberService.addTeamMemberToTeam(team, teamMember);
        team.getScoreboardTeam().addMember(source.getTeamRepresentation());
        teamService.addPlayerToScoreboard(source);
        setTeamPermission(source, team, true);

        msg.info(source, "You have joined the team ", team, ".");
    }

    private void setTeamPermission(Player player, BattleTeam team, boolean value) {
        player.getSubjectData().setPermission(
                SubjectData.GLOBAL_CONTEXT,
                "atherysbattlegrounds.team." + team.getId(),
                value ? Tristate.TRUE : Tristate.FALSE
        );
    }

    public Map<String, BattleTeam> getTeamChoices() {
        Map<String, BattleTeam> choices = new HashMap<>();

        teamService.getAllTeams().forEach(team -> {
            AtherysBattlegrounds.getInstance().getLogger().info("Yup we added a team!");
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
