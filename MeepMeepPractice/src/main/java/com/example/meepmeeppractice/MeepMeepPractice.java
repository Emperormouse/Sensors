package com.example.meepmeeppractice;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepPractice {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
            .build();

        Action threeSpecimen = new SequentialAction(myBot.getDrive().actionBuilder(new Pose2d(0, -60, PI/2))
            //Hook first specimen
            .waitSeconds(1.0)
            .lineToY(-35)
            .waitSeconds(2.0)

            //Push third specimen into observation zone
            .setTangent(0)
            .splineToConstantHeading(new Vector2d(20, -35), 0)
            .splineToConstantHeading(new Vector2d(30, -20), PI/2)
            .splineToConstantHeading(new Vector2d(37, -10), 0)
            .setTangent(0)
            .splineToConstantHeading(new Vector2d(45, -30), -PI/2)
            .lineToY(-55)

            //Push fourth specimen into observation zone
            .lineToYSplineHeading(-30, PI/2)
            .splineToConstantHeading(new Vector2d(50, -10), 0)
            .splineToConstantHeading(new Vector2d(55, -20), -PI/2)
            .lineToY(-55)

            //Grab and hook second specimen
            .strafeToLinearHeading(new Vector2d(45, -45), -PI/2)
            .strafeTo(new Vector2d(45, -60))
            .waitSeconds(2.0)
            .strafeToLinearHeading(new Vector2d(5, -35), PI/2)
            .waitSeconds(2.0)

            //Grab and hook third
            .strafeToSplineHeading(new Vector2d(45, -60), -PI/2)
            .waitSeconds(2.0)
            .strafeToLinearHeading(new Vector2d(-5, -35), PI/2)
            .waitSeconds(2.0)
            .lineToY(-36)


            .build());

        myBot.runAction(threeSpecimen);

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }
}