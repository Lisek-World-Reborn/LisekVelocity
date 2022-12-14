package me.dhcpcd.lisek.utils.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.dhcpcd.lisek.utils.UtilsPlugin;
import me.dhcpcd.lisek.utils.api.response.LisekServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

import java.net.InetSocketAddress;
import java.util.Optional;

public class Redis {

    private JedisPooled jedisPooled;

    public Redis(String host, int port) {
        jedisPooled = new JedisPooled(host, port);
    }

    public JedisPooled getJedisPooled() {
        return jedisPooled;
    }

    public void listenToServerAdded() {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                UtilsPlugin.getInstance().getLogger().info("Received message from channel " + channel + ": " + message);
                if (channel.equals("servers:added")) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

                    int id = jsonObject.get("server_id").getAsInt();

                    //Fetching server

                    LisekServerInfo si = UtilsPlugin.getInstance().getLisekApi().getServerStatus(id);

                    UtilsPlugin.getInstance().getServer().registerServer(new ServerInfo(si.name, new InetSocketAddress(si.ip, si.port)));

                    UtilsPlugin.getInstance().getLogger().info("Registered server " + si.name);
                }

                if (channel.equalsIgnoreCase("players:connect")) {
                    JsonObject jsonObject = new Gson().fromJson(message, JsonObject.class);
                    String username = jsonObject.get("username").getAsString();
                    String server = jsonObject.get("server").getAsString();

                    UtilsPlugin.getInstance().getServer().getPlayer(username).ifPresent(player -> {
                        player.sendMessage(Component.text("Connecting to ").append(Component.text(server).color(NamedTextColor.BLUE)));
                        Optional<RegisteredServer> registeredServer = UtilsPlugin.getInstance().getServer().getServer(server);

                        if (!registeredServer.isPresent()) {
                            player.sendMessage(Component.text("Could not connect to server: ").append(Component.text(server).color(NamedTextColor.RED)).append(Component.text(" because it does not exist.").color(NamedTextColor.RED)));
                            return;
                        }

                        player.createConnectionRequest(registeredServer.get()).fireAndForget();

                    });

                }

                if (channel.equalsIgnoreCase("server:removed")) {
                    JsonObject jsonObject = new Gson().fromJson(message, JsonObject.class);
                    String server = jsonObject.get("server").getAsString();

                    UtilsPlugin.getInstance().getServer().getServer(server).ifPresent(serverInfo -> {
                        UtilsPlugin.getInstance().getServer().unregisterServer(serverInfo.getServerInfo());
                    });
                }
            }
        };

        jedisPooled.subscribe(jedisPubSub, "servers:added", "players:connect", "server:removed");

        UtilsPlugin.getInstance().getLogger().info("Subscribed to Redis");
    }


    public void registerCurrentServers() {

        UtilsPlugin.getInstance().getLogger().info("Registering current servers...");

        UtilsPlugin.getInstance().getLisekApi().getServers().forEach(server -> {


            UtilsPlugin.getInstance().getServer().getServer(server.name).ifPresent(serverInfo -> {
                UtilsPlugin.getInstance().getServer().unregisterServer(serverInfo.getServerInfo());
            });

            UtilsPlugin.getInstance().getServer().registerServer(new ServerInfo(server.name, new InetSocketAddress(server.ip, server.port)));

            UtilsPlugin.getInstance().getLogger().info("Registered server " + server.name + " from api");
        });
    }

}
