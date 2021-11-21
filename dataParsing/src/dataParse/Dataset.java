package dataParse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//A Dataset represents a specific RKI COVID-19 data
public class Dataset {
    private final List<ArrayList<String>> data;

    public Dataset(String filepath, String splitter) {
  //      System.out.print("Parsing \"" + filepath + "\"...");
        long before = System.currentTimeMillis();

        data = parseCsvFile(new File(filepath), splitter);

   //     System.out.println(" Done after " + (System.currentTimeMillis() - before) + "ms");
    }

    public Stream<ArrayList<String>> stream() {
        return data.stream();
    }

    /*
     * ! Potential error: if file is poorly formatted and 1st line is split into several lines this
     * could result in a problem !
     */
    public static int[] findIndex(String filepath, String splitter, String[] args) {
        final File csvFile = new File("dataset/" + filepath);
        try {
            List<ArrayList<String>> l = Files.lines(csvFile.toPath()).limit(1) // only get head of file
                    .map((lines) -> lineToEntry(lines, splitter)) // convert line String[]
                    .collect(Collectors.toList());
            ArrayList<String> list = l.get(0);
            int[] result = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                result[i] = list.indexOf(args[i]);
                if (result[i] == -1) {
                    throw new NoSuchElementException(args[i] + " was not found in line 1 of " + csvFile);
                }
            }

            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("File " + csvFile + " not found");
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error while parsing file " + csvFile + ": " + e);
        }
    }

    //Given a CSV file, attempt to parse it into a list of Entry objects
    private static List<ArrayList<String>> parseCsvFile(final File csvFile, String splitter) {
        try {
            return Files.lines(csvFile.toPath()).skip(1) // skip header of csv file
                    .map((lines) -> lineToEntry(lines, splitter)) // convert line to Entry object
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("File " + csvFile + " not found");
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error while parsing file " + csvFile + ": " + e);
        }
    }

    //Given string of a csv row, returns a corresponding Entry object
    //MONATSZAHL AUSPRAEGUNG JAHR MONAT WERT VORJAHRESWERT VERAEND-VORMONAT-PROZENT VERAEND-VORJAHRESMONAT-PROZENT ZWOELF-MONATE-MITTELWERT
    //    0         1          2    3     4     5                   6                               7                       8
    private static ArrayList<String> lineToEntry(String line, String splitter) {
        final String[] fields = line.split(splitter);
        return new ArrayList<>(List.of(fields));
    }
}
