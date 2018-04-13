/*
Thema: Collections, Autoboxing/Wrapper, Wrapperklassen + statische Methoden
 */
package sammlungen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sammlungen {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        /*Collection sammlung1 = new ArrayList();
        System.out.println("Sammlung1 hat aktuell: " + sammlung1.size() + " Elemente");
        sammlung1.add("Hallo");
        System.out.println("Sammlung1 hat aktuell: " + sammlung1.size() + " Elemente");
        sammlung1.add("Hallo");
        System.out.println("Sammlung1 hat aktuell: " + sammlung1.size() + " Elemente");

        Object[] objs = new Object[3];

        //Array als Objekt einspeichern
        sammlung1.add(objs);

        for (Object o : sammlung1) {
            System.out.println("Element in der Sammlung1: " + o);
        }

        //Autoboxing
        sammlung1.add(3.1415);

        Double wrapperPi = new Double(3.1415);

        System.out.println("Der Wert von wrapperPi ist: " + wrapperPi.doubleValue());

        double pi = wrapperPi.doubleValue();
        wrapperPi = new Double("3.1415");
        wrapperPi = 3.1415;
        pi = wrapperPi;
        double quadrat = wrapperPi * wrapperPi;

        Float f = new Float(2.34);
        //Integer zahl = new Integer("234"); //auf die Weise nicht zulässig, sondern:
        Integer zahl = new Integer("0345"); //Erlaubt, FÜHRUNGS-0 wird NICHT als octaldeklaration gewertet!
        System.out.println("Neue Zahl ist:" + ++zahl);

        //0=Octalsystem!
        int zahlInt = 0345;//Erlaubt, FÜHRUNGS-0 wird als octaldeklaration gewertet!

        zahl = new Integer((int) 'a');
        //entspricht:
        zahl = new Integer('a');

        System.out.println("Neue Zahl ist: " + zahl);
        System.out.println("Neue Zahl ist: " + zahlInt);

        System.out.println("Neue Zahl ist: " + (char) zahl.intValue());

        Character c1 = new Character('x');
        System.out.println("Der neue Buchstabe ist: " + c1);*/
        //Anmerkung: Auskommentiert, um später noch nützlich zu sein
        //------------------Ab hier: 11.04.18------------------
        /*List sammlung2 = new ArrayList();
        sammlung2.add("Hallo");
        sammlung2.add("Welt");
        sammlung2.add(3.1415);
        sammlung2.add(new Scanner(System.in));

        Scanner eingabe = new Scanner(System.in);
        sammlung2.add(eingabe);

        System.out.println("Anzahl der Elemente in sammlung2: " + sammlung2.size());

        double e = Math.E;

        //Elemente weiterschieben, dann an die freie Stelle 3 schreiben
        sammlung2.add(3, e);
        for (int i = 0; i < sammlung2.size(); i++) {
            System.out.println("Das Element mit dem Index " + i + " ist: " + sammlung2.get(i));

        }
        sammlung2.remove(5);
        for (int i = 0; i < sammlung2.size(); i++) {
            System.out.println("Das Element mit dem Index " + i + " ist: " + sammlung2.get(i));
        }

        for (int i = 0; i < sammlung2.size(); i++) {
            if (sammlung2.get(i).equals("Welt")) {
                sammlung2.remove("Welt");
            }
        }

        for (int i = 0; i < sammlung2.size(); i++) {
            System.out.println("Das Element mit dem Index " + i + " ist: " + sammlung2.get(i));
        }
        
        //indexOf Beispiel
        int i= sammlung2.indexOf("Hallo");
        System.out.println("Das Element \"Hallo\" ist an der Stelle "+i);

        i= sammlung2.indexOf("xxx");
        //Gibt -1 für nicht vorhandenes Objekt
        System.out.println("Das Element \"XXX\" ist an der Stelle "+i);*/
        Kunde wichtig = new Kunde("Justus Jonas", 1000);
        Kunde interessant = new Kunde("Peter Shaw", 0);
        Kunde langweilig = new Kunde("Bob Andrews", 100);
        List alleKunden = new ArrayList();
        alleKunden.add(wichtig);
        alleKunden.add(interessant);
        alleKunden.add(langweilig);
        alleKunden.add("Hans im Glück");
        alleKunden.add(new Kunde("Hans im Glück", 0));

        //Beispiel: unsicheres typecasting
        for (Object o : alleKunden) {
            System.out.println("Kundenguthaben ist: " + ((Kunde) o).getRechnungsBetrag());
        }

        //Alternative (Generics)
        List<Kunde> alleKundenGeneric = new ArrayList<>();
        alleKundenGeneric.add(wichtig);
        alleKundenGeneric.add(interessant);
        alleKundenGeneric.add(langweilig);
        for (Kunde o : alleKundenGeneric) {
            System.out.println("Kundenguthaben ist: " + o.getRechnungsBetrag());
        }
        //vererbung in generics als Beispiel
        alleKundenGeneric.add(new StammKunde("miau", 3));

        //TODO nochmal anschauen!
        List<Kunde> alleKundenGeneric2 = new ArrayList<>();
        alleKundenGeneric2.add(null);
        alleKundenGeneric2.add(new Kunde("justus", 12));

        //------------Referatscherze------------\\
        //Erlaubt nur null?
        List<StammKunde> frageStammkunde = new ArrayList<>();
        List<? extends Kunde> frageKunde = new ArrayList<>();
        //Geht
        frageKunde = frageStammkunde;
        //ABER erlaubt immer noch kein add außer null!
        frageKunde.add(new Kunde("bla", 2));

    }

    //Beispiel mit ?: Hier nötig, dafür geht list<kunde> nicht für list<stammkunde>
    //WICHTIG: list<kunde> ist NICHT parent für list<stammkunde>!
    //deshalb bei methoden ? extends schokokuchen!
    public static void testFrage(List<? extends Kunde> kListe) {
        for (Kunde k : kListe) {
            //Für Stammkunden wäre typecasting möglich usw...
        }
    }

}
