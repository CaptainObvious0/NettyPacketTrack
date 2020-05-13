package com.github.captainobvious0.version;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ServerVersion {

    private static String version;

    Player player;
    ChannelPipeline pipeline;
    ChannelDuplexHandler handler;

    public ServerVersion() { }

    public void setServerVersion() {
        try {
            String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (bukkitVersion.equals("v1_8_R3")) {
                version = "1.8";
                Bukkit.getLogger().log(Level.INFO, "Server version set to: " + version);
            } else if (bukkitVersion.equals("v1_9_R1")) {
                version = "1.9";
                Bukkit.getLogger().log(Level.INFO, "Server version set to: " + version);
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Server version not supported (" + bukkitVersion + ")");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Server version not found");
        }
    }

    public ServerVersion(Player player) {
        this.player = player;
    }

    public ServerVersion serverVersion() {
        if (version.equalsIgnoreCase("1.8")) {
            return new Version1_8(this.player);
        }
        return new Version1_9(this.player);

    }

    public ChannelPipeline getPipeline() {
        return null;
    }

    public void handlePacket(Object packet, boolean read) {
        // Output packet to console
        //Bukkit.broadcastMessage((read ? "&aRead: " : "&cWrite: ") + packet.toString());
    }

    public void addChannel(ChannelPipeline pipeline, ChannelDuplexHandler handler) {
        this.pipeline = pipeline;
        this.handler = handler;
    }

}
