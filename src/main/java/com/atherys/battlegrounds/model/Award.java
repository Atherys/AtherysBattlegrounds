package com.atherys.battlegrounds.model;

import org.spongepowered.api.service.economy.Currency;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Award award = (Award) o;
        return Objects.equals(currency, award.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }

    @Override
    public String toString() {
        return "Award{" +
                "currency=" + currency +
                '}';
    }
}
