package frc.robot.commands.swerve;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.DriverConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utils.TeleopTrajectory;

public class StrafeToSlot extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final DriveSubsystem m_drive;
  private final TeleopTrajectory m_telTraj;

  /**
   * Creates a new ExampleCommand.
   *
   * @param swerveDriveSubsystem The subsystem used by this command.
   */
  private double endYTarget;
  private double endXTarget;

  private PIDController m_pidY = new PIDController(.5, 0, 0);
  private double strafe_max = .25;

  private PIDController m_pidX = new PIDController(.5, 0, 0);
  private double drive_max = .25;


  public StrafeToSlot(
      DriveSubsystem drive,
      TeleopTrajectory telTraj,
      DoubleSupplier strafeInput) {

    m_drive = drive;

    m_telTraj = telTraj;


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    endYTarget = m_telTraj.getActiveDrop().getT2d().getY();
    endXTarget = m_telTraj.getActiveDrop().getT2d().getX();

    SmartDashboard.putNumber("ENDPTY", endYTarget);
    SmartDashboard.putNumber("ENDPTX", endXTarget);
  }

  /**
   * Used to position the robot to one of the target slots
   * Robot will run at 50% strafe until it reaches the slowdown distance
   * from the target end.
   * T that point speed will be reduced proportional to remaining distance
   * 
   * 
   */
  // https://www.chiefdelphi.com/t/swerve-controller-joystick/392544/5
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double pidOutY = m_pidY.calculate(m_drive.getY(), endYTarget);

    double latchpidY = pidOutY;
    
    if (Math.abs(pidOutY) > strafe_max) {
      pidOutY = strafe_max;
      if (latchpidY < 0)
        pidOutY = -pidOutY;
    }

    double pidOutX = m_pidX.calculate(m_drive.getX(), endYTarget);

    double latchpidX = pidOutX;
    
    if (Math.abs(pidOutX) > drive_max) {
      pidOutX = drive_max;
      if (latchpidX < 0)
        pidOutX = -pidOutX;
    }





    m_drive.drive(pidOutX, pidOutY, 0);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stopModules();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(endYTarget - m_drive.getY()) < .1;
  }
}
