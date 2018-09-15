package com.atherys.battlegrounds;

import com.atherys.battlegrounds.listener.BattlepointListener;
import com.atherys.battlegrounds.model.Battlepoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.persistence.BattlegroundsDatabase;
import com.atherys.battlegrounds.persistence.TeamMemberManager;
import com.atherys.battlegrounds.service.BattlepointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.service.TeamService;
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
                @Dependency( id = "atheryscore" )
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

    private BattlegroundsDatabase database;

    private TeamMemberManager teamMemberManager;

    private TeamService teamService;
    private BattlepointService battlepointService;
    private RespawnService respawnService;

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
        teamService = TeamService.getInstance();
        battlepointService = BattlepointService.getInstance();

        Sponge.getRegistry().registerModule(Team.class, teamService);
        Sponge.getRegistry().registerModule(Battlepoint.class, battlepointService);

        database = BattlegroundsDatabase.getInstance();

        respawnService = RespawnService.getInstance();

        teamMemberManager = TeamMemberManager.getInstance();
        teamMemberManager.loadAll();

        Sponge.getEventManager().registerListeners(this, new BattlepointListener());
    }

    private void stop() {
        teamMemberManager.saveAll();
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

    public TeamService getTeamService() {
        return teamService;
    }

    public BattlepointService getBattlepointService() {
        return battlepointService;
    }

    public BattlegroundsDatabase getDatabase() {
        return database;
    }

    public RespawnService getRespawnService() {
        return respawnService;
    }

    public TeamMemberManager getTeamMemberManager() {
        return teamMemberManager;
    }
}
