// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotonVision extends SubsystemBase {
  /** Creates a new LimelightVision. */

  public PhotonCamera cam_tag_11;

  public int tagId;

  public Transform3d t3d;

  public boolean hasTargets;

  public static Map<String, Integer> tapePipelines;

  public static Map<String, Integer> cam_tagPipelines;
  static {
    cam_tagPipelines = new HashMap<>();
    cam_tagPipelines.put("tag_0", 0);
    cam_tagPipelines.put("tag_1", 1);
    cam_tagPipelines.put("PL2", 2);
    cam_tagPipelines.put("PL3", 3);
    cam_tagPipelines.put("tape_4", 4);
    cam_tagPipelines.put("PL5", 5);
    cam_tagPipelines.put("PL6", 6);
    cam_tagPipelines.put("PL7", 7);
    cam_tagPipelines.put("PL8", 8);
    cam_tagPipelines.put("PL0", 9);

  }

  static {
    tapePipelines = new HashMap<>();
    tapePipelines.put("PL)", 0);
    tapePipelines.put("PL1", 1);
    tapePipelines.put("PL2", 2);
    tapePipelines.put("PL3", 3);
    tapePipelines.put("tape_4", 4);
    tapePipelines.put("PL5", 5);
    tapePipelines.put("PL6", 6);
    tapePipelines.put("PL7", 7);
    tapePipelines.put("PL8", 8);
    tapePipelines.put("PL9", 9);

  }

  public PhotonVision() {

    cam_tag_11 = new PhotonCamera("cam_tag_11");
    // cam_tag_15.ref.setLEDMode(LedMode.kpipeLine);
    // cam_tag_15.setCamMode(CamMode.kvision);
    // cam_tag_15.setStream(StreamType.kStandard);
    // cam_tag_15Display = new ShuffleboardLL(cam_tag_15);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("DriverMode", cam_tag_11.getDriverMode());

    SmartDashboard.putNumber("PV Pipeline", cam_tag_11.getPipelineIndex());

    if (hasTargets) {

      SmartDashboard.putNumber("PVTagID", tagId);
      SmartDashboard.putString("PVTagTransform3d", t3d.toString());
      SmartDashboard.putString("PVTagTranslation3d", t3d.getTranslation().toString());
      SmartDashboard.putString("LLTagRotation3d", t3d.getRotation().toString());

      double x = round2dp(t3d.getTranslation().getX());
      double y = round2dp(t3d.getTranslation().getY());
      double z = round2dp(t3d.getTranslation().getZ());

      SmartDashboard.putNumber("PV X", x);
      SmartDashboard.putNumber("PV Y", y);
      SmartDashboard.putNumber("PV Z", z);

    }
  }

  public int getPipelineNumber(String key) {
    return cam_tagPipelines.get(key);
  }

  public Command SetDriverMode(PhotonCamera phcam, boolean on) {
    return new InstantCommand(() -> phcam.setDriverMode(on));
  }

  public Command SetPipeline(PhotonCamera phcam, int pipelineNumber) {

    return new InstantCommand(() -> phcam.setPipelineIndex(pipelineNumber));
  }

  public double round2dp(double number) {

    number = Math.round(number * 100);
    number /= 100;
    return number;
  }

}
