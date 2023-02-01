// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.oi.LimeLight;
import frc.robot.subsystems.LimelightVision;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SetPipeline extends InstantCommand {
  private LimelightVision m_llv;
  private LimeLight m_llcam;

  public SetPipeline(LimelightVision llv, LimeLight llcam, String name) {
    m_llv = llv;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }
}
