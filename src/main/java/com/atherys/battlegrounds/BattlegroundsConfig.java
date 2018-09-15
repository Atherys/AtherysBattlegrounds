package com.atherys.battlegrounds;

import com.atherys.battlegrounds.model.Battlepoint;
import com.atherys.battlegrounds.model.RespawnPoint;
import com.atherys.battlegrounds.model.Team;
import com.atherys.core.database.mongo.MongoDatabaseConfig;
import com.atherys.core.utils.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BattlegroundsConfig extends PluginConfig {

    @Setting
    public MongoDatabaseConfig DATABASE = new MongoDatabaseConfig();

    @Setting
    public boolean IS_DEFAULT = true;

    @Setting
    public Set<Battlepoint> BATTLEPOINTS = new HashSet<>();

    {
        Battlepoint defaultBattlepoint = new Battlepoint(
                "default-point",
                "Default Point",
                new Location<>(
                        Sponge.getServer().getWorld("world").get(),
                        0.0d,
                        0.0d,
                        0.0d
                ),
                10.0,
                100.0
        );

        defaultBattlepoint.addRespawnPoint(new RespawnPoint(
                new Location<>(
                        Sponge.getServer().getWorld("world").get(),
                        10.0d,
                        0.0d,
                        10.0d
                ),
                10.0d
        ));

        BATTLEPOINTS.add(defaultBattlepoint);
    }

    @Setting
    public long BATTLEPOINTS_UPDATE_INTERVAL = 500;

    @Setting
    public float CAPTURE_AMOUNT = 0.01f;

    @Setting
    public Set<Team> TEAMS = new HashSet<>();

    {
        TEAMS.add(Team.NONE);
    }

    @Setting
    public long RESPAWN_INTERVAL = 1;

    @Setting
    public long RESPAWN_DURATION = 30;

    protected BattlegroundsConfig() throws IOException {
        super("config/" + AtherysBattlegrounds.ID, "config.conf");
    }

}
