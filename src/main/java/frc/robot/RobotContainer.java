// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.Vision.PipelinesCam15;
import frc.robot.commands.Vision.PipelinesCam16;
import frc.robot.commands.Vision.SetActivePipeline;
import frc.robot.commands.Vision.SetDriverMode;
import frc.robot.commands.Vision.ToggleCamera;
import frc.robot.commands.swerve.JogDriveModule;
import frc.robot.commands.swerve.JogTurnModule;
import frc.robot.commands.swerve.PositionTurnModule;
import frc.robot.commands.swerve.SetSwerveDrive;
import frc.robot.simulation.FieldSim;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;
import frc.robot.subsystems.VisionPoseEstimator;
import frc.robot.utils.AutoSelect;
import frc.robot.utils.FFDisplay;
import frc.robot.utils.LEDControllerI2C;
import frc.robot.utils.limelight.LimeLight;
import frc.robot.utils.limelight.LimeLightControlMode.LedMode;

public class RobotContainer {
  // The robot's subsystems
  final DriveSubsystem m_drive = new DriveSubsystem();

  public VisionPoseEstimator m_vpe = new VisionPoseEstimator(m_drive);

  public FFDisplay ff1 = new FFDisplay("test");

  public AutoSelect m_autoSelect;

  LEDControllerI2C lcI2;

  public final FieldSim m_fieldSim = new FieldSim(m_drive);

  // The driver's controller

  static Joystick leftJoystick = new Joystick(OIConstants.kDriverControllerPort);

  private CommandXboxController m_coDriverController = new CommandXboxController(OIConstants.kCoDriverControllerPort);
  private CommandXboxController testController = new CommandXboxController(2);

  final PowerDistribution m_pdp = new PowerDistribution();

  final LimelightVision llvis = new LimelightVision();
  
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

    SmartDashboard.putData("SetDriverMode1", new SetDriverMode(m_vpe.m_cam1, true));
    SmartDashboard.putData("ResetDriverMode1", new SetDriverMode(m_vpe.m_cam1, false));

    SmartDashboard.putData("SetIndex0Cam1", new SetActivePipeline(m_vpe.m_cam1, 0));
    SmartDashboard.putData("SetIndex1Cam1", new SetActivePipeline(m_vpe.m_cam1, 1));
    SmartDashboard.putData("SetIndex2Cam1", new SetActivePipeline(m_vpe.m_cam1, 2));

    // SmartDashboard.putData("SetDriverMode2", new SetDriverMode(m_vpe.m_cam2,
    // true));
    // SmartDashboard.putData("ResetDriverMode2", new SetDriverMode(m_vpe.m_cam2,
    // false));

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

    JoystickButton button_8 = new JoystickButton(leftJoystick, 8);
    JoystickButton button_7 = new JoystickButton(leftJoystick, 7);

    
    // position turn modules individually
    m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
  m_drive.m_frontLeft));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_frontRight));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_backLeft));
    // m_coDriverController.rightBumper().whileTrue(new PositionTurnModule(m_drive,
    // m_drive.m_backRight));

    testController.a().whileTrue(llvis.setLeds(LedMode.kforceBlink));
    testController.x().whileTrue(llvis.setLeds(LedMode.kforceOff));
    testController.b().whileTrue(llvis.getSnapShot2());


  }

  public void simulationPeriodic() {

    m_fieldSim.periodic();
  }

  public void periodic() {
    m_fieldSim.periodic();
  }

  public double getThrottle() {
    return -leftJoystick.getThrottle();
  }

  // public Command getAutonomousCommand() {
  // return autoBuilder.fullAuto(pathGroup);
  // }
}