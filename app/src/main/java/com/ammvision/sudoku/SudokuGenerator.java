package com.ammvision.sudoku;

import java.util.ArrayList;
import java.lang.reflect.Array;

public class SudokuGenerator
{
    // structure for holding sudoku grid
    ArrayList<Integer>[] cells=new ArrayList[81];

    private int[][] row=new int[][] {
            { 0, 1, 2, 3, 4, 5, 6, 7, 8}, // row 0
            { 9,10,11,12,13,14,15,16,17}, // row 1
            {18,19,20,21,22,23,24,25,26}, // row 2
            {27,28,29,30,31,32,33,34,35}, // row 3
            {36,37,38,39,40,41,42,43,44}, // row 4
            {45,46,47,48,49,50,51,52,53}, // row 5
            {54,55,56,57,58,59,60,61,62}, // row 6
            {63,64,65,66,67,68,69,70,71}, // row 7
            {72,73,74,75,76,77,78,79,80}  // row 8
    };

    private int[][] column=new int[][] {
            { 0, 9,18,27,36,45,54,63,72}, // column 0
            { 1,10,19,28,37,46,55,64,73}, // column 1
            { 2,11,20,29,38,47,56,65,74}, // column 2
            { 3,12,21,30,39,48,57,66,75}, // column 3
            { 4,13,22,31,40,49,58,67,76}, // column 4
            { 5,14,23,32,41,50,59,68,77}, // column 5
            { 6,15,24,33,42,51,60,69,78}, // column 6
            { 7,16,25,34,43,52,61,70,79}, // column 7
            { 8,17,26,35,44,53,62,71,80}  // column 8
    };

    private int[][] region=new int[][] {
            { 0, 1, 2, 9,10,11,18,19,20}, // region 0, upper left region (North West)
            { 3, 4, 5,12,13,14,21,22,23}, // region 1, top region (North)
            { 6, 7, 8,15,16,17,24,25,26}, // region 2, upper right region (North East)
            {27,28,29,36,37,38,45,46,47}, // region 3, left region (west)
            {30,31,32,39,40,41,48,49,50}, // region 4, middle region
            {33,34,35,42,43,44,51,52,53}, // region 5, right region (east)
            {54,55,56,63,64,65,72,73,74}, // region 6, lower left region (South West)
            {57,58,59,66,67,68,75,76,77}, // region 7, bottom region (south)
            {60,61,62,69,70,71,78,79,80}  // region 8, lower right right (South East)
    };


    public void init(String puzzle)
    {
        for (int i=0;i<81;i++)
        {
            cells[i]=new ArrayList<Integer>();
            String s=puzzle.substring(i,i+1);
            if (s.equals("."))  // unsolved cells, all values are possible
                for (int digit=1;digit<=9;digit++) cells[i].add(digit);
            else  // solved cells, only one possible value
                cells[i].add(Integer.parseInt(s));
        }
    }

    // display all possible of the grid
    public void printGrid()
    {
        String delimiter="";

        for (int i=0;i<81;i++)
        {
            String s="";
            for (int j=0;j<cells[i].size();j++)
            {
                s+=cells[i].get(j);
            }
            // make sure each cell contains exactly 8 character
            for (int j=0;j<8-cells[i].size();j++) s+=" ";

            if (s.length()>8) s="ALL     ";  // overflow
            s=delimiter+s;
            delimiter="|";
            System.out.print(s);
            if ((i+1)%9==0) { System.out.println(); delimiter=""; }
        }
    }


    // eliminate duplicate digit of the same row
    public boolean rowElimination()
    {
        boolean newSolved=false;
        for (int i=0;i<81;i++) // loop over all cells
        {
            int n=cells[i].size(); // get number of possible values of current cell
            if (n==1)  // this is a solved cell
            {
                Integer solvedDigit=cells[i].get(0);
                int rowNumber=i/9;   // which row does the current cell belong to ?
                for (int j=0;j<9;j++)  // loop over all cells in the affected row
                {
                    int cellNum=row[rowNumber][j];
                    // all other cells in the row cannot contain the solved digit
                    if (cellNum!=i && cells[cellNum].contains(solvedDigit))
                    {
                        cells[cellNum].remove(solvedDigit);
                        if (cells[cellNum].size()==1) newSolved=true;  // newly solved cell found
                    }
                }
            }
        }
        return newSolved;
    }

