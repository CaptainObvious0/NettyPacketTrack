package com.github.captainobvious0;

import com.github.captainobvious0.version.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NettyPacketTrack extends JavaPlugin {

    @Override
    public void onEnable() {
        new ServerVersion().setServerVersion();
        Bukkit.getPluginManager().registerEvents(new NettyHandler(), this);
    }

    @Override
    public void onDisable() {

    }

}
