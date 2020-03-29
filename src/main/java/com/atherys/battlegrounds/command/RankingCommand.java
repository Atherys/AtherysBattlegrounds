package com.atherys.battlegrounds.command;

import com.atherys.core.command.PlayerCommand;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Children;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nonnull;

@Aliases("ranking")
@Children({
})
public class RankingCommand implements PlayerCommand {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        //AtherysBattlegrounds.getInstance().getTeamFacade().sendPlayerRanking(source);

        return CommandResult.success();
    }
}
