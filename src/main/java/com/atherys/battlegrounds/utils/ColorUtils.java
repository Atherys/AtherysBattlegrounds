package com.atherys.battlegrounds.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static TextColor bossBarColorToTextColor(BossBarColor color) {
        if (BossBarColors.PINK.equals(color)) {
            return TextColors.LIGHT_PURPLE;
        }

        if (BossBarColors.PURPLE.equals(color)) {
            return TextColors.DARK_PURPLE;
        }

        return Sponge.getRegistry().getType(TextColor.class, color.getId()).orElse(TextColors.WHITE);
    }

    public static BossBarColor textColorToBossBarColor(TextColor textColor) {
        if (TextColors.LIGHT_PURPLE.equals(textColor)) {
            return BossBarColors.PINK;
        }

        if (TextColors.DARK_PURPLE.equals(textColor)) {
            return BossBarColors.PURPLE;
        }

        return Sponge.getRegistry().getType(BossBarColor.class, textColor.getId()).orElse(BossBarColors.WHITE);
    }
}
