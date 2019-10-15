package com.atherys.battlegrounds;

import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.battlegrounds.serialize.DurationTypeSerializer;
import com.atherys.battlegrounds.serialize.LocationTypeSerializer;
import com.atherys.core.utils.PluginConfig;
import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
    public Duration TICK_INTERVAL = Duration.of(500, ChronoUnit.MILLIS);

    @Setting("minimum-players-required-to-capture-point")
    public int MINIMUM_PLAYERS_REQUIRED_TO_CAPTURE_POINT;

    protected BattlegroundsConfig() throws IOException {
        super("config/" + AtherysBattlegrounds.ID, "config.conf");
        init();
    }

    @Override
    protected ConfigurationOptions getOptions() {
        ConfigurationOptions options = super.getOptions();
        TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();

        serializers.registerType(new TypeToken<Location<World>>() {}, new LocationTypeSerializer());
        serializers.registerType(new TypeToken<Duration>() {}, new DurationTypeSerializer());
        options.setSerializers(serializers);
        return options;
    }
}
