package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.model.entity.TeamMember;

import java.util.Optional;
import java.util.Set;

public class TeamService { //} implements CatalogRegistryModule<Team> {

    public void distributeTickAwards(BattlePoint battlePoint, Team team) {

    }

    public void distributeCaptureAwards(BattlePoint battlePoint, Team team) {

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
