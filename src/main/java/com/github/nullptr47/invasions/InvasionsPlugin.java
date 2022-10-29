package com.github.nullptr47.invasions;

import com.github.nullptr47.invasions.listeners.EntityExplodeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InvasionsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);

    }
}
