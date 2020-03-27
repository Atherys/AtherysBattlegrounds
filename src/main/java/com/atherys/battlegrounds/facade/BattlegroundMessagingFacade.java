package com.atherys.battlegrounds.facade;

import com.atherys.core.utils.AbstractMessagingFacade;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;

@Singleton
public class BattlegroundMessagingFacade extends AbstractMessagingFacade {
    public BattlegroundMessagingFacade() {
        super("Battle");
    }

    public void broadcast(Object... message) {
        Sponge.getServer().getBroadcastChannel().send(formatInfo(message));
    }

    public CommandException exception(Object... message) {
        return new CommandException(formatError(message));
    }

}
