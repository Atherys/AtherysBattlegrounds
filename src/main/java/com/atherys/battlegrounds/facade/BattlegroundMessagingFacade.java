package com.atherys.battlegrounds.facade;

import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;

@Singleton
public class BattlegroundMessagingFacade {

    public static final Text PREFIX = Text.of(TextColors.DARK_AQUA, "[", TextColors.DARK_PURPLE, "Battle", TextColors.DARK_AQUA, "] ", TextColors.RESET);

    public Text formatInfo(Object... message) {
        return Text.of(PREFIX, Text.of(message));
    }

    public Text formatError(Object... message) {
        return Text.of(PREFIX, TextColors.RED, Text.of(message));
    }

    public void info(MessageReceiver messageReceiver, Object... message) {
        messageReceiver.sendMessage(formatInfo(message));
    }

    public void error(MessageReceiver messageReceiver, Object... message) {
        messageReceiver.sendMessage(formatError(message));
    }

    public CommandException exception(Object... message) {
        return new CommandException(formatError(message));
    }

}
