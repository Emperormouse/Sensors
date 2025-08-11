# Robotics Projects Repository

This is the repositoy for my OpenCV practice and a couple other smaller things

## Computer Vision Practice
The "CameraTest" Auto Opmode is an opmode which I created to learn about some of OpenCV's features. It can identify samples from Into the Deep if they're laying horizontally, and the camera is on the ground. It can differentiate the three colors of samples, and can differentiate samples from other objects based on samples' rectangular shape. It also estimates the samples' distances in inches, although it is usually a couple inches off. It also reports the distance in pixels between the center of its view and the center of each samples. These two pieces of data could be used with a PID algorithm to make the robot move to nearby one of the samples. It only works up to about 5 or 6 feet.

![opencv1](https://github.com/user-attachments/assets/592b34a5-9899-47f0-9d0d-fd1f3713c95c)
![opencv2](https://github.com/user-attachments/assets/ede17ddb-b0a5-41c1-8871-841f6bc2e9b4)

## Field Centric
The "FieldCentric" TeleOp Opmode contains the code that I wrote after last year's season for a field-centric tele-op. However it requires some method to determine the current rotation of the robot, which I didn't include because I don't know what odometer system we'll be using, so that has to be added before it works.

## Improved Gamepad Class
We might not use this, but I think that it makes writing the controls for tele-op easier. The three files "ImprovedGamepad.java", "Button.java", and "Anologue.java" in the utility folder contain the code for an improved version of the Gamepad class that FTC provides, which has methods for checking if a button/analogue device is pressed, just been pressed, just been released, just been double-tapped, how long it's been pressed, and how long it's been released. analogue devices include the triggers and analogue sticks, and have the same functionality as buttons but you can also check their exact values. A demo of the ImprovedGamepad class is in the ButtonTest.java tele-op mode.

## RoadRunner Practice
In the "MeepMeepPractice.java" file I wrote the pathing for a hypothetical auto op-mode which would score 3 specimens and put and extra sample into the observation zone for tele-op. It can be run with the "MeepMeepRun" app which can be found in the drop-down that the "TeamCode" app is on.

https://github.com/user-attachments/assets/84db9d75-fe09-401b-a890-93ba88b4c595


