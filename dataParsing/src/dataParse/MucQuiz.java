package dataParse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;

public class MucQuiz {

    public static void main(String[] args) {
        //  “Wie viele Einwohner (alle Altersgruppen) werden im Durchschnitt monatlich eingebürgert?”
        averageEinbürgerungen();
        // “Wie viele Kinobesucher gab es insgesamt im Jahr 2020?”
         sumCinemaVisitors();
        //"Wie viele Spielplätze gibt es in München?"
        countPlayyards();
        //"Wie viele Haushalte gibt es in München?"
        hausHalte();
        //In welchem Stadtteil gibt es die meisten Haushalte?
         maxHaushalte();
        //“Wie groß ist ein durchschnittlicher Haushalt in München?”
        averageHaushalt();
        //Wie viel Prozent der Einwohner Münchens haben ihren Erstwohnsitz im größten Stadtbezirk (Ramersdorf-Perlach) von München?
        //7,6%
        // “Wie viele Einwohner hat München?”
        numberInhabitants();
        // “Wie hoch ist der Ausländeranteil in München”
       percentageForeigners();
        // Wie hoch ist der Anteil der männlichen Bevölkerung in München?”
        percentageMenWomen();
        // “Wie hoch war der durchschnittliche Zuwachs an Einwohnern pro Jahr in den letzten 50 Jahren“
        avgIncreasePopulation();
        // “Welcher Stadtteil hatte 2015 die höchste durchschnittliche Lebenserwartung?”""

        //Wie viele Menschen besuchen das Oktoberfest durchschnittlich pro Tag?
        avgVisitorsOktoberfestDay();

    }

    public static void averageEinbürgerungen() {
        String einbuergerungen_csv = "210619monatszahlenjuni2021monatszahlen2106einbuergerungen.csv";
        Dataset einbuergerungen = new Dataset("dataset/" + einbuergerungen_csv, ",");

        System.out.println(einbuergerungen.stream()
                .filter(x ->
                        x.get(0).equalsIgnoreCase("Geschlecht") &&
                                x.get(1).equalsIgnoreCase("insgesamt") &&
                                x.get(3).equalsIgnoreCase("Summe")
                )
                .mapToInt(x -> Integer.parseInt(x.get(4)))
                .average().getAsDouble());
    }

    public static void sumCinemaVisitors() {
        String cinema_csv = "210619monatszahlenjuni2021monatszahlen2106kinos.csv";
        Dataset cinema_visitors = new Dataset("dataset/" + cinema_csv, ",");
        System.out.println(cinema_visitors.stream().filter(x ->
                x.get(2).equals("2020") && !x.get(3).equalsIgnoreCase("summe") && !x.get(4).equals("")).mapToInt(x -> Integer.parseInt(x.get(4))).sum());
    }

    public static void countPlayyards() {
        String play_yards = "spielplaetzemuenchenohneleerespalten2016-06-13.csv";
        Dataset playyards = new Dataset("dataset/" + play_yards, ",");
        System.out.println(playyards.stream().filter(x -> x.get(0).charAt(0) >= '0' && x.get(0).charAt(0) <= '9').count());
    }

    public static void hausHalte() {
        String hausHalte = "privathaushaltestadtbezirke2013.csv";
        Dataset hausHalteGesamt = new Dataset("dataset/" + hausHalte, ",");
        System.out.println(hausHalteGesamt.stream().mapToInt(x -> Integer.parseInt(x.get(2))).sum());
    }

    public static void maxHaushalte() {
        String hausHalte = "privathaushaltestadtbezirke2013.csv";
        Dataset hausHalteGesamt = new Dataset("dataset/" + hausHalte, ",");
        String[] args = {"\"haushalte_zusammen\""};
        int[] index = Dataset.findIndex(hausHalte, ",", args);
        System.out.println(hausHalteGesamt.stream().max(Comparator.comparing(x -> Integer.parseInt(x.get(index[0])))).map(x -> x.get(1)).get());
    }

