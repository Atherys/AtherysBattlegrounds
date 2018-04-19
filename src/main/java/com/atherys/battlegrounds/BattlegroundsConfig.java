package com.atherys.battlegrounds;

import com.atherys.battlegrounds.point.BattlePoint;
import com.atherys.core.utils.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BattlegroundsConfig extends PluginConfig {

    @Setting( "is_default" )
    public boolean IS_DEFAULT = true;

    @Setting( "battle_points" )
    public List<BattlePoint> BATTLE_POINTS = new ArrayList<>();

    @Setting( "respawn_timeout" )
    public long RESPAWN_TIMEOUT = 60*1000;

    @Setting( "respawn_tick" )
    public long RESPAWN_TICK = 60;

    @Setting( "minimum_players" )
    public int MIN_PLAYERS_PER_TEAM = 3;

    @Setting( "capture_tick" )
    public long CAPTURE_TICK = 10;

    @Setting( "capture_rate" )
    public double CAPTURE_RATE = 10;

    @Setting( "max_capture" )
    public double MAX_CAPTURE = 100;

    protected BattlegroundsConfig() throws IOException {
        super( "config/" + AtherysBattlegrounds.ID, "config.conf" );
    }

}
