// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.limelight;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
/** Add your docs here. */
public class ShuffleboardLL {

    public static void initSll(LimeLight ll) {
        String rlayout = "LL";
        ShuffleboardLayout reflective = Shuffleboard.getTab("Limelight")
                .getLayout(rlayout, BuiltInLayouts.kList).withPosition(0, 0)
                .withSize(2, 2).withProperties(Map.of("Label position", "LEFT"));
   
        
        reflective.addBoolean("Has Target", () -> ll.ref.getIsTargetFound());

        reflective.addBoolean("Connected", () -> ll.isConnected());

        reflective.addNumber(" Hor Offset", () -> ll.ref.getdegRotationToTarget());

        reflective.addNumber("Vert Offset", () -> ll.ref.getdegVerticalToTarget());

        reflective.addNumber("% Area", () -> ll.ref.getTargetArea());

        reflective.addNumber("Skew", () -> ll.ref.getSkew_Rotation());

        reflective.addNumber("BBShort", () -> ll.ref.getBoundingBoxShortestSide());

        reflective.addNumber("BBLong", () -> ll.ref.getBoundingBoxLongestSide());

        reflective.addNumber("BBVert", () -> ll.ref.getBoundingBoxHeight());

        reflective.addNumber("BBHor", () -> ll.ref.getBoundingBoxWidth());

        reflective.addString("CamMode", () -> ll.getCamMode().toString());
  
        reflective.addString("LedMode", () -> ll.ref.getLEDMode().toString());

    }

}
