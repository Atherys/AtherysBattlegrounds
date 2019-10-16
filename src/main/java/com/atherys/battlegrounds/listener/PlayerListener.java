package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.atherys.battlegrounds.facade.RespawnFacade;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;

@Singleton
public class PlayerListener {

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
    public void onPlayerDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player) {
        respawnFacade.offerRespawn(player);
    }

}
