package edu.commonwealthu.revolution;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Revolution game.
 * This game will create a table which length and width is specified by the user.
 * Revolution is a game where you are given a set of ascending numbers starting
 *  at 1 that are scrambled and the user must rotate 2x2 blocks of the table
 *  until the numbers are sorted in ascending order
 *
 * ex. in a 3x3 grid sort the numbers 1-9 in order to win
 *
 * @author Justin Peasley
 * 9-9-2024
 */
public class Revolution
{
    private final int row;
    private final int col;
    private final int solDepth;
    private int copyLimiter =0; //used to limit copying
                                // the base table until its completely scrambled
    private int[][] table;
    private final Stack<int[][]> gameStates = new Stack<int[][]>();

    //constructors for specified and unspecified dimensions
    public Revolution(int solDepth) {
        this(3,3, solDepth); //constructor chaining
    }

    public Revolution(int x, int y, int solDepth) {
        //check if at least 3,3
        if(x>3 || y>3)
            {row = x; col = y;}
        else {row=3; col=3;}
        this.solDepth = solDepth;
        initializeTable();
        randRotate(solDepth);
    }

    /**
     * Creates a base solved table to work with
     */
    private void initializeTable()
    {
        int initial=0; //used to fill array
        table= new int[row][col];

        //initialize the array values
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
            {table[i][j]=(initial+=1);}
        }
    }

    /**
     * returns the number in col,row position of table
     * @param col col of table
     * @param row row of table
     * @return number of col,row position
     */
    public final String getOccupant(int col, int row)
    {return Integer.toString(table[col][row]);}

    /**
     * undoes the last move and restores last board
     * @return false if no moves to undo otherwise true
     */
    public boolean undo()
    {
        if(gameStates.size()<2) return false;
        table = gameStates.pop();
        table = gameStates.pop();
        return true;
    }

    /**
     * makes deep copy of the grid
     */
    private void copyOfGrid()
    {
        int[][] copy = new int[row][col];
        for (int i = 0; i < row; i++) {
                System.arraycopy(table[i], 0, copy[i], 0, col);
        }
        gameStates.push(copy);
    }

    /**
     *  this will generate the table and scramble the table for the puzzle based on
     *      the supplied solution depth
     * @param depth steps to solve solution
     */
    private void randRotate(int depth)
    {
        int anchorX=0, anchorY=0, LorR=0;
        //used to check if the anchor point is same as last rotate to prevent undoing in
        //scrambling process
        int tempX=row, tempY=col; //row & col are unreachable nums for random generation

        //for randomly picking an anchor point to rotate & direction of rotation
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = 0; i < depth; i++) {
            anchorX = rand.nextInt(0, row-1);
            anchorY = rand.nextInt(0, col-1);

            //test if the last anchor point same as last instance or not
            while(tempX == anchorX && tempY == anchorY) {
                anchorX = rand.nextInt(0, row - 1);
                anchorY = rand.nextInt(0, col - 1);
            }

            LorR=rand.nextInt(0,2);
            if(LorR == 0) rotateLeft(anchorY, anchorX);
            else rotateRight(anchorY,anchorX);
            copyLimiter++;
            tempX=anchorX; tempY=anchorY;
        }
    }

    /**
     *  rotates the 2x2 section based on anchor point right
     * @param row anchor row
     * @param col anchor col
     */
    public void rotateRight(int col, int row)
    {
        if(!validAnchor(row, col)) return;

        int temp=table[row][col];
        table[row][col] = table[row+1][col];    //downLeft
        table[row+1][col] = table[row+1][col+1];//DownRight
        table[row+1][col+1] = table[row][col+1];//topRight
        table[row][col+1] = temp;    //Anchor

        if(copyLimiter >= solDepth-1) copyOfGrid();
    }

    /**
     * rotates the 2x2 section based on anchor point left
     * @param row anchor row
     * @param col anchor col
     */
    public void rotateLeft(int col, int row)
    {
        if(!validAnchor(row, col)) return;

        int temp=table[row][col];
        table[row][col] = table[row][col+1];    //downLeft
        table[row][col+1] = table[row+1][col+1];//DownRight
        table[row+1][col+1] = table[row+1][col];//topRight
        table[row+1][col] = temp;    //Anchor

        if(copyLimiter >= solDepth-1) copyOfGrid();
    }

    /**
     * test if all the numbers are in ascending order
     * @return true and ends game if so else continues game
     */
    public boolean isOver()
    {
        int temp =1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(table[i][j] != temp) return false;
                temp+=1;
            }
        }
        return true;
    }

    /**
     * @return moves taken to win
     */
    public int moves()
    {return gameStates.size();}

    public boolean validAnchor(int x, int y)
    {
        return !(x>row-2 || y>col-2);
    }

    @Override
    public String toString()
    {
        String output ="  "; String midLine = "   "; String botLine = "   ";
        for (int i = 0; i < col; i++)  output += String.format("    %d", i);
        output+="\n";

        //builds table lines for output
        for (int i = 1; i < col; i++) {
            if(i==1)     {output +="   ┌────"; midLine+="├────"; botLine+="└────";}
            if(i==col-1) {output+="┬────┐\n"; midLine+="┼────┤"; botLine+="┴────┘";}
            else         { output += "┬────"; midLine+="┼────"; botLine+="┴────";}
        }

        //Loop to build the table
        for (int i = 0; i < row; i++) {
            output += String.format(" %d │",i);
            for (int j = 0; j < col; j++)
            {output += String.format(" %02d │", table[i][j]);}

            output+= "\n";
            if(i < this.row-1)output +=midLine + "\n";
        }
        output+=botLine;
        return output;
    }
}