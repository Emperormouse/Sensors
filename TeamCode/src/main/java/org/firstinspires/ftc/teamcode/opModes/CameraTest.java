package org.firstinspires.ftc.teamcode.opModes;

import android.annotation.SuppressLint;

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

    @SuppressLint("DefaultLocale")
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
            ArrayList<Sample> samplesCopy = pipeline.samples;
            samplesCopy.sort((Sample x, Sample y) -> (int)(y.area - x.area));
            for (Sample sample : samplesCopy) {
                if (sample != null) {
                    telemetry.addLine("\n" + sample.color + " Sample:");
                    telemetry.addData("Estimated distance: ", sample.distance);
                    telemetry.addData("Area: ", sample.area);
                    telemetry.addData("Coverage: ", sample.coverage);
                }
            }
            telemetry.addLine();
            telemetry.update();
        }
    }

    enum Color {
        RED,
        BLUE,
        YELLOW,
    }
    static class Sample {
        public double distance;
        public double coverage;
        public double area;
        public Color color;

        public Sample(double distance, double coverage, double area, Color color) {
            this.distance = distance;
            this.coverage = coverage;
            this.area = area;
            this.color = color;
        }
    }

    static class testPipeline extends OpenCvPipeline {
        private Mat hsv = new Mat();
        private Mat mask1 = new Mat();
        private Mat mask2 = new Mat();
        private Mat fullMask = new Mat();
        private Mat hierarchy = new Mat();
        public ArrayList<Sample> samples = new ArrayList<>();

        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            //Red is on the top and bottom of the HSV spectrum, so they both have to covered and then combined
            //Bottom part
            Scalar lowHSV1 = new Scalar(0, 125, 40);
            Scalar highHSV1 = new Scalar(15, 255, 255);

            //Top part
            Scalar lowHSV2 = new Scalar(345, 125, 40);
            Scalar highHSV2 = new Scalar(360, 255, 255);

            Core.inRange(hsv, lowHSV1, highHSV1, mask1);
            Core.inRange(hsv, lowHSV2, highHSV2, mask2);

            //Combines the two masks
            Core.bitwise_or(mask1, mask2, fullMask);

            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(fullMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            ArrayList<Sample> tmpSamples = new ArrayList<>();
            for (MatOfPoint contour : contours) {
                Rect boundingBox = Imgproc.boundingRect(contour);

                Mat boxSubmat = fullMask.submat(boundingBox);
                double area = Core.sumElems(boxSubmat).val[0];
                double coverage = area / boundingBox.area();

                if (area > 100_000 && coverage > 190 && boundingBox.width*1.2 > boundingBox.height) {
                    Imgproc.rectangle(fullMask, boundingBox, new Scalar(255, 255, 255));
                    Imgproc.rectangle(input, boundingBox, new Scalar(0, 255, 0));

                    int height = boundingBox.height;
                    double distance = (700.7782976 - (double)height*0.0485562)/(double)(height-1); //Equation determined using regression
                    tmpSamples.add(new Sample (
                            distance,
                            coverage,
                            area,
                            Color.RED
                    ));
                }
            }
            samples = tmpSamples;


            return fullMask;
        }
    }
}