package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utility.Controller;

@TeleOp(name = "ButtonTest")
public class ButtonTest extends LinearOpMode {
    public void waitSeconds(double seconds) {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < (seconds * 1000));
    }

    @Override
    public void runOpMode() throws InterruptedException {

        Controller pad1 = new Controller(gamepad1);

        waitForStart();

        int a_count = 0;

        int rt_count = 0;
        int rt_dbl_count = 0;
        int rt_release_count = 0;

        while (opModeIsActive()) {
            //odo.update();

            if (pad1.a.justPressed()) {
                a_count += 1;
            }

            if (pad1.right_trigger.justPressed()) {
                rt_count += 1;
            }
            if (pad1.right_trigger.doubleTapped()) {
                rt_dbl_count += 1;
            }
            if (pad1.right_trigger.justReleased()) {
                rt_release_count += 1;
            }

            telemetry.addLine("A Button:");
            telemetry.addData("IsPressed: ", pad1.a.isPressed());
            telemetry.addData("timesPressed: ", a_count);

            telemetry.addLine("\nRight Trigger:");
            telemetry.addData("AnalogueValue: ", pad1.right_trigger.val);
            telemetry.addData("IsPressed: ", pad1.right_trigger.isPressed());
            telemetry.addData("timesPressed: ", rt_count);
            telemetry.addData("timesDoubleTapped: ", rt_dbl_count);
            telemetry.addData("durationPressed: ", pad1.right_trigger.durationPressed());
            telemetry.addData("timesReleased: ", rt_release_count);
            telemetry.addData("durationReleased: ", pad1.right_trigger.durationReleased());

            telemetry.update();
            pad1.update();
        }
    }
}