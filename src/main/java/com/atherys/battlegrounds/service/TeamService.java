package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.core.AtherysCore;
import com.google.inject.Singleton;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
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

    public void distributeAwards(Set<Award> awards, Team team) {
        awards.forEach(award -> {
            AtherysCore.getEconomyService().flatMap(economyService -> economyService.getOrCreateAccount(team.getId())).ifPresent(account -> {
                award.getCurrency().forEach((c, amount) -> account.deposit(c, BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), AtherysBattlegrounds.getInstance())));
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
