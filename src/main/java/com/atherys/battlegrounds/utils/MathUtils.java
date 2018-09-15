package com.atherys.battlegrounds.utils;

public final class MathUtils {

    public static double clamp(double min, double max, double amount) {
        return amount > max ? max : amount < min ? min : amount;
    }

}
