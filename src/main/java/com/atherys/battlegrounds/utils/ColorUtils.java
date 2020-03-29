package com.atherys.battlegrounds.utils;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static TextColor bossBarColorToTextColor(BossBarColor color) {
        if (BossBarColors.PINK == color) return TextColors.LIGHT_PURPLE;
        if (BossBarColors.PURPLE == color) return TextColors.DARK_PURPLE;
        if (BossBarColors.GREEN == color) return TextColors.GREEN;
        if (BossBarColors.BLUE == color) return TextColors.BLUE;
        if (BossBarColors.RED == color) return TextColors.RED;
        if (BossBarColors.YELLOW == color) return TextColors.YELLOW;

        return TextColors.WHITE;
    }

    public static BossBarColor textColorToBossBarColor(TextColor textColor) {
        if (TextColors.LIGHT_PURPLE == textColor) return BossBarColors.PINK;
        if (TextColors.DARK_PURPLE == textColor) return BossBarColors.PURPLE;
        if (TextColors.GREEN == textColor) return BossBarColors.GREEN;
        if (TextColors.BLUE == textColor) return BossBarColors.BLUE;
        if (TextColors.RED == textColor) return BossBarColors.RED;
        if (TextColors.YELLOW == textColor) return BossBarColors.YELLOW;

        return BossBarColors.WHITE;
    }
}
