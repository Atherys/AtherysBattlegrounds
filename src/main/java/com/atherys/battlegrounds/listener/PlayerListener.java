package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.atherys.battlegrounds.facade.RespawnFacade;
import com.atherys.battlegrounds.facade.TeamFacade;
import com.atherys.core.utils.EntityUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

@Singleton
public class PlayerListener {

    @Inject
    private TeamFacade teamFacade;

    @Inject
    private BattlePointFacade battlePointFacade;

    @Inject
    private RespawnFacade respawnFacade;

    public PlayerListener() {
    }

    @Listener
    public void onPlayerMovement(MoveEntityEvent event, @Getter("getTargetEntity") Player player) {
        battlePointFacade.onPlayerMovement(player, event.getFromTransform(), event.getToTransform());
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player victim, @Root EntityDamageSource source) {
        EntityUtils.playerAttackedEntity(source).ifPresent(attacker -> teamFacade.grantPointsOnKill(victim, attacker));
    }

    @Listener
    public void onPlayerDeathAny(DestructEntityEvent event, @Getter("getTargetEntity") Player victim) {
        respawnFacade.offerRespawn(victim);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Root Player player) {
        battlePointFacade.onPlayerJoin(player);
        teamFacade.onPlayerJoin(player);
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event, @Root Player player) {
        battlePointFacade.onPlayerDisconnect(player);
    }
}
