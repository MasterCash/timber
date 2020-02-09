package io.github.mastercash.timber;

import io.github.mastercash.timber.events.LogBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Utils.CoreProtect =  Utils.getCoreProtect();
        if(Utils.CoreProtect != null)
        {
            getServer().getConsoleSender().sendMessage("[Timber] CoreProtect Found");
        }
        getServer().getPluginManager().registerEvents(new LogBreakEvent(), this);
    }

    @Override
    public void onDisable() {

    }
}
