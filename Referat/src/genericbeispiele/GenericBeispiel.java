/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericbeispiele;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author msauer2
 * @param <T>
 */
public class GenericBeispiel<T> {

    //Speichert unsere Daten
    private Collection<T> meineDaten = new HashSet<>();

    //(Bullsh*t)
    private Collection<? super T> superDaten = new HashSet<>();

    public GenericBeispiel() {

    }

    //Variante, die nur für den Generictyp T funktioniert
    public void passendeDatenHinzufügen(Collection<T> neueDaten) {
        meineDaten.addAll(neueDaten);
    }

    //Variante, die für den Generictyp T und alle davon erbenden Klassen funktioniert
    public void irgendwelcheDatenHinzufügen(Collection<? extends T> neueDaten) {
        meineDaten.addAll(neueDaten);
    }

    //(Bullsh*t)
    public void superDatenHinzufügen(Collection<? super T> neueDaten) {
        //Bullsh*t bleibt bullsh*t
        superDaten = neueDaten;
    }

    public void statusAusgeben() {
        System.out.println(this.meineDaten.getClass().getName());
        for (T variable : meineDaten) {
            System.out.println(variable.getClass().toString() + " Wert: " + variable);
        }

        //Bullsh*t prints
        if (superDaten != null) {
            for (Object o : superDaten) {
                System.out.println(o.getClass().toString() + " Wert: " + o.toString());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Beispiel: Integers
        //Objekt für Integer erstellen
        GenericBeispiel<Integer> meinBeispielInt = new GenericBeispiel<>();

        Collection<Integer> neueDaten = new HashSet<>();
        //Erstelle eine Liste von Integers
        neueDaten.add(1);
        neueDaten.add(2);
        neueDaten.add(3);
        neueDaten.add(4);
        neueDaten.add(5);
        meinBeispielInt.passendeDatenHinzufügen(neueDaten);
        meinBeispielInt.statusAusgeben();

        //Beispiel: Doubles
        //Objekt für Doubles erstellen
        GenericBeispiel<Double> meinBeispielDouble = new GenericBeispiel<>();
        Collection<Double> neueDoubleDaten = new HashSet<>();
        neueDoubleDaten.add(1.0);
        neueDoubleDaten.add(2.0);
        neueDoubleDaten.add(3.0);
        neueDoubleDaten.add(4.0);
        neueDoubleDaten.add(5.0);
        meinBeispielDouble.passendeDatenHinzufügen(neueDoubleDaten);
        meinBeispielDouble.statusAusgeben();

        //NICHT erlaubt sind:
        //meinBeispielDouble.passendeDatenHinzufügen(neueDaten);**
        //meinBeispielInt.passendeDatenHinzufügen(neueDoubleDaten);**
        //Der Datentyp von GenericBeispiel muss hier immer dem Datentyp der übergebenden Liste entsprechen.
        //Unterschiedliche Kriterien greifen hier:
        //Obwohl int implizit zu einem double umgewandelt werden kann,
        //erlauben Collections mit Double oder Integer keine solche direkte Zuweisung!
        //Double und Integer erben beide von Number
        Number nummer = new Integer(1);
        nummer = new Double(1);

        //Im Gegensatz dazu erben Collection<Int> und Collection<Double> NICHT
        //von Collection<Number>!
        //Collection<Number> numberCollection = new HashSet<Integer>();
        //Hier kommt die "Wildcard" zum Einsatz!
        //Wenn wir wollen, dass der Typ des Generics Vererbung unterstützen soll,
        //nutzen wir das "extends" Schlüsselwort:
        // <? extends Typ>
        GenericBeispiel<Number> meinBeispielNumber = new GenericBeispiel<>();

        //Beispiel: Double erbt zwar von Number, ABER HashSet<Double> erbt NICHT
        //von HashSet<Number> !
        //meinBeispielNumber.passendeDatenHinzufügen(neueDoubleDaten);
        //Hier die Variante mit <? extends T>, die darauf prüfen kann, ob der
        //GENERIC TYP vom gewünschten Typ erbt:
        //Collection<Integer> entspricht <? extends Number>
        meinBeispielNumber.irgendwelcheDatenHinzufügen(neueDaten);

        meinBeispielNumber.statusAusgeben();

        //Collection<Double> entspricht <? extends Number>
        meinBeispielNumber.irgendwelcheDatenHinzufügen(neueDoubleDaten);

        meinBeispielNumber.statusAusgeben();

        //Die Wildcard ist mehr als deklarativ zu sehen und kann nicht direkt
        //gefüllt werden, außer mit null Werten
        //Beispiel:
        Collection<? extends Number> wildcardCollection = neueDaten;
        //HashSet<Integer> passt  zwar zu Collection<? extends Number>, ABER:
        //Collection<? extends Number> bietet KEINE echte Funktionalität!

        System.out.println("wildcardCollection enthält:" + wildcardCollection.toString());

        wildcardCollection.add(null);

        //wildcardCollection.add(new Integer(1));
        //Man könnte aber an neueDaten manipulieren:
        neueDaten.add(6);
        //Der Pointer von wildcardCollection würde auf dasselbe Objekt zeigen,
        //aber das Hinzufügen von Daten direkt über wildcardCollection ist trotzdem illegal!
        System.out.println("wildcardCollection enthält:" + wildcardCollection.toString());

        //Spezialfall: Es gibt auch <? super T>, das die Parentklassen von T als Argument erlaubt
        //Findet in der Praxis so gut wie gar keine Anwendung, aber hier ein Beispiel:
        //Man möchte, dass GenericBeispiel<Integer> auch in der Lage sein soll,
        //Daten, die vom Parent erben auszugeben, also z.B. auch Doubles ausgeben kann.
    }

}
