package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MilestoneConfig {

    @Setting("award")
    private AwardConfig award = new AwardConfig();

    @Setting("threshold")
    private double threshold;

    @Setting("message")
    private String message;

    @Setting("display")
    private String display;

    public AwardConfig getAward() {
        return award;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getMessage() {
        return message;
    }

    public String getDisplay() {
        return display;
    }
}
