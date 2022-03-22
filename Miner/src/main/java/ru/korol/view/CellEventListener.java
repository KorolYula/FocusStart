package ru.korol.view;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType);

    void onNewGameStarted();
}