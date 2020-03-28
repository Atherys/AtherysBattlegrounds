package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.model.Team;
import com.atherys.core.economy.Economy;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TeamService {

    private Set<Team> teams = new HashSet<>();

    public TeamService() {
    }

    public Team createTeam(String id, String name, TextColor color) {
        Team team = new Team(id);
        team.setName(name);
        team.setColor(color);

        teams.add(team);

        return team;
    }

    public void distributeAward(AwardConfig award, Team team) {
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

    public Set<Team> getAllTeams() {
        return teams;
    }

    public Optional<Team> getTeamFromId(String teamId) {
        return teams.parallelStream().filter(team -> team.getId().equals(teamId)).findFirst();
    }
}
