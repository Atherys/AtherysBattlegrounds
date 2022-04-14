package com.atherys.battlegrounds.integration;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.towns.AtherysTowns;
import com.atherys.towns.api.event.ResidentEvent;
import com.atherys.towns.model.entity.Resident;
import com.atherys.towns.model.entity.Town;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class AtherysTownsIntegration {

    public static Set<TeamConfig> fetchNationsAndConvertToTeamConfigs() {
        return AtherysTowns.getInstance().getNationService().getAllNations()
                .stream()
                .map(nation -> {
                    TeamConfig teamConfig = new TeamConfig();
                    teamConfig.setId(nation.getId().toString());
                    teamConfig.setName(nation.getName());
                    teamConfig.setColor(nation.getColor());

                    return teamConfig;
                })
                .collect(Collectors.toSet());
    }

    public static void updatePlayerTeam(Player player) {
        Optional<Town> town = AtherysTowns.getInstance().getResidentFacade().getPlayerTown(player);

        if (town.isPresent() && town.get().getNation() != null) {
            BattleTeam team = AtherysBattlegrounds.getInstance().getTeamService().getTeamFromId(town.get().getNation().getId().toString()).get();
            AtherysBattlegrounds.getInstance().getTeamFacade().addPlayerToTeam(player, team);
        } else {
            AtherysBattlegrounds.getInstance().getTeamFacade().removePlayerFromTeam(player);
        }
    }

    @Listener
    public void onPlayerJoinTown(ResidentEvent.JoinedTown event) {
        Sponge.getServer().getPlayer(event.getResident().getId()).ifPresent(AtherysTownsIntegration::updatePlayerTeam);
    }

    @Listener
    public void onPlayerLeaveTown(ResidentEvent.LeftTown event) {
        Sponge.getServer().getPlayer(event.getResident().getId()).ifPresent(player -> {
            AtherysBattlegrounds.getInstance().getTeamFacade().removePlayerFromTeam(player);
        });
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Root Player player) {
        updatePlayerTeam(player);
    }
}
