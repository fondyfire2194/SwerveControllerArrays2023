// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.Auto.StartTrajectory;
import frc.robot.commands.Vision.PhotonVision.TargetThread1;
import frc.robot.commands.swerve.SetSwerveDrive;
import frc.robot.commands.swerve.StrafeToSlot;
import frc.robot.oi.ShuffleboardLLTag;
import frc.robot.simulation.FieldSim;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;
import frc.robot.utils.AutoFactory;
import frc.robot.utils.LEDControllerI2C;
import frc.robot.utils.TeleopTrajectory;
import frc.robot.utils.TeleopTrajectory.GridDrop;
import frc.robot.utils.TrajectoryFactory;

public class RobotContainer {
        // The robot's subsystems
        final DriveSubsystem m_drive = new DriveSubsystem();

        final ArmSubsystem m_arm = new ArmSubsystem();

        final LimelightVision m_llv = new LimelightVision();

        private ShuffleboardLLTag sLLtag;

        private ShuffleboardLLTag sLLtape;

        // final PhotonVision m_pv = new PhotonVision();

        TargetThread1 tgtTh1 = null;

        public AutoFactory m_autoFactory;

        public TrajectoryFactory m_tf;

        public TeleopTrajectory m_ttj;

        public LEDControllerI2C lcI2;

        public final FieldSim m_fieldSim = new FieldSim(m_drive);

        // The driver's controller

        private CommandXboxController m_driverController = new CommandXboxController(
                        OIConstants.kDriverControllerPort);

        private CommandXboxController m_testController = new CommandXboxController(OIConstants.kTestControllerPort);

        final PowerDistribution m_pdp = new PowerDistribution();

        final LimelightVision llvis = new LimelightVision();

        private boolean useLimeLight = true;

        private boolean usePhotonVision = false;

        private boolean usePS4;

        // temp controller for testing -matt
        private PS4Controller m_ps4controller = new PS4Controller(OIConstants.kDriverControllerPort);
        // public PoseTelemetry pt = new PoseTelemetry();

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                // Preferences.removeAll();
                Pref.deleteUnused();

                Pref.addMissing();

                // if (useLimeLight)

                // if (usePhotonVision)

                // tgtTh1 = new TargetThread1(m_drive, m_pv);

                SmartDashboard.putData("Scheduler", CommandScheduler.getInstance());

                LiveWindow.disableAllTelemetry();
                // Configure the button bindings

                m_fieldSim.initSim();

                m_autoFactory = new AutoFactory(m_drive, m_arm);

                m_tf = new TrajectoryFactory(m_drive);

                m_ttj = new TeleopTrajectory(m_drive);

                SmartDashboard.putData(m_drive);

                // m_ls = new LightStrip(9, 60);

                // lc = LEDController.getInstance();
                lcI2 = LEDControllerI2C.getInstance();

                sLLtag = new ShuffleboardLLTag(m_llv.cam_tag_15);

                // sLLtape = new ShuffleboardLLTag(m_llv.cam_tape_16);

                // PortForwarder.add(5800, "10.21.94.11", 5800);
                // PortForwarder.add(1181, "10.21.94.11", 1181);
                // PortForwarder.add(1182, "10.21.94.11", 1182);
                // PortForwarder.add(1183, "10.21.94,11", 1183);
                // PortForwarder.add(1184, "10.21.94.11", 1184);

                // () -> -m_coDriverController.getRawAxis(1),
                // () -> -m_coDriverController.getRawAxis(0),
                // () -> -m_coDriverController.getRawAxis(4)));

                if (usePS4) {
                        m_drive.setDefaultCommand(
                                        new SetSwerveDrive(
                                                        m_drive,
                                                        () -> m_ps4controller.getRawAxis(1),
                                                        () -> m_ps4controller.getRawAxis(0),
                                                        () -> m_ps4controller.getRawAxis(2)));
                }
//Logitech gamepad
                else {
                        m_drive.setDefaultCommand(
                                        new SetSwerveDrive(
                                                        m_drive,
                                                        () -> m_driverController.getRawAxis(1),
                                                        () -> m_driverController.getRawAxis(0),
                                                        () -> m_driverController.getRawAxis(4)));// logitech gamepad
                }

                m_testController.a()
                                .onTrue(Commands.runOnce(() -> m_ttj.setActiveDrop(GridDrop.COOP_LEFT_PIPE)));
                m_testController.b().onTrue(Commands.runOnce(() -> m_ttj.setActiveDrop(GridDrop.RIGHT_ONE_CENTER)));
                m_testController.x().onTrue(Commands.runOnce(() -> m_ttj.setActiveDrop(GridDrop.LEFT_ONE_CENTER)));
                m_testController.y().onTrue(Commands.runOnce(() -> m_ttj.setActiveDrop(GridDrop.RIGHT_THREE_PIPE)));

                m_testController.leftBumper().onTrue(new StartTrajectory(m_tf));

                m_driverController.rightBumper().whileTrue(getStrafeToTargetCommand());

        }

        public double getThrottle() {

                return 0;

        }

        public Command getStrafeToTargetCommand() {

                return new StrafeToSlot(m_drive, m_ttj, () -> m_driverController.getRawAxis(0));
        }

}
