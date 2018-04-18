package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattleMsg;
import com.atherys.battlegrounds.respawn.Respawn;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RespawnManager {

    private static RespawnManager instance = new RespawnManager();

    private Map<UUID, Respawn> respawnQueue = new HashMap<>();

    private RespawnManager() {
        Task.builder()
                .name( "atherysbattlegrounds-respawn-manager" )
                .interval( AtherysBattlegrounds.getConfig().RESPAWN_TICK, TimeUnit.SECONDS )
                .execute( this::tick )
                .submit( AtherysBattlegrounds.getInstance() );
    }

    public void queueRespawn( Respawn respawn ) {
        this.respawnQueue.put( respawn.getPlayer(), respawn );
    }

    public void tick() {
        Sponge.getServer().getOnlinePlayers().forEach( player -> {
            Respawn respawn = respawnQueue.get( player.getUniqueId() );
            if ( respawn != null ) {
                if ( respawn.isValid() ) {
                    if ( respawn.isReady() ) {
                        respawn.invoke( player );
                        BattleMsg.info( player, "You have been teleported to a respawn point within the Battlepoint where you last perished." );
                        respawnQueue.remove( player.getUniqueId() );
                    }
                } else {
                    BattleMsg.warn( player, "Your respawn has expired and you will no longer be able to rejoin the fight." );
                }
            }
        } );
    }

    public static RespawnManager getInstance() {
        return instance;
    }

    public void setReady( Player player, boolean value ) {
        Respawn respawn = respawnQueue.get( player.getUniqueId() );
        if ( respawn != null ) respawn.setReady( value );
    }
}
