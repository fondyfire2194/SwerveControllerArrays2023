// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private double m_disableStartTime;

  public static int lpctra;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Logger.getInstance().recordMetadata("ProjectName", "MyProject"); // Set a
    // metadata value

    // if (isReal()) {
    // Logger.getInstance().addDataReceiver(new WPILOGWriter("/media/sda1/")); //
    // Log to a USB stick
    // Logger.getInstance().addDataReceiver(new NT4Publisher()); // Publish data to
    // NetworkTables
    // // new PowerDistribution(1, ModuleType.kRev); // Enables power distribution
    // // logging
    // } else {
    // // setUseTiming(false); // Run as fast as possible
    // String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from
    // AdvantageScope (or prompt the user)
    // Logger.getInstance().setReplaySource(new WPILOGReader(logPath)); // Read
    // replay log
    // Logger.getInstance().addDataReceiver(new
    // WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save
    // // outputs to
    // // a new log
    // }

    // Logger.getInstance().start(); // Start logging! No more data receivers,
    // replay sources, or metadata values may be added.

    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    lpctra++;


    m_robotContainer.m_drive.throttleValue = m_robotContainer.getThrottle();

    /*
     * Retrieves the temperature of the PDP, in degrees Celsius.
     */
    SmartDashboard.putNumber("Temperature", m_robotContainer.m_pdp.getTemperature());

  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    m_disableStartTime = 0;
  }

  @Override
  public void disabledPeriodic() {
    if (DriverStation.getAlliance() == Alliance.Blue) {
      // m_robotContainer.m_ls.forceAllianceColor(true);

      if (m_disableStartTime == 0)
        m_disableStartTime = Timer.getFPGATimestamp();

      if (m_disableStartTime != 0 && Timer.getFPGATimestamp() > m_disableStartTime + 3) {
        m_robotContainer.m_drive.setIdleMode(false);

      }

    }

    if (DriverStation.getAlliance() != Alliance.Blue) {
      // m_robotContainer.m_ls.forceAllianceColor(false);

    }
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    // m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    m_autonomousCommand = m_robotContainer.m_autoSelect.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    // new SetSwerveOdometry(m_robotContainer.m_robotDrive,
    // m_robotContainer.m_fieldSim,new Pose2d(6.13, 5.23,
    // Rotation2d.fromDegrees(-41.5))).schedule();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // m_robotContainer.m_ls.rainbow();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

  }

  @Override
  public void simulationPeriodic() {
    m_robotContainer.m_fieldSim.periodic();
  }

}
