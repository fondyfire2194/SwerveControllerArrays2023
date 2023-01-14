package frc.robot.utils.limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.utils.limelight.LimeLightControlMode.Advanced_Crosshair;
import frc.robot.utils.limelight.LimeLightControlMode.Advanced_Target;

/**
 * Lime Light Avvanced
 * use .adv.instruction
 * crop Sets the crop rectangle. The pipeline must utilize the default crop
 * rectangle in the web interface. The array must have exactly 4 entries.
 * [0] X0 - Min or Max X value of crop rectangle (-1 to 1)
 * [1] X1 - Min or Max X value of crop rectangle (-1 to 1)
 * [2] Y0 - Min or Max Y value of crop rectangle (-1 to 1)
 * [3] Y1 - Min or Max Y value of crop rectangle (-1 to 1)
 * 
 * 
 * Enable “send contours” in the “Output” tab to stream corner coordinates:
 * 
 * tcornxy Number array of corner coordinates [x0,y0,x1,y1……]
 * 
 * tx0 Raw Screenspace X
 * ty0 Raw Screenspace Y
 * ta0 Area (0% of image to 100% of image)
 * ts0 Skew or rotation (-90 degrees to 0 degrees)
 * tx1 Raw Screenspace X
 * ty1 Raw Screenspace Y
 * ta1 Area (0% of image to 100% of image)
 * ts1 Skew or rotation (-90 degrees to 0 degrees)
 * tx2 Raw Screenspace X
 * ty2 Raw Screenspace Y
 * ta2 Area (0% of image to 100% of image)
 * ts2 Skew or rotation (-90 degrees to 0 degrees)
 * 
 * 
 * cx0 Crosshair A X in normalized screen space
 * cy0 Crosshair A Y in normalized screen space
 * cx1 Crosshair B X in normalized screen space
 * cy1 Crosshair B Y in normalized screen space
 * 
 */
public class LimeLightADV {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private NetworkTable m_table;

    /**
     * Send an instance of the NetworkTabe
     */
    public LimeLightADV(NetworkTable table) {
        m_table = table;
        // ToDo
        // m_tableName = get the name of the NT key.

    }

    public boolean rawContoursOn() {
        return m_table.getEntry("tx0").exists();
    }


    public boolean rawCornersOn() {
        return m_table.getEntry("tcornxy").exists();
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

    public double[] getAveXHairColor() {
        double[] temp = { 0, 0, 0 };
        NetworkTableEntry value = m_table.getEntry("tc");
        double[] hsv = value.getDoubleArray(temp);
        return hsv;

    }
    // *************** Advanced Usage with Raw Contours *********************

    /**
     * Limelight posts three raw contours to NetworkTables that are not influenced
     * by your grouping mode.
     * That is, they are filtered with your pipeline parameters, but never grouped.
     * X and Y are returned
     * in normalized screen space (-1 to 1) rather than degrees. *
     */

    public double getAdvanced_RotationToTarget(Advanced_Target raw) {
        NetworkTableEntry txRaw = m_table.getEntry("tx" + Integer.toString(raw.getValue()));
        double x = txRaw.getDouble(0.0);
        return x;
    }

    public double getAdvanced_degVerticalToTarget(Advanced_Target raw) {
        NetworkTableEntry tyRaw = m_table.getEntry("ty" + Integer.toString(raw.getValue()));
        double y = tyRaw.getDouble(0.0);
        return y;
    }

    public double getAdvanced_TargetArea(Advanced_Target raw) {
        NetworkTableEntry taRaw = m_table.getEntry("ta" + Integer.toString(raw.getValue()));
        double a = taRaw.getDouble(0.0);
        return a;
    }

    public double getAdvanced_Skew_Rotation(Advanced_Target raw) {
        NetworkTableEntry tsRaw = m_table.getEntry("ts" + Integer.toString(raw.getValue()));
        double s = tsRaw.getDouble(0.0);
        return s;
    }

    // Raw Crosshairs:
    // If you are using raw targeting data, you can still utilize your calibrated
    // crosshairs:

    public double[] getAdvanced_RawCrosshair(Advanced_Crosshair raw) {
        double[] crosshairs = new double[2];
        crosshairs[0] = getAdvanced_RawCrosshair_X(raw);
        crosshairs[1] = getAdvanced_RawCrosshair_Y(raw);
        return crosshairs;
    }

    public double getAdvanced_RawCrosshair_X(Advanced_Crosshair raw) {
        NetworkTableEntry cxRaw = m_table.getEntry("cx" + Integer.toString(raw.getValue()));
        double x = cxRaw.getDouble(0.0);
        return x;
    }

    public double getAdvanced_RawCrosshair_Y(Advanced_Crosshair raw) {
        NetworkTableEntry cyRaw = m_table.getEntry("cy" + Integer.toString(raw.getValue()));
        double y = cyRaw.getDouble(0.0);
        return y;
    }

    /**
     * Combined bounding box width
     * 
     * @return
     */
    public double getBoundingBoxWidth() {
        NetworkTableEntry thor = m_table.getEntry("thor");
        double t = thor.getDouble(0.0);
        return t;
    }

    /**
     * Combined bounding box height
     * 
     * @return
     */
    public double getBoundingBoxHeight() {
        NetworkTableEntry tvert = m_table.getEntry("tvert");
        double t = tvert.getDouble(0.0);
        return t;
    }

    /**
     * tshort Shortest side of fitted bounding box
     * 
     * @return
     */
    public double getBoundingBoxShortestSide() {
        NetworkTableEntry tsh = m_table.getEntry("tshort");
        double sh = tsh.getDouble(0.0);
        return sh;
    }

    /**
     * tlong Longest side of fitted bounding box
     * 
     * @return
     */
    public double getBoundingBoxLongestSide() {
        NetworkTableEntry tlng = m_table.getEntry("tlong");
        double l = tlng.getDouble(0.0);
        return l;
    }

    /**
     * Following are only available if advance targeting is turned on
     * 
     * @return
     */

    public double[] getCorners() {
        double[] temp = { 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("cornxy");
        double[] result = value.getDoubleArray(temp);
        return result;

    }

}
