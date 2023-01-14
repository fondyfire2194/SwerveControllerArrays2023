package frc.robot.utils.limelight;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.limelight.LimeLightControlMode.CamMode;
import frc.robot.utils.limelight.LimeLightControlMode.LedMode;
import frc.robot.utils.limelight.LimeLightControlMode.StreamType;

/**
 * Lime Light Class was started by Corey Applegate of Team 3244
 * Granite City Gearheads. We Hope you Enjoy the Lime Light
 * Camera.
 */
public class LimeLight extends SubsystemBase {
    /**
     * This class contains the camera constructor and the following
     * 
     * v Whether the limelight has any valid targets (0 or 1)
     * tx Horizontal Offset From Crosshair To Target (LL1: -27 degrees to 27 degrees
     * | LL2: -29.8 to 29.8 degrees)
     * ty Vertical Offset From Crosshair To Target (LL1: -20.5 degrees to 20.5
     * degrees | LL2: -24.85 to 24.85 degrees)
     * ta Target Area (0% of image to 100% of image)
     * ts Skew or rotation (-90 degrees to 0 degrees)
     * tl The pipeline’s latency contribution (ms) Add at least 11ms for image
     * capture latency.
     * tshort Sidelength of shortest side of the fitted bounding box (pixels)
     * tlong Sidelength of longest side of the fitted bounding box (pixels)
     * thor Horizontal sidelength of the rough bounding box (0 - 320 pixels)
     * tvert Vertical sidelength of the rough bounding box (0 - 320 pixels)
     * getpipe True active pipeline index of the camera (0 .. 9)
     * tid ID of primary AprilTag
     * camtran camera Camera transform in target space of primary apriltag or
     * solvepnp target.
     * json Full JSON dump of targeting results
     * botpose Robot transform in field-space. Translation (X,Y,Z) Rotation(X,Y,Z)
     * tclass Class ID of primary neural detector result or neural classifier result
     * 
     * ledMode Sets limelight’s LED state
     * 0 use the LED Mode set in the current pipeline
     * 1 force off
     * 2 force blink
     * 3 force on
     * camMode Sets limelight’s operation mode
     * 0 Vision processor
     * 1 Driver Camera (Increases exposure, disables vision processing)
     * pipeline Sets limelight’s current pipeline
     * 0 .. 9 Select pipeline 0..9
     * stream Sets limelight’s streaming mode
     * 0 Standard - Side-by-side streams if a webcam is attached to Limelight
     * 1 PiP Main - The secondary camera stream is placed in the lower-right corner
     * of the primary camera stream
     * 2 PiP Secondary - The primary camera stream is placed in the lower-right
     * corner of the secondary camera stream
     * snapshot Allows users to take snapshots during a match
     * 0 Reset snapshot mode
     * 1 Take exactly one snapshot
     * 
     */

    /**
     * See the LimeLightADv class for remaining
     * 
     * 
     * 
     * 
     */

    private NetworkTable m_table;
    private String m_tableName;

    private int lpctr = 0;
    private int lpctr_last = 1;
    private boolean connected = false;
    private double snapshotStartTime;
    private double snapshotTakeTime = .02;
    private double snapshotRepeatTime = .05;
    private int snapshotCounter;

    private Translation3d tran3d = new Translation3d();
    private Rotation3d r3d = new Rotation3d();
    private Pose3d p3d = new Pose3d();
   
    public LimeLightADV adv;
  

    /**
     * Using the Default Lime Light NT table
     */
    public LimeLight() {
        m_tableName = "limelight";
        m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
        adv = new LimeLightADV(m_table);
    }

    /**
     * If you changed the name of your Lime Light tell Me the New Name
     */
    public LimeLight(String tableName) {
        m_tableName = tableName;
        m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
        adv = new LimeLightADV(m_table);
    }

    /**
     * Send an instance of the NetworkTabe
     */
    public LimeLight(NetworkTable table) {
        m_table = table;
        adv = new LimeLightADV(m_table);

    }

    public void LimeLightInit() {
        // testAllTab();
    }

    private void testAllTab() {
        ShuffleboardTab LimeLightTab = Shuffleboard.getTab(m_tableName);
        // To Do
        // populate tab with all the data

    }

    @Override
    public void periodic() {

        NetworkTableEntry ab = m_table.getEntry("tv");

        connected = ab.exists();

    }

    public boolean isConnected() {

        return connected;

    }

    /**
     * tv Whether the limelight has any valid targets (0 or 1)
     * 
     * @return
     */
    public boolean getIsTargetFound() {
        NetworkTableEntry tv = m_table.getEntry("tv");
        boolean v = tv.getBoolean(false);
        return v;
    }

    /**
     * tx Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
     * 
     * @return
     */
    public double getdegRotationToTarget() {
        NetworkTableEntry tx = m_table.getEntry("tx");
        double x = tx.getDouble(0.0);
        return x;
    }

    /**
     * ty Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
     * 
     * @return
     */
    public double getdegVerticalToTarget() {
        NetworkTableEntry ty = m_table.getEntry("ty");
        double y = ty.getDouble(0.0);
        return y;
    }

    /**
     * ta Target Area (0% of image to 100% of image)
     * 
     * @return
     */
    public double getTargetArea() {
        NetworkTableEntry ta = m_table.getEntry("ta");
        double a = ta.getDouble(0.0);
        return a;
    }

