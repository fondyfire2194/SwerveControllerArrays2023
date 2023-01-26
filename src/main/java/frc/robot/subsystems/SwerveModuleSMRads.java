// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.CTRECanCoder;
import frc.robot.Constants.CurrentLimitConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.ModuleTuneConstants;
import frc.robot.Constants.SYSIDConstants;
import frc.robot.Pref;
import frc.robot.utils.AngleUtils;
import frc.robot.utils.ShuffleboardContent;

public class SwerveModuleSMRads extends SubsystemBase {

  public final CANSparkMax m_driveMotor;

  public final CANSparkMax m_turnMotor;

  public final RelativeEncoder m_driveEncoder;

  private final RelativeEncoder m_turnEncoder;

  public final int m_locationIndex;

  private final PIDController m_driveVelController = new PIDController(ModuleTuneConstants.kPModuleDriveController,
      ModuleTuneConstants.kIModuleDriveController, ModuleTuneConstants.kDModuleDriveController);

  private PIDController m_turnPosController = new PIDController(ModuleTuneConstants.kPModuleTurningController,
      ModuleTuneConstants.kIModuleTurningController, ModuleTuneConstants.kDModuleTurningController);

  public final CTRECanCoder m_turnCANcoder;

  SwerveModuleState state;

  public int m_moduleNumber;

  public String[] modAbrev = { "FL ", "FR ", "RL ", "RR " };
  String driveLayout;

  String turnLayout;

  String canCoderLayout;

  Pose2d m_pose;

  double testAngle;

  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(
      SYSIDConstants.ksDriveVoltSecondsPerMeter,
      SYSIDConstants.kvDriveVoltSecondsSquaredPerMeter,
      SYSIDConstants.kaDriveVoltSecondsSquaredPerMeter);

  private double m_lastAngle;
  public double angle;

  public double m_turnEncoderOffset;

  private int tuneOn;

  private double tolRadPerSec = .0001;
  private double toleranceRads = .005;
  public boolean driveMotorConnected;
  public boolean turnMotorConnected;
  public boolean turnCoderConnected;
  private boolean showOnShuffleboard = true;

  public SendableBuilder m_builder;
  private boolean m_isOpenLoop;

  public boolean driveBrakeMode;

  public boolean turnBrakeMode;

