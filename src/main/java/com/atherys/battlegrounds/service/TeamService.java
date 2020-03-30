package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.core.economy.Economy;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TeamService {

    private Set<BattleTeam> teams = new HashSet<>();

    private Scoreboard scoreboard;

    public TeamService() {
    }

    public void setScoreboard(List<Team> scoreboardTeams) {
        this.scoreboard = Scoreboard.builder()
                .teams(scoreboardTeams)
                .build();
    }

    public void addPlayerToScoreboard(Player source) {
        source.setScoreboard(scoreboard);
    }

    public BattleTeam createTeam(String id, String name, TextColor color) {
        BattleTeam team = new BattleTeam(id);
        team.setName(name);
        team.setColor(color);
        team.setScoreboardTeam(
                Team.builder()
                        .color(color)
                        .prefix(Text.of(color))
                        .name(name)
                        .build()
        );

        teams.add(team);

        return team;
    }

    public void distributeAward(AwardConfig award, BattleTeam team) {
        Economy.getAccount(team.getId()).ifPresent(account -> {
            award.getCurrency().forEach(((currency, amount) -> {
                account.deposit(currency, BigDecimal.valueOf(amount), Sponge.getCauseStackManager().getCurrentCause());
            }));
        });
    }

    public void distributeAwardsToMembers(AwardConfig award, Set<Player> players) {
        players.forEach(player -> {
            award.getCurrency().forEach((currency, amount) -> {
                Economy.addCurrency(player.getUniqueId(), currency, BigDecimal.valueOf(amount), Sponge.getCauseStackManager().getCurrentCause());
            });
        });
    }

    public Set<BattleTeam> getAllTeams() {
        return teams;
    }

    public Optional<BattleTeam> getTeamFromId(String teamId) {
        return teams.parallelStream().filter(team -> team.getId().equals(teamId)).findFirst();
    }
}
