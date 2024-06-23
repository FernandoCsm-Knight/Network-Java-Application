package app.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.types.IPVersion;

public class SettingsService {
    private static String serverIP = "::1";
    private static int serverPort = 8080;
    private static IPVersion serverIPVersion = IPVersion.IPv6;

    public static InetAddress getServerAddress() throws UnknownHostException {
        return InetAddress.getByName(serverIP);
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static IPVersion getServerIPVersion() {
        return serverIPVersion;
    }

    public static void loadSettings() {
        // TODO: load settings from file
    }

    public static void saveSettings(IPVersion ipVersion, String ipAddress, int port) {
        serverIP = ipAddress;
        serverPort = port;
        serverIPVersion = ipVersion;
    }
}
