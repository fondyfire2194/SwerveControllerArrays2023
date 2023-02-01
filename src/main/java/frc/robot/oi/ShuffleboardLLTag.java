// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.oi;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.subsystems.LimelightVision;

/** Add your docs here. */
public class ShuffleboardLLTag {

        private LimelightVision m_llv;
        
        private LimeLight llcam;

        public ShuffleboardLLTag(LimelightVision llv) {
                {

                        m_llv = llv;

                        llcam = m_llv.cam_tag_15;

                        String name = m_llv.getName();

                        ShuffleboardLayout col1_2 = Shuffleboard.getTab(name)
                                        .getLayout("A", BuiltInLayouts.kList).withPosition(0, 0)
                                        .withSize(2, 6).withProperties(Map.of("Label position", "TOP"));

                        ShuffleboardLayout col3_4 = Shuffleboard.getTab(name)
                                        .getLayout("B", BuiltInLayouts.kList).withPosition(2, 0)
                                        .withSize(2, 6).withProperties(Map.of("Label position", "TOP"));

                        ShuffleboardLayout col5_6 = Shuffleboard.getTab(name)
                                        .getLayout("C", BuiltInLayouts.kList).withPosition(4, 0)
                                        .withSize(6, 6).withProperties(Map.of("Label position", "TOP"));

                        // columns 1 and 2

                        col1_2.addNumber("HeartBeat", () -> llcam.getHeartbeat());

                        col1_2.addBoolean(name + " hasTarget)", () -> llcam.getIsTargetFound());

                        col1_2.addBoolean(name + " Connected", () -> llcam.isConnected());

                        col1_2.addString(name + " Cam Mode ", () -> llcam.getCamMode().toString());

                        col1_2.addString(name + " Led Mode", () -> llcam.getLEDMode().toString());

                        col1_2.addString(name + " Stream Mode", () -> llcam.getStream().toString());

                        col1_2.addNumber(name + " Pipeline", () -> llcam.getPipeline());

                        col1_2.addNumber(name + " Latency", () -> llcam.getPipelineLatency());

                        // cols 3 and 4.

                        col3_4.addNumber(name + " BB Short", () -> llcam.getBoundingBoxShortestSide());

                        col3_4.addNumber(name + " BB Long", () -> llcam.getBoundingBoxLongestSide());

                        col3_4.addNumber(name + " BB Height", () -> llcam.getBoundingBoxHeight());

                        col3_4.addNumber(name + " BB Width", () -> llcam.getBoundingBoxWidth());

                        col3_4.addNumber(name + " BB Area ", () -> llcam.getTargetArea());

                        col3_4.addNumber(name + " Deg Rot", () -> llcam.getdegRotationToTarget());

                        col3_4.addNumber(name + " Deg Vert", () -> llcam.getdegVerticalToTarget());

                        col3_4.addNumber(name + " Deg Skew", () -> llcam.getSkew_Rotation());

                        // cols 5 and 6

                        col5_6.addNumber(name + " Tag ID", () -> llcam.getAprilTagID());

                        col5_6.addString(name + " Cam Tran3d", () -> llcam.getCamPose().toString());

                        col5_6.addString(name + " RobPose3d", () -> llcam.getRobotPose().toString());

                        col5_6.addString(name + " RobPose3d-WPI", () -> llcam.getRobotPoseWPI().toString());

                        col5_6.addString(name + " VisionCorrection", () -> m_llv.visionPoseEstimatedData.toString());

                        col5_6.addNumber(name + " Neur ID", () -> llcam.getNeuralClassID());

                        col5_6.addDoubleArray(name + " HSV tc", () -> llcam.getAveCrossHairColor());

                }
        }
}
