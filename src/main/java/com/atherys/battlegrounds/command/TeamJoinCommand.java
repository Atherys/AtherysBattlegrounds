package com.atherys.battlegrounds.command;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.core.command.ParameterizedCommand;
import com.atherys.core.command.PlayerCommand;
import com.atherys.core.command.annotation.Aliases;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

@Aliases("join")
public class TeamJoinCommand implements PlayerCommand, ParameterizedCommand {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        AtherysBattlegrounds.getInstance().getTeamFacade().addPlayerToTeam(
                source,
                args.<String>getOne("team-id").orElse(null)
        );
        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[] {
                GenericArguments.choices(Text.of("team-id"), AtherysBattlegrounds.getInstance().getTeamFacade().getTeamChoices(), true, false)
        };
    }
}
