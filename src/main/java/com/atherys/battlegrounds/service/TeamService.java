package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.Award;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.core.AtherysCore;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public class TeamService {

    public void distributeTickAwards(BattlePoint battlePoint, Team team) {
        distributeAwards(battlePoint.getTickAwards(), team);
    }

    public void distributeCaptureAwards(BattlePoint battlePoint, Team team) {
        distributeAwards(battlePoint.getCaptureAwards(), team);
    }

    protected void distributeAwards(Set<Award> awards, Team team) {
        awards.forEach(award -> {
            AtherysCore.getEconomyService().flatMap(economyService -> economyService.getOrCreateAccount(team.getId())).ifPresent(account -> {
                award.getCurrency().forEach((c, amount) -> account.deposit(c, BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), AtherysBattlegrounds.getInstance())));
            });
        });
    }

//    private static final TeamService instance = new TeamService();
//
//    @Override
//    @Nonnull
//    public Optional<Team> getById(@Nonnull String id) {
//        for (Team team : getAll()) {
//            if ( team.getId().equals(id) ) return Optional.of(team);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    @Nonnull
//    public Collection<Team> getAll() {
//        return AtherysBattlegrounds.getConfig().TEAMS;
//    }
//
//    public static TeamService getInstance() {
//        return instance;
//    }
}