    /**
     * ts Skew or rotation (-90 degrees to 0 degrees)
     * 
     * @return
     */
    public double getSkew_Rotation() {
        NetworkTableEntry ts = m_table.getEntry("ts");
        double s = ts.getDouble(0.0);
        return s;
    }

    /**
     * tl The pipeline’s latency contribution (ms) Add at least 11ms for image
     * capture latency.
     * 
     * @return
     */
    public double getPipelineLatency() {
        NetworkTableEntry tl = m_table.getEntry("tl");
        double l = tl.getDouble(0.0);
        return l;
    }

    /**
     * tl Reset the pipeline’s latency contribution/**
     * tl The pipeline’s latency contribution (ms) Add at least 11ms for image
     * capture latency.
     * 
     * @return
     *
     * 
     * @return
     */

    private void resetPilelineLatency() {
        m_table.getEntry("tl").setValue(0.0);
    }
    // Setters

    /**
     * LedMode Sets limelight’s LED state
     * 
     * kon
     * koff
     * kblink
     * 
     * @param ledMode
     */
    public void setLEDMode(LedMode ledMode) {
        m_table.getEntry("ledMode").setValue(ledMode.ordinal());
    }

    /**
     * Returns current LED mode of the Lime Light
     * 
     * @return LedMode
     */
    // public LedMode getLEDMode() {
    // NetworkTableEntry ledMode = m_table.getEntry("ledMode");
    // double led = ledMode.getDouble(0.0);
    // LedMode mode = LedMode.getByValue(led);
    // return mode;
    // }

    /**
     * camMode Sets limelight’s operation mode
     * 
     * kvision
     * kdriver (Increases exposure, disables vision processing)
     * 
     * @param camMode
     */

    public void setCamMode(CamMode camMode) {
        m_table.getEntry("camMode").setValue(camMode.ordinal());
    }

    /**
     * Returns current Cam mode of the Lime Light
     * 
     * @return CamMode
     */
    public CamMode getCamMode() {
        NetworkTableEntry camMode = m_table.getEntry("camMode");
        double cam = camMode.getDouble(0.0);
        CamMode mode = CamMode.getByValue(cam);
        return mode;
    }

    /**
     * pipeline Sets limelight’s current pipeline
     * 
     * 0 . 9 Select pipeline 0.9
     * 
     * @param pipeline
     */

    public void setPipeline(int pipeline) {
        if (pipeline < 0) {
            pipeline = 0;
            throw new IllegalArgumentException("Pipeline can not be less than zero");
        } else if (pipeline > 9) {
            pipeline = 9;
            throw new IllegalArgumentException("Pipeline can not be greater than nine");
        }
        m_table.getEntry("pipeline").setValue(pipeline);
    }

    /**
     * Returns current Pipeline of the Lime Light
     * 
     * @return Pipelinge
     */
    public int getPipeline() {
        NetworkTableEntry pipeline = m_table.getEntry("pipeline");
        double pipe = pipeline.getInteger(0);
        return (int) pipe;
    }

    /**
     * Returns ID of the April Tag
     * 
     * @return Pipelinge
     */

    public int getAprilTagID() {
        NetworkTableEntry id = m_table.getEntry("tid");
        int value = (int) id.getDouble(0.0f);
        return value;
    }

    /**
     * Returns pose of the April Tag
     * 
     * @return Pose3d
     */

    public Pose3d getRobotPose()

    {

        double[] temp = { 0, 0, 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("botpose");

        double[] result = value.getDoubleArray(temp);
        if (result.length == 6) {

            tran3d = new Translation3d(result[0], result[1], result[2]);
            r3d = new Rotation3d(result[3], result[4], result[5]);
            p3d = new Pose3d(tran3d, r3d);
        } else
            p3d = new Pose3d();
        return p3d;
    }

    /**
     * camtran Camera transform in target space of primary apriltag or solvepnp
     * target.
     * NumberArray: Translation (x,y,z) Rotation(pitch,yaw,roll)
     * 
     * @return
     */
    public Pose3d getCamTran() {
        double[] temp = { 0, 0, 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("camtran");
        double[] result = value.getDoubleArray(temp);
        tran3d = new Translation3d(result[0], result[1], result[2]);
        r3d = new Rotation3d(result[3], result[4], result[5]);
        p3d = new Pose3d(tran3d, r3d);

        return p3d;

    }

    /**
     * Returns class of the detected object
     * 
     * @return Pipelinge
     */

    public int getNeuralClassID() {
        NetworkTableEntry id = m_table.getEntry("tclass");
        int value = (int) id.getDouble(0.0f);
        return value;
    }

    /**
     * stream Sets limelight’s streaming mode
     * 
     * kStandard - Side-by-side streams if a webcam is attached to Limelight
     * kPiPMain - The secondary camera stream is placed in the lower-right corner of
     * the primary camera stream
     * kPiPSecondary - The primary camera stream is placed in the lower-right corner
     * of the secondary camera stream
     * 
     * @param stream
     */
    public void setStream(StreamType stream) {
        m_table.getEntry("stream").setValue(stream.ordinal());
    }

    public StreamType getStream() {
        NetworkTableEntry stream = m_table.getEntry("stream");
        double st = stream.getDouble(0.0);
        StreamType mode = StreamType.getByValue(st);
        return mode;
    }


    public void snap(int on) {
        NetworkTableEntry sn = m_table.getEntry("snapshot");
        sn.setValue(on);
    }

}
