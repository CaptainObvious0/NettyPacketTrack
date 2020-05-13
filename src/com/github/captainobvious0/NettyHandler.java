package com.github.captainobvious0;

import com.github.captainobvious0.version.ServerVersion;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NettyHandler implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        ServerVersion serverVersion = new ServerVersion(event.getPlayer()).serverVersion();
        injectListener(event.getPlayer(), serverVersion);
    }

    private void injectListener(Player player, ServerVersion serverVersion) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext handlerContext, Object packet) throws Exception {
                serverVersion.handlePacket(packet, true);
                super.channelRead(handlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext handlerContext, Object packet, ChannelPromise promise) throws Exception {
                serverVersion.handlePacket(packet, false);
                super.write(handlerContext, packet, promise);
            }
        };
        ChannelPipeline pipeline = serverVersion.getPipeline();
        pipeline.addBefore("packet_handler", "CubeCraft", channelDuplexHandler);
        serverVersion.addChannel(pipeline, channelDuplexHandler);
    }

}
