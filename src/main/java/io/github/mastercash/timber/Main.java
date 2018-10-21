package io.github.mastercash.timber;

import io.github.mastercash.timber.events.LogBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LogBreakEvent(), this);
    }

    @Override
    public void onDisable() {

    }
}
