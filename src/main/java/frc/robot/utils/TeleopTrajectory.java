// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.DriveSubsystem;

/** Add your docs here. */
public class TeleopTrajectory {

    /**
     * Grid as viewed by Driver starting on their right
     */
    public enum GridDrop {

        RIGHT_ONE_CENTER(new Translation2d(1.5, .23), new Rotation2d(0)),

        RIGHT_ONE_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        RIGHT_TWO_SHELF(new Translation2d(1.5, .23), new Rotation2d(0)),

        RIGHT_THREE_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        COOP_RIGHT_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        COOP_SHELF(new Translation2d(1.5, .23), new Rotation2d(0)),

        COOP_LEFT_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        LEFT_THREE_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        LEFT_TWO_SHELF(new Translation2d(1.5, .23), new Rotation2d(0)),

        LEFT_ONE_PIPE(new Translation2d(1.5, .23), new Rotation2d(0)),

        LEFT_ONE_CENTER(new Translation2d(1.5, .23), new Rotation2d(0));

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

    private DriveSubsystem m_drive;

    public TeleopTrajectory(DriveSubsystem drive) {
        m_drive = drive;
    }

    public Translation2d getT2d(GridDrop gridSlot) {
        return gridSlot.getT2d();
    }

    public Rotation2d getR2d(GridDrop gridSlot) {
        return gridSlot.getR2d();
    }

    public PathPlannerTrajectory getSimpleTrajectory(GridDrop dropPoint, double maxVel, double maxAccel) {

        PathPlannerTrajectory traj1 = PathPlanner.generatePath(

                new PathConstraints(maxVel, maxAccel),

                new PathPoint(m_drive.getTranslation(), m_drive.getHeadingRotation2d()),

                new PathPoint(getT2d(dropPoint), getR2d(dropPoint)));

        return traj1;
    }

    public PathPlannerTrajectory getFromMovingTrajectory(GridDrop dropPoint, double maxVel, double maxAccel) {

        PathPlannerTrajectory traj = PathPlanner.generatePath(

                new PathConstraints(maxVel, maxAccel),

                new PathPoint(new Translation2d(1.0, 1.0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0), 2),

                new PathPoint(m_drive.getTranslation(), m_drive.getHeadingRotation2d()),

                new PathPoint(getT2d(dropPoint), getR2d(dropPoint)));

        return traj;
    }

}
