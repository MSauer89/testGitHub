
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author msauer2
 */
public class Tag4Noiser {

    private double[][] data;
    private Random rng;
    private int stepDistance;
    private int maxPeak;

    public Tag4Noiser(long seed, int sizeX, int sizeY, int stepDistance, int maxPeak) {
        rng = new Random(seed);
        data = new double[sizeX][sizeY];
        this.stepDistance = stepDistance;
        this.maxPeak = maxPeak;
    }

    /*
    Füllen des Array durch zufällige Werte
     */
    public void noise() {
        for (int countx = 0; countx < data.length; countx++) {
            for (int county = 0; county < data.length; county++) {
                //countx für die x Koordinate, county für die Y Koordinate
                data[countx][county] = rng.nextDouble() * maxPeak;
            }
        }
    }

    /*
    Wert aus einem Punkt im Koordinatensystem auslesen
     */
    public int heightAt(int coordX, int coordY) {

        //Bei überschreiten der Grenzen wird das Array wieder von vorne gezählt
        //**Im Beispiel: zwischen [x] und [y] liegen 16 Koordinaten
        //X---->, Y^
        //Normalisierung zur Feststellung der zugehörigen Punkte:
        int fieldSizeX = data.length;
        int fieldSizeY = data[0].length;
        coordX = coordX % (stepDistance * fieldSizeX);
        coordY = coordY % (stepDistance * fieldSizeY);

        /*System.out.println("coordx=" + coordX);
        System.out.println("coordy=" + coordY);*/

        //Arbeit sparen:
        int cxAllocation = (coordX / 16) % fieldSizeX;
        int cyAllocation = (coordY / 16) % fieldSizeY;

        /*System.out.println("cxAllocation=" + cxAllocation);
        System.out.println("cyAllocation=" + cyAllocation);*/

        //Eckpunkte scheinen korrekt gewählt zu sein... TODO
        //Zugehörige Eckpunkte ermitteln
        double valueBotLeft = data[cxAllocation][cyAllocation];
        double valueTopLeft = data[cxAllocation][(cyAllocation + 1) % fieldSizeY];

        double valueBotRight = data[(cxAllocation + 1) % fieldSizeX][cyAllocation];
        double valueTopRight = data[(cxAllocation + 1) % fieldSizeX][(cyAllocation + 1) % fieldSizeY];

        //TODO hier stimmts noch!
        /*System.out.println("BL=" + valueBotLeft);
        System.out.println("TL=" + valueTopLeft);
        System.out.println("BR=" + valueBotRight);
        System.out.println("TR=" + valueTopRight);*/

        //TODO Werte zu hoch, nicht korrekt nach eckpunkten gesucht!
        //Werte der nahen Knotenpunkte basierend auf ihrer Distanz zur
        //gewählten Koordinate einrechnen
        //Utility, zur Übersicht: pure distanz x+y
        //System.out.println("coordx= "+coordX);
        //System.out.println("coordy= "+coordY);
        double distanceBLPreX = (coordX - cxAllocation * stepDistance);
        double distanceBLPreY =(coordY - cyAllocation * stepDistance);
        
        //System.out.println("coordx= "+coordX+"-> "+distanceBLPreX);
        //System.out.println("coordy= "+coordY+"-> "+distanceBLPreY);
        
        double distanceTLPreX = (coordX - cxAllocation * stepDistance);
        //double distanceTLPreY = coordY - (cyAllocation+1) * 16 ;//+ stepDistance;
        double distanceTLPreY = (cyAllocation+1) * stepDistance-coordY ;//+ stepDistance;
        
        //System.out.println("-> "+distanceTLPreX);
        //System.out.println("-> "+distanceTLPreY);
        
        double distanceBRPreX = (cxAllocation+1) * stepDistance -coordX;
        double distanceBRPreY = (coordY - cyAllocation * stepDistance);
        
        double distanceTRPreX = (cxAllocation+1) * stepDistance-coordX;
        double distanceTRPreY =(cyAllocation+1) * stepDistance-coordY;
        
        
        //TODO Werte zu hoch, nicht korrekt berechnet
        double distanceBL = Math.sqrt(distanceBLPreX * distanceBLPreX+distanceBLPreY*distanceBLPreY);
        double distanceTL = Math.sqrt(distanceTLPreX * distanceTLPreX+distanceTLPreY*distanceTLPreY);
        double distanceBR = Math.sqrt(distanceBRPreX * distanceBRPreX+distanceBRPreY*distanceBRPreY);
        double distanceTR = Math.sqrt(distanceTRPreX * distanceTRPreX+distanceTRPreY*distanceTRPreY);

        double totalDistance=distanceBL+distanceTL+distanceBR+distanceTR;
        /*System.out.println("distanceBL =" + distanceBL + ", value=" + valueBotLeft);
        System.out.println("distanceTL =" + distanceTL + ", value=" + valueTopLeft);
        System.out.println("distanceBR =" + distanceBR + ", value=" + valueBotRight);
        System.out.println("distanceTR =" + distanceTR + ", value=" + valueTopRight);*/

        //return (int) (valueBotLeft * distanceBL / stepDistance + valueTopLeft * distanceTL / stepDistance + valueBotRight * distanceBR / stepDistance + valueTopRight * distanceTR / stepDistance);
        double returnValue=0;
        //problem: Wert=0 wirft Probleme auf
        //TODO richtung Mitte fallen die Werte zu sehr ab
        if (distanceBL==0) return (int) valueBotLeft;
        else returnValue+=valueBotLeft * (1-distanceBL/totalDistance);
        
        if (distanceTL==0) return (int) valueTopLeft;
        else returnValue+=valueTopLeft * (1-distanceTL/totalDistance);
        
        if (distanceBR==0) return (int) valueBotRight;
        else returnValue+=valueBotRight * (1-distanceBR/totalDistance);
        
        
        if (distanceTR==0) return (int) valueTopRight;//TODO TL wert stimmt noch nicht!
        else returnValue+=valueTopRight * (1-distanceTR/totalDistance);
        
        /* TODO backup
        if (distanceBL==0) return (int) valueBotLeft;
        else returnValue+=valueBotLeft * 1/distanceBL;
        
        if (distanceTL==0) return (int) valueTopLeft;
        else returnValue+=valueTopLeft * 1/distanceTL;
        
        if (distanceBR==0) return (int) valueBotRight;
        else returnValue+=valueBotRight * 1/distanceBR;
        
        if (distanceTR==0) return (int) valueTopRight;
        else returnValue+=valueTopRight * 1/distanceTR;
        */
        /*return (int) (
                valueBotLeft * 1/distanceBL +
                valueTopLeft * 1/distanceTL +
                valueBotRight * 1/distanceBR +
                valueTopRight * 1/distanceTR);*/
        return (int) returnValue;
        //return (int) (valueBotLeft * distanceBL + valueTopLeft * distanceTL + valueBotRight * distanceBR + valueTopRight * distanceTR);

    }

    public static void main(String... args) {

        //***Anzahl der Eckpunkte entlang Kante X***
        final int sizeX = 10;
        //***Anzahl der Eckpunkte entlang Kante Y***
        final int sizeY = 10;

        //***Anzahl der Schritte/Zwischenpunkte zwischen zwei Knoten***
        final int stepDistance = 16;

        //Maximale Ausschlagshöhe?
        final int maxPeak = 10;

        Tag4Noiser wuff = new Tag4Noiser(System.currentTimeMillis(), sizeX, sizeY, stepDistance, maxPeak);
        wuff.noise();

        final int ausleseGrenzeX = 16;
        final int ausleseGrenzeY = 16;

        for (int i = 0; i < ausleseGrenzeX; i++) {
            //System.out.print("i= "+i+" ");
            for (int bla = 0; bla < ausleseGrenzeY; bla++) {
                //wuff.heightAt(i, bla);
                System.out.print(wuff.heightAt(i,bla)+" ");
            }
            System.out.println();
        }
    }

}
