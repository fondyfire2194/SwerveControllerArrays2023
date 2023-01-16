// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision.Limelight;

import java.util.List;
import java.util.Optional;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.VisionConstants;
import frc.robot.oi.LimeLight;
import frc.robot.subsystems.VisionPoseEstimatorLL;

/** Add your docs here. */
// This is the private constructor that will be called once by getInstance() and
// it
// should instantiate anything that will be required by the class
public class TargetThread1LL {

    private VisionPoseEstimatorLL m_vpell;

    private LimeLight m_cam;


    private Pose3d camPose = new Pose3d();

    private Pose3d targetPose = new Pose3d();

    public Transform3d camToTarget = new Transform3d();

    public int fiducialId;

    public int numberTargets = 0;

    double imageCaptureTime;

    int loopctr;

    public TargetThread1LL(VisionPoseEstimatorLL vpell, LimeLight cam) {
        m_vpell = vpell;
        m_cam = cam;

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

        imageCaptureTime = m_cam.getPipelineLatency() / 1000d;

        fiducialId = m_cam.getAprilTagID();

        if (fiducialId != -1) {

            Optional<Pose3d> temp = m_vpell.m_fieldLayout.getTagPose(fiducialId);

            if (temp.isPresent()) {

                targetPose = temp.get();

                camToTarget =  m_cam.getRobotTransform();

                camPose = targetPose.transformBy(camToTarget.inverse());

                m_vpell.setVisionPoseEsitmatedData(camPose);
                        camPose.transformBy(VisionConstants.CAMERA_TO_ROBOT_3D).toPose2d();

            }
        }


    }

}
