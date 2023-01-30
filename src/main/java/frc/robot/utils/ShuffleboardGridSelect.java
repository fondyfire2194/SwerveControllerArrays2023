// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.swerve.Test.SetActiveGrid;
import frc.robot.subsystems.GameHandlerSubsystem;

/** Add your docs here. */
public class ShuffleboardGridSelect {

        public static SendableChooser<Integer> gamePiece = new SendableChooser<>();

        public static SendableChooser<Integer> gridLevel = new SendableChooser<>();

        ShuffleboardTab x = Shuffleboard.getTab("Grid");

        public ShuffleboardGridSelect() {
                int n = 0;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1)
                                .withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[0])
                                .withPosition(n, 1)
                                .withSize(1, 1)
                                .withWidget(BuiltInWidgets.kBooleanBox);
                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);

                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[1])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);

                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[2])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);

                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[3])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[4])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[5])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[6])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[7])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                n++;
                x.add("Grid " + String.valueOf(n), new SetActiveGrid(n))
                                .withPosition(n, 0)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kCommand);
                x.addBoolean("State " + String.valueOf(n), () -> GameHandlerSubsystem.grid[8])
                                .withPosition(n, 1)
                                .withSize(1, 1).withWidget(BuiltInWidgets.kBooleanBox);

                x.addString("ActiveDrop", () -> GameHandlerSubsystem.getActiveDrop().toString())
                                .withPosition(4, 3).withSize(4, 1);

                gamePiece.setDefaultOption("Cone", 0);

                gamePiece.addOption("Cube", 1);

                gridLevel.setDefaultOption("Ground", 0);
                gridLevel.addOption("MidLevel", 1);
                gridLevel.addOption("TopLevel", 2);
                gridLevel.addOption("MidLevel1", 3);
                gridLevel.addOption("TopLevel1", 4);
                gridLevel.addOption("MidLevel2", 5);
                gridLevel.addOption("TopLevel2", 6);
                gridLevel.addOption("MidLevel3", 7);
                gridLevel.addOption("TopLevel3", 8);








                x.add("Select",gamePiece).withPosition(0, 3).withSize(1, 1);
                x.add("Choose",gridLevel).withPosition(1, 3).withSize(2, 1);

        }

}
