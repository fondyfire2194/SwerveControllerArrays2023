// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve.Test;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GameHandlerSubsystem;
import frc.robot.utils.ShuffleboardGridSelect;

public class SetActiveGrid extends CommandBase {
  /** Creates a new SetGrid. */
  private GameHandlerSubsystem m_ghs;
  private ShuffleboardGridSelect m_sgs;
  private int selectedGrid;
  private double startTime;

  public SetActiveGrid(ShuffleboardGridSelect sgs, GameHandlerSubsystem ghs) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_ghs = ghs;
    m_sgs = sgs;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    selectedGrid = m_sgs.gridSlot.getSelected();
    startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putNumber("GSW", selectedGrid);
    m_ghs.setDropByNumber(selectedGrid);

  };

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Timer.getFPGATimestamp() > startTime + 1;
  }
}
