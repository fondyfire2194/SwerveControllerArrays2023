// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.oi;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

/** Add your docs here. */
public class ShuffleboardLLTag {

        private LimelightVision m_llv;

        private DriveSubsystem m_drive;

        private LimeLight llcam;

        public ShuffleboardLLTag(LimelightVision llv, DriveSubsystem drive) {
                {

                        m_llv = llv;

                        llcam = m_llv.cam_tag_15;

                        m_drive = drive;

                        String name = m_llv.cam_tag_15.getName();
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

                        col1_2.addNumber("HeartBeat", () -> llv.getHeartbeat(llcam));

                        col1_2.addBoolean(name + " hasTarget)", () -> llv.isTargetFound(llcam));

                        col1_2.addBoolean(name + " Connected", () -> llcam.isConnected());

                        col1_2.addString(name + " Led Mode", () -> llv.getLedMode(llcam).toString());

                        // col1_2.addString(name + " Stream Mode", () -> llcam.getStream().toString());

                        col1_2.addNumber(name + " Pipeline", () -> llv.getPipelineNumber(llcam));

                        col1_2.addNumber(name + " Latency", () -> round2dp(llv.getPipelineLatency(llcam)));

                        // cols 3 and 4.

                        col3_4.addNumber(name + " BB Short", () -> round2dp(llcam.getBoundingBoxShortestSide()));

                        col3_4.addNumber(name + " BB Long", () -> round2dp(llcam.getBoundingBoxLongestSide()));

                        col3_4.addNumber(name + " BB Height", () -> round2dp(llcam.getBoundingBoxHeight()));

                        col3_4.addNumber(name + " BB Width", () -> round2dp(llcam.getBoundingBoxWidth()));

                        col3_4.addNumber(name + " BB Area ", () -> round2dp(llcam.getTargetArea()));

                        col3_4.addNumber(name + " Deg Rot", () -> round2dp(llcam.getdegRotationToTarget()));

                        col3_4.addNumber(name + " Deg Vert", () -> round2dp(llcam.getdegVerticalToTarget()));

                        col3_4.addNumber(name + " Deg Skew", () -> round2dp(llcam.getSkew_Rotation()));

                        // cols 5 and 6

                        col5_6.addNumber(name + " Tag ID", () -> llv.getAprilTagID(llcam));

                        col5_6.addString("Actual Robot Pose", () -> m_drive.getEstimatedPose().toString());

                        // col5_6.addString(name + " RobPose FS", () ->
                        // llv.getRobotPose_FS(llcam).toString());

                        col5_6.addString(name + " RobPoseFS-WPIBL",
                                        () -> llv.getRobotPose_FS_WPIBlue(llcam).toString());

                  //      col5_6.addString(name + " CurrTagPose", () -> llv.getCurrentTagPose(llcam).toString());

                        // col5_6.addNumber("XDistToTag", () -> round2dp(m_llv.distToTagX));

                        // col5_6.addNumber("YDistToTag", () -> round2dp(llv.distToTagY));

                        // col5_6.addNumber("DegToTag",()->round2dp(llv.degToTag));

                        // col5_6.addNumber("RobDiffX",()->round2dp(llv.robDiffX));

                        // col5_6.addNumber("RobDiffY",()->round2dp(llv.robDiffY));

                        // col5_6.addNumber("RobDiffDeg",()->round2dp(llv.robDiffDeg));

                }

        }

        public double round2dp(double number) {
                number = Math.round(number * 100);
                number /= 100;
                return number;
        }
}
