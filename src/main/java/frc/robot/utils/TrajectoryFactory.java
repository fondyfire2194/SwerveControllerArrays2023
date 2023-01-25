// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.PPConstants;
import frc.robot.subsystems.DriveSubsystem;

/** Add your docs here. */
public class TrajectoryFactory {

    public SendableChooser<String> ppTrajChooser = new SendableChooser<String>();

    private DriveSubsystem m_drive;

    private boolean tune;

    public TrajectoryFactory(DriveSubsystem drive) {

        m_drive = drive;

        ppTrajChooser.setDefaultOption("DriveForward", "DriveForward");

        ppTrajChooser.addOption("DriveToPickup", "DriveToPickup");

        SmartDashboard.putData("TrajChoice", ppTrajChooser);
    }

    public String getSelectedTrajectory() {
        return ppTrajChooser.getSelected();
    }

    public PathPlannerTrajectory getTrajectory(String name) {
        return PathPlanner.loadPath(name, Units.feetToMeters(2),
                Units.feetToMeters(2), false);
    }

    public Command followTrajectoryCommand(PathPlannerTrajectory traj, boolean isFirstPath) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    // Reset odometry for the first path you run during auto
                    if (isFirstPath) {
                        m_drive.resetOdometry(traj.getInitialHolonomicPose());
                    }
                }),
                new PPSwerveControllerCommand(

                        traj,

                        m_drive::getEstimatedPose, // Pose supplier

                        m_drive.m_kinematics, // SwerveDriveKinematics
                        m_drive.getXPID(),
                        m_drive.getYPID(),
                        m_drive.getThetaPID(),
                        
                        
                      
                        m_drive::setModuleStates, // Module states consumer

                        m_drive // Requires this drive subsystem
                ));
    }

}
