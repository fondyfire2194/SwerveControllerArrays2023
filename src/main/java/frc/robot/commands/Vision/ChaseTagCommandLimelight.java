package frc.robot.commands.Vision;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LimeLight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

/**
 * 
 * Command to assist in getting robot to LOad Station position
 * Driver should bring robot in range of tag
 * 
 * 
 */

public class ChaseTagCommandLimelight extends CommandBase {

  private final LimelightVision m_llv;
  private final DriveSubsystem m_drive;
  private LimeLight m_llcam;
  private int m_tagID;
  private double m_xOffset;
  private double m_yOffset;
  private double m_omegaOffset;
  private DoubleSupplier m_xSpeed;
  private double xTol = 2;// inches
  private double yTol = 2;// inches
  private double omegaTol = 2;// degrees

  private final PIDController pidX = new PIDController(1.0, 0, 0);
  private final PIDController pidY = new PIDController(1.0, 0, 0);
  private final PIDController pidOmega = new PIDController(.5, 0, 0);

  public ChaseTagCommandLimelight(LimelightVision llv, LimeLight llcam,
      int tagId, double xOffset, double yOffset,
      double omegaOffset, DriveSubsystem drive, DoubleSupplier xSpeed) {
    m_llv = llv;
    m_drive = drive;
    m_llcam = llcam;
    m_tagID = tagId;
    m_xOffset = xOffset;
    m_yOffset = yOffset;
    m_omegaOffset = omegaOffset;
    m_xSpeed = xSpeed;
    addRequirements(m_drive);
  }

  @Override
  public void initialize() {
    super.initialize();
    pidX.reset();
    pidY.reset();
    pidOmega.reset();

    pidX.setSetpoint(Units.inchesToMeters(m_xOffset)); // Move forward/backwork to keep 36 inches from the target
    pidX.setTolerance(Units.inchesToMeters(xTol));

    pidY.setSetpoint(m_yOffset); // Move side to side to keep target centered
    pidX.setTolerance(Units.inchesToMeters(yTol));

    pidOmega.setSetpoint(Units.degreesToRadians(m_omegaOffset)); // Rotate the keep perpendicular with the target
    pidOmega.setTolerance(Units.degreesToRadians(omegaTol));
  }

  @Override
  public void execute() {

    if (m_llcam.getIsTargetFound() && m_llcam.getAprilTagID() == m_tagID) {

      // Get the transformation from the camera to the tag (in 2d)
      var cameraToTarget = m_llcam.getRobotPose_FS();
      var distanceFromTarget = cameraToTarget.getX();
      var xSpeed = pidX.calculate(distanceFromTarget);
      if (pidX.atSetpoint()) {
        xSpeed = 0;
      }
      SmartDashboard.putNumber("xSpeed", xSpeed);
      var targetY = cameraToTarget.getY();
      var ySpeed = pidY.calculate(targetY);
      if (pidY.atSetpoint()) {
        ySpeed = 0;
      }
      SmartDashboard.putNumber("ySpeed", ySpeed);
      // var targetYaw = cameraToTarget.getRotation().getZ();
      // var omegaSpeed = pidOmega.calculate(targetYaw);
      // if (pidOmega.atSetpoint()) {
      // omegaSpeed = 0;
      // }
      // Issues with target Rotation and Limelight
      m_drive.drive(xSpeed, -ySpeed, 0);

    } else {
      
      m_drive.stopModules();

    }
  }

  @Override
  public void end(boolean interrupted) {
    m_drive.stopModules();
  }

}