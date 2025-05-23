package org.firstinspires.ftc.teamcode.utility;

public class Button {
    private final ButtonStatus status;
    private boolean lastPoll;
    private boolean currentPoll;
    private long lastPressTime;
    private long lastReleaseTime;

    public Button(ButtonStatus status) {
        this.status = status;
        lastPressTime = System.currentTimeMillis();
        lastReleaseTime = System.currentTimeMillis();
    }

    public boolean poll() {
        return currentPoll;
    }

    public boolean justPressed() {
        return currentPoll && !lastPoll;
    }

    public boolean justReleased() {
        return !currentPoll && lastPoll;
    }

    public boolean doubleTapped() {
        return justPressed() && System.currentTimeMillis() - lastPressTime < 500;
    }

    public int durationPressed() {
        if (!currentPoll) {
            return -1;
        } else {
            return (int)(System.currentTimeMillis() - lastPressTime);
        }
    }

    public int durationReleased() {
        if (currentPoll) {
            return -1;
        } else {
            return (int)(System.currentTimeMillis() - lastReleaseTime);
        }
    }

    public void update() {
        if (justPressed()) {
            lastPressTime = System.currentTimeMillis();
        }
        if (justReleased()) {
            lastReleaseTime = System.currentTimeMillis();
        }
        lastPoll = currentPoll;
        currentPoll = status.poll();
    }

    @FunctionalInterface
    public interface ButtonStatus {
        boolean poll();
    }
}
