// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision.Limelight;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.VisionConstants;
import frc.robot.oi.LimeLight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

/** Add your docs here. */
// This is the private constructor that will be called once by getInstance() and
// it
// should instantiate anything that will be required by the class
public class TargetThread1LL {

    private DriveSubsystem m_drive;

    private LimelightVision m_llv;

    private Pose3d camPose = new Pose3d();

    private Pose3d targetPose = new Pose3d();

    public Transform3d camToTarget = new Transform3d();

    public int fiducialId;

    public int numberTargets = 0;

    double imageCaptureTime;

    int loopctr;

    private Pose2d visionPoseEstimatedData;

    public TargetThread1LL(DriveSubsystem drive, LimelightVision llv) {
        m_drive = drive;
        m_llv = llv;

        Thread tagThread1LL = new Thread(new Runnable() {
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

        tagThread1LL.setPriority(Thread.NORM_PRIORITY);
        tagThread1LL.setName("AprilTagThread 1LL");
        tagThread1LL.start();

    }

    public void execute() {
        // Update pose estimator with visible targets,

        SmartDashboard.putNumber("LPCTT2LL ", loopctr++);// thread running indicator

        imageCaptureTime = m_llv.cam_tag_15.getPipelineLatency() / 1000d;

        fiducialId = m_llv.cam_tag_15.getAprilTagID();

        if (fiducialId != -1) {

            Optional<Pose3d> temp = m_drive.m_fieldLayout.getTagPose(fiducialId);

            if (temp.isPresent()) {

                targetPose = temp.get();

                camToTarget = m_llv.cam_tag_15.getRobotTransform();

                camPose = targetPose.transformBy(camToTarget.inverse());

                visionPoseEstimatedData =

                        camPose.transformBy(VisionConstants.CAMERA_TO_ROBOT_3D).toPose2d();

            }

        }
        visionPoseEstimatedData = new Pose2d();

    }

    public Pose2d getVisionPoseEstimatedData() {
        return visionPoseEstimatedData;
    }
}
