package com.atherys.battlegrounds;

import com.atherys.battlegrounds.commands.RespawnReadyCommand;
import com.atherys.battlegrounds.listeners.PlayerListener;
import com.atherys.battlegrounds.managers.BattlePointManager;
import com.atherys.battlegrounds.managers.RespawnManager;
import com.atherys.battlegrounds.managers.TeamManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.IOException;

import static com.atherys.battlegrounds.AtherysBattlegrounds.*;

@Plugin(
        id = ID,
        name = NAME,
        description = DESCRIPTION,
        version = VERSION,
        dependencies = {
                @Dependency( id = "atheryscore" ),
                @Dependency( id = "atherystowns" )
        }
)
public class AtherysBattlegrounds {

    static final String ID = "atherysbattlegrounds";
    static final String NAME = "A'therys Battlegrounds";
    static final String DESCRIPTION = "A capture-and-hold mini-game intended to be played in the open-world for the A'therys Horizons server";
    static final String VERSION = "1.0.0a";

    private static AtherysBattlegrounds instance;
    private static boolean init;

    @Inject
    Logger logger;

    private BattlegroundsConfig config;

    private void init() {
        instance = this;

        try {
            config = new BattlegroundsConfig();
        } catch ( IOException e ) {
            init = false;
            logger.info( "Failed to create config." );
            e.printStackTrace();
            return;
        }

        if ( config.IS_DEFAULT ) {
            logger.info( "AtherysBattlegrounds config.conf is set to default. Change 'is_default' to false in order for the plugin to continue." );
        }

        init = true;
    }

    private void start() {
        Sponge.getEventManager().registerListeners( this, new PlayerListener() );

        BattlePointManager.getInstance();
        RespawnManager.getInstance();
        TeamManager.getInstance();

        Sponge.getCommandManager().register( this, new RespawnReadyCommand().getSpec() );
    }

    private void stop() {

    }

    @Listener
    public void onInit( GameInitializationEvent event ) {
        init();
    }

    @Listener
    public void onStart( GameStartingServerEvent event ) {
        if ( init ) start();
    }

    @Listener
    public void onStop( GameStoppingServerEvent event ) {
        if ( init ) stop();
    }

    public static AtherysBattlegrounds getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public static BattlegroundsConfig getConfig() {
        return getInstance().config;
    }

}
