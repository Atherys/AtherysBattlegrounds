package com.atherys.battlegrounds.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class AwardConfig {

    @Setting("currency")
    private Map<Currency, Double> currency = new HashMap<>();

    public AwardConfig() {
    }

    public Map<Currency, Double> getCurrency() {
        return currency;
    }
}