  /**
   * Constructs a SwerveModule.
   *
   * @param driveMotorChannel       The channel of the drive motor.
   * @param turningMotorChannel     The channel of the turning motor.
   * @param driveEncoderChannels    The channels of the drive encoder.
   * @param turningCANCoderChannels The channels of the turning encoder.
   * @param driveEncoderReversed    Whether the drive encoder is reversed.
   * @param turningEncoderReversed  Whether the turning encoder is reversed.
   * @param turningEncoderOffset
   */
  public SwerveModuleSMRads(
      int locationIndex,
      int driveMotorCanChannel,
      int turningMotorCanChannel,
      int cancoderCanChannel,
      boolean driveMotorReversed,
      boolean turningMotorReversed,
      int pdpCDrivehannel,
      int pdpTurnChannel,
      boolean isOpenLoop,
      double turningEncoderOffset) {

    m_locationIndex = locationIndex;

    m_isOpenLoop = isOpenLoop;

    m_driveMotor = new CANSparkMax(driveMotorCanChannel, MotorType.kBrushless);

    m_turnMotor = new CANSparkMax(turningMotorCanChannel, MotorType.kBrushless);

    m_turnMotor.restoreFactoryDefaults();

    m_driveMotor.restoreFactoryDefaults();

    m_turnMotor.setSmartCurrentLimit(CurrentLimitConstants.turnMotorSmartLimit);

    m_driveMotor.setSmartCurrentLimit(CurrentLimitConstants.driveMotorSmartLimit);

    m_driveMotor.enableVoltageCompensation(ModuleConstants.kVoltCompensation);

    m_turnMotor.enableVoltageCompensation(ModuleConstants.kVoltCompensation);

    // absolute encoder used to establish known wheel position on start position
    m_turnCANcoder = new CTRECanCoder(cancoderCanChannel);
    m_turnCANcoder.configFactoryDefault();
    m_turnCANcoder.configAllSettings(AngleUtils.generateCanCoderConfig());
    m_turnEncoderOffset = turningEncoderOffset;

    m_driveMotor.setInverted(driveMotorReversed);

    m_turnMotor.setInverted(turningMotorReversed);

    m_driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 100);
    m_driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 20);
    m_driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 20);
    // Set neutral mode to brake
    m_driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    m_turnMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 10);
    m_turnMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 20);
    m_turnMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 50);
    // Set neutral mode to brake
    m_turnMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    m_driveEncoder = m_driveMotor.getEncoder();

    m_driveEncoder.setPositionConversionFactor(ModuleConstants.kDriveMetersPerEncRev);

    m_driveEncoder.setVelocityConversionFactor(ModuleConstants.kDriveMetersPerEncRev
        / 60);

    m_turnEncoder = m_turnMotor.getEncoder();

    m_turnEncoder.setPositionConversionFactor(ModuleConstants.kTurningRadiansPerEncoderRev);

    m_turnEncoder.setVelocityConversionFactor(ModuleConstants.kTurningRadiansPerEncoderRev / 60);

    m_turnPosController.enableContinuousInput(-Math.PI, Math.PI);

    checkCAN();

    resetAngleToAbsolute();

    if (showOnShuffleboard) {

      ShuffleboardContent.initDriveShuffleboard(this);
      ShuffleboardContent.initTurnShuffleboard(this);
      ShuffleboardContent.initCANCoderShuffleboard(this);
      ShuffleboardContent.initBooleanShuffleboard(this);
      ShuffleboardContent.initCoderBooleanShuffleboard(this);
    }

  }

  @Override
  public void periodic() {

    if (Pref.getPref("SwerveTune") == 1 && tuneOn == 0) {

      tuneOn = 1;

      tunePosGains();

      tuneDriveVelGains();
    }

    if (tuneOn == 1) {

      tuneOn = (int) Pref.getPref("SwerveTune");
    }

    if (m_turnCANcoder.getFaulted()) {
      // SmartDashboard.putStringArray("CanCoderFault"
      // + m_modulePosition.toString(), m_turnCANcoder.getFaults());
      SmartDashboard.putStringArray("CanCoderStickyFault"
          + String.valueOf(m_locationIndex), m_turnCANcoder.getStickyFaults());

    }
  }

  public void tunePosGains() {
    m_turnPosController.setP(Pref.getPref("SwerveTurnPoskP"));
    m_turnPosController.setI(Pref.getPref("SwerveTurnPoskI"));
    m_turnPosController.setD(Pref.getPref("SwerveTurnPoskD"));
    // m_turnController.setIZone(Pref.getPref("SwerveTurnPoskIz"));
  }

  public void tuneDriveVelGains() {
    m_driveVelController.setP(Pref.getPref("SwerveVelkP"));
    m_driveVelController.setI(Pref.getPref("SwerveVelkI"));
    m_driveVelController.setD(Pref.getPref("SwerveVelkD"));

  }

  public SwerveModuleState getState() {

    return new SwerveModuleState(getDriveVelocity(), Rotation2d.fromRadians((getTurnAngleRads())));
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getDrivePosition(), getHeadingRotation2d());

  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {

    state = AngleUtils.optimize(desiredState, getHeadingRotation2d());

    SmartDashboard.putNumber("StateSpeed", state.speedMetersPerSecond);

    SmartDashboard.putNumber("StateRads", state.angle.getRadians());

    // turn motor code
    // Prevent rotating module if speed is less then 1%. Prevents Jittering.
    angle = (Math.abs(state.speedMetersPerSecond) <= (DriveConstants.kMaxSpeedMetersPerSecond * 0.01))

        ? m_lastAngle

        : state.angle.getRadians();

    m_lastAngle = angle;

    SmartDashboard.putNumber("TESTSP", state.speedMetersPerSecond);
    SmartDashboard.putNumber("TEST", angle);

    driveMotorMove(state.speedMetersPerSecond);

    // turn axis

    positionTurn(angle);

  }

  public void driveMotorMove(double speed) {

    if (m_isOpenLoop) {
  m_driveMotor.set(speed / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
    SmartDashboard.putNumber("DrFF Volts", feedforward.calculate(speed));
  
  // m_driveMotor.setVoltage(feedforward.calculate(speed));
    }
    else {

      m_driveMotor.setVoltage(feedforward.calculate(speed)
          + m_driveVelController.calculate(getDriveVelocity(), speed));

    }
  }

  public void positionTurn(double angle) {

    double pidOut = m_turnPosController.calculate(m_turnEncoder.getPosition(), angle);
    SmartDashboard.putNumber("PIDOUT", pidOut);
    double turnAngleError = Math.abs(angle - m_turnEncoder.getPosition());

    SmartDashboard.putNumber("ATAERads", turnAngleError);

    // if robot is not moving, stop the turn motor oscillating
    // if (turnAngleError < turnDeadband

    // && Math.abs(state.speedMetersPerSecond) <=
    // (DriveConstants.kMaxSpeedMetersPerSecond * 0.01))

    // pidOut = 0;

    m_turnMotor.setVoltage(pidOut * RobotController.getBatteryVoltage());

  }

  public static double limitMotorCmd(double motorCmdIn) {
    return Math.max(Math.min(motorCmdIn, 1.0), -1.0);
  }

  /** Zeroes all the SwerveModule encoders. */
  public void resetEncoders() {
    m_driveEncoder.setPosition(0);
    m_turnEncoder.setPosition(0);
  }

  public double getHeadingRadians() {
    return getTurnAngleRads();
  }

  public Rotation2d getHeadingRotation2d() {
    return Rotation2d.fromRadians(getHeadingRadians());
  }

  public void setModulePose(Pose2d pose) {
    m_pose = pose;
  }

  public void setDriveBrakeMode(boolean on) {
    driveBrakeMode = on;
    if (on)
      m_driveMotor.setIdleMode(IdleMode.kBrake);
    else
      m_driveMotor.setIdleMode(IdleMode.kCoast);
  }

  public void setTurnBrakeMode(boolean on) {
    turnBrakeMode = on;
    if (on) {
      m_turnMotor.setIdleMode(IdleMode.kBrake);

    } else
      m_turnMotor.setIdleMode(IdleMode.kCoast);
  }

  public void resetAngleToAbsolute() {
    double angle = m_turnCANcoder.getAbsolutePosition() - m_turnEncoderOffset;
    m_turnEncoder.setPosition(angle);
  }

  public double getTurnAngleRads() {

    return m_turnEncoder.getPosition();

  }

  public void turnMotorMove(double speed) {

    m_turnMotor.setVoltage(speed * RobotController.getBatteryVoltage());
  }

  public double getDriveVelocity() {

    return m_driveEncoder.getVelocity();
  }

  public double getDrivePosition() {

    return m_driveEncoder.getPosition();

  }

  public double getDriveCurrent() {
    return m_driveMotor.getOutputCurrent();
  }

  public double getTurnVelocity() {
    return m_turnEncoder.getVelocity();
  }

  public double getTurnCurrent() {
    return m_turnMotor.getOutputCurrent();
  }

  public boolean turnInPosition(double targetAngle) {
    return Math.abs(targetAngle - getTurnAngleRads()) < toleranceRads;
  }

  public boolean turnIsStopped() {

    return Math.abs(m_turnEncoder.getVelocity()) < tolRadPerSec;
  }

  public boolean checkCAN() {

    driveMotorConnected = m_driveMotor.getFirmwareVersion() != 0;
    turnMotorConnected = m_turnMotor.getFirmwareVersion() != 0;
    turnCoderConnected = m_turnCANcoder.getFirmwareVersion() > 0;

    return driveMotorConnected && turnMotorConnected && turnCoderConnected;

  }

  public void stop() {
    m_driveMotor.set(0);
    m_turnMotor.set(0);
  }

}