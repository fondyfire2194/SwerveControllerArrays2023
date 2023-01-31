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
import frc.robot.subsystems.GameHandlerSubsystem.GridDrop;

/** Add your docs here. */
public class ShuffleboardGridSelect {

        public SendableChooser<Integer> gridSlot = new SendableChooser<>();

        public static SendableChooser<Integer> gamePiece = new SendableChooser<>();

        public static SendableChooser<Integer> gridLevel = new SendableChooser<>();

        ShuffleboardTab x = Shuffleboard.getTab("Grid");

        public ShuffleboardGridSelect(GameHandlerSubsystem ghs) {

                gridSlot.setDefaultOption("RightHybridPipe", 0);

                gridSlot.addOption("RightCube", 1);

                gridSlot.addOption("RightPipe", 2);

                gridSlot.addOption("RightCoopPipe", 3);

                gridSlot.addOption("CoopCube", 4);

                gridSlot.addOption("LeftCoopPipe", 5);

                gridSlot.addOption("LeftPipe", 6);

                gridSlot.addOption("LeftCube", 7);

                gridSlot.addOption("LeftHybridPipe", 8);
 

                gridLevel.setDefaultOption("Ground", 0);
                gridLevel.addOption("MidLevel", 1);
                gridLevel.addOption("TopLevel", 2);
                gridLevel.addOption("MidLevel1", 3);
                gridLevel.addOption("TopLevel1", 4);
                gridLevel.addOption("MidLevel2", 5);
                gridLevel.addOption("TopLevel2", 6);
                gridLevel.addOption("MidLevel3", 7);
                gridLevel.addOption("TopLevel3", 8);

                gamePiece.setDefaultOption("Cone", 0);
                gamePiece.addOption("Cube", 1);

                x.add("GridSlot", gridSlot).withPosition(0, 1).withSize(2, 1);

                x.add("GridLevel", gridLevel).withPosition(3, 0).withSize(2, 1);

                x.add("PieceType", gamePiece).withPosition(5, 0).withSize(1, 1);

                x.add("SetSlot", new SetActiveGrid(this, ghs))
                                .withPosition(0, 2).withSize(1, 1);

                x.addString("ActiveDrop", () -> ghs.getActiveName())

                                .withPosition(0, 3).withSize(4, 1);

                x.addBoolean("BlueAlliance", () -> ghs.getAllianceBlue())
                                .withPosition(0, 1).withSize(1, 1);

                x.addNumber("Active X", () -> ghs.getXDistance())
                                .withPosition(6, 0).withSize(1, 1);

                x.addNumber("Active Y", () -> ghs.getActiveDrop().getYVal())
                                .withPosition(6, 1).withSize(1, 1);

              

        }

}
