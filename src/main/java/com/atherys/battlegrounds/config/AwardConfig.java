package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.service.economy.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class AwardConfig {

    @Setting("currency")
    private Map<Currency, Double> currency = new HashMap<>();

    @Setting("commands")
    private List<String> commands = new ArrayList<>();

    public AwardConfig() {
    }

    public Map<Currency, Double> getCurrency() {
        return currency;
    }

    public List<String> getCommands() {
        return commands;
    }
}
