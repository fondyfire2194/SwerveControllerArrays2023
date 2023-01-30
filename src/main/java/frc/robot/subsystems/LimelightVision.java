// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.VisionConstants;
import frc.robot.oi.LimeLight;
import frc.robot.oi.LimeLight.CamMode;
import frc.robot.oi.LimeLight.LedMode;
import frc.robot.oi.LimeLight.StreamType;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */

  private int numCams = 1;

  public LimeLight cam_tag_15;

  public LimeLight cam_tape_16;

  Rotation2d rr = new Rotation2d(1.57);
  Translation3d tl3 = new Translation3d(1, 2, 3);
  Rotation3d rr3 = new Rotation3d(1.57, 1.00, .44);
  Transform3d tran3d = new Transform3d(tl3, rr3);

  public int[][] targetMatrix = new int[8][3];

  private double imageCaptureTime;

  private int fiducialId;

  private Pose3d camPose = new Pose3d();

  private Pose2d camPose2d = new Pose2d();

  private Transform3d robTran = new Transform3d();

  private Pose2d targetPose2d = new Pose2d();

  public Transform3d camTran = new Transform3d();

  private Pose2d visionPoseEstimatedData;

  public static Map<String, Integer> tapePipelines;

  public static Map<String, Integer> tagPipelines;

  static {
    tagPipelines = new HashMap<>();
    tagPipelines.put("tag_0", 0);
    tagPipelines.put("PL1", 1);
    tagPipelines.put("PL2", 2);
    tagPipelines.put("PL3", 3);
    tagPipelines.put("tape_4", 4);
    tagPipelines.put("PL5", 5);
    tagPipelines.put("PL6", 6);
    tagPipelines.put("PL7", 7);
    tagPipelines.put("PL8", 8);
    tagPipelines.put("PL0", 9);

  }


  public LimelightVision() {

    cam_tag_15 = new LimeLight("limelight-tags");
    cam_tag_15.setLEDMode(LedMode.kforceOff);
    cam_tag_15.setCamMode(CamMode.kvision);
    cam_tag_15.setStream(StreamType.kStandard);

   
  }

  @Override
  public void periodic() {
    

  }

  public Transform3d getCamTransform(LimeLight cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {

      camTran = cam.getCamTran();
    }

    return camTran;

  }

  public Transform3d getRobotTransform(LimeLight cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {
      robTran = cam.getRobotTransform();

    }

    return robTran;

  }

  public double round2dp(double number) {

    number = Math.round(number * 100);
    number /= 100;
    return number;

  }

}
