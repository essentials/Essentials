package org.mcess.essentials.modules;

import com.me4502.modularframework.module.Module;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

@Module(moduleName = "Teleport", onEnable = "onInitialize")
public class Teleport {

    public void onInitialize() {
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Text.of("Teleport to a player"))
                .permission("essentials.teleport")
                .executor(new TeleportCommand())
                .build();
    }

    private static class TeleportCommand implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            return CommandResult.success();
        }
    }
}
