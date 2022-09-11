package me.dhcpcd.lisek.utils.api;


import com.google.gson.Gson;
import com.sendgrid.Client;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import me.dhcpcd.lisek.utils.api.response.LisekServerInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LisekApi {
    public Client client;
    private String token;
    private String host;

    public LisekApi(String host, String secret) {
        this.host = host;
        client = new Client(true);


    }

    private Request getRequest(String endpoint, Method method) {
        Request request = new Request();


        request.setMethod(method);
        request.setEndpoint(endpoint);
        request.addHeader("Authorization", token);
        request.setBaseUri(host);

        return request;
    }

    /**
     * @description Fetches server info
     * @param serverId Server ID
     * @return ServerInfo
     */

    public LisekServerInfo getServerStatus(int serverId) {
        Request request = getRequest("/servers/" + serverId, Method.GET);

        try {
            Response response = client.api(request);

            if (response.getStatusCode() == 200) {
                LisekServerInfo[] serverInfo = new Gson().fromJson(response.getBody(), LisekServerInfo[].class);

                return Arrays.stream(serverInfo).filter(s -> s.id == serverId).findFirst().orElse(null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<LisekServerInfo> getServers() {
        Request request = getRequest("/servers", Method.GET);


        try {
            Response response = client.api(request);

            if (response.getStatusCode() == 200) {
                LisekServerInfo[] serverInfo = new Gson().fromJson(response.getBody(), LisekServerInfo[].class);

                return Arrays.asList(serverInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
