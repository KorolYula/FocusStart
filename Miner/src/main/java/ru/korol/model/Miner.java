package ru.korol.model;

import ru.korol.view.ButtonType;
import ru.korol.view.GameImage;
import ru.korol.view.GameType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Miner {
    private final MinerField minerField;
    private GameType gameType;
    private final MinerTimer timer;
    private ModelListener modelListener;
    public int flagsCount;
    private boolean isNewGame;
    private final Map<GameType, int[]> gameTypeHashMap = new HashMap<>(3);
    private final Map<GameType, Result> gameRecordsHashMap = new HashMap<>(3);

    {
        gameTypeHashMap.put(GameType.NOVICE, new int[]{9, 9, 10});   // Данные об типе игры:
        gameTypeHashMap.put(GameType.MEDIUM, new int[]{16, 16, 40}); //Название, размеры поля
        gameTypeHashMap.put(GameType.EXPERT, new int[]{16, 30, 99});//и количество мин

        //Поднимается таблица рекордов из файла "HighScores.txt".
        // Если это не удалось (файла не существует, или была ошибка при чтении),
        // то устанавливаются значения по умолчанию.
        if (!getHighScoresFromFile()) {
            gameRecordsHashMap.put(GameType.NOVICE, new Result("Unknown", 999));
            gameRecordsHashMap.put(GameType.MEDIUM, new Result("Unknown", 999));
            gameRecordsHashMap.put(GameType.EXPERT, new Result("Unknown", 999));
        }
    }

    public Miner() {
        timer = new MinerTimer();
        minerField = new MinerField(gameTypeHashMap);
        isNewGame = true;
        flagsCount = 0;
    }

    private boolean getHighScoresFromFile() {
        String fileName = "HighScores.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String string;
            while ((string = reader.readLine()) != null) {
                String[] s = string.split(" ");
                switch (s[0]) {
                    case "NOVICE":
                        gameRecordsHashMap.put(GameType.NOVICE, new Result(s[1], Integer.parseInt(s[2])));
                    case "MEDIUM":
                        gameRecordsHashMap.put(GameType.MEDIUM, new Result(s[1], Integer.parseInt(s[2])));
                    case "EXPERT":
                        gameRecordsHashMap.put(GameType.EXPERT, new Result(s[1], Integer.parseInt(s[2])));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
        timer.setTimerListener(modelListener);
        //Заполняем первоначальную вью рекордов
        modelListener.setNewRecord(GameType.NOVICE, gameRecordsHashMap.get(GameType.NOVICE).getName(), gameRecordsHashMap.get(GameType.NOVICE).getGameTime());
        modelListener.setNewRecord(GameType.MEDIUM, gameRecordsHashMap.get(GameType.MEDIUM).getName(), gameRecordsHashMap.get(GameType.MEDIUM).getGameTime());
        modelListener.setNewRecord(GameType.EXPERT, gameRecordsHashMap.get(GameType.EXPERT).getName(), gameRecordsHashMap.get(GameType.EXPERT).getGameTime());
    }

    public void setNewRecord(String name) {
        gameRecordsHashMap.get(gameType).setName(name);
    }

    private void putHighScoresInFile() throws FileNotFoundException {
        String fileName = "HighScores.txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            String string = "NOVICE" + " " + gameRecordsHashMap.get(GameType.NOVICE).getName() + " " + gameRecordsHashMap.get(GameType.NOVICE).getGameTime() + "\n" +
                    "MEDIUM" + " " + gameRecordsHashMap.get(GameType.MEDIUM).getName() + " " + gameRecordsHashMap.get(GameType.MEDIUM).getGameTime() + "\n" +
                    "EXPERT" + " " + gameRecordsHashMap.get(GameType.EXPERT).getName() + " " + gameRecordsHashMap.get(GameType.EXPERT).getGameTime();
            writer.println(string);
        }
    }

    public void newGame() {
        changeGame(this.gameType);
    }

    public void changeGame(GameType gameType) {
        timer.stopTimer();
        this.gameType = gameType;
        isNewGame = true;
        flagsCount = 0;
        minerField.setField(gameType);

        if (modelListener != null) {
            modelListener.onFieldSizeChanged(new int[]{gameTypeHashMap.get(gameType)[0], gameTypeHashMap.get(gameType)[1]});
            modelListener.onFlagsCountChanged(gameTypeHashMap.get(gameType)[2]);
        }
    }

    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> openCell(x, y);
            case RIGHT_BUTTON -> flagCell(x, y);
            case MIDDLE_BUTTON -> showCell(x, y);
        }
    }

    public void openCell(int x, int y) {
        if (isNewGame) {
            minerField.createBombs(x, y);
            timer.startTimer();
            isNewGame = false;
        }
        if (!minerField.isClose(x, y)) {
            return;
        }

        if (minerField.isBombs(x, y)) {
            gameLoseExit();
            return;
        }
        painCell(x, y);

        if (minerField.isFinished(flagsCount)) {
            gameWinExit();
        }
    }

    public void painCell(int x, int y) {
        int nearbyBombsCount = minerField.setNearbyBombsCount(x, y);
        if (nearbyBombsCount != 0) {
            String name = "NUM_" + nearbyBombsCount;
            modelListener.onsetCellImage(x, y, GameImage.valueOf(name));
        } else {
            modelListener.onsetCellImage(x, y, GameImage.EMPTY);
            openNearbyCell(x, y);
        }
    }

    public void openNearbyCell(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int newX = x + i;
                int newY = y + j;

                if (newX >= 0 && newX < gameTypeHashMap.get(gameType)[0] && newY >= 0 && newY < gameTypeHashMap.get(gameType)[1]) {
                    if (minerField.isClose(newX, newY) && !minerField.isBombs(newX, newY)) {
                        painCell(newX, newY);
                    }
                }
            }
        }
    }

    public void flagCell(int x, int y) {
        if (minerField.isOpened(x, y)) {
            return;
        }
        if (minerField.setFlag(x, y)) {
            modelListener.onsetCellImage(x, y, GameImage.MARKED);
            flagsCount++;
        } else {
            modelListener.onsetCellImage(x, y, GameImage.CLOSED);
            flagsCount--;
        }
        modelListener.onFlagsCountChanged(gameTypeHashMap.get(gameType)[2] - flagsCount);
        if (minerField.isFinished(flagsCount)) {
            gameWinExit();
        }
    }

    public void showCell(int x, int y) {
        if (minerField.isOpened(x, y)) {
            if (minerField.hasAllFlag(x, y)) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        int newX = x + i;
                        int newY = y + j;

                        if (newX >= 0 && newX < gameTypeHashMap.get(gameType)[0] && newY >= 0 && newY < gameTypeHashMap.get(gameType)[1]) {
                            openCell(newX, newY);
                        }
                    }
                }
            }
        }
    }

    private void gameLoseExit() {
        timer.stopTimer();
        showAllBombs();
        modelListener.setGameLose();
    }

    private void gameWinExit() {
        timer.stopTimer();
        int timerFinishSeconds = timer.getSeconds() - 1;

        if(!minerField.isAllRight()){
            gameLoseExit();
        }
        if (timerFinishSeconds < gameRecordsHashMap.get(gameType).getGameTime()) {
            gameRecordsHashMap.get(gameType).setGameTime(timerFinishSeconds);
            modelListener.onRecordChanged();
            modelListener.setNewRecord(gameType, gameRecordsHashMap.get(gameType).getName(), gameRecordsHashMap.get(gameType).getGameTime());
            try {
                putHighScoresInFile();
            } catch (FileNotFoundException ignored) {
            }
        }
        modelListener.setGameWin();
    }

    public void showAllBombs() {
        for (int x = 0; x < gameTypeHashMap.get(gameType)[0]; x++) {
            for (int y = 0; y < gameTypeHashMap.get(gameType)[1]; y++) {
                if (minerField.isBombs(x, y)) {
                    modelListener.onsetCellImage(x, y, GameImage.BOMB_ICON);
                }
            }
        }
    }
}