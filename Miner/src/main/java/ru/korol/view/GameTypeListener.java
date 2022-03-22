package ru.korol.view;

public interface GameTypeListener {
    void onGameTypeChanged(GameType gameType);

    void onNewGameStarted();
}