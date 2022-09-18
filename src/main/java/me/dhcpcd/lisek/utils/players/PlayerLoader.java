package me.dhcpcd.lisek.utils.players;

import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import com.velocitypowered.api.proxy.ProxyServer;
import me.dhcpcd.lisek.utils.UtilsPlugin;
import me.dhcpcd.lisek.utils.players.conversations.Conversation;

import javax.inject.Inject;

public class PlayerLoader {
    
    @Inject
    public ProxyServer proxyServer;

    public PlayerLoader() {

    }

    @Subscribe
    public void onJoin(PostLoginEvent e) {
        PlayerInfo info = new PlayerInfo(e.getPlayer().getUniqueId(), proxyServer);
        UtilsPlugin.players.put(e.getPlayer().getUniqueId(), info);

        List<Conversation> converstations = UtilsPlugin.conversations.stream().filter(c -> c.getAutoJoin()).collect(Collectors.toList());

        for (Conversation conversation : converstations) {
            conversation.joinConversation(e.getPlayer());
        }



    }
}
