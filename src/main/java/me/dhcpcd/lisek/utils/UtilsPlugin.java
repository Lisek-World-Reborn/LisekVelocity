package me.dhcpcd.lisek.utils;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dhcpcd.lisek.utils.commands.base.CommandBase;
import me.dhcpcd.lisek.utils.commands.base.CommandInfo;
import me.dhcpcd.lisek.utils.players.PlayerInfo;
import me.dhcpcd.lisek.utils.players.PlayerLoader;
import me.dhcpcd.lisek.utils.players.conversations.Conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.reflections.Reflections;
import org.slf4j.Logger;

@Plugin(
        id = "lisekutils",
        name = "LisekUtils",
        version = BuildConstants.VERSION
)
public class UtilsPlugin {

    public static UtilsPlugin instance;

    @Inject
    private ProxyServer server;


    @Inject
    private Logger logger;
    public static List<Conversation> conversations = new ArrayList<>();
    public static HashMap<UUID, PlayerInfo> players = new HashMap<>();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("LisekUtils v" + BuildConstants.VERSION + " loaded!");
        registerCommands();
        setInstance(this);
        conversations.add(new Conversation(UUID.randomUUID(), "global", true, this));
        server.getEventManager().register(this, new PlayerLoader());
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("me.dhcpcd.lisek.utils.commands");

        for (Class<?> commandClass : reflections.getSubTypesOf(CommandBase.class)) {
            try {
                CommandBase command = (CommandBase) commandClass.getConstructor().newInstance();
                CommandInfo info = command.getInfo();

                server.getEventManager().register(this, command);
                CommandMeta meta = server.getCommandManager().metaBuilder(info.name())
                                    .aliases(info.aliases())
                                    .build();

                server.getCommandManager().register(meta, command);
                logger.info("Registered command " + info.name());
            } catch (Exception e) {
                logger.error("Failed to register command " + commandClass.getName(), e);
            }
        }
    }


    public static UtilsPlugin getInstance() {
        return instance;
    }

    public static void setInstance(UtilsPlugin instance) {
        UtilsPlugin.instance = instance;
    }


    public ProxyServer getServer() {
        return server;
    }

    public void setServer(ProxyServer server) {
        this.server = server;
    }
}