    // eliminate duplicate digit of the same column
    public boolean columnElimination()
    {
        boolean newSolved=false;
        for (int i=0;i<81;i++) // loop over all cells
        {
            int n=cells[i].size(); // get number of possible values of current cell
            if (n==1)  // this is a solved cell
            {
                Integer solvedDigit=cells[i].get(0);
                int columnNumber=i % 9;   // which column does the current cell belong to ?
                for (int j=0;j<9;j++)  // loop over all cells in the affected column
                {
                    int cellNum=column[columnNumber][j];
                    // all other cells in the column cannot contain the solved digit
                    if (cellNum!=i && cells[cellNum].contains(solvedDigit))
                    {
                        cells[cellNum].remove(solvedDigit);
                        if (cells[cellNum].size()==1) newSolved=true;  // newly solved cell found
                    }
                }
            }
        }
        return newSolved;
    }

    // eliminate duplicate digit of the same region
    public boolean regionElimination()
    {
        boolean newSolved=false;
        for (int i=0;i<81;i++) // loop over all cells
        {
            int n=cells[i].size(); // get number of possible values of current cell
            if (n==1)  // this is a solved cell
            {
                Integer solvedDigit=cells[i].get(0);
                int rowNumber = i / 9;    // which row does the current cell belong to ?
                int columnNumber=i % 9;   // which column does the current cell belong to ?
                int regionNumber=3*(rowNumber/3) + columnNumber/3;
                for (int j=0;j<9;j++)  // loop over all cells in the affected region
                {
                    int cellNum=region[regionNumber][j];
                    // all other cells in the region cannot contain the solved digit
                    if (cellNum!=i && cells[cellNum].contains(solvedDigit))
                    {
                        cells[cellNum].remove(solvedDigit);
                        if (cells[cellNum].size()==1) newSolved=true;  // newly solved cell found
                    }
                }
            }
        }
        return newSolved;
    }


    public void solveByElimination()
    {
        boolean newCellSolved=false;
        while (true)
        {
            newCellSolved=false;
            if (rowElimination()) newCellSolved=true;
            if (columnElimination()) newCellSolved=true;
            if (regionElimination()) newCellSolved=true;
            if (!newCellSolved) break;  // no newly solved cell generated, quit
        }
    }

    // check whether the puzzle is solved
    public boolean isSolved()
    {
        boolean solved=true;
        for (int i=0;i<81;i++)
        {
            if (cells[i].size()!=1) { solved=false; break; }
        }
        return solved;
    }

    // check whether the puzzle is in a conflict state
    // if some cell contains no possible values at all, then it is in a conflict state
    public boolean isConflict()
    {
        boolean conflict=false;
        for (int i=0;i<81;i++)
        {
            if (cells[i].size()==0) { conflict=true; break; }
        }
        return conflict;
    }

    // backup or restore a puzzle
    private void cellCopy(ArrayList<Integer>[] src,ArrayList<Integer>[] dest)
    {
        for (int i=0;i<81;i++)
        {
            dest[i].clear();
            for (int j=0;j<src[i].size();j++)
            {
                dest[i].add(src[i].get(j));
            }
        }
    }

