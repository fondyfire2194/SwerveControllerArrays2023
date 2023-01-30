// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve.Test;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GameHandlerSubsystem;
import frc.robot.utils.ShuffleboardRunGame;

public class SetActiveData extends CommandBase {
  /** Creates a new SetGrid. */
  private GameHandlerSubsystem m_ghs;

  public SetActiveData(GameHandlerSubsystem ghs) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_ghs = ghs;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_ghs.selectedGrid = ShuffleboardRunGame.gridSlot.getSelected();
    m_ghs.selectedLevel = ShuffleboardRunGame.gridLevel.getSelected();
    m_ghs.selectedPiece = ShuffleboardRunGame.gamePiece.getSelected();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    GameHandlerSubsystem.setDropByNumber(m_ghs.selectedGrid);
    
    GameHandlerSubsystem.grid[m_ghs.selectedGrid] = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return GameHandlerSubsystem.getActiveDrop().ordinal() == m_ghs.selectedGrid;
  }
}
