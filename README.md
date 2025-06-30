# Robotics Projects Repository

This is the repositoy for my OpenCV practice and a couple other smaller things

## Computer Vision Practice
The "CameraTest" Opmode is an opmode which I created to learn about some of OpenCV's features. It can identify samples from Into the Deep if they're laying horizontally, and the camera is on the ground. It can differentiate the three colors of samples, and can differentiate samples from other objects based on samples' rectangular shape. It also estimates the samples' distances in inches, although it is usually a couple inches off. It also reports the distance in pixels between the center of its view and the center of each samples. These two pieces of data could be used with a PID algorithm to make the robot move to nearby one of the samples. It only works up to about 5 or 6 feet.

![opencv1](https://github.com/user-attachments/assets/592b34a5-9899-47f0-9d0d-fd1f3713c95c)
![opencv2](https://github.com/user-attachments/assets/ede17ddb-b0a5-41c1-8871-841f6bc2e9b4)
