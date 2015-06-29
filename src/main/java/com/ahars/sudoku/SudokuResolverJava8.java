package com.ahars.sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuResolverJava8 {

    private static String path = "src/main/resources/";
    private static String file = "grid.txt";

    private static List<Integer> initialGrid;
    private static List<Integer> resultGrid;
    private static boolean ok;
    private static int val;

    /**
     * Launch the reading of a file, the data ingestion into a Grid and the resolving of this Grid.
     * @param args param not used.
     */
    public static void main(String[] args) {

        try {
            initGivenGrid(file);
            displayGrid(initialGrid);

            solveGridAndDisplay();

        } catch (StringIndexOutOfBoundsException sioobe) {
            println("Error in the given grid : " + sioobe);
        } catch (NumberFormatException nfe) {
            println("Wrong character in the given grid : " + nfe);
        } catch (IndexOutOfBoundsException ioobe) {
            println("Error in the given grid : " + ioobe);
        } catch (IOException ioe) {
            println("Error in opening the file : " + ioe);
        }
    }

    /**
     * Read of the given file containing the data and initialize the Grid with them.
     * @param file The file containing the data for the Grid.
     * @throws IOException if there is a problem during the reading of the file.
     */
    private static void initGivenGrid(String file) throws IOException {

        initialGrid = new ArrayList<>();
        Stream<String> lines = Files.lines(Paths.get(path, file));

        lines.map(line -> Arrays.asList(line.split(" ")))
                .forEach(x -> x.forEach(t -> Arrays.asList(t.split(""))
                        .forEach(z -> initialGrid.add(Integer.parseInt(z)))));
        lines.close();
    }

    /**
     * Display a Grid in a user-friendly way.
     * @param grid the Grid to display
     */
    private static void displayGrid(List<Integer> grid) {

        // Loop For with Streams
        IntStream.range(0, 9).forEach(i -> {
            if (i % 3 == 0)
                println(" -----------------------");

            // Loop For with Streams
            IntStream.range(0, 9).forEach(j -> {
                if (j % 3 == 0)
                    print("| ");

                print(ifGridColumnEmpty(grid, i * 9 + j) == 0 ? " " : grid.get(i * 9 + j));
                print(" ");
            });
            println("|");
        });
        println(" -----------------------");
    }

    /**
     * Print an object to an output
     * @param o Object to print
     */
    private static void print(Object o) {
        System.out.print(o);
    }

    /**
     * Println an object to an output
     * @param o Object to println
     */
    private static void println(Object o) {
        System.out.println(o);
    }

    /**
     * Test on the value of the Grid.
     * @param grid Grid on which the test is launched on.
     * @param index Index of the value inside the Grid.
     * @return Return the value for the index or 0.
     */
    private static Integer ifGridColumnEmpty(List<Integer> grid, int index) {
        return index >= grid.size() ? 0 : grid.get(index);
    }

    /**
     * Resolve the given Grid and display the result.
     */
    private static void solveGridAndDisplay() {

        resultGrid = new ArrayList<>(initialGrid);
        if (sve(0, 0))
            displayGrid(resultGrid);
        else
            println("NO SOLUTION");
    }

    /**
     * Core of the resolving of Grid (recursive function).
     * @param col Column of the Grid
     * @param lin Line of the Grid
     * @return Return TRUE if the resolving is ok or FALSE
     */
    private static boolean sve(int col, int lin) {

        // End of the column => grid solved
        if (col == 9)
            return true;

        int index = col * 9 + lin;

        if (initialGrid.get(index) != 0) { // skip filled cells
            resultGrid.add(index, initialGrid.get(index));
            return sve(col, lin + 1);
        }

        for (val = 1; val < 10; ++val) {
            ok = true;

            ok = IntStream.range(0, 9) // row
                    .filter(k -> val == initialGrid.get(k * 9 + lin))
                    .anyMatch(x -> false);

            ok = IntStream.range(0, 9) // col
                    .filter(k -> val == initialGrid.get(col * 9 + k))
                    .anyMatch(x -> false);

            int box = lin - (col * 9);

            IntStream.range(0, 3)
                    .forEach(m -> ok = IntStream.range(0, 3) // box
                                    .filter(k -> val == initialGrid.get(k * 9 + m + box))
                                    .anyMatch(x -> false)
                    );

            if (ok) {
                resultGrid.remove(index);
                resultGrid.add(index, val);
                if (sve(col, lin + 1))
                    return true;
            }
        }

        resultGrid.remove(index);
        resultGrid.add(index, 0);
        return false;
    }
}
