package me.dhcpcd.lisek.utils.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.dhcpcd.lisek.utils.UtilsPlugin;
import me.dhcpcd.lisek.utils.commands.base.CommandBase;
import me.dhcpcd.lisek.utils.commands.base.CommandInfo;
import me.dhcpcd.lisek.utils.commands.base.FallbackMethod;
import me.dhcpcd.lisek.utils.commands.base.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@CommandInfo(name = "server", aliases = {}, description = "Teleport you to provided server")
public class ServerCommand extends CommandBase {

    @FallbackMethod(consoleAllowed = false, autoComplete = "getServers")
    public void fallback(Sender sender, Invocation invocation) {
        String[] args = invocation.arguments().split(" ");
        String server = args[0];

         Optional<RegisteredServer> registredServer = UtilsPlugin.getInstance().getServer().getServer(server);

         if (!registredServer.isPresent()) {
             invocation.source().sendMessage(Component.text("Server not found."));
             return;
         }

        try {
            sender.getPlayer().createConnectionRequest(registredServer.get());
            sender.getPlayer().sendMessage(Component.text("Connecting to ").append(Component.text(server).color(NamedTextColor.RED)));
        }catch (Exception e) {
            invocation.source().sendMessage(Component.text("Failed to connect to " + server + ": " + e.getMessage()));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return super.suggest(invocation);
    }

    public List<String> getServers(Invocation invocation) {
        return UtilsPlugin.getInstance().getServer().getAllServers().stream().map(c -> c.getServerInfo().getName()).toList();
    }
}
