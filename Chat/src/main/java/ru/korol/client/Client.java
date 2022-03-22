package ru.korol.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.korol.common.Message;
import ru.korol.common.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {
    public static final String CHARSET_NAME = "UTF-8";
    private static String serverName;
    private static String nickName;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(Client.class.getName());

    public static void getPort() {
        serverName = JOptionPane.showInputDialog("Введите порт");
    }

    public static String getNick(String s) {
        return JOptionPane.showInputDialog(s);
    }

    public static void getAuthorization(Socket socket) {
        String s = "Введите ник";
        while (true) {
            nickName = getNick(s);
            try {
                Message message = new Message(nickName, null);
                byte[] json = mapper.writeValueAsBytes(message);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(json);
                outputStream.flush();
                log.debug("Клиент отправил запрос на ник " + message.userNick);
                while (true) {
                    int available = socket.getInputStream().available();
                    if (available > 0) {
                        byte[] bytes = socket.getInputStream().readNBytes(available);
                        log.debug("Клиент получил сообщение от сервера" + new String(bytes, CHARSET_NAME));
                        ServerMessage serverMessage = mapper.readValue(bytes, ServerMessage.class);
                        if (serverMessage.status == 0) {
                            return;

                        } else {
                            log.debug("Такой ник уже существует");
                            s = "Такой ник уже существует. Введите другой ник";


                            break;
                        }
                    }
                }

            } catch (JsonParseException e) {
                log.error(e.getMessage());
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error("Сервер отпал");

            }
        }
    }

    public static Socket connect() throws IOException {
        return new Socket("127.0.0.1", Integer.parseInt(serverName));
    }


    public static String getInputText(ViewChat viewChat) {
        String result = null;
        synchronized (viewChat) {
            if (viewChat.textIsReady) {
                viewChat.textIsReady = false;
                result = viewChat.textField.getText();
                viewChat.textField.setText("");
            }
        }
        return result;
    }

    public static void chatLoop(ViewChat viewChat, Socket socket) throws IOException {
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException ignored) {
            }
            String data = getInputText(viewChat);
            if (data != null) {
                Message message = new Message(nickName, data);
                byte[] json = mapper.writeValueAsBytes(message);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(json);
                outputStream.flush();
                log.debug("Клиент отправил сообщение " + message.text);
            }

            int available = socket.getInputStream().available();
            if (available > 0) {
                byte[] bytes = socket.getInputStream().readNBytes(available);
                log.debug("Клиент получил сообщение от сервера" + new String(bytes, CHARSET_NAME));

                ServerMessage serverMessage = mapper.readValue(bytes, ServerMessage.class);
                synchronized (viewChat) {
                    viewChat.addChat(serverMessage.toString());
                    viewChat.setNicks(serverMessage.nicksToSting());
                }
                log.debug("Получено от сервера сообщение");
            }
        }
    }

    public static void main(String[] args) {
        getPort();
        try (Socket socket = connect()) {
            getAuthorization(socket);
            ViewChat viewChat = new ViewChat(nickName);
            log.debug("Запущено окно чата");
            chatLoop(viewChat, socket);
        } catch (IOException e) {
            log.error("Нет сервера на таком порту");
            JOptionPane.showMessageDialog(null, "Нет сервера на таком порту");
        }
    }
}










