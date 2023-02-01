// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision.Limelight;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

/** Add your docs here. */
// This is the private constructor that will be called once by getInstance() and
// it
// should instantiate anything that will be required by the class
public class TargetThreadLLDataTX {

    private LimelightVision m_llv;

    private DriveSubsystem m_drive;

    public TargetThreadLLDataTX(LimelightVision llv, DriveSubsystem drive) {
        m_llv = llv;
        m_drive = drive;

        Thread tagThreadLL = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {

                        execute();

                        Thread.sleep(100);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Set up thread properties and start it off

        tagThreadLL.setPriority(Thread.NORM_PRIORITY);
        tagThreadLL.setName("AprilTagTx");
        tagThreadLL.start();

    }

    // transfer vision pose correction between 2 subsystems
    public void execute() {

        m_drive.getVisionCorrection(m_llv.visionPoseEstimatedData, m_llv.imageCaptureTime);

    }
}
