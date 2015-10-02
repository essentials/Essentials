package org.mcess.essentials.modules;

import com.me4502.modularframework.module.Module;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

@Module(moduleName = "Teleport", onEnable = "onInitialize")
public class Teleport {

    public void onInitialize() {
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Texts.of("Teleport to a player"))
                .permission("essentials.teleport")
                .executor(new TeleportCommand())
                .build();
    }

    private class TeleportCommand implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            return null;
        }
    }
}
