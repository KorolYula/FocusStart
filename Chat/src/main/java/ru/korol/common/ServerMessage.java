package ru.korol.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.korol.client.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ServerMessage {
    static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public int status;
    public ArrayList<Message> messagesList;
    public TreeSet<String> usersNicks;
    public String time;
    private static final Logger log = LoggerFactory.getLogger(Client.class.getName());

    public ServerMessage() {
    }

    public ServerMessage(int status, ArrayList<Message> messagesList, TreeSet<String> usersNicks) {

        this.status = status;
        this.messagesList = messagesList;
        this.usersNicks = usersNicks;
        this.time = dateFormat.format(new Date());
    }

    public String toString() {
        String result = messagesList.stream()
                .map(Message::toString)
                .collect(Collectors.joining("\n\n"));
        log.debug("Форматируем тексты сообщений от сервера: "+result);
        return result;
    }
    public String nicksToSting (){
        return String.join("\n",usersNicks);
    }
}