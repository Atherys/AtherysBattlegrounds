package com.atherys.battlegrounds;

import com.atherys.battlegrounds.command.TeamCommand;
import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.atherys.battlegrounds.facade.RespawnFacade;
import com.atherys.battlegrounds.facade.TeamFacade;
import com.atherys.battlegrounds.listener.BattlePointListener;
import com.atherys.battlegrounds.listener.PlayerListener;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.persistence.TeamMemberRepository;
import com.atherys.battlegrounds.serialize.DurationTypeSerializer;
import com.atherys.battlegrounds.service.BattlePointService;
import com.atherys.battlegrounds.service.RespawnService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.AtherysCore;
import com.atherys.core.command.CommandService;
import com.atherys.core.event.AtherysHibernateConfigurationEvent;
import com.atherys.core.event.AtherysHibernateInitializedEvent;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.time.Duration;

import static com.atherys.battlegrounds.AtherysBattlegrounds.*;

@Plugin(
        id = ID,
        name = NAME,
        description = DESCRIPTION,
        version = VERSION,
        dependencies = {
                @Dependency(id = "atheryscore")
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

        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Duration.class), new DurationTypeSerializer());

        components = new Components();
        battlegroundsInjector = spongeInjector.createChildInjector();
        battlegroundsInjector.injectMembers(components);

        // init the team facade now in order to populate team choices for the team command
        components.teamFacade.init();

        Sponge.getEventManager().registerListeners(this, components.battlePointListener);
        Sponge.getEventManager().registerListeners(this, components.playerListener);

        try {
            AtherysCore.getCommandService().register(new TeamCommand(), this);
        } catch (CommandService.AnnotatedCommandException e) {
            e.printStackTrace();
        }

        init = true;
    }

    private void start() {
        components.battlePointFacade.init();
        components.respawnFacade.init();

        components.teamMemberRepository.initCache();
    }

    private void stop() {
        components.teamMemberRepository.flushCache();
    }

    @Listener
    public void onHibernateRegistration(AtherysHibernateConfigurationEvent event) {
        event.registerEntity(TeamMember.class);
    }

    @Listener
    public void onHibernateInit(AtherysHibernateInitializedEvent event) {
        init();
    }

    @Listener
    public void onStart(GameStartingServerEvent event) {
        if (init) start();
    }

    @Listener
    public void onStop(GameStoppingServerEvent event) {
        if (init) stop();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        components.battlePointFacade.reload();
    }

    public TeamFacade getTeamFacade() {
        return components.teamFacade;
    }

    public TeamService getTeamService() {
        return components.teamService;
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
        private RespawnFacade respawnFacade;

        @Inject
        private BattlePointFacade battlePointFacade;

        @Inject
        private TeamFacade teamFacade;

        @Inject
        private PlayerListener playerListener;

        @Inject
        private BattlePointListener battlePointListener;
    }

}
