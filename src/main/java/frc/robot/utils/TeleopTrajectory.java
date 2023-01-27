// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

/** Add your docs here. */
public class TeleopTrajectory {

    /**
     * Grid as viewed by Driver starting on their right
     */
    public enum GridDrop {

        RIGHT_ONE_CENTER(new Translation2d(.5, .23), new Rotation2d(0)),

        RIGHT_ONE_PIPE(new Translation2d(1.0, .23), new Rotation2d(0)),

        RIGHT_TWO_SHELF(new Translation2d(1.5, .23), new Rotation2d(0)),

        RIGHT_THREE_PIPE(new Translation2d(2.0, .23), new Rotation2d(0)),

        COOP_RIGHT_PIPE(new Translation2d(2.5, .23), new Rotation2d(0)),

        COOP_SHELF(new Translation2d(3.0, .23), new Rotation2d(0)),

        COOP_LEFT_PIPE(new Translation2d(3.5, .23), new Rotation2d(0)),

        LEFT_THREE_PIPE(new Translation2d(4.0, .23), new Rotation2d(0)),

        LEFT_TWO_SHELF(new Translation2d(4.5, .23), new Rotation2d(0)),

        LEFT_ONE_PIPE(new Translation2d(5.0, .23), new Rotation2d(0)),

        LEFT_ONE_CENTER(new Translation2d(5.5, .23), new Rotation2d(0));

        private final Translation2d t2d;

        private final Rotation2d r2d;

        private GridDrop(Translation2d t2d, Rotation2d r2d) {
            this.t2d = t2d;
            this.r2d = r2d;
        }

        public Translation2d getT2d() {
            return t2d;
        }

        public Rotation2d getR2d() {
            return r2d;
        }

    }

    private int tryit;
    private DriveSubsystem m_drive;

    private GridDrop activeDrop = GridDrop.COOP_SHELF;

    public TeleopTrajectory(DriveSubsystem drive) {
        m_drive = drive;

    }

    public Translation2d getT2d(GridDrop gridSlot) {
        return gridSlot.getT2d();
    }

    public Rotation2d getR2d(GridDrop gridSlot) {
        return gridSlot.getR2d();
    }

    public void setActiveDrop(GridDrop drop) {
        SmartDashboard.putNumber("AABB", tryit++);
        SmartDashboard.putString("ActDrop", getActiveDrop().toString());
        activeDrop = drop;
    }

    public GridDrop getActiveDrop() {
        return activeDrop;
    }

    public PathPlannerTrajectory getSimpleTrajectory(double maxVel, double maxAccel) {

        PathPlannerTrajectory traj1 = PathPlanner.generatePath(

                new PathConstraints(maxVel, maxAccel),

                new PathPoint(m_drive.getTranslation(), m_drive.getHeadingRotation2d()),

                new PathPoint(getT2d(activeDrop), getR2d(activeDrop)));

        return traj1;
    }

    public PathPlannerTrajectory getFromMovingTrajectory(double maxVel, double maxAccel) {

        PathPlannerTrajectory traj = PathPlanner.generatePath(

                new PathConstraints(maxVel, maxAccel),

                new PathPoint(new Translation2d(1.0, 1.0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0), 2),

                new PathPoint(m_drive.getTranslation(), m_drive.getHeadingRotation2d()),

                new PathPoint(getT2d(activeDrop), getR2d(activeDrop)));

        return traj;
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
                ),

                new InstantCommand(() -> m_drive.stopModules()));
    }

}
