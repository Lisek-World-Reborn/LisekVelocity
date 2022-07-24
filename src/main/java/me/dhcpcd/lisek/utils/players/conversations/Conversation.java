package me.dhcpcd.lisek.utils.players.conversations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dhcpcd.lisek.utils.UtilsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Conversation {

    @Inject
    private ProxyServer server;

    public List<UUID> participants;
    public UUID uuid;
    public String name;
    public Boolean autoJoin;

    public Conversation(UUID uuid, String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.autoJoin = false;
    }

    public Conversation() {
        
    }

    @Inject
    public Conversation(UUID uuid, String name, Boolean autoJoin, UtilsPlugin plugin) {
        this.uuid = uuid;
        this.name = name;
        this.autoJoin = autoJoin;
        server = plugin.getServer();
    
        initilize();
    }

    public void initilize() {
        this.participants = new ArrayList<>();
        server.sendMessage(Component.text("Conversation #" + name + " (" + uuid.toString() + ") created!"));
    }

    public void joinConversation(UUID uuid) {
        this.participants.add(uuid);
        UtilsPlugin.players.get(uuid).conversations.add(this);
    }
    public void joinConversation(Player player) {
        player.sendMessage(
            Component.text("System").color(NamedTextColor.WHITE)
            .append(Component.text(" >").color(NamedTextColor.GRAY))
            .append(Component.text(" You have joined conversation ").color(NamedTextColor.WHITE))
            .append(Component.text(name).color(NamedTextColor.AQUA)));
            
        joinConversation(player.getUniqueId());
    }

    public void leaveConversation(UUID uuid) {
        this.participants.remove(uuid);
        UtilsPlugin.players.get(uuid).conversations.remove(this);
    }

    public void leaveConversation(Player player) {
        player.sendMessage(
            Component.text("System").color(NamedTextColor.WHITE)
            .append(Component.text(" >").color(NamedTextColor.GRAY))
            .append(Component.text(" You left from conversation ").color(NamedTextColor.WHITE))
            .append(Component.text(name).color(NamedTextColor.AQUA)));
    }

    public List<Player> getParticipants() {
        List<Player> players = new ArrayList<Player>();
        for (UUID uuid : participants) {
            server.getPlayer(uuid).ifPresent(players::add);
        }
        return players;
    }

    public void sendMessage(Component component) {
        for (Player p : getParticipants()) {
            p.sendMessage(
                Component.text(name)
                .color(NamedTextColor.WHITE)
                .append(Component.text(" > ")).color(NamedTextColor.GRAY)
                .append(component));
        }
    }

    public boolean getAutoJoin() {
        return autoJoin;
    }

}
