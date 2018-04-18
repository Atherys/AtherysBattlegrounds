package com.atherys.battlegrounds;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BattleMsg {

    public static Text PREFIX = Text.of( TextColors.DARK_RED, "[", TextColors.DARK_GRAY, "Battle", TextColors.DARK_RED, "] ", TextColors.RESET );

    public static void info( Player player, Object... msg ) {
        player.sendMessage( Text.of( PREFIX, TextColors.DARK_GREEN, Text.of( msg ) ) );
    }

    public static void warn( Player player, Object... msg ) {
        player.sendMessage( Text.of( PREFIX, TextColors.RED, Text.of( msg ) ) );
    }

}
