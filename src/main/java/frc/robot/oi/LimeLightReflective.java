package frc.robot.oi;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * /**
     * This class contains reflective mode operations
     * 
     * tv Whether the limelight has any valid targets (0 or 1)
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
 */

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
public class LimeLightReflective {

    public enum LedMode {
        kpipeLine, // 0 use the LED Mode set in the current pipeline
        kforceOff, // 1 force off
        kforceBlink, // 2 force blink
        kforceOn; // 3 force on

    }

    private LedMode currentLedMode = LedMode.kpipeLine;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private NetworkTable m_table;

    /**
     * Send an instance of the NetworkTabe
     */
    public LimeLightReflective(NetworkTable table) {
        m_table = table;
        // ToDo
        // m_tableName = get the name of the NT key.

    }

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
        currentLedMode = ledMode;
        m_table.getEntry("ledMode").setValue(ledMode.ordinal());
    }

    /**
     * Returns current LED mode of the Lime Light
     * 
     * @return LedMode
     */
    public LedMode getLEDMode() {
        int lmode = (int) m_table.getEntry("ledMode").getDouble(0.);
        switch (lmode) {
            case 0:
                currentLedMode = LedMode.kpipeLine;
                break;
            case 1:
                currentLedMode = LedMode.kforceOff;
                break;
            case 2:
                currentLedMode = LedMode.kforceBlink;
                break;
            case 3:
                currentLedMode = LedMode.kforceOn;
                break;
            default:
                currentLedMode = LedMode.kpipeLine;
                return currentLedMode;

        }

        return currentLedMode;
    }

    /**
     * tv Whether the limelight has any valid targets (0 or 1)
     * 
     * @return
     */
    public boolean getIsTargetFound() {
        NetworkTableEntry tv = m_table.getEntry("tv");
        double v = tv.getDouble(0.);

        return v==1.0;
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


   
    // *************** Advanced Usage with Raw Contours *********************

    /**
     * Limelight posts three raw contours to NetworkTables that are not influenced
     * by your grouping mode.
     * That is, they are filtered with your pipeline parameters, but never grouped.
     * X and Y are returned
     * in normalized screen space (-1 to 1) rather than degrees. *
     */

    public double getAdvanced_RotationToTarget(int n) {
        String nt = "tx" + String.valueOf(n);
        NetworkTableEntry txRaw = m_table.getEntry(nt);
        double x = txRaw.getDouble(0.0);
        return x;
    }

    public double getAdvanced_degVerticalToTarget(int n) {
        String nt = "ty" + String.valueOf(n);
        NetworkTableEntry tyRaw = m_table.getEntry(nt);
        double y = tyRaw.getDouble(0.0);
        return y;
    }

    public double getAdvanced_TargetArea(int n) {
        String nt = "ta" + String.valueOf(n);
        NetworkTableEntry taRaw = m_table.getEntry(nt);
        double a = taRaw.getDouble(0.0);
        return a;
    }

    public double getAdvanced_Skew_Rotation(int n) {
        String nt = "ts" + String.valueOf(n);
        NetworkTableEntry tsRaw = m_table.getEntry(nt);
        double s = tsRaw.getDouble(0.0);
        return s;
    }

    // Raw Crosshairs:
    // If you are using raw targeting data, you can still utilize your calibrated
    // crosshairs:

    public double getAdvanced_RawCrosshair_X(int n) {
        String nt = "cx" + String.valueOf(n);
        NetworkTableEntry cxRaw = m_table.getEntry(nt);
        double x = cxRaw.getDouble(0.0);
        return x;
    }

    public double getAdvanced_RawCrosshair_Y(int n) {
        String nt = "cy" + String.valueOf(n);
        NetworkTableEntry cyRaw = m_table.getEntry(nt);
        double y = cyRaw.getDouble(0.0);
        return y;
    }

  
    /**
     * Is only available if send corners is turned on
     * 
     * @return
     */


    public double[] getCorners() {

        double[] temp = { 0, 0, 0, 0 };// default for getEntry
        NetworkTableEntry value = m_table.getEntry("cornxy");
        double[] result = value.getDoubleArray(temp);
        if (result.length == 4)
            return result;
        else
            return temp;

    }

    public Command ChangeLEDMode(LedMode mode) {
        return new InstantCommand(() -> setLEDMode(mode));
    }

}
