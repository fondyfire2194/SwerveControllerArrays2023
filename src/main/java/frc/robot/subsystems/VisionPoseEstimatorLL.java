package frc.robot.subsystems;

import java.io.IOException;
import java.util.HashMap;

import org.photonvision.PhotonCamera;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Vision.Limelight.TargetThread1LL;
import frc.robot.oi.LimeLight;
import frc.robot.oi.LimeLightReflective.LedMode;

public class VisionPoseEstimatorLL extends SubsystemBase {

  public DriveSubsystem m_drive;

  public AprilTagFieldLayout m_fieldLayout;

  TargetThread1LL tgtTh1ll;

  private String camera1Name = "limelight-sixteen";// 10.21.94.15

  public LimeLight m_cam;

  private final int targetsInFle = 24;

  private boolean fieldFileRead;

  public HashMap<Integer, String> pipelines = new HashMap<Integer, String>();

  private Pose3d visionPoseEstimatedData;

  public VisionPoseEstimatorLL(DriveSubsystem drive) {

    PhotonCamera.setVersionCheckEnabled(false);

    pipelines.put(0, "ReflectiveTape");
    pipelines.put(1, "AprilTag16h");
    pipelines.put(2, "Triangle");

    m_drive = drive;

    m_cam = new LimeLight(camera1Name);

    String fieldFile = AprilTagFields.k2023ChargedUp.m_resourceFile;

    try {

      m_fieldLayout = new AprilTagFieldLayout(fieldFile);

    } catch (IOException e) {

      fieldFileRead = false;

      e.printStackTrace();

    }

    tgtTh1ll = new TargetThread1LL(this, m_cam);

  }

  @Override
  public void periodic() {

  }

  public void setVisionPoseEsitmatedData(Pose3d pose) {

    visionPoseEstimatedData = pose;

  }

  public Pose3d getVisionPoseEstimatedData() {

    return visionPoseEstimatedData;
  }
}