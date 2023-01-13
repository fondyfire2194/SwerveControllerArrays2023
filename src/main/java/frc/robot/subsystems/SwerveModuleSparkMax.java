// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.CTRECanCoder;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.TrapezoidConstants;
import frc.robot.Pref;
import frc.robot.utils.AngleUtils;
import frc.robot.utils.ShuffleboardContent;

public class SwerveModuleSparkMax extends SubsystemBase {

  public final CANSparkMax m_driveMotor;

  public final CANSparkMax m_turnMotor;

  public final RelativeEncoder m_driveEncoder;

  private final RelativeEncoder m_turnEncoder;

  public final int m_locationIndex;

  private final PIDController m_driveVelController = new PIDController(.0005, 0, 0);

  // private ProfiledPIDController m_turnPosController = new ProfiledPIDController(.006, 0, 0,
  //     TrapezoidConstants.kThetaControllerConstraints);
  private PIDController m_turnPosController = new PIDController(.006, 0, 0);

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
      ModuleConstants.ksVolts,
      ModuleConstants.kvVoltSecondsPerMeter,
      ModuleConstants.kaVoltSecondsSquaredPerMeter);

  private double m_lastAngle;
  public double angle;

  public double m_turnEncoderOffset;

  private int tuneOn;
  // public double actualAngleDegrees;

  private double tolDegPerSec = .05;
  private double toleranceDeg = .25;
  public boolean driveMotorConnected;
  public boolean turnMotorConnected;
  public boolean turnCoderConnected;
  private double turnDeadband = .5;
  private boolean showOnShuffleboard = true;
  private boolean driveBrakeMode;
  private boolean turnBrakeMode;
  public SendableBuilder m_builder;
  private boolean m_isOpenLoop;

  private SparkMaxPIDController m_driveSimPidController;

  private SparkMaxPIDController m_turnSimPidController;

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
  public SwerveModuleSparkMax(
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

    m_turnMotor.setSmartCurrentLimit(20);

    m_driveMotor.setSmartCurrentLimit(20);

    m_driveMotor.enableVoltageCompensation(DriveConstants.kVoltCompensation);

    m_turnMotor.enableVoltageCompensation(DriveConstants.kVoltCompensation);

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

    if (RobotBase.isReal()) {

      m_driveEncoder.setPositionConversionFactor(ModuleConstants.kDriveMetersPerEncRev);

      m_driveEncoder.setVelocityConversionFactor(ModuleConstants.kDriveMetersPerEncRev
          / 60);
    }

    m_turnEncoder = m_turnMotor.getEncoder();

    m_turnEncoder.setPositionConversionFactor(ModuleConstants.kTurningDegreesPerEncRev);

    m_turnEncoder.setVelocityConversionFactor(ModuleConstants.kTurningDegreesPerEncRev / 60);

    if (RobotBase.isReal()) {
      tunePosGains();
      tuneDriveVelGains();
    }
  

    m_turnPosController.enableContinuousInput(-180, 180);

    checkCAN();

    if (RobotBase.isReal())

      resetAngleToAbsolute();

    if (showOnShuffleboard) {

      ShuffleboardContent.initDriveShuffleboard(this);
      ShuffleboardContent.initTurnShuffleboard(this);
      ShuffleboardContent.initCANCoderShuffleboard(this);
      ShuffleboardContent.initBooleanShuffleboard(this);
      ShuffleboardContent.initCoderBooleanShuffleboard(this);
    }

    if (RobotBase.isSimulation()) {

      REVPhysicsSim.getInstance().addSparkMax(m_driveMotor, 3, 5600);

      m_driveSimPidController = m_driveMotor.getPIDController();

      m_driveSimPidController.setP(1);

      m_driveEncoder.setPositionConversionFactor(1);

      m_driveEncoder.setVelocityConversionFactor(1);

      REVPhysicsSim.getInstance().addSparkMax(m_turnMotor, 3, 5600);

      m_turnSimPidController = m_turnMotor.getPIDController();

      m_turnSimPidController.setP(1);

      m_turnEncoder.setPositionConversionFactor(1);

      m_turnEncoder.setVelocityConversionFactor(1);
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

      if (DriverStation.isDisabled()) {
        if (turnBrakeMode)
          setTurnBrakeMode(false);

        if (driveBrakeMode)
          setDriveBrakeMode(false);
      }

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

  @Override
  public void simulationPeriodic() {
    REVPhysicsSim.getInstance().run();
  }

  public SwerveModuleState getState() {

    return new SwerveModuleState(getDriveVelocity(), Rotation2d.fromDegrees((getTurnAngle())));
  }

  public SwerveModulePosition getPosition() {

    double tempPos = getDrivePosition();
    Rotation2d tempAngle = getHeadingRotation2d();
    return new SwerveModulePosition(tempPos, tempAngle);

  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {

    state = AngleUtils.optimize(desiredState, getHeadingRotation2d());

    SmartDashboard.putNumber("StateSpeed", state.speedMetersPerSecond);

    SmartDashboard.putNumber("StateAngle", state.angle.getDegrees());

    // turn motor code
    // Prevent rotating module if speed is less then 1%. Prevents Jittering.
    angle = (Math.abs(state.speedMetersPerSecond) <= (DriveConstants.kMaxSpeedMetersPerSecond * 0.01))

        ? m_lastAngle

        : state.angle.getDegrees();

    m_lastAngle = angle;

    SmartDashboard.putNumber("TESTSP", state.speedMetersPerSecond);
    SmartDashboard.putNumber("TESTAng",angle);

    driveMotorMove(state.speedMetersPerSecond);

    // turn axis

    positionTurn(angle);

  }

  public static double limitMotorCmd(double motorCmdIn) {
    return Math.max(Math.min(motorCmdIn, 1.0), -1.0);
  }

  /** Zeroes all the SwerveModule encoders. */
  public void resetEncoders() {
    m_driveEncoder.setPosition(0);
    m_turnEncoder.setPosition(0);
  }

  public double getHeadingDegrees() {
    return getTurnAngle();
  }

  public Rotation2d getHeadingRotation2d() {
    return Rotation2d.fromDegrees(getHeadingDegrees());
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

  public double getTurnAngle() {

    if (RobotBase.isReal())

      return m_turnEncoder.getPosition();

    else

      return m_turnEncoder.getPosition() * ModuleConstants.kTurningDegreesPerEncRev;
  }

  public void turnMotorMove(double speed) {

    m_turnMotor.setVoltage(speed * RobotController.getBatteryVoltage());
  }

  public void positionTurn(double angle) {

    if (RobotBase.isReal()) {

      double pidOut = m_turnPosController.calculate(m_turnEncoder.getPosition(), angle);
      SmartDashboard.putNumber("PIDOUT", pidOut);
      double turnAngleError = Math.abs(angle - m_turnEncoder.getPosition());

      SmartDashboard.putNumber("ATAE", turnAngleError);

      // if robot is not moving, stop the turn motor oscillating
      // if (turnAngleError < turnDeadband

      //     && Math.abs(state.speedMetersPerSecond) <= (DriveConstants.kMaxSpeedMetersPerSecond * 0.01))

      //   pidOut = 0;

      m_turnMotor.setVoltage(pidOut * RobotController.getBatteryVoltage());

    }

    if (RobotBase.isSimulation()) {

      double turnAngleError = angle - getTurnAngle();

      turnAngleError *= 15;

      m_turnSimPidController.setReference(turnAngleError, ControlType.kVelocity);

    }

  }

  public void driveMotorMove(double speed) {

    if (RobotBase.isReal()) {

      double pidOut = 0;

      if (m_isOpenLoop) {

        m_driveMotor.set(speed / ModuleConstants.kFreeMetersPerSecond);

      } else {

        pidOut = m_driveVelController.calculate(getDriveVelocity(), speed);

        m_driveMotor.set(speed * 8 + pidOut);
      }
    }
    if (RobotBase.isSimulation()) {

      /**
       * speed is in meters per second
       * no position or velocity conversions so convert MPS to RPM
       * multiply speed by 60 and divide by kDriveMetersPerEncRev
       * 
       */
      double speedConv = 60 / ModuleConstants.kDriveMetersPerEncRev;

      SmartDashboard.putNumber("DSSPD", speed);
      SmartDashboard.putNumber("DSCONS", speedConv);

      m_driveSimPidController.setReference(speed * speedConv, ControlType.kVelocity);

    }

  }

  public double getDriveVelocity() {
    if (RobotBase.isReal())
      return m_driveEncoder.getVelocity();
    else
      // return is in revs per minute so divide by 60 and multiply by meters per rev
      return (m_driveEncoder.getVelocity() * ModuleConstants.kDriveMetersPerEncRev) / 60;
  }

  public double getDrivePosition() {
    if (RobotBase.isReal())
      return m_driveEncoder.getPosition();
    else {

      // count is in encoder revs in simulation
      return m_driveEncoder.getPosition() / ModuleConstants.kEncoderRevsPerMeter;

    }

  }

  public double getDriveCurrent() {
    return m_driveMotor.getOutputCurrent();
  }

  public double getTurnVelocity() {
    if (RobotBase.isReal())
      return m_turnEncoder.getVelocity();
    else
      // convert rpm to degrees per sec
      return (m_turnEncoder.getVelocity() * ModuleConstants.kTurningDegreesPerEncRev) / 60;
  }

  public double getTurnCurrent() {
    return m_turnMotor.getOutputCurrent();
  }

  public boolean turnInPosition(double targetAngle) {
    return Math.abs(targetAngle - getTurnAngle()) < toleranceDeg;
  }

  public boolean turnIsStopped() {

    return Math.abs(m_turnEncoder.getVelocity()) < tolDegPerSec;
  }

  public boolean checkCAN() {

    driveMotorConnected = m_driveMotor.getFirmwareVersion() != 0;
    turnMotorConnected = m_turnMotor.getFirmwareVersion() != 0;
    turnCoderConnected = m_turnCANcoder.getFirmwareVersion() > 0 || RobotBase.isSimulation();

    return driveMotorConnected && turnMotorConnected && turnCoderConnected;

  }

}