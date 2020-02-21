package com.atherys.battlegrounds;

import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.battlegrounds.serialize.DurationTypeSerializer;
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

    @Setting("battlepoint-cooldown")
    public Duration BATTLEPOINT_COOLDOWN = Duration.of(5, ChronoUnit.MINUTES);

    @Setting("reputation-currency")
    public String REPUTATION_CURRENCY = "atherys:reputation";

    @Setting("reputation-difference-award-cutoff")
    public int REPUTATION_DIFFERENCE_AWARD_CUTOFF = 50;

    @Setting("reputation-awarded-per-kill")
    public int REPUTATION_AWARDED_PER_KILL = 10;

    @Setting("reputation-lost-per-death")
    public int REPUTATION_LOST_PER_DEATH = 10;

    @Setting("currency-awarded-per-kill")
    public String CURRENCY_AWARDED_PER_KILL = "atherys:argent";

    @Setting("amount-of-currency-awarded-per-kill")
    public double AMOUNT_OF_CURRENCY_AWARDED_PER_KILL = 100;

    @Setting("combat-duration")
    public Duration COMBAT_DURATION = Duration.of(30, ChronoUnit.SECONDS);

    protected BattlegroundsConfig() throws IOException {
        super("config/" + AtherysBattlegrounds.ID, "config.conf");
        init();
    }

    @Override
    protected ConfigurationOptions getOptions() {
        ConfigurationOptions options = super.getOptions();

        TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();

        serializers.registerType(TypeToken.of(Duration.class), new DurationTypeSerializer());

        options.setSerializers(serializers);
        return options;
    }
}
