package com.github.captainobvious0.version;

import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class Version1_8 extends ServerVersion {

    Player player;

    public Version1_8(Player player) {
        this.player = player;
    }

    @Override
    public ChannelPipeline getPipeline() {
        CraftPlayer cPlayer = (CraftPlayer) this.player;
        Bukkit.getLogger().log(Level.INFO, "Getting 1.8 pipeline for " + player.getName());
        return cPlayer.getHandle().playerConnection.networkManager.channel.pipeline();
    }

    @Override
    public void handlePacket(Object packet, boolean read) {
        if (packet instanceof PacketPlayInChat) {
            PacketPlayInChat chatPacket = (PacketPlayInChat) packet;
            try {
                addToChatMessageIn(chatPacket, ((PacketPlayInChat) packet).a());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bukkit.getLogger().info("New Message: " + ((PacketPlayInChat) packet).a());
        } else if (packet instanceof PacketPlayOutChat) {
            try {
                PacketPlayOutChat chatPacket = (PacketPlayOutChat) packet;
                addToChatMessageOut(chatPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addToChatMessageOut(PacketPlayOutChat packet) throws NoSuchFieldException, IllegalAccessException {
        Field f = packet.getClass().getDeclaredField("a");
        f.setAccessible(true);
        IChatBaseComponent message = (IChatBaseComponent) f.get(packet);
        message.a(" Arrived");
        f.set(packet, message);
    }

    private void addToChatMessageIn(PacketPlayInChat packet, String oldMessage) throws NoSuchFieldException, IllegalAccessException {
        Field f = packet.getClass().getDeclaredField("a");
        f.setAccessible(true);
        String message;
        message = oldMessage + " Sent";
        f.set(packet, message);
    }

}
