// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.FieldConstants2023;
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

  public Transform3d robotPose_FS;

  public boolean allianceBlue;

  public  double distToTagX;
  public double distToTagY;
  public double degToTag;
  public double distToTag;

  public double visPosX;
  public double visPosY;
  public double visPosDeg;

  public double robX;
  public double robY;
  public double robDeg;

  public double robDiffX;
  public double robDiffY;
  public double robDiffDeg;

  

  public double curTagX;

  public double curTagY;

  public double curTagDeg;

  static enum pipelinetype {
    retroreflective,
    grip,
    python,
    fiducialmarkers,
    classifier,
    detector
  }

  static enum pipelines {
    TAPE0(0, pipelinetype.retroreflective),
    LOADCUBE(1, pipelinetype.fiducialmarkers),
    LOADCONE(2, pipelinetype.fiducialmarkers),
    SPARE3(3, pipelinetype.fiducialmarkers),
    TAGS(4, pipelinetype.fiducialmarkers),
    SPARE5(5, pipelinetype.fiducialmarkers),
    TAPE(6, pipelinetype.retroreflective),
    SPARE7(7, pipelinetype.fiducialmarkers),
    SPARE8(8, pipelinetype.detector),
    SPARE9(9, pipelinetype.classifier);

    private int number;

    private pipelinetype type;

    private pipelines(int number, pipelinetype type) {
      this.number = number;
      this.type = type;
    }

    private int getNumber() {
      return number;
    }

    private pipelinetype getType() {
      return type;
    }
  }

  public LimelightVision() {

    cam_tag_15 = new LimeLight("limelight-tags");
    cam_tag_15.setLEDMode(LedMode.kpipeLine);
    cam_tag_15.setCamMode(CamMode.kvision);
    cam_tag_15.setStream(StreamType.kStandard);

  }

  public void setAllianceBlue(boolean alliance) {
    allianceBlue = alliance;
  }

  @Override
  public void periodic() {

    visionPoseEstimatedData = getVisionCorrection(getRobotPose_FS_WPIBlue(cam_tag_15));
  }

  public Transform3d getRobotPose_FS(LimeLight cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {

      robotPose_FS = cam.getRobotPose_FS();
    }

    return robotPose_FS;

  }

  public Transform3d getRobotPose_FS_WPIBlue(LimeLight cam) {

    imageCaptureTime = cam.getPipelineLatency() / 1000d;

    fiducialId = cam.getAprilTagID();

    if (fiducialId != -1) {

      robotPose_FS = cam.getRobotPose_FS_WPIBL();
    }
    return robotPose_FS;
  }

  public int getAprilTagID(LimeLight cam) {
    return cam.getAprilTagID();
  }

  public double getPipelineLatency(LimeLight cam) {
    return cam.getPipelineLatency();
  }

  public boolean isConnected(LimeLight cam) {
    return cam.isConnected();
  }

  public boolean isTargetFound(LimeLight cam) {
    return cam.getIsTargetFound();
  }

  public double getHeartbeat(LimeLight cam) {
    return cam.getHeartbeat();
  }

  public int getPipelineNumber(LimeLight cam) {
    return cam.getPipeline();
  }

  public LedMode getLedMode(LimeLight cam) {
    return cam.getLEDMode();
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

  public void setTapePipeline() {
    cam_tag_15.setPipeline(pipelines.TAPE.ordinal());
  }

  public void setAprilTagPipeline() {
    cam_tag_15.setPipeline(pipelines.TAGS.ordinal());
  }

  public Pose3d getTagPose(int tagID) {
    return FieldConstants2023.aprilTags.get(tagID);
  }

  public Pose3d getCurrentTagPose(LimeLight cam) {
    return FieldConstants2023.aprilTags.get(getAprilTagID(cam));
  }

}
