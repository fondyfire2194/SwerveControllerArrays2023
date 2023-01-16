// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.oi.LimeLight;
import frc.robot.oi.LimeLight.CamMode;
import frc.robot.oi.LimeLight.StreamType;
import frc.robot.oi.LimeLightReflective.LedMode;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */

  private int numCams = 1;

  public LimeLight cam_tag_15;

  public LimeLight cam_tape_16;

  // private ShuffleboardLL cam_tag_15Display;

  public static Map<String, Integer> tapePipelines;

  public static Map<String, Integer> cam_tagPipelines;
  static {
    cam_tagPipelines = new HashMap<>();
    cam_tagPipelines.put("tag_0", 0);
    cam_tagPipelines.put("PL1", 1);
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

  public LimelightVision() {

    cam_tag_15 = new LimeLight("limelight-tag_15");
    cam_tag_15.ref.setLEDMode(LedMode.kpipeLine);
    cam_tag_15.setCamMode(CamMode.kvision);
    cam_tag_15.setStream(StreamType.kStandard);
    // cam_tag_15Display = new ShuffleboardLL(cam_tag_15);

    if (numCams > 1) {

      cam_tape_16 = new LimeLight("limelight-tape_16");

      cam_tape_16.ref.setLEDMode(LedMode.kpipeLine);
      cam_tape_16.setCamMode(CamMode.kvision);
      cam_tape_16.setStream(StreamType.kStandard);

      // ShuffleboardLL cam_tape_16Display = new ShuffleboardLL(cam_tape_16);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (cam_tag_15.isConnected()) {
      SmartDashboard.putNumber("LLTagID", cam_tag_15.getAprilTagID());
      SmartDashboard.putString("LLTagTransform3d", cam_tag_15.getRobotTransform().toString());
      SmartDashboard.putString("LLTagTranslation3d", cam_tag_15.getRobotTransform().getTranslation().toString());
      SmartDashboard.putString("LLTagRotation3d", cam_tag_15.getRobotTransform().getRotation().toString());

      Translation3d t3d = cam_tag_15.getRobotTransform().getTranslation();

      double x = round2dp(t3d.getX());
      double y = round2dp(t3d.getY());
      double z = round2dp(t3d.getZ());

      SmartDashboard.putNumber("LL X", x);
      SmartDashboard.putNumber("LL Y", y);
      SmartDashboard.putNumber("LL Z", z);

    }

  }

  public double round2dp(double number) {

    number = Math.round(number * 100);
    number /= 100;
    return number;

  }

}
