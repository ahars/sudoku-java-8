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

    public static void main(String[] args) {

        try {
            initGivenGrid(file);
            solveGrid();
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("Error in the given grid : " + sioobe);
        } catch (NumberFormatException nfe) {
            System.out.println("Wrong character in the given grid : " + nfe);
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Error in the given grid : " + ioobe);
        } catch (IOException ioe) {
            System.out.println("Error in opening the file : " + ioe);
        }
    }

    private static void initGivenGrid(String file) throws IOException {

        initialGrid = new ArrayList<>();

        Stream<String> lines = Files.lines(Paths.get(path, file));

        lines.map(line -> Arrays.asList(line.split(" ")))
                .forEach(x -> x.forEach(t -> Arrays.asList(t.split(""))
                        .forEach(z -> initialGrid.add(Integer.parseInt(z)))));
        lines.close();

        displayGrid(initialGrid);
    }

    private static void displayGrid(List<Integer> grid) {

        IntStream.range(0, 9).forEach(i -> {
            if (i % 3 == 0)
                System.out.println(" -----------------------");

            IntStream.range(0, 9).forEach(j -> {
                if (j % 3 == 0)
                    System.out.print("| ");

                System.out.print(ifGridColumnEmpty(grid, i * 9 + j) == 0 ? "  " : grid.get(i * 9 + j) + " ");
            });
            System.out.println("|");
        });
        System.out.println(" -----------------------");
    }

    private static Integer ifGridColumnEmpty(List<Integer> grid, int index) {
        return index >= grid.size() ? 0 : grid.get(index);
    }

    private static void solveGrid() {

        resultGrid = new ArrayList<>(initialGrid);
        if (sve(0, 0)) {
            displayGrid(resultGrid);
        } else
            System.out.println("NO SOLUTION");
        displayGrid(resultGrid);
    }

    // DON'T WORK
    private static boolean sve(int i, int j) {

        if (i == 9) // end of the column => grid solved
            return true;

        int index = i * 9 + j;

        if (initialGrid.get(index) != 0) { // skip filled cells
            resultGrid.add(index, initialGrid.get(index));
            return sve(i, j + 1);
        }

        for (val = 1; val < 10; ++val) {
            ok = true;

            ok = IntStream.range(0, 9) // row
                    .filter(k -> val == initialGrid.get(k * 9 + j))
                    .anyMatch(x -> false);

            ok = IntStream.range(0, 9) // col
                    .filter(k -> val == initialGrid.get(i * 9 + k))
                    .anyMatch(x -> false);

            int box = j - (i * 9);

            IntStream.range(0, 3)
                    .forEach(m -> ok = IntStream.range(0, 3) // box
                                    .filter(k -> val == initialGrid.get(k * 9 + m + box))
                                    .anyMatch(x -> false)
                    );

            if (ok) {
                resultGrid.remove(index);
                resultGrid.add(index, val);
                if (sve(i, j + 1))
                    return true;
            }
        }

        resultGrid.remove(index);
        resultGrid.add(index, 0);
        return false;
    }
}
