package com.atherys.battlegrounds.command;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.BattleTeam;
import com.atherys.core.command.ParameterizedCommand;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Description;
import com.atherys.core.command.annotation.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

@Aliases("info")
@Permission("atherysbattlegrounds.team.info")
@Description("Displays information for the given team.")
public class TeamInfoCommand implements ParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) throws CommandException {
        AtherysBattlegrounds.getInstance().getTeamFacade().showTeamInfo(source, args.<BattleTeam>getOne("team").get());
        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.choices(
                        Text.of("team"),
                        AtherysBattlegrounds.getInstance().getTeamFacade().getTeamChoices(),
                        true,
                        false
                )
        };
    }
}
