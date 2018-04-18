package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.BattleMsg;
import com.atherys.battlegrounds.respawn.Respawn;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RespawnManager {

    private static RespawnManager instance = new RespawnManager();

    private Map<UUID, Respawn> respawnQueue = new HashMap<>();

    private RespawnManager() {
    }

    public void queueRespawn( Respawn respawn ) {
        this.respawnQueue.put( respawn.getPlayer(), respawn );
    }

    public void tick() {
        Sponge.getServer().getOnlinePlayers().forEach( player -> {
            Respawn respawn = respawnQueue.get( player.getUniqueId() );
            if ( respawn != null ) respawn.invoke( player );
            BattleMsg.info( player, "You have been teleported to a respawn point within the Battlepoint where you last perished." );
            respawnQueue.remove( player.getUniqueId() );
        } );
    }

    public static RespawnManager getInstance() {
        return instance;
    }

}
