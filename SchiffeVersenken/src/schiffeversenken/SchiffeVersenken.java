/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schiffeversenken;

/**
 *
 * @author maggo
 */
public class SchiffeVersenken implements Interactive {

    /**
     * @param args the command line arguments
     *
     * TODO!! coord priorities nicht nach min, sondern max (bis 4), um platz
     * mehrerer Stellen zu ermitteln!
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //AiBoardStatus wuff=new AiBoardStatus(3,3);
        final int sizeX = 10, sizeY = 10;

        ShipAI wuff = new ShipAI(sizeX, sizeY);

        wuff.testMethode();

        AiBoardStatus status = wuff.updateCoordPriorities();

        System.out.print("y= ");
        for (int y = 0; y < sizeY; y++) {
            System.out.print(y + "  ");
        }
        System.out.println();
        for (int x = 0; x < status.getSizeX(); x++) {
            System.out.print("  ");
            for (int y = 0; y < status.getSizeY(); y++) {
                int miau = status.getStatus(x, y);
                String blubb = status.getStatus(x, y) + " ";

                if (miau >= 0) {
                    blubb = "+" + blubb;
                }
                System.out.print(blubb);
            }
            System.out.println("x=" + x);
        }

    }

}
