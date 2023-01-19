// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision.PhotonVision;

import java.util.List;
import java.util.Optional;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PhotonVision;

/** Add your docs here. */
// This is the private constructor that will be called once by getInstance() and
// it
// should instantiate anything that will be required by the class
public class TargetThread1 {

    private Pose3d camPose = new Pose3d();

    private Pose3d targetPose = new Pose3d();

    public Transform3d camToTarget = new Transform3d();

    public int fiducialId;

    private Pose2d visionPoseEstimatedData;

    double imageCaptureTime;

    int loopctr;

    private DriveSubsystem m_drive;

    private PhotonVision m_pv;

    private boolean hasTargets;

    private Transform3d t3d;

    List<PhotonTrackedTarget> tpr;

    public TargetThread1(DriveSubsystem drive, PhotonVision pv) {
        m_drive = drive;
        m_pv = pv;

        Thread tagThread1 = new Thread(new Runnable() {
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

        tagThread1.setPriority(Thread.NORM_PRIORITY);
        tagThread1.setName("AprilTagThread 1");
        tagThread1.start();

    }

    public void execute() {
        // Update pose estimator with visible targets,

        SmartDashboard.putNumber("LPCTT1 ", loopctr++);// thread running indicator

        // Query the latest result from PhotonVision
        // var result = m_pv.cam_tag_11.getLatestResult();

        var pipelineResult = m_pv.cam_tag_11.getLatestResult();

        // Check if the latest result has any targets.

        hasTargets = false;

        if (pipelineResult.hasTargets()) {

            hasTargets = true;

            PhotonTrackedTarget target = pipelineResult.getBestTarget();

            imageCaptureTime = pipelineResult.getLatencyMillis() / 1000d;

            // tpr = pipelineResult.targets;

            fiducialId = target.getFiducialId();

            // Optional<Pose3d> temp = m_drive.m_fieldLayout.getTagPose(fiducialId);

            // if (temp.isPresent())

            // targetPose = temp.get();

            // else

            camToTarget = target.getBestCameraToTarget();
            t3d = camToTarget;
            camPose = targetPose.transformBy(camToTarget.inverse());

            visionPoseEstimatedData = camPose.transformBy(VisionConstants.CAMERA_TO_ROBOT_3D).toPose2d();

        } else
            fiducialId = -1;

        setFoundTagID(fiducialId);
        setFoundTagTransform(camToTarget);
        sentHasTargets();
    }

    public void sentHasTargets() {
        m_pv.hasTargets = hasTargets;
    }

    public void setFoundTagID(int id) {
        m_pv.tagId = id;
    }

    public void setFoundTagTransform(Transform3d t3d) {
        m_pv.t3d = t3d;
    }

    public Pose2d getVisionPoseEstimatedData() {
        return visionPoseEstimatedData;
    }
}
