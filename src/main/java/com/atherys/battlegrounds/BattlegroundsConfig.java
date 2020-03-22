package com.atherys.battlegrounds;

import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.core.utils.PluginConfig;
import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class BattlegroundsConfig extends PluginConfig {

    @Setting("is-default")
    public boolean IS_DEFAULT = true;

    @Setting("battle-points")
    public Set<BattlePointConfig> BATTLE_POINTS = new HashSet<>();

    @Setting("teams")
    public Set<TeamConfig> TEAMS = new HashSet<>();

    @Setting("tick-interval")
    public Duration TICK_INTERVAL = Duration.of(1, ChronoUnit.SECONDS);

    @Setting("respawn-interval")
    public Duration RESPAWN_INTERVAL = Duration.of(5, ChronoUnit.SECONDS);

    @Setting("minimum-players-required-to-capture-point")
    public int MINIMUM_PLAYERS_REQUIRED_TO_CAPTURE_POINT;

    @Setting("title-fade-ticks")
    public int TITLE_FADE_TICKS = 7;

    @Setting("title-stay-ticks")
    public int TITLE_STAY_TICKS = 10;

    protected BattlegroundsConfig() throws IOException {
        super("config/" + AtherysBattlegrounds.ID, "config.conf");
        init();
    }
}
