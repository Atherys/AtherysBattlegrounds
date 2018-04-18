package com.atherys.battlegrounds.commands;

import com.atherys.battlegrounds.managers.RespawnManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class RespawnReadyCommand implements CommandExecutor {
    @Override
    public CommandResult execute( CommandSource src, CommandContext args ) throws CommandException {
        if ( src instanceof Player ) {
            RespawnManager.getInstance().setReady( ( Player ) src, true );
        }
        return CommandResult.success();
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
                .permission( "atherysbattlegrounds.respawn.ready" )
                .executor( this )
                .build();
    }
}
