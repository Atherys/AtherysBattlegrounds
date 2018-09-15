package com.atherys.battlegrounds;

import com.atherys.battlegrounds.model.Battlepoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.battlegrounds.serialize.LocationTypeSerializer;
import com.atherys.core.database.mongo.MongoDatabaseConfig;
import com.atherys.core.utils.PluginConfig;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BattlegroundsConfig extends PluginConfig {

    @Setting("database")
    public MongoDatabaseConfig DATABASE = new MongoDatabaseConfig();

    @Setting("is_default")
    public boolean IS_DEFAULT = true;

    @Setting("battlepoints")
    public Set<Battlepoint> BATTLEPOINTS = new HashSet<>();

//    {
//        Battlepoint defaultBattlepoint = new Battlepoint(
//                "default-point",
//                "Default Point",
//                new Location<>(
//                        Sponge.getServer().getWorld("world").get(),
//                        0.0d,
//                        0.0d,
//                        0.0d
//                ),
//                10.0,
//                100.0
//        );
//
//        defaultBattlepoint.addRespawnPoint(new RespawnPoint(
//                new Location<>(
//                        Sponge.getServer().getWorld("world").get(),
//                        10.0d,
//                        0.0d,
//                        10.0d
//                ),
//                10.0d
//        ));
//
//        BATTLEPOINTS.add(defaultBattlepoint);
//    }

    @Setting("tick_interval")
    public long BATTLEPOINTS_UPDATE_INTERVAL = 500;

    @Setting("capture_amount_per_tick")
    public float CAPTURE_AMOUNT = 0.01f;

    @Setting("teams")
    public Set<Team> TEAMS = new HashSet<>();

    {
        TEAMS.add(Team.NONE);
    }

    @Setting("respawn_interval")
    public long RESPAWN_INTERVAL = 1;

    @Setting("respawn_duration")
    public long RESPAWN_DURATION = 30;

    protected BattlegroundsConfig() throws IOException {
        super("config/" + AtherysBattlegrounds.ID, "config.conf");
    }

    @Override
    protected ConfigurationOptions getOptions() {
        ConfigurationOptions options = super.getOptions();
        TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();

        serializers.registerType(new TypeToken<Location<World>>() {}, new LocationTypeSerializer());

        options.setSerializers(serializers);
        return options;
    }
}
