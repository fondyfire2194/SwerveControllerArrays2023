package frc.robot.commands.swerve.Test;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

public class ChaseTagCommandLimelight extends CommandBase {

  private final LimelightVision limelightVision;
  private final DriveSubsystem drivetrainSubsystem;

  private final PIDController pidControllerX = new PIDController(1.0, 0, 0);
  private final PIDController pidControllerY = new PIDController(1.0, 0, 0);
  private final PIDController pidControllerOmega = new PIDController(.5, 0, 0);

  public ChaseTagCommandLimelight(
      LimelightVision limelightVision,
      DriveSubsystem drivetrainSubsystem) {
    this.limelightVision = limelightVision;
    this.drivetrainSubsystem = drivetrainSubsystem;

    addRequirements(drivetrainSubsystem);
  }

  @Override
  public void initialize() {
    super.initialize();
    pidControllerX.reset();
    pidControllerY.reset();
    pidControllerOmega.reset();

    pidControllerX.setSetpoint(Units.inchesToMeters(-40)); // Move forward/backwork to keep 36 inches from the target
    pidControllerX.setTolerance(Units.inchesToMeters(7.5));

    pidControllerY.setSetpoint(0); // Move side to side to keep target centered
    pidControllerX.setTolerance(Units.inchesToMeters(2.5));

    pidControllerOmega.setSetpoint(Units.degreesToRadians(-90)); // Rotate the keep perpendicular with the target
    pidControllerOmega.setTolerance(Units.degreesToRadians(1));
  }

  @Override
  public void execute() {

    if (limelightVision.cam_tag_15 != null) {
      if (limelightVision.cam_tag_15.getIsTargetFound()) {

        // Get the transformation from the camera to the tag (in 2d)
        var cameraToTarget = limelightVision.getCamTransform(limelightVision.cam_tag_15);
        var distanceFromTarget = cameraToTarget.getX();
        var xSpeed = pidControllerX.calculate(distanceFromTarget);
        if (pidControllerX.atSetpoint()) {
          xSpeed = 0;
        }
        SmartDashboard.putNumber("xSpeed", xSpeed);
        var targetY = cameraToTarget.getY();
        var ySpeed = pidControllerY.calculate(targetY);
        if (pidControllerY.atSetpoint()) {
          ySpeed = 0;
        }
        SmartDashboard.putNumber("ySpeed", ySpeed);
        // var targetYaw = cameraToTarget.getRotation().getZ();
        // var omegaSpeed = pidControllerOmega.calculate(targetYaw);
        // if (pidControllerOmega.atSetpoint()) {
        //   omegaSpeed = 0;
        // }
        // Issues with target Rotation and Limelight
        drivetrainSubsystem.drive(xSpeed, -ySpeed, 0);
      }
      else {
        drivetrainSubsystem.stopModules();
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.stopModules();
  }

}