package com.atherys.battlegrounds.command;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.Team;
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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@Aliases("add")
@Permission("atherysbattlegrounds.team.add")
@Description("Adds a player to the team.")
public class AddPlayerTeamCommand implements ParameterizedCommand {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        AtherysBattlegrounds.getInstance().getTeamFacade().addPlayerToTeam(
                args.<Player>getOne("player").get(),
                args.<Team>getOne("team").get()
        );
        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[] {
                GenericArguments.player(Text.of("player")),
                GenericArguments.choices(
                        Text.of("team"),
                        AtherysBattlegrounds.getInstance().getTeamFacade().getTeamChoices(),
                        true,
                        false
                )
        };
    }

}
