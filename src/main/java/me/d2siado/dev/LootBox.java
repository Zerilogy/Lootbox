/*
 | me.d2siado.dev.LootBox Plugin
 | this plugin was maked by D2SIADO#0001
 | 4/09/2022 ! me.d2siado.dev.LootBox.java
 | Dependencies Maven:
 |  - Spigot
 |
 | Social:
 |  - _ede.sal in instagram
 |  - D2SIADO#0001 in discord
 |  - D2SIADO in twitter
 */
package me.d2siado.dev;

import me.d2siado.dev.Other.events.lootbox;
import me.d2siado.dev.Utils.CC;
import me.d2siado.dev.Utils.Glow;
import me.d2siado.dev.Utils.Plugin;
import me.d2siado.dev.Utils.files.ConfigFile;
import me.d2siado.dev.Utils.files.DataFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class LootBox extends JavaPlugin
{
    private static LootBox instance;

    /*
     - Other
     */
    public static LootBox getInstance() {
        return instance;
    }

    public String getLastUpdate() {
        return "05/09/2022";
    }

    public ConfigFile getConfig() {
        return ConfigFile.getConfig();
    }

    public DataFile getData() {
        return DataFile.getConfig();
    }

    /*
     - Plugin
     */
    @Override
    public void onEnable() {
        instance = this;
        getConfig();
        getData();
        Glow.registerGlow();
        Plugin.registerListeners(Arrays.asList(new lootbox()));
        getServer().getPluginCommand("lootbox").setExecutor(new me.d2siado.dev.Other.commands.lootbox());
        CC.logWith(Arrays.asList("&aPlugin is enabled correctly, LootBox works","&aA custom plugin to ZyonPvP, maked by D2SI4DO"));
    }
}
