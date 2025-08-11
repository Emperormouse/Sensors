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


@Autonomous(name = "Symbol Identification")
public class SymbolIdentification extends OpMode {
    private static final int width = 640;
    private static final int height = 480;
    public static Mat shapeRef = new Mat();
    private ShapePipeline pipeline;

    public void init() {
        /*File f = new File("/data/pi.png");
        telemetry.addData("Exists: ", f.exists());
        telemetry.addData("Read: ", f.canRead());
        telemetry.update();*/

        //The image of pi must be in this location for this program to work
        Mat shapeImage = Imgcodecs.imread("/data/pi.png", Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(shapeImage, shapeRef, 128, 255, Imgproc.THRESH_BINARY);

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
        telemetry.addData("Data: ", pipeline.data);
    }


    static class ShapePipeline extends OpenCvPipeline {
        public double data = 0.0;

        //All mats must be declared here to prevent memory leaks, filling up ram over time
        private final Mat grey = new Mat();
        private Mat mask = new Mat();
        private Mat subMask = new Mat();
        private Mat maskSized = new Mat();
        private Mat xorMask = new Mat();
        private Mat hierarchy = new Mat();

        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(grey, mask, 70, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_not(mask, mask);

            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            for (Mat shape : contours) {
                Rect boundingBox = Imgproc.boundingRect(shape);
                if (boundingBox.area() > 250) {
                    subMask = mask.submat(boundingBox);

                    Imgproc.resize(subMask, maskSized, new Size(shapeRef.width(), shapeRef.height()));

                    Core.bitwise_xor(shapeRef, maskSized, xorMask);
                    double averageColor = Core.sumElems(xorMask).val[0]/(xorMask.rows()*xorMask.cols());
                    if (averageColor > 210) {
                        Imgproc.rectangle(input, boundingBox, new Scalar(0, 255, 0));
                    }
                }
            }
            return input;
        }
    }
}