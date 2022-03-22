package ru.korol.app;

import ru.korol.model.Miner;

import ru.korol.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements GameTypeListener, CellEventListener, ActionListener, RecordNameListener {

    public Miner miner;

    public Controller(Miner miner) {
        this.miner = miner;
    }

    @Override
    public void onNewGameStarted() {
        miner.newGame();
    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        miner.changeGame(gameType);
    }

    public void onMouseClick(int x, int y, ButtonType buttonType) {
        miner.onMouseClick(x, y, buttonType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        miner.newGame();
    }

    @Override
    public void onRecordNameEntered(String name) {
        miner.setNewRecord(name);
    }
}