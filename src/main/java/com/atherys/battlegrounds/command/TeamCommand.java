package com.atherys.battlegrounds.command;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.core.command.PlayerCommand;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Children;
import com.atherys.core.command.annotation.Description;
import com.atherys.core.command.annotation.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nonnull;

@Aliases("team")
@Children({
        TeamJoinCommand.class,
        TeamLeaveCommand.class,
        TeamInfoCommand.class,
        AddPlayerTeamCommand.class,
        RemovePlayerTeamCommand.class
})
@Permission("atherysbattlegrounds.team.base")
@Description("Displays information for your current team.")
public class TeamCommand implements PlayerCommand {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        AtherysBattlegrounds.getInstance().getTeamFacade().showTeamInfo(source);
        return CommandResult.success();
    }
}
