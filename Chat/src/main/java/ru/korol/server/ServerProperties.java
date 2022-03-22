package ru.korol.server;

import java.io.IOException;
import java.io.InputStream;

public class ServerProperties {
    private int port;
    private final String filename;


    public int getPort() {
        return port;
    }

    public ServerProperties(String filename) {
        this.filename = filename;
    }
    void getDataFromFile() throws IOException {
        try (InputStream input = Server.class.getClassLoader().getResourceAsStream(filename)) {
            java.util.Properties property = new java.util.Properties();
            property.load(input);
            port = Integer.parseInt(property.getProperty("port"));
        }
    }
}

