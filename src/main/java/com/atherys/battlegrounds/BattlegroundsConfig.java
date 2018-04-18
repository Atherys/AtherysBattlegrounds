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
    public final long RESPAWN_TIMEOUT = 60*1000;

    @Setting( "respawn_tick" )
    public final long RESPAWN_TICK = 60;

    protected BattlegroundsConfig() throws IOException {
        super( "config/" + AtherysBattlegrounds.ID, "config.conf" );
    }

}
