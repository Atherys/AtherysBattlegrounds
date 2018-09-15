package com.atherys.battlegrounds.service;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.Team;
import org.spongepowered.api.registry.CatalogRegistryModule;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public class TeamService implements CatalogRegistryModule<Team> {

    private static final TeamService instance = new TeamService();

    @Override
    @Nonnull
    public Optional<Team> getById(@Nonnull String id) {
        for (Team team : getAll()) {
            if ( team.getId().equals(id) ) return Optional.of(team);
        }
        return Optional.empty();
    }

    @Override
    @Nonnull
    public Collection<Team> getAll() {
        return AtherysBattlegrounds.getConfig().TEAMS;
    }

    public static TeamService getInstance() {
        return instance;
    }
}
