package de.ostfalia.remotecar.mqtt;

/**
 * Created by Marius Reimer on 02.01.2017.
 */
public class BrokerConnectInfo {

    private String ip;
    private int port;

    public BrokerConnectInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
