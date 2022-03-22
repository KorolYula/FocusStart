package ru.korol.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.korol.common.Message;
import ru.korol.common.ServerMessage;

import static java.lang.Thread.sleep;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class.getName());
    private final List<Socket> clients;
    private final HashMap<Socket, String> users = new HashMap<>();
    private Thread clientProcessingThread;

    public Server() {
        clients = new ArrayList<>();
    }

    public static void main(String[] args) {
        String filename = "data.properties";
        try {
            ServerProperties serverProperties = new ServerProperties(filename);
            serverProperties.getDataFromFile();

            log.debug("Порт " + serverProperties.getPort());
            new Server().startServer(serverProperties.getPort());
        } catch (Exception IO) {
            log.debug("не найден файл" + filename);
        }
    }


    private void startServer(int port) throws IOException {
        clientProcessingThread = new Thread(this::prosessClient);
        clientProcessingThread.setDaemon(true);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Сервер запущен");
            clientProcessingThread.start();
            while (true) {
                Socket socket = serverSocket.accept();
                log.debug("New client accept");// потом убрать
                synchronized (clients) {
                    clients.add(socket);
                }
            }
        }
    }

    private void prosessClient() {
        log.debug("Запущен обработчик");
        ObjectMapper mapper = new ObjectMapper();
        //Создаем список новых сообщений
        ArrayList<Message> messages = new ArrayList<>();
        while (true) {
            try {
                sleep(10);
            } catch (InterruptedException ignored) {;
            }
            //Фиксируем список клиентов и их ников
            List<Socket> clientes;
            synchronized (this.clients) {
                clientes = new ArrayList<>(this.clients);
            }
            TreeSet<String> usersNicks = new TreeSet<>(this.users.values());

            log.debug("У меня клиентов " + clientes.size());

            //Слушаем всех клиентов, Наполняем список новыми сообщениями
            for (Socket client : clientes) {
                log.debug("Слушаю клиента " + client);
                try {
                    InputStream inputStream = client.getInputStream();
                    int available = inputStream.available();
                    if (available > 0) {
                        byte[] bytes = inputStream.readNBytes(available);
                        Message clientMessage = mapper.readValue(bytes, Message.class);
                        log.debug("Сервер получил сообщение" + clientMessage.text);
                        log.debug("Ники сейчас \"" + String.join(",", usersNicks) + "\"");
                        if (clientMessage.text == null) {
                            if (users.containsValue(clientMessage.userNick)) {
                                ServerMessage message = new ServerMessage(1, null, null);
                                byte[] json = mapper.writeValueAsBytes(message);

                                OutputStream outputStream = client.getOutputStream();
                                outputStream.write(json);
                                outputStream.flush();

                            } else {
                                this.users.put(client, clientMessage.userNick);
                                usersNicks.add(clientMessage.userNick);
                                ServerMessage message = new ServerMessage(0, null, usersNicks);
                                byte[] json = mapper.writeValueAsBytes(message);
                                OutputStream outputStream = client.getOutputStream();
                                outputStream.write(json);
                                log.debug("Подтвердили ник: " + clientMessage.userNick);
                                outputStream.flush();

                                messages.add(new Message("Server", "Встречайте нового пользователя " + clientMessage.userNick));
                            }
                        } else {
                            if (usersNicks.contains(clientMessage.userNick)) {
                                messages.add(clientMessage);
                            }
                        }
                    }

                } catch (IOException e) {
                    log.error("Клиент отвалился при слушании? " + this.users.getOrDefault(client, client.toString()));
                    messages.add(new Message("Server", "Нас покинул " + usersNicks.remove(client)));
                    usersNicks.remove(this.users.get(client));
                    this.users.remove(client);
                    synchronized (this.clients) {
                        this.clients.remove(client);
                    }
                }
            }
            if (messages.isEmpty()) {
                continue;
            }
            byte[] json;
            try {
                ServerMessage serverMessage = new ServerMessage(0, messages, usersNicks);
                json = mapper.writeValueAsBytes(serverMessage);
            } catch (JsonProcessingException e) {
                log.error("Ошибка при формировании сообщений от сервера" + e.getMessage());
                continue;
            }
            log.debug("Ники сейчас \"" + String.join(",", usersNicks) + "\"");
            messages = new ArrayList<>();

            for (Socket client : clientes) {

                if (this.users.containsKey(client)) {
                    log.debug("Собираемся писать " + this.users.get(client));
                    try {
                        OutputStream outputStream = client.getOutputStream();
                        outputStream.write(json);
                        outputStream.flush();
                        log.debug("Сервер отправил сообщение  клиенту " + this.users.get(client));

                    } catch (IOException e) {

                        log.error("Клиент отвалился при отправке " + this.users.getOrDefault(client, client.toString()));
                        messages.add(new Message("Server", "Нас покинул " + this.users.get(client)));
                        usersNicks.remove(this.users.get(client));
                        this.users.remove(client);
                        synchronized (this.clients) {
                            this.clients.remove(client);
                        }
                    } catch (Exception e) {
                        log.error("Ошибка: " + e.getMessage());
                    }

                }
            }


        }
    }
}



