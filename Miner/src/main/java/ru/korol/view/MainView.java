package ru.korol.view;

import ru.korol.app.Controller;

public class MainView implements View{
    private final Controller controller;
    private final MainWindow mainWindow;
    private final SettingsWindow settingsWindow;
    private final HighScoresWindow highScoresWindow;
    private final WinWindow winWindow;
    private final LoseWindow loseWindow;
    private final RecordsWindow recordsWindow;

    public MainView(ru.korol.app.Controller controller) {
        this.controller = controller;
        mainWindow = new MainWindow();
        settingsWindow = new SettingsWindow(mainWindow);
        highScoresWindow = new HighScoresWindow(mainWindow);
        loseWindow = new LoseWindow(mainWindow);
        winWindow = new WinWindow(mainWindow);
        recordsWindow = new RecordsWindow(mainWindow);
    }

    public  void start() {
        mainWindow.setCellListener(controller);
        settingsWindow.setGameTypeListener(controller);
        mainWindow.setNewGameMenuAction(e -> controller.onNewGameStarted());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        loseWindow.setNewGameListener(controller);
        winWindow.setNewGameListener(controller);
        recordsWindow.setNameListener(controller);
        mainWindow.setVisible(true);
    }

    @Override
    public void onFieldSizeChanged(int[] fieldSize) {
        mainWindow.createGameField(fieldSize[0], fieldSize[1]);
    }

    @Override
    public void onsetCellImage(int x, int y, GameImage gameImage) {
        mainWindow.setCellImage(x, y, gameImage);
    }

    @Override
    public void onFlagsCountChanged(int bombsCount) {
        mainWindow.setBombsCount(bombsCount);
    }
    @Override
    public void setGameWin() {
       winWindow.setVisible(true);
    }
    @Override
    public void onTimerChange(int value) {
        mainWindow.setTimerValue(value);
    }
    @Override
    public void setGameLose() {
        loseWindow.setVisible(true);
    }
    @Override
    public void setNewRecord(GameType gameType, String winnerName, int timeValue) {
        switch (gameType){
            case NOVICE -> highScoresWindow.setNoviceRecord( winnerName,  timeValue);
            case MEDIUM -> highScoresWindow.setMediumRecord(winnerName,  timeValue);
            case EXPERT -> highScoresWindow.setExpertRecord(winnerName,  timeValue);
        }
    }
    @Override
    public void onRecordChanged() {
        recordsWindow.setVisible(true);
    }
}