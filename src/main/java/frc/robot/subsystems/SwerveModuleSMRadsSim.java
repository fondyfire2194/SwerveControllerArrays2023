// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.SYSIDConstants;
import frc.robot.utils.AngleUtils;

public class SwerveModuleSMRadsSim extends SubsystemBase {

  public final CANSparkMax m_driveMotor;

  public final CANSparkMax m_turnMotor;

  public final RelativeEncoder m_driveEncoder;

  private final RelativeEncoder m_turnEncoder;

  public final int m_locationIndex;

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
      SYSIDConstants.kaTurnVoltSecondsSquaredPerRadian);

  private double m_lastAngle;
  public double angle;

  public double m_turnEncoderOffset;

  private int tuneOn;

  private double tolRadPerSec = .0001;
  private double toleranceRads = .005;
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
  public SwerveModuleSMRadsSim(
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

    m_driveMotor.enableVoltageCompensation(ModuleConstants.kVoltCompensation);

    m_turnMotor.enableVoltageCompensation(ModuleConstants.kVoltCompensation);

    m_driveMotor.setInverted(driveMotorReversed);

    m_turnMotor.setInverted(turningMotorReversed);

    // Set neutral mode to brake
    m_driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    // Set neutral mode to brake
    m_turnMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    m_driveEncoder = m_driveMotor.getEncoder();

    m_turnEncoder = m_turnMotor.getEncoder();

    m_turnEncoder.setPositionConversionFactor(ModuleConstants.kTurningRadiansPerEncoderRev);

    m_turnEncoder.setVelocityConversionFactor(ModuleConstants.kTurningRadiansPerEncoderRev / 60);

    if (showOnShuffleboard) {

      // ShuffleboardContent.initDriveShuffleboard(this);
      // ShuffleboardContent.initTurnShuffleboard(this);
      // ShuffleboardContent.initCANCoderShuffleboard(this);
      // ShuffleboardContent.initBooleanShuffleboard(this);
      // ShuffleboardContent.initCoderBooleanShuffleboard(this);
    }

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

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

    REVPhysicsSim.getInstance().run();
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

  public double getTurnAngleRads() {

    return m_turnEncoder.getPosition() * ModuleConstants.kTurningRadiansPerEncoderRev;
  }

  public void turnMotorMove(double speed) {

    m_turnMotor.setVoltage(speed * RobotController.getBatteryVoltage());
  }

  public void positionTurn(double angle) {

    double turnAngleError = angle - getTurnAngleRads();

    turnAngleError *= 15;

    m_turnSimPidController.setReference(turnAngleError, ControlType.kVelocity);

  }

  public void driveMotorMove(double speed) {

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

  public double getDriveVelocity() {

    // return is in revs per minute so divide by 60 and multiply by meters per rev
    return (m_driveEncoder.getVelocity() * ModuleConstants.kDriveMetersPerEncRev) / 60;
  }

  public double getDrivePosition() {

    // count is in encoder revs in simulation
    return m_driveEncoder.getPosition() / ModuleConstants.kEncoderRevsPerMeter;

  }

  public double getDriveCurrent() {
    return m_driveMotor.getOutputCurrent();
  }

  public double getTurnVelocity() {
    // convert rpm to degrees per sec
    return (m_turnEncoder.getVelocity() * ModuleConstants.kTurningDegreesPerEncRev) / 60;
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

    return true;

  }

  public void stop() {
    m_driveMotor.set(0);
    m_turnMotor.set(0);
  }

}