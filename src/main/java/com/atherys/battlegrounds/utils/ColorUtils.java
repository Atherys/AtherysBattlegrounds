package com.atherys.battlegrounds.utils;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
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

    public static DyeColor textColorToDyeColor(TextColor textColor) {
        if (TextColors.LIGHT_PURPLE == textColor) return DyeColors.PINK;
        if (TextColors.DARK_PURPLE == textColor) return DyeColors.PURPLE;
        if (TextColors.GREEN == textColor) return DyeColors.LIME;
        if (TextColors.DARK_GREEN == textColor) return DyeColors.GREEN;
        if (TextColors.BLUE == textColor) return DyeColors.LIGHT_BLUE;
        if (TextColors.DARK_BLUE == textColor) return DyeColors.BLUE;
        if (TextColors.AQUA == textColor) return DyeColors.LIGHT_BLUE;
        if (TextColors.DARK_AQUA == textColor) return DyeColors.BLUE;
        if (TextColors.GOLD == textColor) return DyeColors.ORANGE;
        if (TextColors.YELLOW == textColor) return DyeColors.YELLOW;
        if (TextColors.BLACK == textColor) return DyeColors.BLACK;
        if (TextColors.RED == textColor) return DyeColors.RED;

        return DyeColors.WHITE;
    }
}
