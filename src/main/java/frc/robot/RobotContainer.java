// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.Vision.Limelight.TargetThread1LL;
import frc.robot.commands.Vision.PhotonVision.TargetThread1;
import frc.robot.commands.swerve.JogDriveModule;
import frc.robot.commands.swerve.JogTurnModule;
import frc.robot.commands.swerve.PositionTurnModule;
import frc.robot.commands.swerve.SetSwerveDrive;
import frc.robot.simulation.FieldSim;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;
import frc.robot.subsystems.PhotonVision;
import frc.robot.utils.AutoSelect;
import frc.robot.utils.LEDControllerI2C;

public class RobotContainer {
  // The robot's subsystems
  final DriveSubsystem m_drive = new DriveSubsystem();

  final LimelightVision m_llv = new LimelightVision();

  final PhotonVision m_pv = new PhotonVision();

  TargetThread1LL tgtTh1ll = null;

  TargetThread1 tgtTh1 = null;

  public AutoSelect m_autoSelect;

  public LEDControllerI2C lcI2;

  public final FieldSim m_fieldSim = new FieldSim(m_drive);

  // The driver's controller

  static Joystick leftJoystick = new Joystick(OIConstants.kDriverControllerPort);

  private CommandXboxController m_coDriverController = new CommandXboxController(OIConstants.kCoDriverControllerPort);

  private CommandXboxController m_testController = new CommandXboxController(OIConstants.kTestControllerPort);

  final PowerDistribution m_pdp = new PowerDistribution();

  final LimelightVision llvis = new LimelightVision();

  private boolean useLimeLight;

  private boolean usePhotonVision;

  // temp controller for testing -matt
  // private PS4Controller m_ps4controller = new PS4Controller(1);
  // public PoseTelemetry pt = new PoseTelemetry();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Preferences.removeAll();
    Pref.deleteUnused();

    Pref.addMissing();

    if (useLimeLight)

      tgtTh1ll = new TargetThread1LL(m_drive, m_llv);

    if (usePhotonVision)

      tgtTh1 = new TargetThread1(m_drive, m_pv);

    SmartDashboard.putData("Scheduler", CommandScheduler.getInstance());

    LiveWindow.disableAllTelemetry();
    // Configure the button bindings

    m_fieldSim.initSim();

    m_autoSelect = new AutoSelect(m_drive);

    // m_ls = new LightStrip(9, 60);

    // lc = LEDController.getInstance();
    lcI2 = LEDControllerI2C.getInstance();

    // PortForwarder.add(5800, "10.21.94.11", 5800);
    // PortForwarder.add(1181, "10.21.94.11", 1181);
    // PortForwarder.add(1182, "10.21.94.11", 1182);
    // PortForwarder.add(1183, "10.21.94,11", 1183);
    // PortForwarder.add(1184, "10.21.94.11", 1184);

    // () -> -m_coDriverController.getRawAxis(1),
    // () -> -m_coDriverController.getRawAxis(0),
    // () -> -m_coDriverController.getRawAxis(4)));
    // m_drive.setDefaultCommand(
    // new SetSwerveDrive(
    // m_drive,
    // () -> m_ps4controller.getRawAxis(1),
    // () -> m_ps4controller.getRawAxis(0),
    // () -> m_ps4controller.getRawAxis(2)));

    m_drive.setDefaultCommand(
        new SetSwerveDrive(
            m_drive,
            () -> leftJoystick.getRawAxis(1),
            () -> leftJoystick.getRawAxis(0),
            () -> leftJoystick.getRawAxis(2)));

    m_coDriverController.leftTrigger().whileTrue(new JogTurnModule(
        m_drive,
        () -> -m_coDriverController.getRawAxis(1),
        () -> m_coDriverController.getRawAxis(0),
        () -> m_coDriverController.getRawAxis(4),
        () -> m_coDriverController.getRawAxis(5)));

    // individual modules
    m_coDriverController.leftBumper().whileTrue(new JogDriveModule(
        m_drive,
        () -> -m_coDriverController.getRawAxis(1),
        () -> m_coDriverController.getRawAxis(0),
        () -> m_coDriverController.getRawAxis(4),
        () -> m_coDriverController.getRawAxis(5),
        true));

    // all modules
    m_coDriverController.rightBumper().whileTrue(new JogDriveModule(
        m_drive,
        () -> -m_coDriverController.getRawAxis(1),
        () -> m_coDriverController.getRawAxis(0),
        () -> m_coDriverController.getRawAxis(2),
        () -> m_coDriverController.getRawAxis(3),
        false));

    // position turn modules individually
    m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
        m_drive.m_frontLeft));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_frontRight));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_backLeft));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_backRight));

    m_testController.a().whileTrue(getDriverSetCommand(m_pv.cam_tag_11, true));

    m_testController.a().whileTrue(getDriverSetCommand(m_pv.cam_tag_11, false));

    m_testController.x().whileTrue(getSetPVPipelineCommand(m_pv.cam_tag_11, 1));  
    m_testController.y().whileTrue(getSetPVPipelineCommand(m_pv.cam_tag_11, 4));

  }

  public double getThrottle() {

    return -leftJoystick.getThrottle();
  }

  public Command getDriverSetCommand(PhotonCamera cam, boolean on) {

    return m_pv.SetDriverMode(cam, on);

  }

  public Command getSetPVPipelineCommand(PhotonCamera cam, int n) {

    return m_pv.SetPipeline(cam, n);

  }

  // public Command getAutonomousCommand() {
  // return autoBuilder.fullAuto(pathGroup);
  // }
}