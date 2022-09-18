package me.dhcpcd.lisek.utils.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dhcpcd.lisek.utils.UtilsPlugin;
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

        //Setting player preferences

        UtilsPlugin.getInstance().redis.getJedisPooled().set(String.format("%s:connection", player().getUsername().toLowerCase(Locale.ROOT)), player().getUsername());
        UtilsPlugin.getInstance().redis.getJedisPooled().set(String.format("%s:language", player().getUsername().toLowerCase(Locale.ROOT)), player().getPlayerSettings().getLocale().getISO3Language());
    }

    public Player player() {
        return server.getPlayer(this.uuid).get();
    }

    public String name() {
        return player().getUsername();
    }
}
