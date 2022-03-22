package ru.korol.model;

public class Result {
    private String name;
    private int gameTime;

    public Result(String name, int gameTime) {
        this.name = name;
        this.gameTime = gameTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }
}