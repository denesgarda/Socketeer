package com.denesgarda.Socketeer.data;

import java.util.Timer;
import java.util.TimerTask;

public class Timeout {
    String address;
    Timer timer;
    TimerTask timerTask;
    int delay;

    protected Timeout(String address) {
        this.address = address;
        this.timer = new Timer();
    }

    public void setDelay(int milliseconds) {
        this.delay = milliseconds;
    }

    public void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    public String getAddress() {
        return address;
    }

    public void startTimer() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(timerTask, delay);
    }
}
