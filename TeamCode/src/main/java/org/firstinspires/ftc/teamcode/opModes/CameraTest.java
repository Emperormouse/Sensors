package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

@Autonomous(name = "Camera Test")
public class CameraTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Camera");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.NATIVE_VIEW);
        testPipeline pipeline = new testPipeline();
        camera.setPipeline(pipeline);

        camera.openCameraDevice();

        camera.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);

        while(!isStarted()) {
            telemetry.addData("Estimated distance: ", pipeline.distance);
            telemetry.update();
        }
    }

    static class testPipeline extends OpenCvPipeline {
        private Mat hsv = new Mat();
        private Mat mask1 = new Mat();
        private Mat mask2 = new Mat();
        private Mat fullMask = new Mat();
        private Mat hierarchy = new Mat();
        public double distance;

        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            Scalar lowHSV1 = new Scalar(0, 75, 75);
            Scalar highHSV1 = new Scalar(15, 255, 255);

            Scalar lowHSV2 = new Scalar(345, 75, 75);
            Scalar highHSV2 = new Scalar(360, 255, 255);

            Core.inRange(hsv, lowHSV1, highHSV1, mask1);
            Core.inRange(hsv, lowHSV2, highHSV2, mask2);

            Core.bitwise_or(mask1, mask2, fullMask);

            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(fullMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            double maxArea = 0;
            MatOfPoint maxContour = null;
            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea) {
                    maxArea = area;
                    maxContour = contour;
                }
            }

            if (maxContour != null) {
                Rect boundingBox = Imgproc.boundingRect(maxContour);
                Imgproc.rectangle(input, boundingBox, new Scalar(255, 0, 0));

                int height = boundingBox.height;
                distance = (700.7782976 - (double)height*0.0485562)/(double)(height-1);
            }

            return input;
        }
    }
}