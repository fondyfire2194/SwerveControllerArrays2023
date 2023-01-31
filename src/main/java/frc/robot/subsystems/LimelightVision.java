// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.oi.LimeLightV3;
import frc.robot.oi.LimeLightV3.CamMode;
import frc.robot.oi.LimeLightV3.LedMode;
import frc.robot.oi.LimeLightV3.StreamType;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */


  public LimeLightV3 cam_tag_15;

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

  public Transform3d camPoseTargetSpace = new Transform3d();

  private Pose2d visionPoseEstimatedData;

  private boolean allianceBlue;

  static enum pipelines {
    SPARE(0),
    LOADCUBE(1),
    LOADCONE(2),
    SPARE3(3),
    SPARE4(4),
    SPARE5(5),
    SPARE6(6),
    SPARE7(7),
    SPARE8(8),
    SPARE9(9);

    private int number;

    private pipelines(int number) {
      this.number = number;
    }

    private int getNumber() {
      return number;
    }

  }

  public LimelightVision() {

    cam_tag_15 = new LimeLightV3("limelight-tags");
    cam_tag_15.setLEDMode(LedMode.kforceOff);
    cam_tag_15.setCamMode(CamMode.kvision);
    cam_tag_15.setStream(StreamType.kStandard);

  }

  public void setAllianceBlue(boolean alliance) {
    allianceBlue = alliance;
  }

  @Override
  public void periodic() {

  }

  public Transform3d getCamTransform(LimeLightV3 cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {

      camPoseTargetSpace = cam.getCameraPoseTargetSpace();
    }

    return camPoseTargetSpace;

  }

  public Transform3d getRobotTransform(LimeLightV3 cam) {

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

  public void setLoadCubePipeline() {
    cam_tag_15.setPipeline(pipelines.LOADCUBE.ordinal());
  }

  public void setLoadConePipeline() {
    cam_tag_15.setPipeline(pipelines.LOADCONE.ordinal());
  }


}
