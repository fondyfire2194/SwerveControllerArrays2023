// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.swerve.Test.SetActiveData;

/** Add your docs here. */
public class ShuffleboardRunGame {

        public static SendableChooser<Integer> gamePiece = new SendableChooser<>();

        public static SendableChooser<Integer> gridLevel = new SendableChooser<>();

        public static SendableChooser<Integer> gridSlot = new SendableChooser<>();

        ShuffleboardTab x = Shuffleboard.getTab("Grid2");

        public ShuffleboardRunGame(GameHandlerSubsystem ghs) {

                gamePiece.setDefaultOption("Cone", 0);
                gamePiece.addOption("Cube", 1);

                gridLevel.setDefaultOption("Ground", 0);
                gridLevel.addOption("MidLevel", 1);
                gridLevel.addOption("TopLevel", 2);

                gridSlot.setDefaultOption("RightOnePipe", 0);
                gridSlot.addOption("RightTwoShelf", 1);
                gridSlot.addOption("RightThreePipe", 2);
                gridSlot.addOption("Coop Right Pipe", 3);
                gridSlot.addOption("Coop Shelf", 4);
                gridSlot.addOption("Coop Left Pipe", 5);
                gridSlot.addOption("Left Three Pipe", 6);
                gridSlot.addOption("Left Two Shelf", 7);
                gridSlot.addOption("Left One Pipe", 8);

                x.add("PickSlot", gridSlot).withPosition(0, 0).withSize(2, 1);
                x.add("PickPiece", gamePiece).withPosition(2, 0).withSize(2, 1);
                x.add("PickLevel", gridLevel).withPosition(4, 0).withSize(2, 1);

                x.add("SetChoices", new SetActiveData(ghs))
                                .withPosition(6, 0)
                                .withSize(1, 1)
                                .withWidget(BuiltInWidgets.kCommand);

                x.addString("SelectedGrid", () -> GameHandlerSubsystem.getActiveDrop().toString())
                                .withPosition(0, 2).withSize(2, 1);
                x.addString("SelectedPiece", () -> ghs.pieceName[ghs.selectedPiece])
                                .withPosition(2, 2).withSize(1, 1);
                x.addString("SelectedLevel", () -> ghs.levelName[ghs.selectedLevel])
                                .withPosition(3, 2).withSize(1, 1);

        }

}
