// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

public class DriveToTape extends CommandBase {
  /** Creates a new DriveToTape. */
  private DriveSubsystem m_drive;
  private LimelightVision m_llv;
  private DoubleSupplier m_throttle;
  private DoubleSupplier m_strafe;
  private DoubleSupplier m_rotation;

  private PIDController m_dtv = new PIDController(.01, 0, 0);

  public DriveToTape(DriveSubsystem drive, LimelightVision llv,

      DoubleSupplier throttleInput, DoubleSupplier strafeInput, DoubleSupplier rotationInput) {
    m_drive = drive;
    m_llv = llv;
    m_throttle = throttleInput;
    m_strafe = strafeInput;
    m_rotation = rotationInput;

    addRequirements(m_drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_llv.setTapePipeline();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_llv.cam_tag_15.getIsTargetFound()) {
      double pidOut = m_dtv.calculate(m_llv.cam_tag_15.getdegRotationToTarget(), 0);
      m_drive.drive(-m_throttle.getAsDouble(), pidOut, 0);
    }

    else {
      m_drive.drive(-m_throttle.getAsDouble(), m_strafe.getAsDouble(), m_rotation.getAsDouble());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
