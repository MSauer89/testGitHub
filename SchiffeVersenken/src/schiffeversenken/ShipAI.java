/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schiffeversenken;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author maggo
 */
public class ShipAI {

    //TODO später private machen, nur testzwecke
    private AiBoardStatus priorities;
    private LinkedList<FieldNode> nextStrikingTargets = new LinkedList<>();

    //*Höchster erlaubter eingetragener Wert:
    //Größere Werte können im Betrieb effektiver größere Spielfelder nach
    //großen Schiffen absuchen, Masche wird aber erst spät eng
    //Wichtig! Um maximale Effizienz zu erreichen, sollte der maxNoteValue möglichst
    //*nicht größer als maximale Schiffsgröße sein!
    private final int maxNodeValue = 4;

    //Anzahl der Nodes, die als nächste Ziele abgearbeitet werden
    final int basicNodePipeSize = 5;

    public void testMethode() {
        priorities.setStatus(3, 3, -1);
        priorities.setStatus(4, 4, -1);
        priorities.setStatus(2, 4, -1);
        priorities.setStatus(4, 2, -1);
        priorities.setStatus(6, 2, -1);
        priorities.setStatus(5, 3, -1);
    }

    /**
     *
     * @param sizex board's horizontal size
     * @param sizey board's vertical size
     */
    public ShipAI(int sizex, int sizey) {
        priorities = new AiBoardStatus(sizex, sizey);
    }

    public void determineNextTargets() {
        //Periodisches Aufrufen
        //Step 1: Koordinaten mit höchster Priorität aussuchen
        //X anzahl von Zeilen auslesen in jedem Satz, nur höchstwertige nodes eintragen?
        LinkedList<FieldNode> highestNodes = new LinkedList<>();

        int highestValueFound = 0;

        //Alle x durchlaufen und in highestValueFound alle Nodes des höchsten Wertes eintragen
        for (int x = 0; x < this.priorities.getSizeX(); x++) {
            List<FieldNode> l = priorities.getFreePointsInLine(x);

            if (l.size() > 0) {

                Iterator<FieldNode> it = l.iterator();

                while (it.hasNext()) {
                    FieldNode next = it.next();

                    //Wert 0 sollte nicht auftreten. 0 ist Indikator für einen Fehler!
                    if (next.getStatus() == highestValueFound) {
                        highestNodes.add(next);
                    } else {

                        if (next.getStatus() > highestValueFound) {
                            highestValueFound = next.getStatus();
                            highestNodes = new LinkedList<>();
                            highestNodes.add(next);
                        }
                    }
                }

                //Idealfall: eine Koordinate pro Zeile, TODO: nach Status sortieren für priorität!
                //Es existieren noch unbeschossene Punkte in der Reihe
                //final int randomPosition = (int) (Math.random() * l.size());
                //this.nextStrikingTargets.add(l.remove(randomPosition));
            }

        }
        //TODO
        //basicNodePipeSize
        //Maximale Anzahl an "geplanten" Nodes definieren?
        //Anzahl an höchstwertigen Nodes rausziehen und als nächste Punkte listen
        if (highestNodes.isEmpty()) {
            System.out.println("highestNodes ist leer, Spielende?");
            return;
        }

        int count = 0;

        while (count < basicNodePipeSize && !highestNodes.isEmpty()) {
            final int randomPosition = (int) (Math.random() * highestNodes.size());
            nextStrikingTargets.add(highestNodes.remove(randomPosition));
            count++;
        }

        //TODO Achtung, highestNodes kann Größe 0 haben, wenn schon alles abgeschossen wurde (Spielende)
    }

    private void executeShot(FieldNode target) {
        //TODO Text für Frage des Ergebnisses        
        //(Auslagern in public Methode der main)

        //int i=-1 für leer, i=-2 für Treffer, TODO versenkt?
        int result = -1;//TODO Aufruf der Main-methode!
        
        switch (result){
            
            //Treffer, nicht versenkt
            case (-2):{
                //TODO analyse der Schiffsposition mit höchstpriorisierten nahen Schüssen
                //nächste Ziele in die dringliche Liste einfügen? alternativ: priorität auf max+1
                //DONE check erste priorityschleife in der methode, funzt für weitere aufrufe?
                
                break;
            }
            
            //Versenkt
            case (-3):{
                //TODO prioritäten der höchstpriorisierten testschüssen zurücksetzen?
                //Achtung, ausnutzbar durch Schiff-an-Schiff Strukturen! vielleicht lassen?
                break;
            }
            default:{
                //Ging ins Wasser, Wert eintragen
                //Wert ist also -1
                target.setStatus(result);
            }
        }

    }

