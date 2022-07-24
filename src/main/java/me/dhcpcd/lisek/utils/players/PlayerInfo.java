package me.dhcpcd.lisek.utils.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dhcpcd.lisek.utils.players.conversations.Conversation;

public class PlayerInfo {
    
    public UUID uuid;

    public List<Conversation> conversations;

    @Inject
    private ProxyServer server;

    @Inject
    public PlayerInfo(UUID uuid, ProxyServer server) {
        this.uuid = uuid;
        this.conversations = new ArrayList<>();
        this.server = server;
    }

    public Player player() {
        return server.getPlayer(this.uuid).get();
    }

    public String name() {
        return player().getUsername();
    }
}
