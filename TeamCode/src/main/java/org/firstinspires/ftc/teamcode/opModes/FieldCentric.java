package org.firstinspires.ftc.teamcode.opModes;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "FieldCentric")
public class FieldCentric extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = (DcMotorEx) hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeft = (DcMotorEx) hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRight = (DcMotorEx) hardwareMap.dcMotor.get("rightFront");
        DcMotor backRight = (DcMotorEx) hardwareMap.dcMotor.get("rightBack");

        //May be different for different drive trains
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            //TODO: Replace this with some method to determine orientation based on your odometer system
            double botRot = 0.0; //RADIANS

            double frPower = 0;
            double flPower = 0;
            double brPower = 0;
            double blPower = 0;

            double y = -gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1.1;
            double rx = -gamepad1.right_stick_x;

            frPower += rx;
            brPower += rx;
            flPower -= rx;
            blPower -= rx;

            //X-DIRECTION
            frPower += y * cos(botRot);
            brPower += y * cos(botRot);
            flPower += y * cos(botRot);
            blPower += y * cos(botRot);

            frPower -= y * sin(botRot);
            brPower += y * sin(botRot);
            flPower += y * sin(botRot);
            blPower -= y * sin(botRot);

            //Y-DIRECTION
            frPower += x * sin(botRot);
            brPower += x * sin(botRot);
            flPower += x * sin(botRot);
            blPower += x * sin(botRot);

            frPower += x * cos(botRot);
            brPower -= x * cos(botRot);
            flPower -= x * cos(botRot);
            blPower += x * cos(botRot);

            double denominator = max(1, max(max(max(abs(frPower), abs(brPower)), abs(flPower)), abs(blPower)));

            frontLeft.setPower(flPower / denominator);
            frontRight.setPower(frPower / denominator);
            backLeft.setPower(blPower / denominator);
            backRight.setPower(brPower / denominator);

        }
    }
}

