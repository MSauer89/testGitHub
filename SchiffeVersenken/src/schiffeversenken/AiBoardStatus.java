/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schiffeversenken;

import java.util.LinkedList;

/**
 *
 * @author maggo
 */
public class AiBoardStatus {

    private FieldNode[][] board;

    public AiBoardStatus(int sizex, int sizey) {
        board = new FieldNode[sizex][sizey];

        for (int x = 0; x < sizex; x++) {
            for (int y = 0; y < sizey; y++) {
                board[x][y] = new FieldNode(x, y);

            }
        }
    }

    /**
     *
     * @param x Die Zeile, in der unbeschossene Plätze vorhanden sind
     * @return Noch unbeschossene Plätze in der Zeile x ausgeben
     */
    public LinkedList<FieldNode> getFreePointsInLine(int x) {

        LinkedList<FieldNode> l = new LinkedList<>();

        for (FieldNode wuff : board[x]) {
            if (wuff.getStatus() >= 0) {
                l.add(wuff);
            }
        }

        return l;
    }

    public int getStatus(int coordx, int coordy) {
        return board[coordx][coordy].getStatus();
    }

    public boolean setStatus(int coordx, int coordy, int state) {
        board[coordx][coordy].setStatus(state);
        return true;
    }

    public int getSizeX() {
        return board.length;
    }

    public int getSizeY() {
        return board[0].length;
    }

}
