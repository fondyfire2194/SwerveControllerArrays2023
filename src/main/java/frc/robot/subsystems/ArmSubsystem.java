// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SolenoidConstants;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */

  public final PWMSparkMax m_turnArmMotor;

  public final PWMSparkMax m_extendArmMotor;

  public final Encoder m_turnArmEncoder = new Encoder(1, 2);

  private final Encoder m_extendEncoder = new Encoder(3, 4);

  private final DoubleSolenoid gripperOpenClose = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
      SolenoidConstants.GRIPPER_CLOSE, SolenoidConstants.GRIPPER_OPEN);;

  private final DoubleSolenoid gripperTilt = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
      SolenoidConstants.GRIPPER_LOWER, SolenoidConstants.GRIPPER_RAISE);

  public ArmSubsystem() {

    m_extendArmMotor = new PWMSparkMax(1);

    m_turnArmMotor = new PWMSparkMax(2);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void positionTurnArm(double angle) {

  }

  public void positionExtendArm(double angle) {

  }

  public void openGripper() {

  }

  public void closeGripper() {

  }

  public void raiseGripper() {

  }

  public void lowerGripper() {

  }
}
