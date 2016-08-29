package com.ammvision.sudoku;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Created by ashiminty on 9/21/2014.
 */
public class SudokuPuzzels {

    private static List<Puzzel> Easy;
    private static List<Puzzel> Medium;
    private static List<Puzzel> Hard;

    private static int easy = 0;
    private static int medium = 0;
    private static int hard = 0;

    public SudokuPuzzels()
    {
        easy = 0;
        medium = 0;
        hard = 0;
    }

    private static void PopulatePuzzels()
    {
        Easy = new ArrayList<Puzzel>();
        Easy.add(new Puzzel("13.2..74..25.1....48..6..5....78.21.5...9.37.9...3...5.4...689..53..14..6........"));
        Easy.add(new Puzzel("1.....276..914.....2...6.91.8...961.73..84.....2..5.8.5.6..3.....7....5.34.59...."));
        Easy.add(new Puzzel("4.6...2.957.2.6.....1..5..86.34817..7..5..3....5.......89...43..6......13.4....67"));
        Easy.add(new Puzzel("487.5..6.9..4....32.6.895....4.156..1....4.5..782..........8.7.75.....3..2..3741."));
        Easy.add(new Puzzel("19.5...64.4...87...781..9..7...........3792....9.1435..52.3647..1....68..8..9..3."));
        Easy.add(new Puzzel("437.1.26..5...63.1.......85.7462......97.3.1..2.9.5....93......7..8.2.348..379..."));
        Easy.add(new Puzzel("1..2..3.69....742..7......1.....45.7.1..9.6..245..8....58732....6.....3....9....4"));
        Easy.add(new Puzzel("2..61.378...9..1....1.37..5..514.2.39.2.8.4..84..7.........475..5..9.81.........."));
        Easy.add(new Puzzel("749..13.2.2.6..4..6.39.417.2...8...3...1...9...4.6.....3.2.8.......3.81..7859.2.."));
        Easy.add(new Puzzel("24...5...3...246.9.9.3..5...6.5..9..7.3...4.1....78.....82..745..58.31621........"));

        Medium = new ArrayList<Puzzel>();
        Medium.add(new Puzzel("892..3.14.............68.7.45..8...1..8...2..1.37..5...71..6.5.5.92...8.6....7..9"));
        Medium.add(new Puzzel("91624....23..........1.8..2521.....7.89...51.76..9...3....8.721.9...46.5.73..5..."));
        Medium.add(new Puzzel("73.294.6...1..6...45..8.......3...86283..74...6.....1..7..25...8...7......54..79."));
        Medium.add(new Puzzel("16.3.......85.934...427..58...9.5.3.........5..3.1.4.....6..9.4.9..83..6.52...7.."));
        Medium.add(new Puzzel("3.4.185.91...64....82.......1...694..96.........7.163..2..53....579...8.......4.."));
        Medium.add(new Puzzel("468.3.57...2.5.81.13...96....41957..9.......4..1..3.9......4.21.4....9.8...5....."));
        Medium.add(new Puzzel("8...........6..125.4....6..7...498.136.7...49.....8.5..218.53..5.9...4..63...2..."));
        Medium.add(new Puzzel("6..72..31.351698..9...8.....56.9......1....7.34.5..2....86.375..7..5...3.......8."));
        Medium.add(new Puzzel("7.26.9..1.6.15...7.1..823......3.5.85..8.7.1.....2...9..9.7.....46.1......85....4"));
        Medium.add(new Puzzel("5.3.68.9.96.7.2.8.8.........49.1..5.......8..3....94..1......5.31..6.5...7.8329..."));

        Hard = new ArrayList<Puzzel>();
        Hard.add(new Puzzel("43876.1.22...9.53......26.8..4.23.5.3.....8..6..........5.1.3.9.1.....8.9..6...7."));
        Hard.add(new Puzzel("1.8369.75.7..1....3..........7..21.9.......5....9.1.4.98.52.4..52.6.4..3.....8..."));
        Hard.add(new Puzzel("13.27...6.6...3.2.8.9.6..5.4.79..2......1.......75.81.9.85..7.......7.9...1......"));
        Hard.add(new Puzzel("5.9.7...3..86....21....3......9.7.46..3.8...165.3.......6....9..8..2.6.5.9.7.1..."));
        Hard.add(new Puzzel("6....13...9.....1..3..29.4...7.56..........2......39..3.1..7489.4..187..76......."));
        Hard.add(new Puzzel("1.5...96..8.5.......237..45...9.....247...15..6..4........94.2...17.3..4.58.....3"));
        Hard.add(new Puzzel("3.......9...4.3...7.46....59...2..6...583..4...8.1.3...83...6..6.9..52.4.....9..."));
        Hard.add(new Puzzel("8.....147..4.9...6.237..8....9..1..2...32.....8...94....1......9.614..385...63..."));
        Hard.add(new Puzzel("5..68.91..8..........29...6.2.....438....15.2....7....7945..3....3...42....1.4.9."));
        Hard.add(new Puzzel("1...68..9.849......3..42......5...7.79..3.4...5...49...4...3.....6..7..4..2.86.3."));
    }
    
