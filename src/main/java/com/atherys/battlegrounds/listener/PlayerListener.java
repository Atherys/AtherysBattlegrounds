package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

@Singleton
public class PlayerListener {

    @Inject
    private BattlePointFacade battlePointFacade;

    public PlayerListener() {
    }

    @Listener
    public void onPlayerMovement(MoveEntityEvent event, @Root Player player) {
        battlePointFacade.onPlayerMovement(player, event.getFromTransform(), event.getToTransform());
    }

}
