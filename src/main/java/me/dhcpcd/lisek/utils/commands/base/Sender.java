package me.dhcpcd.lisek.utils.commands.base;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public class Sender {
    private Player player;
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    private CommandSource source;
    public CommandSource getSource() {
        return source;
    }
    public void setSource(CommandSource source) {
        this.source = source;
    }

    public Sender(Player p, CommandSource source) {
        player = p;
        this.source = source;
    }
}
