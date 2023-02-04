package frc.robot.commands.swerve.Test;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;
import frc.robot.utils.TrajectoryFactory;

public class DriveToAprilTag extends CommandBase {
    private DriveSubsystem m_drive;
    private LimelightVision m_limelightVision;
    private boolean endingCondition;

    public DriveToAprilTag(DriveSubsystem drive, LimelightVision llv) {
        m_drive = drive;
        m_limelightVision = llv;
        endingCondition = false; 
    }

    @Override
    public void initialize() {
        
        if (m_limelightVision.getAprilTagID(m_limelightVision.cam_tag_15) != -1) {
            Pose2d currentPose = m_drive.getEstimatedPose();
            Pose3d tag_in_field_space = m_limelightVision.getCurrentTagPose(m_limelightVision.cam_tag_15); 
            Pose2d endPose = tag_in_field_space.toPose2d();
            SmartDashboard.putString("AAAStartPose", currentPose.toString());
            SmartDashboard.putString("AAAEndPose", endPose.toString());

            PathPlannerTrajectory trajectory = PathPlanner.generatePath(
                    new PathConstraints(1, 1),

                    new PathPoint(currentPose.getTranslation(), currentPose.getRotation()),

                    new PathPoint(endPose.getTranslation().minus(new Translation2d(1, 
                    0)), currentPose.getRotation()));
            
            new TrajectoryFactory(m_drive).followTrajectoryCommand(trajectory, true).schedule();

        }
        endingCondition = true; 
    }
    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return endingCondition;
    }

}