    public boolean solveByRecursion()
    {
        // before doing anything, backup the puzzle first
        ArrayList<Integer>[] backupCells=new ArrayList[81];
        for (int i=0;i<81;i++) // allocate backup structure
        {
            backupCells[i]=new ArrayList();
        }

        cellCopy(cells, backupCells);  // backup

        // get next unsolved cell with minimum number of possible values
        int unsolvedCellIndex=-1;
        int minPossible=100;
        for (int i=0;i<81;i++)
        {
            int nPossible = cells[i].size();
            if (nPossible>1  && nPossible<minPossible)
            {
                minPossible = nPossible;
                unsolvedCellIndex = i;
            }
        }

        // no unsolved cell, the puzzle is solved
        if (unsolvedCellIndex==-1) return true;

        for (int j=0;j<cells[unsolvedCellIndex].size();j++) // try all possible digits
        {
            Integer digit = cells[unsolvedCellIndex].get(j);
            cells[unsolvedCellIndex].clear();  // clear all values
            cells[unsolvedCellIndex].add(digit);  // then fill it with only one value
            solveByElimination();
            if (isSolved()) return true;  // solved
            if (!isConflict())
            {
                // The unsolved cell is filled with a trial value, but the puzzle is not solved,
                // call myself recursively to do a trial and error on another unsolved cell.
                if (solveByRecursion()) return true; // solved
            }
            // the trial digit cannot solve the puzzle
            cellCopy(backupCells,cells); // restore from backup, try next digit
        }
        // all values of a cell have been tried
        return false;   // cannot solve this way.
    }


    public int solutionCount()
    {
        // before doing anything, backup the puzzle first
        ArrayList<Integer>[] backupCells=new ArrayList[81];
        for (int i=0;i<81;i++) // allocate backup structure
        {
            backupCells[i]=new ArrayList();
        }
        cellCopy(cells, backupCells);  // backup

        // get next unsolved cell with minimum number of possible values
        int unsolvedCellIndex=-1;
        int minPossible=100;
        for (int i=0;i<81;i++)
        {
            int nPossible = cells[i].size();
            if (nPossible>1  && nPossible<minPossible)
            {
                minPossible = nPossible;
                unsolvedCellIndex = i;
            }
        }

        if (unsolvedCellIndex==-1 && !isConflict()) return 1;

        int solutionCount=0;
        for (int j=0;j<cells[unsolvedCellIndex].size();j++) // try all possible digits
        {
            Integer digit = cells[unsolvedCellIndex].get(j);
            cells[unsolvedCellIndex].clear();  // clear all values
            cells[unsolvedCellIndex].add(digit);  // then fill it with only one value
            if (solveByRecursion())
            {
                solutionCount++;
            }
            if (solutionCount>1) break; // more than 1 solution found
            cellCopy(backupCells,cells); // restore from backup, try next digit
        }

        cellCopy(backupCells,cells); // restore from backup
        return solutionCount;

    }

    public void generate(int seed)
    {

        // allocate structure for potential puzzle
        ArrayList<Integer>[] potentialPuzzle=new ArrayList[81];
        for (int i=0;i<81;i++)
        {
            potentialPuzzle[i]=new ArrayList();
        }

        // backup
        cellCopy(cells, potentialPuzzle);


        java.util.Random random = new java.util.Random(seed);

        while (true)
        {
            cellCopy(potentialPuzzle,cells);
            int cellIndex=Math.abs(random.nextInt()) % 81;  // 0-80
            int trialDigit=(Math.abs(random.nextInt()) % 9)+1;  // 1-9
            if (cells[cellIndex].size()!=9) continue;  // try unsolved cell only

            cells[cellIndex].clear();
            cells[cellIndex].add(trialDigit);

            int c=solutionCount();
            if (c==1) return;  // puzzle with unique solution is generated sucessfully
            if (c>1) // this trial produces a potential puzzle with multiple solutions
            {
                potentialPuzzle[cellIndex].clear();
                potentialPuzzle[cellIndex].add(trialDigit);
            }
        }

    }

    public String outputPuzzle()
    {
        String puzzel = "";
        for (int i=0,row=0;row<9;row++)
        {
            for (int column=0;column<9;column++,i++)
            {
                if (cells[i].size()>1)
                    puzzel+=".";
                else
                    puzzel+=cells[i].get(0);
            } // next column
        } // next row

        return puzzel;
    }
}