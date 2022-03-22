package ru.korol.model;

import ru.korol.view.GameImage;
import ru.korol.view.GameType;

public interface ModelListener {

    void onFieldSizeChanged(int[] fieldSize);

    void onsetCellImage(int x, int y, GameImage gameImage);

    void onFlagsCountChanged(int bombsCount);

    void onTimerChange(int value);

    void setGameWin();

    void setGameLose();

    void setNewRecord(GameType gameType, String winnerName, int timeValue);

    void onRecordChanged();
}