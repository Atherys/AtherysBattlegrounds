package com.atherys.battlegrounds.facade;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;

@Singleton
public class BattlegroundMessagingFacade {

    public static final Text PREFIX = Text.of(TextColors.DARK_AQUA, "[", TextColors.DARK_PURPLE, "Battle", TextColors.DARK_AQUA, "] ", TextColors.RESET);

    public void info(MessageReceiver messageReceiver, Text message) {
        messageReceiver.sendMessage(Text.of(PREFIX, message));
    }

    public CommandException exception(Text message) {
        return new CommandException(Text.of(PREFIX, TextColors.RED, message));
    }

}
