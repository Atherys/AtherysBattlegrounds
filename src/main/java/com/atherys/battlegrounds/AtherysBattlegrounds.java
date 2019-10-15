package com.atherys.battlegrounds;

import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.atherys.battlegrounds.facade.TeamFacade;
import com.atherys.battlegrounds.persistence.TeamMemberRepository;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.service.TeamService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

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
    private Logger logger;

    @Inject
    private Injector spongeInjector;

    private Injector battlegroundsInjector;

    private Components components;

    public static AtherysBattlegrounds getInstance() {
        return instance;
    }

    private void init() {
        instance = this;

        components = new Components();
        battlegroundsInjector = spongeInjector.createChildInjector();
        battlegroundsInjector.injectMembers(components);

        init = true;
    }

    private void start() {
        components.teamMemberRepository.initCache();

        components.battlePointFacade.init();
        components.teamFacade.init();
    }

    private void stop() {
        components.teamMemberRepository.flushCache();
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

    private static class Components {

        @Inject
        private BattlegroundsConfig config;

        @Inject
        private TeamMemberRepository teamMemberRepository;

        @Inject
        private BattlePointService battlePointService;

        @Inject
        private RespawnService respawnService;

        @Inject
        private TeamService teamService;

        @Inject
        private BattlePointFacade battlePointFacade;

        @Inject
        private TeamFacade teamFacade;
    }

}
