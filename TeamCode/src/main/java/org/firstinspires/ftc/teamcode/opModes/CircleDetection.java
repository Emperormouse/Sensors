package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;


@Autonomous(name = "Circle Identification")
public class CircleDetection extends OpMode {
    private static final int width = 640;
    private static final int height = 480;
    private ShapePipeline pipeline;

    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Camera");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.NATIVE_VIEW);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(width, height, OpenCvCameraRotation.UPSIDE_DOWN);
                pipeline = new ShapePipeline();
                camera.setPipeline(pipeline);
            }
            @Override
            public void onError(int errorCode)
            {
                telemetry.addData("Error code: ", errorCode);
                telemetry.update();
            }
        });

    }
    public void loop() {

    }


    static class ShapePipeline extends OpenCvPipeline {
        Mat hsv = new Mat();
        Mat orangeMask = new Mat();

        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            Scalar lowHSVOrange = new Scalar(5, 80, 80);
            Scalar highHSVOrange = new Scalar(15, 255, 255);
            Core.inRange(hsv, lowHSVOrange, highHSVOrange, orangeMask);

            return orangeMask;
        }
    }
}