    public static void averageHaushalt() {
        String hausHalte = "privathaushaltestadtbezirke2013.csv";
        Dataset hausHalteGesamt = new Dataset("dataset/" + hausHalte, ",");
        double one = hausHalteGesamt.stream().filter(x -> !x.get(3).equals("")).mapToInt(x -> Integer.parseInt(x.get(3))).average().getAsDouble();
        double two = hausHalteGesamt.stream().filter(x -> !x.get(4).equals("")).mapToInt(x -> Integer.parseInt(x.get(4))).average().getAsDouble();
        double three = hausHalteGesamt.stream().filter(x -> !x.get(5).equals("")).mapToInt(x -> Integer.parseInt(x.get(5))).average().getAsDouble();
        double four = hausHalteGesamt.stream().filter(x -> !x.get(6).equals("")).mapToInt(x -> Integer.parseInt(x.get(6))).average().getAsDouble();
        double fiveAndMore = hausHalteGesamt.stream().filter(x -> !x.get(7).equals("")).mapToInt(x -> Integer.parseInt(x.get(7))).average().getAsDouble();
        double r = (one + three * 3 + two * 2 + fiveAndMore * 5 + four * 4) / (one + three + two + four + fiveAndMore);
        r = (double) Math.round(r * 1000) / 1000;
        System.out.printf("%2.3f\n", r);
    }

    public static void numberInhabitants() {
        String inhabitants_csv = "jt1401012018.csv";
        Dataset inhabitants = new Dataset("dataset/" + inhabitants_csv, ",");
        String[] args = {"jahr", "einwohner_Insgesamt"};
        int[] index = Dataset.findIndex(inhabitants_csv, ",", args);
        System.out.println(inhabitants.stream().max(Comparator.comparing(x -> Integer.parseInt(x.get(index[0]))))
                .map(x -> x.get(index[1])).get());
    }

    public static void percentageForeigners() {
        String foreigners_csv = "jt1401012018.csv";
        Dataset foreigners = new Dataset("dataset/" + foreigners_csv, ",");
        String[] args = {"jahr", "einwohner_auslaender_innen", "einwohner_Insgesamt"};
        int[] index = Dataset.findIndex(foreigners_csv, ",", args);
        double r = foreigners.stream().max(Comparator.comparing(x -> Integer.parseInt(x.get(index[0]))))
                .map(x -> (Double.parseDouble(x.get(index[1])) / Double.parseDouble(x.get(index[2])))).get() * 100;
        r = (double) Math.round(r * 10) / 10;
        System.out.printf("%2.1f%%\n", r);
    }

    public static void percentageMenWomen() {
        String foreigners_csv = "jt1401012018.csv";
        Dataset foreigners = new Dataset("dataset/" + foreigners_csv, ",");
        String[] args = {"jahr", "einwohner_maennlich", "einwohner_Insgesamt"};
        int[] index = Dataset.findIndex(foreigners_csv, ",", args);
        double r = foreigners.stream().max(Comparator.comparing(x -> Integer.parseInt(x.get(index[0]))))
                .map(x -> (Double.parseDouble(x.get(index[1])) / Double.parseDouble(x.get(index[2])))).get();
        r = (double) Math.round(r * 1000) / 10;
        System.out.printf("%2.1f%%\n", r);
    }

    public static void avgIncreasePopulation() {
        String foreigners_csv = "jt1401012018.csv";
        Dataset foreigners = new Dataset("dataset/" + foreigners_csv, ",");
        String[] args = {"jahr", "einwohner_Insgesamt"};
        int[] index = Dataset.findIndex(foreigners_csv, ",", args);
        int maxYear = foreigners.stream()
                .mapToInt(x -> Integer.parseInt(x.get(index[0]))).max().getAsInt();
        List<Integer> l = foreigners.stream()
                .filter(x -> (maxYear - Integer.parseInt(x.get(index[0]))) <= 50)
                .map(x -> Integer.parseInt(x.get(index[1]))).collect(Collectors.toList());
        List<Double> list = new ArrayList<>();
        for (int i = 0, j = 1; j < l.size(); i++, j++) {
            list.add((double) (l.get(j) - l.get(i)) / l.get(i));
        }
        double r = list.stream().mapToDouble(x -> x).average().getAsDouble();
        r = (double) Math.round(r * 1000) / 10;
        System.out.printf("%2.1f%%\n", r);
    }

    public static void avgVisitorsOktoberfestDay() {
        String visitors = "oktoberfestbesucher19852019.csv";
        Dataset oktoberfestInfo = new Dataset("dataset/" + visitors, ",");
        System.out.println(oktoberfestInfo.stream().mapToDouble(x -> Double.parseDouble(x.get(3))).average().getAsDouble());
    }

}