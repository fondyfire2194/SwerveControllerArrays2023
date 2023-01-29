// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class GameHandlerSubsystem extends SubsystemBase {

    private static GridDrop activeDrop = GridDrop.RIGHT_ONE_PIPE;
    /**
     * Grid as viewed by Driver starting on their right
     */

    public static int gridHeight = 0;// 0 floor, 1 mid, 2 high

    public static final double xDist = 1.25;

    public static boolean[] grid = new boolean[9];

    public static boolean setDone;

    public enum GridDrop {

        RIGHT_ONE_PIPE(xDist, 1, true),

        RIGHT_TWO_SHELF(xDist, 1.5, false),

        RIGHT_THREE_PIPE(xDist, 2, true),

        COOP_RIGHT_PIPE(xDist, 2.5, true),

        COOP_SHELF(xDist, 3, false),

        COOP_LEFT_PIPE(xDist, 3.5, true),

        LEFT_THREE_PIPE(xDist, 4, true),

        LEFT_TWO_SHELF(xDist, 4.5, false),

        LEFT_ONE_PIPE(xDist, 5, true);

        private final double yVal;

        private final double xVal;

        private final boolean isPipe;;

        private GridDrop(double xVal, double yVal, boolean isPipe) {
            this.yVal = yVal;
            this.xVal = xVal;
            this.isPipe = isPipe;
        }

        public double getyVal() {
            return yVal;
        }

        public double getXVal() {
            return xVal;
        }

        public boolean getIsPipe() {
            return isPipe;
        }
    }

    String[] pieceName = { "CONE", "CUBE" };
    String[] levelName = { "FLOOR", "MID", "HIGH" };

    public int selectedGrid;
    public int selectedPiece;
    public int selectedLevel;

    public GameHandlerSubsystem() {

    }

    public static void setActiveDrop(GridDrop drop) {
        activeDrop = drop;
    }

    public static void clearGrid() {
        for (int i = 0; i < grid.length; i++) {
            grid[i] = false;
        }
    }

    public static GridDrop getActiveDrop() {
        return activeDrop;
    }

    public static void setActiveDropNumber(int n) {
        grid[n] = true;
    }

    public static void setDropByNumber(int n) {
        setDone = false;
        switch (n) {
            case 0:
                activeDrop = GridDrop.RIGHT_ONE_PIPE;
                break;
            case 1:
                activeDrop = GridDrop.RIGHT_TWO_SHELF;
                break;
            case 2:
                setActiveDrop(GridDrop.RIGHT_THREE_PIPE);
                break;
            case 3:
                setActiveDrop(GridDrop.COOP_RIGHT_PIPE);
                break;
            case 4:
                setActiveDrop(GridDrop.COOP_SHELF);
                break;
            case 5:
                setActiveDrop(GridDrop.COOP_LEFT_PIPE);
                break;
            case 6:
                setActiveDrop(GridDrop.LEFT_THREE_PIPE);
                break;
            case 7:
                setActiveDrop(GridDrop.LEFT_TWO_SHELF);
                break;
            case 8:
                setActiveDrop(GridDrop.LEFT_ONE_PIPE);
                break;
            default:
                break;

        }
        setDone = true;
        SmartDashboard.putString("ADRP", activeDrop.toString());

    }

    @Override

    public void periodic() {
    }

}
