package com.github.nullptr47.invasions;

import com.github.nullptr47.invasions.listeners.EntityExplodeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InvasionsPlugin extends JavaPlugin {

    public static InvasionsPlugin getInstance() {

        return JavaPlugin.getPlugin(InvasionsPlugin.class);

    }

    public void onEnable() {

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);

    }
}
