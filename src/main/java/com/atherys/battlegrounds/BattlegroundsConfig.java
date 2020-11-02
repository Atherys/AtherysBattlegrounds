package com.atherys.battlegrounds;

import com.atherys.battlegrounds.config.AwardConfig;
import com.atherys.battlegrounds.config.BattlePointConfig;
import com.atherys.battlegrounds.config.MilestoneConfig;
import com.atherys.battlegrounds.config.TeamConfig;
import com.atherys.core.utils.PluginConfig;
import com.google.inject.Singleton;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.service.economy.Currency;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class BattlegroundsConfig extends PluginConfig {

    @Setting("battle-points")
    public Set<BattlePointConfig> BATTLE_POINTS = new HashSet<>();

    @Setting("milestones-title")
    public String MILESTONES_TITLE = "Rewards";

    @Setting("milestones-currency-base")
    public int CURRENCY_BASE = 300;

    @Setting("milestones")
    public List<MilestoneConfig> MILESTONES = new ArrayList<>();

    @Setting("milestones-enabled")
    public boolean MILESTONES_ENABLED;

    @Setting("milestone-currency")
    public String MILESTONE_CURRENCY;

    @Setting("teams")
    public Set<TeamConfig> TEAMS = new HashSet<>();

    @Setting("team-currencies")
    public Set<Currency> TEAM_CURRENCIES = new HashSet<>();

    @Setting("award-for-kills")
    public AwardConfig KILL_AWARD = new AwardConfig();

    @Setting("tick-interval")
    public Duration TICK_INTERVAL = Duration.of(1, ChronoUnit.SECONDS);

    @Setting("respawn-interval")
    public Duration RESPAWN_INTERVAL = Duration.of(5, ChronoUnit.SECONDS);

    @Setting("warning-time")
    public Duration WARNING_TIME = Duration.of(5, ChronoUnit.MINUTES);

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
