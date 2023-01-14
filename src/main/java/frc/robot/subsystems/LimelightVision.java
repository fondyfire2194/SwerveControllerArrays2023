// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Vision.LimelightLeds;
import frc.robot.commands.Vision.LimelightSetPipeline;
import frc.robot.commands.Vision.PipelinesCam15;
import frc.robot.commands.Vision.PipelinesCam16;
import frc.robot.commands.Vision.ToggleCamera;
import frc.robot.utils.limelight.LimeLight;
import frc.robot.utils.limelight.LimeLightControlMode.LedMode;

public class LimelightVision extends SubsystemBase {
  /** Creates a new LimelightVision. */
  private LimeLight cam15;
  private LimeLight cam16;
  double[] temp;
  Pose3d botPose;

  public LimelightVision() {
    cam15 = new LimeLight("limelight-fifteen");
    cam16 = new LimeLight("limelight-sixteen");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putBoolean("Cam15HasTarget", cam15.getIsTargetFound());
    SmartDashboard.putNumber("Cam15 Pipeline#", cam15.getPipeline());
    SmartDashboard.putNumber("Cam15 Latency ms", cam15.getPipelineLatency());
    SmartDashboard.putNumber("Cam15 Tag ID", cam15.getAprilTagID());
    SmartDashboard.putBoolean("Cam15 Connected", cam15.isConnected());

    SmartDashboard.putBoolean("Cam16HasTarget", cam16.getIsTargetFound());
    SmartDashboard.putNumber("Cam16 Pipeline#", cam16.getPipeline());
    SmartDashboard.putNumber("Cam16 Latency ms", cam16.getPipelineLatency());
    SmartDashboard.putNumber("Cam16 Tag ID", cam16.getAprilTagID());

    SmartDashboard.putBoolean("TargetAvailaBE", cam16.getIsTargetFound());
    SmartDashboard.putBoolean("Cam16 Connected", cam16.isConnected());
    SmartDashboard.putString("Cam16 Pose", cam16.getRobotPose().toString());
    SmartDashboard.putString("Cam16 Camtran", cam16.getCamTran().toString());

    double[] test = cam15.adv.getAveXHairColor();

    SmartDashboard.putNumberArray("COLOR", test);
    double[] cropSize = { .5, 5., 5., 5 };
    cam15.adv.setCropRectangle(cropSize);

  }

  public Command toggleCamera() {
    return new ToggleCamera(cam16);
  }

  public Command setLeds(LedMode mode) { // doesn't work properly
    return new LimelightLeds(cam16, mode);
  }

  public Command setPipeline(PipelinesCam15 pipelines) {
    return new LimelightSetPipeline(cam15, pipelines.ordinal()); // 0-9
  }

  public Command setPipeline(PipelinesCam16 pipelines) {
    return new LimelightSetPipeline(cam16, pipelines.ordinal()); // 0-9
  }

  public Command getSnapShot2() {
    return new SequentialCommandGroup(new InstantCommand(() -> cam16.snap(1)), new WaitCommand(1),
    new InstantCommand(() -> cam16.snap(0)));
  }
}
