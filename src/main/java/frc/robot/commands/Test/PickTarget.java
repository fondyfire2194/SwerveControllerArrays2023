// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Test;



import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.LimelightVision;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PickTarget extends InstantCommand {
  private int m_row;
  private int m_column;
  private LimelightVision m_llvis;

  public PickTarget(LimelightVision llvis, int row, int column) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_row = row;
    m_column = column;
    m_llvis = llvis;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_llvis.targetMatrix[m_row][m_column]=1;
   
  }
}
