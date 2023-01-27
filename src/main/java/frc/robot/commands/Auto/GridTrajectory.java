// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auto;

import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.swerve.Test.MessageCommand;
import frc.robot.utils.TeleopTrajectory;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GridTrajectory extends SequentialCommandGroup {
  /** Creates a new GridTrajectory. */

  public GridTrajectory(TeleopTrajectory ttj, double maxVel, double maxAccel) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    PathPlannerTrajectory traj = ttj.getSimpleTrajectory(maxVel, maxAccel);

    SmartDashboard.putNumber("TRAJTIme", traj.getTotalTimeSeconds());
    SmartDashboard.putString("TRANS", ttj.getActiveDrop().getT2d().toString());

    addCommands(
        new MessageCommand("Hello"),
        ttj.followTrajectoryCommand(traj, true),
        new MessageCommand("HelloAgain")
    );
  }
}