    public static int[][] GetNextEasyPuzzel()
    {
        if(Easy == null) PopulatePuzzels();

        if(easy >= Easy.size()) easy = 0;

        int[][] d = new int[9][9];
        String puzzle = Easy.get(easy).Puzzle;
        easy++;

        int x = 0;
        for (int i=0;i<81;i++)
        {
            int y = i / 9;
            if(x == 9)
                x = 0;

            String s = puzzle.substring(i,i+1);
            if (s.equals("."))  // unsolved cells, all values are possible
                d[y][x] = 0;
            else  // solved cells, only one possible value
                d[y][x] = Integer.parseInt(s);

            x++;
        }

        return d;
    }

    public static int[][] GetNextMediumPuzzel()
    {
        if(Medium == null) PopulatePuzzels();

        if(medium >= Medium.size()) medium = 0;

        int[][] d = new int[9][9];
        String puzzle = Medium.get(medium).Puzzle;
        medium++;

        int x = 0;
        for (int i=0;i<81;i++)
        {
            int y = i / 9;
            if(x == 9)
                x = 0;

            String s = puzzle.substring(i,i+1);
            if (s.equals("."))  // unsolved cells, all values are possible
                d[y][x] = 0;
            else  // solved cells, only one possible value
                d[y][x] = Integer.parseInt(s);

            x++;
        }

        return d;
    }


    public static int[][] GetNextHardPuzzel()
    {
        if(Hard == null) PopulatePuzzels();

        if(hard >= Hard.size()) hard = 0;

        int[][] d = new int[9][9];
        String puzzle = Hard.get(hard).Puzzle;
        hard++;

        int x = 0;
        for (int i=0;i<81;i++)
        {
            int y = i / 9;
            if(x == 9)
                x = 0;

            String s = puzzle.substring(i,i+1);
            if (s.equals("."))  // unsolved cells, all values are possible
                d[y][x] = 0;
            else  // solved cells, only one possible value
                d[y][x] = Integer.parseInt(s);

            x++;
        }

        return d;
    }

    /*private void Generate()
    {
        try
        {
            BufferedWriter bW = new BufferedWriter(new FileWriter("/storage/emulated/0/Documents/sudoku.txt", true));

            String puzzle= "........."+
                    "........."+
                    "........."+
                    "........."+
                    "........."+
                    "........."+
                    "........."+
                    "........."+
                    ".........";

            SudokuGenerator generator=new SudokuGenerator();
            generator.init(puzzle);

            generator.generate(seed);

            puzzle = generator.outputPuzzle();

            bW.append(puzzle);
            bW.append("#");

            Easy.add("puzzle);

            bW.close();

        }
        catch(Exception ex) {

            Easy.add(""");

        }
    }*/
}
