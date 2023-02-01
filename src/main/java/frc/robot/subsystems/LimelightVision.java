// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Vision.Limelight.TargetThreadLLDataTX;
import frc.robot.oi.LimeLight;
import frc.robot.oi.LimeLight.CamMode;
import frc.robot.oi.LimeLight.LedMode;
import frc.robot.oi.LimeLight.StreamType;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */

  public LimeLight cam_tag_15;

  Rotation2d rr = new Rotation2d(1.57);
  Translation3d tl3 = new Translation3d(1, 2, 3);
  Rotation3d rr3 = new Rotation3d(1.57, 1.00, .44);
  public Transform3d tran3d = new Transform3d(tl3, rr3);

  public Pose2d visionPoseEstimatedData;

  public double imageCaptureTime;

  private int fiducialId;

  public Transform3d robotPoseTS;

  public boolean allianceBlue;

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

    cam_tag_15 = new LimeLight("limelight-tags");
    cam_tag_15.setLEDMode(LedMode.kforceOff);
    cam_tag_15.setCamMode(CamMode.kvision);
    cam_tag_15.setStream(StreamType.kStandard);

   
  }

  public void setAllianceBlue(boolean alliance) {
    allianceBlue = alliance;
  }

  @Override
  public void periodic() {

    tran3d = getRobotPoseTS(cam_tag_15);

    visionPoseEstimatedData = getVisionCorrection(tran3d);

  }

  public Transform3d getRobotPoseTS(LimeLight cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {

      robotPoseTS = cam.getRobotPose();
    }

    return robotPoseTS;

  }

  public Pose2d getVisionCorrection(Transform3d t3d) {
    Rotation2d r2d = t3d.getRotation().toRotation2d();
    Translation2d t2d = new Translation2d(t3d.getX(), t3d.getY());
    Pose2d rp = new Pose2d(t2d, r2d);
    return rp;
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

  public boolean gotAprilTag(LimeLight cam, int id) {
    return cam.getAprilTagID() == id;
  }

}