    //Man weiß ja nie =)
    //Main Thread
    public void run() {

        while (!nextStrikingTargets.isEmpty()) {
            //Nächsten Punkt auswählen und befeuern, solange punkte vorhanden sind
            FieldNode nextTarget = nextStrikingTargets.removeFirst();
            executeShot(nextTarget);
        }

        //Liste leer?
        if (nextStrikingTargets.isEmpty()) {
            this.updateCoordPriorities();
            this.determineNextTargets();
        }

    }

    public AiBoardStatus updateCoordPriorities() {

        /*
        Feld erstellen, das Abstände zu nahen Trefferpunkten markiert
         */
        AiBoardStatus newStatus = new AiBoardStatus(priorities.getSizeX(), priorities.getSizeY());

        /*
        Zählt mit, wann zuletzt ein Treffer bzw eine Grenze auftrat
         */
        int stepsSinceLastBlock = 0;

        /*
        Erster Durchlauf:
        Step x++-> y++;
         */
        for (int x = 0; x < priorities.getSizeX(); x++) {
            //stepsSinceLastBlock anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int y = 0; y < priorities.getSizeY(); y++) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getStatus(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    //Wird nur bis Größe 4 notiert!
                    if (stepsSinceLastBlock < maxNodeValue) {
                        stepsSinceLastBlock++;
                    }

                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Höchster Wert wird für die Eintragung markiert
                    //Für spätere:
                    //minValue = Math.min(Math.min(newStatus.getState(x, y), 4), stepsSinceLastBlock);
                    //Achtung! Erster Durchlauf, alle Felder sind garantiert 0, noch keine Überschreibung!
                    //Bei folgenden Schritten muss der vorherige Status mit einbezogen werden!
                    minValue = Math.max(newStatus.getStatus(x, y), stepsSinceLastBlock);//TODO immer korrekt?
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getStatus(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

            //**************************
            //Rücklauf: Umgekehrte Y-Richtung
            //Step x++-> y--;
            //**************************
            //stepsSinceLastBlock anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int y = priorities.getSizeY() - 1; y >= 0; y--) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getStatus(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    if (stepsSinceLastBlock < maxNodeValue) {
                        stepsSinceLastBlock++;
                    }
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Höchster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!

                    minValue = Math.max(newStatus.getStatus(x, y), stepsSinceLastBlock);//TODO immer korrekt?
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getStatus(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

        }//End X-Schleife

        //**************************
        //Zweiter Durchlauf: Y als primärschleife, x als sekundärschleife
        //Step y++ -> x++;
        //**************************
        for (int y = 0;
                y < priorities.getSizeY();
                y++) {

            //Anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int x = 0; x < priorities.getSizeX(); x++) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getStatus(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    if (stepsSinceLastBlock < maxNodeValue) {
                        stepsSinceLastBlock++;
                    }
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Höchster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!

                    minValue = Math.max(newStatus.getStatus(x, y), stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getStatus(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

            //**************************
            //Rücklauf: Umgekehrte X-Richtung
            //Step y++-> x--;
            //**************************
            //Anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int x = priorities.getSizeX() - 1; x >= 0; x--) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getStatus(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    if (stepsSinceLastBlock < maxNodeValue) {
                        stepsSinceLastBlock++;
                    }
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Höchster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!
                    minValue = Math.max(newStatus.getStatus(x, y), stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getStatus(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End X-Schleife

        }//End Y-Schleife

        //*************************************
        //Methodenende, neuen Status übernehmen
        //*************************************
        this.priorities = newStatus;
        return newStatus;
    }
}
/*
    TODO! Später nur privat, updates alle X Läufe oder nach einem Treffer
    (Nochmal speziell behandeln, vor allem wegen "versenkt"-Status!)
    @Deprecated! muss Werte nach max Platz in irgendeine Richtung werten!
 */
 /*@Deprecated
    public AiBoardStatus updateCoordPrioritiesDPRC() {

        /*
        Feld erstellen, das Abstände zu nahen Trefferpunkten markiert

        AiBoardStatus newStatus = new AiBoardStatus(priorities.getSizeX(), priorities.getSizeY());

        /*
        Zählt mit, wann zuletzt ein Treffer bzw eine Grenze auftrat
       
        int stepsSinceLastBlock = 0;

        //Erster Durchlauf:
        //Step x++-> y++;
        for (int x = 0; x < priorities.getSizeX(); x++) {
            /*
            Anfangs jeder Schleife zurücksetzen
            
            stepsSinceLastBlock = 0;
            for (int y = 0; y < priorities.getSizeY(); y++) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getState(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    stepsSinceLastBlock++;
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Niedrigster Wert wird für die Eintragung markiert
                    //Für spätere:
                    //minValue = Math.min(Math.min(newStatus.getState(x, y), 4), stepsSinceLastBlock);

                    //Achtung! Erster Durchlauf, alle Felder sind garantiert 0, noch keine Überschreibung!
                    //Bei folgenden Schritten muss der vorherige Status mit einbezogen werden!
                    minValue = Math.min(4, stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getState(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

        }//End X-Schleife

        //**************************
        //Zweiter Durchlauf: Umgekehrte Y-Richtung
        //Step x++-> y--;
        //**************************
        for (int x = 0; x < priorities.getSizeX(); x++) {
            /*
            Anfangs jeder Schleife zurücksetzen
            
            stepsSinceLastBlock = 0;
            for (int y = priorities.getSizeY() - 1; y >= 0; y--) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getState(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    stepsSinceLastBlock++;
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Niedrigster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!
                    minValue = Math.min(Math.min(newStatus.getState(x, y), 4), stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getState(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

        }//End X-Schleife

        //**************************
        //Dritter Durchlauf: Y als primärschleife, x als sekundärschleife
        //Step y++ -> x++;
        //**************************
        for (int y = 0; y < priorities.getSizeY(); y++) {

            //Anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int x = 0; x < priorities.getSizeX(); x++) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getState(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    stepsSinceLastBlock++;
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Niedrigster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!
                    minValue = Math.min(Math.min(newStatus.getState(x, y), 4), stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getState(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

        }//End X-Schleife

        //**************************
        //Vierter/letzter Durchlauf: Y als primärschleife, umgekehrtes x als sekundärschleife
        //Step y++ -> x--;
        //**************************
        for (int y = priorities.getSizeY() - 1; y >= 0; y--) {

            //Anfangs jeder Schleife zurücksetzen
            stepsSinceLastBlock = 0;
            for (int x = priorities.getSizeX() - 1; x >= 0; x--) {

                //Der eingetragene Wert, Standardmäßig 0
                int minValue = 0;

                //State>=0 entspricht unbeschossener Region
                if (priorities.getState(x, y) >= 0) {
                    //Noch nicht beschossenes Gebiet
                    //Offiziell 1 Feld mehr Platz
                    stepsSinceLastBlock++;
                    //Maximale notierte Distanz: 4
                    //Bezieht X und Y Werte mit ein
                    //Niedrigster Wert wird für die Eintragung markiert
                    //Da nicht der erste Durchlauf, müssen auch vorher eingetragene Werte berücksichtigt werden!
                    minValue = Math.min(Math.min(newStatus.getState(x, y), 4), stepsSinceLastBlock);
                    //Kleinster Wert aus: Vorherigem Status, Schritte, Maximum (4)
                } else {
                    //***************************
                    //Feld wurde schon beschossen
                    //***************************
                    //Beschusswert übertragen, steps zurücksetzen
                    stepsSinceLastBlock = 0;
                    minValue = priorities.getState(x, y);
                }
                newStatus.setStatus(x, y, minValue);
            }//End Y-Schleife

        }//End X-Schleife

        //*************************************
        //Methodenende, neuen Status übernehmen
        //*************************************
        this.priorities = newStatus;
        return newStatus;
    }*/
