package org.firstinspires.ftc.teamcode.opModes;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.FLOODFILL_MASK_ONLY;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.medianBlur;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

@Autonomous(name = "Camera Test")
public class CameraTest extends OpMode {
    private static final int width = 640;
    private static final int height = 480;
    private final TestPipeline pipeline = new TestPipeline();

    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Camera");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.NATIVE_VIEW);
        camera.setPipeline(pipeline);

        camera.openCameraDevice();
        camera.startStreaming(width, height, OpenCvCameraRotation.UPSIDE_DOWN);
    }

    public void loop() {
        ArrayList<Sample> samplesCopy = pipeline.samples;
        samplesCopy.sort((Sample x, Sample y) -> (int)(y.area - x.area));
        for (Sample sample : samplesCopy) {
            if (sample != null) {
                telemetry.addLine("\n" + sample.color + " Sample:");
                telemetry.addData("Estimated distance: ", sample.distance);
                telemetry.addData("Center offset: ", sample.centerOffset);
            }
        }
        telemetry.addLine();
    }


    enum Color {
        RED,
        BLUE,
        YELLOW,
    }
    static class Sample {
        public double distance;
        public double centerOffset;
        public Color color;
        public Point center;
        public double coverage; //Mostly just useful for debugging
        public double area; //Mostly just useful for debugging

        public Sample(double distance, Point center, Color color, double coverage, double area) {
            this.distance = distance;
            this.center = center;
            this.color = color;
            this.coverage = coverage;
            this.area = area;

            centerOffset = center.x - (width/2);
        }
    }

    static class TestPipeline extends OpenCvPipeline {
        //All mats must be declared here to prevent memory leaks, filling up ram over time
        private final Mat hsv = new Mat();
        private final Mat hierarchy = new Mat();
        private final Mat redMask1 = new Mat();
        private final Mat redMask2 = new Mat();
        private final Mat redMask = new Mat();
        private final Mat blueMask = new Mat();
        private final Mat yellowMask = new Mat();
        private Mat mask = null;

        private final Color[] colors = Color.values();
        public ArrayList<Sample> samples = new ArrayList<>();

        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            //===RED===
            //Red is on the top and bottom of the HSV spectrum, so they both have to covered and then combined
            //Bottom part
            Scalar lowHSV1Red = new Scalar(0, 100, 60);
            Scalar highHSV1Red = new Scalar(5, 255, 255);
            //Top part
            Scalar lowHSV2Red = new Scalar(175, 100, 60);
            Scalar highHSV2Red = new Scalar(180, 255, 255);


            Core.inRange(hsv, lowHSV1Red, highHSV1Red, redMask1);
            Core.inRange(hsv, lowHSV2Red, highHSV2Red, redMask2);
            //Combines the two masks
            Core.bitwise_or(redMask1, redMask2, redMask);

            //===BLUE===
            Scalar lowHSVBlue = new Scalar(105, 100, 60);
            Scalar highHSVBlue = new Scalar(125, 255, 255);
            Core.inRange(hsv, lowHSVBlue, highHSVBlue, blueMask);

            //I haven't tested yellow because I don't have a yellow sample
            //===YELLOW===
            Scalar lowHSVYellow = new Scalar(23, 100, 60);
            Scalar highHSVYellow = new Scalar(33, 255, 255);
            Core.inRange(hsv, lowHSVYellow, highHSVYellow, yellowMask);

            ArrayList<Sample> tmpSamples = new ArrayList<>();
            for (Color color : colors) {
                switch (color) {
                    case RED: mask = redMask; break;
                    case BLUE: mask = blueMask; break;
                    case YELLOW: mask = yellowMask; break;
                }

                //Removes noise by going through every 5x5 area and making them all the median color (which is either black or white)
                Imgproc.medianBlur(mask, mask, 5);

                ArrayList<MatOfPoint> contours = new ArrayList<>();
                Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                for (MatOfPoint contour : contours) {
                    Rect boundingBox = Imgproc.boundingRect(contour);

                    // Shrinks the bounding box by 0.9, since the samples always look rough around the edges
                    Point center = new Point(boundingBox.x + boundingBox.width/2.0, boundingBox.y + boundingBox.height/2.0);
                    int shrunkWidth = (int)(boundingBox.width*0.9);
                    int shrunkHeight = (int)(boundingBox.height*0.9);
                    boundingBox = new Rect(
                            (int)center.x - shrunkWidth/2,
                            (int)center.y - shrunkHeight/2,
                            shrunkWidth,
                            shrunkHeight
                    );

                    // Determine how much of the bounding box is filled with white pixels, which
                    // kind of determines how similar to a rectangle the shape is
                    Mat boxSubmat = mask.submat(boundingBox);
                    double area = Core.sumElems(boxSubmat).val[0];
                    double coverage = area / boundingBox.area(); //0-255 scale of how much of bounding box contains white pixels

                    if (area > 50_000 && coverage > 240 && boundingBox.width*1.15 > boundingBox.height) { //Might as well check that the object is not taller than it is wide
                        Imgproc.rectangle(mask, boundingBox, new Scalar(255, 255, 255));
                        Imgproc.rectangle(input, boundingBox, new Scalar(0, 255, 0));

                        int height = (int)(boundingBox.height*1.1);
                        double distance = 2*(700.7782976 - (double)height*0.0485562)/(double)(height-1); //Equation determined using regression
                        tmpSamples.add(new Sample (
                                distance,
                                center,
                                color,
                                coverage,
                                area
                        ));
                    }
                }
            }



            samples = tmpSamples;


            return input;
        }
    }
}