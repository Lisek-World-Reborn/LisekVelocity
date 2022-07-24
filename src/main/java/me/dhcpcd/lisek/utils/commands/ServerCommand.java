package me.dhcpcd.lisek.utils.commands;

import java.util.Arrays;
import java.util.List;

import me.dhcpcd.lisek.utils.UtilsPlugin;
import me.dhcpcd.lisek.utils.commands.base.CommandBase;
import me.dhcpcd.lisek.utils.commands.base.CommandInfo;
import me.dhcpcd.lisek.utils.commands.base.FallbackMethod;
import me.dhcpcd.lisek.utils.commands.base.Sender;

@CommandInfo(name = "server", aliases = {}, description = "Teleport you to provided server")
public class ServerCommand extends CommandBase {

    @FallbackMethod(consoleAllowed = false, autoComplete = "getServers")
    public void fallback(Sender sender, Invocation invocation) {

    }

    public List<String> getPlayers(Invocation invocation) {
        return UtilsPlugin.players.values().stream().map(p -> p.name()).toList();
    }
}
