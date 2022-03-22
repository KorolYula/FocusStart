package ru.korol.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

    public String userNick;
    public String time;
    public String text;

    public Message() {
    }

    public Message(String userNick, String text) {
        this.userNick = userNick;
        this.time = dateFormat.format(new Date());
        this.text = text;
    }

    public String toString (){
        return time+" <"+userNick+">: "+text;
    }

}
