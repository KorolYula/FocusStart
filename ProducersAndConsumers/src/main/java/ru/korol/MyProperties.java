package ru.korol;

import java.io.IOException;
import java.io.InputStream;

public class MyProperties {
    private int producerCount;
    private int consumerCount;
    private int producerTime;
    private int consumerTime;
    private int storageSize;
    private final String filename;

    public int getProducerCount() {
        return producerCount;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public int getProducerTime() {
        return producerTime;
    }

    public int getConsumerTime() {
        return consumerTime;
    }

    public int getStorageSize() {
        return storageSize;
    }

    public MyProperties(String filename) {
        this.filename = filename;
    }

    void getDataFromFile() throws IOException {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(filename)) {
            java.util.Properties property = new java.util.Properties();
            property.load(input);
            producerCount = Integer.parseInt(property.getProperty("producerCount"));
            consumerCount = Integer.parseInt(property.getProperty("consumerCount"));
            producerTime = Integer.parseInt(property.getProperty("producerTime"));
            consumerTime = Integer.parseInt(property.getProperty("consumerTime"));
            storageSize = Integer.parseInt(property.getProperty("storageSize"));
        }
    }
}