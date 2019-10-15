package com.atherys.battlegrounds.model;

import org.spongepowered.api.service.economy.Currency;

import java.util.HashMap;
import java.util.Map;

public class Award {

    private Map<Currency, Double> currency = new HashMap<>();

    public Award() {
    }

    public Map<Currency, Double> getCurrency() {
        return currency;
    }

    public void setCurrency(Map<Currency, Double> currency) {
        this.currency = currency;
    }
}
