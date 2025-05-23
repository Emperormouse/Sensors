package org.firstinspires.ftc.teamcode.utility;

public class Analogue {
    private final ButtonStatus bStatus;
    private final AnalogueStatus aStatus;
    private boolean lastIsPressed;
    private boolean currentIsPressed;
    private double currentPoll;
    private long lastPressTime;
    private long lastReleaseTime;

    public Analogue(AnalogueStatus aStatus, ButtonStatus bStatus) {
        this.bStatus = bStatus;
        this.aStatus = aStatus;
        lastPressTime = System.currentTimeMillis();
        lastReleaseTime = System.currentTimeMillis();
    }

    public double poll() {
        return currentPoll;
    }

    public boolean isPressed() {
        return currentIsPressed;
    }

    public boolean justPressed() {
        return currentIsPressed && !lastIsPressed;
    }

    public boolean justReleased() {
        return !currentIsPressed && lastIsPressed;
    }

    public boolean doubleTapped() {
        return justPressed() && System.currentTimeMillis() - lastPressTime < 500;
    }

    public int durationPressed() {
        if (!currentIsPressed) {
            return -1;
        } else {
            return (int)(System.currentTimeMillis() - lastPressTime);
        }
    }

    public int durationReleased() {
        if (currentIsPressed) {
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
        lastIsPressed = currentIsPressed;
        currentIsPressed = bStatus.poll();
        currentPoll = aStatus.poll();
    }

    @FunctionalInterface
    public interface ButtonStatus {
        boolean poll();
    }
    public interface AnalogueStatus {
        double poll();
    }
}
