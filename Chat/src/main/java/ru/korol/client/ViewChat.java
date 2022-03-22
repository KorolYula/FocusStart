package ru.korol.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ViewChat extends JFrame {
    private final   JTextArea nicks;
    private  final JTextArea chat;
    public   final JTextField textField;
    public  boolean textIsReady=false;
    private static final Logger log = LoggerFactory.getLogger(Client.class.getName());

    public ViewChat(String nickName) {
        super("Вы- "+nickName);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640,480);
        setResizable(true);
        Container contentPane = getContentPane();
        JPanel panel = new JPanel();
        JLabel text = new JLabel("Введите сообщение: ");
        textField = new JTextField(10);
        textField.addActionListener(e -> textIsReady=true);
        chat = new JTextArea(10, 30);
        JScrollPane schat =new JScrollPane(chat);
        //panel.add(new JScrollPane(chat));
        chat.setText("");
        nicks = new JTextArea(10, 5);
        JScrollPane snicks = new JScrollPane(nicks);
        //panel.add(new JScrollPane(nicks));
        nicks.setText("");
        nicks.setEditable(false);
        chat.setEditable(false);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(snicks)
                .addComponent(text));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(schat)
                .addComponent(textField));
        layout.setHorizontalGroup(hGroup);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(schat)
                .addComponent(snicks));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(text)
                .addComponent(textField));
        layout.setVerticalGroup(vGroup);
        contentPane.add(panel);
        panel.setVisible(true);
        pack();
        setVisible(true);
    }

    public    void setNicks(String newNicks ){
        log.debug("Пришли ники: "+newNicks);

        nicks.setText(newNicks);
    }

    public    void addChat(String newMessages) {
        chat.append("\n\n"+newMessages);
    }
}
