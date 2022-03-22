package ru.korol;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static AtomicInteger uniqueId;
    private static Queue<Integer> store;
    private static final Object LOCK = new Object();
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    private static void storeProduction(int producerCount, int storageSize, int producerTime) {
        for (int i = 0; i < producerCount; i++) {
            int threadID = i;
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        synchronized (LOCK) {
                            while (store.size() >= storageSize) {
                                log.info("Производитель #{} встал на ожидание", threadID);
                                LOCK.wait();
                                log.info("Производитель #{} вышел из ожидания", threadID);
                            }
                            store.add(uniqueId.incrementAndGet());
                            log.info("Производитель #{} произвел ресурс #{}.Теперь на складе всего {} единиц ресурса", threadID, uniqueId.get(), store.size());
                            LOCK.notifyAll();
                        }
                        Thread.sleep(producerTime);

                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
        }
    }

    private static void storeConsumption(int consumerCount, int consumerTime) {
        for (int i = 1; i <= consumerCount; i++) {
            int threadID = i;
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        synchronized (LOCK) {
                            while (store.size() < 1) {
                                log.info("Потребитель #{},  встал в ожидание", threadID);
                                LOCK.wait();
                                log.info("Потребитель #{} вышел из ожидания", threadID);
                            }
                            int item = store.remove();
                            log.info("Потребитель #{} забрал ресурс #{}.Теперь на складе всего {} единиц ресурса", threadID,item , store.size());
                            LOCK.notifyAll();
                        }
                        Thread.sleep(consumerTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        String filename = "data.properties";
        try {
            MyProperties myProperties = new MyProperties(filename);
            myProperties.getDataFromFile();
            log.info("Прочитаны данные из properties.файла ");

            uniqueId = new AtomicInteger(0);
            store = new LinkedList<>();
            storeProduction(myProperties.getProducerCount(), myProperties.getStorageSize(), myProperties.getProducerTime());
            storeConsumption(myProperties.getConsumerCount(), myProperties.getConsumerTime());
        } catch (IOException e) {
            log.error("Файл {} не найден", filename);
        } catch (Exception e) {
            log.error("Ошибка! "+e.getMessage());
        }
    }

}