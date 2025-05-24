package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utility.Button;
import org.firstinspires.ftc.teamcode.utility.Controller;

@TeleOp(name = "MainTele")
public class MainTeleOp extends LinearOpMode {
    public void waitSeconds(double seconds) {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < (seconds * 1000));
    }

    @Override
    public void runOpMode() throws InterruptedException {

        Controller pad1 = new Controller(gamepad1);

        waitForStart();

        int count = 0;
        int count2 = 0;
        int count3 = 0;
        while (opModeIsActive()) {
            //odo.update();

            if (pad1.right_trigger.justPressed()) {
                count += 1;
            }
            if (pad1.right_trigger.doubleTapped()) {
                count3 += 1;
            }

            if (pad1.right_trigger.justReleased()) {
                count2 += 1;
            }

            telemetry.addData("Trigger: ", pad1.right_trigger.poll());
            telemetry.addData("TriggerIsPressed: ", pad1.right_trigger.isPressed());
            telemetry.addData("timesPressed: ", count);
            telemetry.addData("doubleTapped: ", count3);
            telemetry.addData("durationPressed: ", pad1.right_trigger.durationPressed());
            telemetry.addData("timesReleased: ", count2);
            telemetry.addData("durationReleased: ", pad1.right_trigger.durationReleased());

            telemetry.update();
            pad1.update();
        }
    }
}