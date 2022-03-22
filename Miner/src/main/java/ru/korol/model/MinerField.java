package ru.korol.model;

import ru.korol.view.GameType;

import java.util.Map;

public class MinerField {
    private boolean[][] bombsField;
    private int[][] field; // Типы клеток: 0-закрыта,-1 флаг,10-открыта, пустая,1-8 - количество мин
    private final Map<GameType, int[]> gameTypeHashMap;
    private GameType gameType;

    public MinerField(Map<GameType, int[]> gameTypeHashMap) {
        this.gameTypeHashMap = gameTypeHashMap;
    }

    public void setField(GameType gameType) {
        this.gameType = gameType;
        field = new int[gameTypeHashMap.get(gameType)[0]][gameTypeHashMap.get(gameType)[1]];
    }

    public boolean isOpened(int x, int y) {
        return (field[x][y] >= 1 && field[x][y] <= 8);
    }

    public boolean isClose(int x, int y) {
        return (field[x][y] == 0);
    }

    public boolean isBombs(int x, int y) {
        return bombsField[x][y];
    }

    public void createBombs(int initialX, int initialY) {
        int sizeX = gameTypeHashMap.get(gameType)[0];
        int sizeY = gameTypeHashMap.get(gameType)[1];
        int bombsCount = gameTypeHashMap.get(gameType)[2];
        bombsField = new boolean[sizeX][sizeY];
        int counter = 1;

        while (counter <= bombsCount) {
            int x = (int) (Math.random() * sizeX);
            int y = (int) (Math.random() * sizeY);

            if (y == initialY && x == initialX) {
                continue;
            }

            if (!bombsField[x][y]) {
                bombsField[x][y] = true;
                counter++;
            }
        }
    }

    private int getNearbyBombsCount(int x, int y) {
        int bombsCount = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < gameTypeHashMap.get(gameType)[0] && y + j >= 0 && y + j < gameTypeHashMap.get(gameType)[1]) {
                    if (bombsField[x + i][y + j]) {
                        bombsCount++;
                    }
                }
            }
        }
        return bombsCount;
    }

    private int getNearbyFlagsCount(int x, int y) {
        int flagsCount = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < gameTypeHashMap.get(gameType)[0] && y + j >= 0 && y + j < gameTypeHashMap.get(gameType)[1]) {
                    if (field[x + i][y + j] == -1) {
                        flagsCount++;
                    }
                }
            }
        }
        return flagsCount;
    }

    public boolean hasAllFlag(int x, int y) {
        return (field[x][y] == getNearbyFlagsCount(x, y));
    }


    public boolean isFinished(int flagsCount) {
        for (int i = 0; i < gameTypeHashMap.get(gameType)[0]; i++) {
            for (int j = 0; j < gameTypeHashMap.get(gameType)[1]; j++) {
                if (field[i][j] == 0) {
                    return false;
                }
            }
        }
        return flagsCount == gameTypeHashMap.get(gameType)[2];
    }

    public boolean setFlag(int x, int y) {
        if (field[x][y] == 0) {
            field[x][y] = -1;
            return true;
        } else if (field[x][y] == -1) {
            field[x][y] = 0;
        }
        return false;
    }

    public int setNearbyBombsCount(int x, int y) {
        int nearbyBombsCount = getNearbyBombsCount(x, y);
        if (nearbyBombsCount != 0) {
            field[x][y] = nearbyBombsCount;
        } else {
            field[x][y] = 10;

        }
        return nearbyBombsCount;
    }

    public boolean isAllRight() {
        for (int i = 0; i < gameTypeHashMap.get(gameType)[0]; i++) {
            for (int j = 0; j < gameTypeHashMap.get(gameType)[1]; j++) {
                if (bombsField[i][j] && field[i][j] != -1) {
                    return false;
                }
            }
        }
        return true;
    }
}
