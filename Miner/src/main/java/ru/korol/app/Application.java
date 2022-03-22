package ru.korol.app;

import ru.korol.model.Miner;
import ru.korol.view.GameType;
import ru.korol.view.MainView;
import ru.korol.view.View;

public class Application {
    public static void main(String[] args) {
        Miner miner = new Miner();
        Controller controller = new Controller(miner);
        View mainView = new MainView(controller);
        miner.setModelListener(mainView);
        miner.changeGame(GameType.NOVICE);
        mainView.start();

    }
}