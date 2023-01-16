// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.oi;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

/** Add your docs here. */
public class ShuffleboardLL {

        public ShuffleboardLL(LimeLight ll) {

                ShuffleboardLayout col1_2 = Shuffleboard.getTab(ll.getName())
                                .getLayout("A", BuiltInLayouts.kList).withPosition(0, 0)
                                .withSize(2, 6).withProperties(Map.of("Label position", "LEFT"));

                ShuffleboardLayout col3_4 = Shuffleboard.getTab(ll.getName())
                                .getLayout("B", BuiltInLayouts.kList).withPosition(2, 0)
                                .withSize(2, 6).withProperties(Map.of("Label position", "LEFT"));

                ShuffleboardLayout col5_6 = Shuffleboard.getTab(ll.getName())
                                .getLayout("C", BuiltInLayouts.kList).withPosition(4, 0)
                                .withSize(2, 6).withProperties(Map.of("Label position", "LEFT"));

                ShuffleboardLayout col7_8 = Shuffleboard.getTab(ll.getName())
                                .getLayout("D", BuiltInLayouts.kList).withPosition(6, 0)
                                .withSize(2, 6).withProperties(Map.of("Label position", "LEFT"));

                // columns 1 and 2

                col1_2.addBoolean("tv (hasTarget)", () -> ll.ref.getIsTargetFound());

                col1_2.addBoolean("Connected", () -> ll.isConnected());

                col1_2.addString("camMode ", () -> ll.getCamMode().toString());

                col1_2.addString("ledMode", () -> ll.ref.getLEDMode().toString());

                col1_2.addString("stream", () -> ll.getStream().toString());

                col1_2.addNumber("getpipe", () -> ll.getPipeline());

                col1_2.addNumber("tl (Latency)", () -> ll.getPipelineLatency());

                // cols 3 and 4

                col3_4.addNumber("tshort", () -> ll.ref.getBoundingBoxShortestSide());

                col3_4.addNumber("tlong", () -> ll.ref.getBoundingBoxLongestSide());

                col3_4.addNumber("tvert", () -> ll.ref.getBoundingBoxHeight());

                col3_4.addNumber("thor", () -> ll.ref.getBoundingBoxWidth());

                col3_4.addNumber("ta ", () -> ll.ref.getTargetArea());

                col3_4.addNumber("tx", () -> ll.ref.getdegRotationToTarget());

                col3_4.addNumber("ty", () -> ll.ref.getdegVerticalToTarget());

                col3_4.addNumber("ts", () -> ll.ref.getSkew_Rotation());


// cols 5 and 6

//col5_6.addBoolean("Advanced On", ()->ll.ref.getAdvanced_RawCrosshair_X(0))




                // cols 7 and 8

                col7_8.addNumber("tid", () -> ll.getAprilTagID());

                col7_8.addString("camtran", () -> ll.getCamTran().toString());

                col7_8.addNumber("tclass", () -> ll.getNeuralClassID());

                col7_8.addDoubleArray("HSV tc", ()->ll.getAveCrossHairColor());

        }

}
