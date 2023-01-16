// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.oi.LimeLight;
import frc.robot.oi.ShuffleboardLL;
import frc.robot.oi.LimeLight.CamMode;
import frc.robot.oi.LimeLight.StreamType;
import frc.robot.oi.LimeLightReflective.LedMode;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */

  private int numCams = 1;

  public LimeLight cam_tag_15;

  public LimeLight cam_tape_16;

  private ShuffleboardLL cam_tag_15Display;

  

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

  }

}
