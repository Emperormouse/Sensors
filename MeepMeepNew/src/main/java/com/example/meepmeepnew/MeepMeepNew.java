package com.example.meepmeepnew;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import static java.lang.Math.toRadians;

public class MeepMeepNew {
    public static void main(String[] args) {
        Pose2d startPos = new Pose2d(61, -20, toRadians(180));

        Vector2d launchVec = new Vector2d(55, -15);
        double launchHeading = toRadians(205);

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
            .build();

        Action pgp = new SequentialAction(myBot.getDrive().actionBuilder(startPos)
                .waitSeconds(2)
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(3)
            .setTangent(toRadians(180))
            .splineToLinearHeading(new Pose2d(35, -50, toRadians(-90)), toRadians(-90))
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(3)
            .splineToLinearHeading(new Pose2d(12, -50, toRadians(-90)), toRadians(-90))
            .strafeToLinearHeading(new Vector2d(0, -60), toRadians(180))
            .waitSeconds(3)
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(3)
            .build());

        Action gpp = new SequentialAction(myBot.getDrive().actionBuilder(startPos)
                .waitSeconds(2)
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(4)
            .setTangent(toRadians(180))
            .splineToLinearHeading(new Pose2d(12, -40, toRadians(-90)), toRadians(-90))
            .lineToY(-50)
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(3)
            .splineToLinearHeading(new Pose2d(36, -40, toRadians(-90)), toRadians(-90))
            .lineToY(-50)
            .strafeToLinearHeading(new Vector2d(0, -60), toRadians(180))
            .waitSeconds(3)
            .strafeToLinearHeading(launchVec, launchHeading)
            .waitSeconds(3)
            .build());

        Action ppg = new SequentialAction(myBot.getDrive().actionBuilder(startPos)
                .waitSeconds(2)
            .strafeToLinearHeading(launchVec, launchHeading)
                .waitSeconds(4)
            .setTangent(toRadians(180))
            .splineToLinearHeading(new Pose2d(12, -40, toRadians(-90)), toRadians(-90))
            .lineToY(-50)
            .splineToLinearHeading(new Pose2d(55, -14, toRadians(-205)), toRadians(-90))
                .waitSeconds(3)
            .splineToLinearHeading(new Pose2d(0, -60, toRadians(180)), toRadians(-90))
            .splineToLinearHeading(new Pose2d(35, -70, toRadians(90)), toRadians(-90))
            .build());


            myBot.runAction(ppg);

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }
}