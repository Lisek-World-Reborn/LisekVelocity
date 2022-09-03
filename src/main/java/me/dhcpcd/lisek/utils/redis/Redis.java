package me.dhcpcd.lisek.utils.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.dhcpcd.lisek.utils.UtilsPlugin;
import me.dhcpcd.lisek.utils.api.response.LisekServerInfo;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

import java.net.InetSocketAddress;

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
                if (channel.equals("servers:added")) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

                    int id = jsonObject.get("server_id").getAsInt();

                    //Fetching server

                    LisekServerInfo si = UtilsPlugin.getInstance().getLisekApi().getServerStatus(id);

                    UtilsPlugin.getInstance().getServer().registerServer(new ServerInfo(si.name, new InetSocketAddress(si.ip, si.port)));
                }
            }
        };
    }

}
