package com.atherys.battlegrounds.listeners;

import com.atherys.battlegrounds.BattleMsg;
import com.atherys.battlegrounds.managers.BattlePointManager;
import com.atherys.battlegrounds.point.BattlePoint;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import java.util.Optional;

public class PlayerListener {

    @Listener
    public void onPlayerMove( MoveEntityEvent event, @Root Player player ) {
        Optional<BattlePoint> point = BattlePointManager.getInstance().getPointFromLocation( event.getToTransform().getLocation() );
        point.ifPresent( battlePoint -> BattleMsg.info( player, "You have entered ", battlePoint.getName() ) );
    }

    @Listener
    public void onPlayerDeath( DestructEntityEvent.Death event ) {
        Living entity = event.getTargetEntity();
        if ( !( entity instanceof Player ) ) return;

        Optional<BattlePoint> point = BattlePointManager.getInstance().getPointFromLocation( entity.getLocation() );
        point.ifPresent( battlePoint -> battlePoint.respawn( ( Player ) entity ) );
    }

}
