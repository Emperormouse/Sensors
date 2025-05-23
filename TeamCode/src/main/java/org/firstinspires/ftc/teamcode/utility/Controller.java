package org.firstinspires.ftc.teamcode.utility;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    public Button a;
    public Button b;
    public Button x;
    public Button y;

    public Button dpad_down;
    public Button dpad_up;
    public Button dpad_right;
    public Button dpad_left;

    public Button right_bumper;
    public Button left_bumper;

    public Button right_trigger;
    public Button left_trigger;

    public float right_trigger_analogue;
    public float left_trigger_analogue;

    public Gamepad pad;

    public Controller(Gamepad pad) {
        a = new Button(() -> pad.a);
        b = new Button(() -> pad.b);
        x = new Button(() -> pad.x);
        y = new Button(() -> pad.y);
        dpad_down = new Button(() -> pad.dpad_down);
        dpad_up = new Button(() -> pad.dpad_up);
        dpad_right = new Button(() -> pad.dpad_right);
        dpad_left = new Button(() -> pad.dpad_left);
        right_bumper = new Button(() -> pad.right_bumper);
        left_bumper = new Button(() -> pad.left_bumper);

        this.pad = pad;
    }

    public void update() {
        a.update();
        b.update();
        x.update();
        y.update();
        dpad_down.update();
        dpad_up.update();
        dpad_right.update();
        dpad_left.update();
        right_bumper.update();
        left_bumper.update();
        right_bumper.update();
        left_bumper.update();
    }
}