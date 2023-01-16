package frc.robot.oi;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

/**
 * Lime Light Class was started by Corey Applegate of Team 3244
 * Granite City Gearheads. We Hope you Enjoy the Lime Light
 * Camera.
 */
public class LimeLight {
    /**
     * getpipe True active pipeline index of the camera (0 .. 9)
     * tid ID of primary AprilTag
     * camtran camera Camera transform in target space of primary apriltag or
     * solvepnp target.ledmode
     * json Full JSON dump of targeting results
     * botpose Robot transform in field-space. Translation (X,Y,Z) Rotation(X,Y,Z)
     * tclass Class ID of primary neural detector result or neural classifier result
     * 
     * 
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

    private boolean connected = false;

    private Translation3d tl3d = new Translation3d();
    private Rotation3d r3d = new Rotation3d();
    private Transform3d tf3d = new Transform3d();
    private Pose3d p3d;
    private CamMode currentCamMode = CamMode.kdriver;

    private StreamType currentStreamType = StreamType.kStandard;

    public LimeLightReflective ref;

    private double _heartBeatPeriod = 0.5;

    private double latencyLast = 0;

    public enum CamMode {
        kvision,
        kdriver;
    }

    public enum StreamType {
        kStandard,
        kPiPMain,
        kPiPSecondary;
    }

    class PeriodicRunnable implements java.lang.Runnable {

        private int testCount;
        private int testCountLimitCheck = 3;
        private double tempLatency;

        public void run() {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            tempLatency = getPipelineLatency();

            if (tempLatency == latencyLast) {
                testCount++;
            } else {
                latencyLast = tempLatency;
                testCount = 0;
                connected = true;
            }

            if (testCount > testCountLimitCheck) {
                connected = false;
            }

        }

    }

    Notifier _heartBeat = new Notifier(new PeriodicRunnable());

    /**
     * Using the Default Lime Light NT table
     */
    public LimeLight() {
        m_tableName = "limelight";
        m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
        ref = new LimeLightReflective(m_table);
        _heartBeat.startPeriodic(_heartBeatPeriod);
    }

    /**
     * If you changed the name of your Lime Light tell Me the New Name
     */
    public LimeLight(String tableName) {
        m_tableName = tableName;
        m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
        ref = new LimeLightReflective(m_table);
        _heartBeat.startPeriodic(_heartBeatPeriod);
    }

    /**
     * Send an instance of the NetworkTabe
     */
    public LimeLight(NetworkTable table) {
        m_table = table;
        ref = new LimeLightReflective(m_table);
        _heartBeat.startPeriodic(_heartBeatPeriod);
    }

    public String getName() {
        return m_tableName;
    }

    public boolean isConnected() {

        return connected;

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

    public void resetPipelelineLatency() {
        m_table.getEntry("tl").setValue(0.0);
    }
    // Setters

    /**
     * camMode Sets limelight’s operation mode
     * 
     * kvision
     * kdriver (Increases exposure, disables vision processing)
     * 
     * @param camMode
     */

    public void setCamMode(CamMode camMode) {
        currentCamMode = camMode;

        m_table.getEntry("camMode").setValue(camMode.ordinal());
    }

    // /**
    // * Returns current Cam mode of the Lime Light
    // *
    // * @return CamMode
    // */
    public CamMode getCamMode() {
        int cmode = (int) m_table.getEntry("camMode").getDouble(0.);
        switch (cmode) {
            case 0:
                currentCamMode = CamMode.kvision;
                break;
            case 1:
                currentCamMode = CamMode.kdriver;
                break;
            default:
                currentCamMode = CamMode.kvision;
                break;
        }
        return currentCamMode;
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
     * @return Transform3d
     */

    public Transform3d getRobotTransform()

    {

        double[] temp = { 0, 0, 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("botpose");

        double[] result = value.getDoubleArray(temp);

        if (result.length == 6) {

            tl3d = new Translation3d(result[0], result[1], result[2]);
            r3d = new Rotation3d(result[3], result[4], result[5]);
            tf3d = new Transform3d(tl3d, r3d);
        } else
            tf3d = new Transform3d();
        return tf3d;
    }

    /**
     * camtran Camera transform in target space of primary apriltag or solvepnp
     * target.
     * NumberArray: Translation (x,y,z) Rotation(pitch,yaw,roll)
     * 
     * @return
     */
    public Transform3d getCamTran() {
        double[] temp = { 0, 0, 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("camtran");
        double[] result = value.getDoubleArray(temp);
        tl3d = new Translation3d(result[0], result[1], result[2]);
        r3d = new Rotation3d(result[3], result[4], result[5]);
        tf3d = new Transform3d(tl3d, r3d);

        return tf3d;

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

    public double[] getAveCrossHairColor() {
        double[] temp = { 0, 0, 0 };
        NetworkTableEntry value = m_table.getEntry("tc");
        double[] hsv = value.getDoubleArray(temp);
        return hsv;

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
    public void setStream(StreamType streamType) {
        currentStreamType = streamType;
        m_table.getEntry("stream").setValue(streamType.ordinal());
    }

    public StreamType getStream() {
        int smode = (int) m_table.getEntry("stream").getDouble(0.);
        switch (smode) {
            case 0:
                currentStreamType = StreamType.kStandard;
                break;
            case 1:
                currentStreamType = StreamType.kPiPMain;
                break;
            case 2:
                currentStreamType = StreamType.kPiPSecondary;
                break;
            default:
                currentStreamType = StreamType.kStandard;
                break;
        }

        return currentStreamType;
    }

    /**
     * 
     * @param sizes
     * 
     *              * crop sets the 4 sides of the crop rectange
     * 
     *              returns fail if parameter array size is not 4
     * 
     *              returns fail if crop values outside range +1 to -1
     * 
     * 
     * @return fail
     */

    public boolean setCropRectangle(double[] sizes) {
        boolean fail = false;
        fail = sizes.length != 4;
        if (!fail) {
            for (int i = 0; i < 4; i++) {
                fail = sizes[i] > 1 || sizes[i] < -1;
                if (fail)
                    break;
            }
            if (!fail)

                m_table.getEntry("crop").setDoubleArray(sizes);

        }
        return fail;
    }

    public void takeSnapshot(int on) {
        NetworkTableEntry sn = m_table.getEntry("snapshot");
        sn.setValue(on);
    }

    public Command GetSnapShot() {
        return new SequentialCommandGroup(new InstantCommand(() -> takeSnapshot(1)),
                new WaitCommand(.1),
                new InstantCommand(() -> takeSnapshot(0)));
    }

    public Command ChangeStreamType(StreamType type) {
        return new InstantCommand(() -> setStream(type));
    }

    public Command ChangePipeline(int n) {
        return new InstantCommand(() -> setPipeline(n)); // 0-9
    }

    public Command ToggleCamMode() {

        return new ConditionalCommand(
                new InstantCommand(() -> setCamMode(CamMode.kdriver)),
                new InstantCommand(() -> setCamMode(CamMode.kvision)),
                () -> (getCamMode() == CamMode.kvision));
    }

    public Command ChangeCropRectangle(double[] sizes) {

        return new InstantCommand(() -> setCropRectangle(sizes));
    }
}
