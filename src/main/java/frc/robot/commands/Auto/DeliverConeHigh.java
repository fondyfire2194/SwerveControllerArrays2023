// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.LinearArm.PositionLinearArm;
import frc.robot.commands.TurnArm.PositionTurnArm;
import frc.robot.subsystems.LinearArmSubsystem;
import frc.robot.subsystems.TurnArmSubsystem;
import frc.robot.subsystems.LinearArmSubsystem.linearDistances;
import frc.robot.subsystems.TurnArmSubsystem.turnAngles;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DeliverConeHigh extends SequentialCommandGroup {
  /** Creates a new DeliverConeHigh. */
  public DeliverConeHigh(TurnArmSubsystem tas, LinearArmSubsystem las) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    tas.setActiveTargetAngle(turnAngles.HIGH_DROPOFF.getAngle());
    las.setActiveTargetDistance(linearDistances.HIGH_DROPOFF.getDistance());

    addCommands(
        new PositionTurnArm(tas, tas.getActiveTargetAngle()),
        new PositionLinearArm(tas, las.getActiveTargetDistance()));
  }
}
