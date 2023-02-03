// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision.Limelight;

import edu.wpi.first.math.util.Units;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightVision;

public class TransferVisionDataToDrive extends CommandBase {
  /** Creates a new TransferVisionDataToDrive. */
  private LimelightVision m_llv;
  private DriveSubsystem m_drive;

  public TransferVisionDataToDrive(LimelightVision llv, DriveSubsystem drive) {
    m_llv = llv;
    m_drive = drive;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (m_llv.isTargetFound(m_llv.cam_tag_15) && m_llv.getAprilTagID(m_llv.cam_tag_15) != -1) {

      m_llv.curTagX = m_llv.getCurrentTagPose(m_llv.cam_tag_15).getX();

      m_llv.curTagY = m_llv.getCurrentTagPose(m_llv.cam_tag_15).getY();

      m_llv.curTagDeg = Units
          .radiansToDegrees(m_llv.getCurrentTagPose(m_llv.cam_tag_15).getRotation().getAngle());

      m_llv.distToTagX = m_llv.curTagX - m_llv.getRobotPose_FS_WPIBlue(m_llv.cam_tag_15).getX();

      m_llv.distToTagY = m_llv.curTagY - m_llv.getRobotPose_FS_WPIBlue(m_llv.cam_tag_15).getY();

      m_llv.distToTag = Math.hypot(m_llv.distToTagX, m_llv.distToTagY);

      m_llv.degToTag = m_llv.curTagDeg - m_llv.getRobotPose_FS_WPIBlue(m_llv.cam_tag_15).getRotation().getAngle();

      m_llv.visPosX = m_llv.visionPoseEstimatedData.getX();

      m_llv.visPosY = m_llv.visionPoseEstimatedData.getY();

      m_llv.visPosDeg = m_llv.visionPoseEstimatedData.getRotation().getDegrees();

      m_llv.robX = m_drive.getEstimatedPose().getX();

      m_llv.robY = m_drive.getEstimatedPose().getY();

      m_llv.robDeg = m_drive.getEstimatedPose().getRotation().getDegrees();

      m_llv.robDiffX = m_llv.visPosX - m_llv.robX;
      m_llv.robDiffY = m_llv.visPosY - m_llv.robY;
      m_llv.robDiffDeg = m_llv.visPosDeg - m_llv.robDeg;



      m_drive.visionDataAvailable = true;// m_llv.distToTag < .5;

      m_drive.getVisionCorrection(m_llv.visionPoseEstimatedData, m_llv.imageCaptureTime);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
