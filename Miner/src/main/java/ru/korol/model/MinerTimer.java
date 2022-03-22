package ru.korol.model;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MinerTimer  {
    private final java.util.Timer timer;

    public int getSeconds() {
        return seconds.get();
    }

    private AtomicInteger seconds;
    private java.util.TimerTask timerTask;
    private ModelListener timerListener;


    public MinerTimer() {
        this.timer = new Timer(true);
        seconds = new AtomicInteger(0);

    }

    public void setTimerListener(ModelListener timerListener) {
        this.timerListener = timerListener;
    }

    public void startTimer() {
        seconds.set(0);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timerListener.onTimerChange(seconds.getAndIncrement());
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

       public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}