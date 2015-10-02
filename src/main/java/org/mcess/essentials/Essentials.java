package org.mcess.essentials;

import com.google.inject.Inject;
import com.me4502.modularframework.ModuleController;
import com.me4502.modularframework.ShadedModularFramework;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;

import java.io.File;

@Plugin(id = "Essentials", name = "Essentials")
public class Essentials {

    ModuleController moduleController;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File mainConfig;

    private File configurationDirectory;

    @Listener
    public void onInitialize(GameStartedServerEvent event) {

        moduleController = ShadedModularFramework.registerModuleController(this, event.getGame());

        configurationDirectory = new File(mainConfig.getParent(), "modules");
        configurationDirectory.mkdir();
        moduleController.setConfigurationDirectory(configurationDirectory);

        discoverModules();

        moduleController.enableModules((moduleWrapper) -> true); //Enable all for now.
    }

    public void discoverModules() {
        //List all the modules that exist.
        moduleController.registerModule("org.mcess.essentials.modules.Teleport");
    }
}
