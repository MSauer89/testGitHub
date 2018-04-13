package schiffeversenken;

/**
 *
 * @author msauer2 Speichert Daten, die zu einem Punkt gehören: 1: Theoretischer
 * Platz, den ein gegnerisches Schiff auf dieser Stelle haben könnte
 * (Maschensystem) 2: Status
 *
 */
public class FieldNode {

    private int xCoord;
    private int yCoord;

    private int status;

    /**
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param status Space value/priority stored for determining the
     * space of opponent's ships
     *
     */
    public FieldNode(int x, int y, int status) {
        this.xCoord = x;
        this.yCoord = y;
        this.status = status;
    }

    public FieldNode(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    /**
     * @return the xCoord
     */
    public int getxCoord() {
        return xCoord;
    }

    /**
     * @param xCoord the xCoord to set
     */
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * @return the yCoord
     */
    public int getyCoord() {
        return yCoord;
    }

    /**
     * @param yCoord the yCoord to set
     */
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
