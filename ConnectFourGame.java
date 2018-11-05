/****************************************************************************************************************************************************
 *  @Author:         Corey M. Moura
 *  @Date:           February 17, 2018
 *  @Professor:      Dr. Trafftz
 *  @Project:        Project 2 of CS163: Connect-4 Game, player vs computer 
 *  @Notes:          Before the game begins, the player will have the option to select the size of the board to play with and also the difficulty level.
 *                   The difficulty level can be selected from 1 to 10.  by selecting 1, the player will have 10 seconds to play their move before the 
 *                   computer skips them, and plays.  At difficulty level 10, the computer will play a move once every second.  When there is a winner,
 *                   the player will be given a choice to play again or to quit.  If the player would like to play again, the first move of that next game
 *                   will be made by whoever won the last game.  
 *                   
 *                   Durring the computers move, it runs through a set of proiotities.  It checks for a winning move, if the player can win on their 
 *                   next move (computers blocking move), then it checks its "smart moves" options.  The "smart moves" check, will play a chip based from 
 *                   the position of the computers last move.  It will check every space around the last move for an openning.  This sequence of checking 
 *                   open squares is determined by the current strategy for winning, by the computer.  So if the computer just played a horizontal chip
 *                   the strategy becomes to win by horizontal moves and it will check the sides of the last chip played before checking other
 *                   spaces, like the above space or diagonals.  If the computer is surrounded, the computer will make a random column selection and 
 *                   continue playing.
/****************************************************************************************************************************************************/

/** Libraries used for the creation of this Class **/
import java.awt.*; 
import java.awt.Point;
import java.util.*;

public class ConnectFourGame {

    /** Final int variables**/
    public static final int USER = 0; /** a possible value for player */ 
    public static final int COMPUTER = 1; /** a possible value for player */
    public static final int FULLCOLUMN = -1;

    /** Variables used in this class **/
    private int size;
    private int player;
    private int playerCount;
    private int connections;
    private int lastColumn;
    private int lastRow;
    private int counter;
    private int nextCol;
    private int trackStrategy;
    private int userPoints;
    private int computerPoints;

    /** Double array used to assign values in the matrix corresponding to a players move  **/
    private int[][] board;

    /** A constructor method that initializes all fields, passed bSize from the GUI  **/
    public ConnectFourGame (int pSize) {
        size = pSize;
        trackStrategy = -1;                              // Used to select the smart moves performed on the computers turn
        board = new int[pSize][pSize];                  // Array used to store the values of the players where they choos to play
        this.playerCount = 2;
        this.connections = 4;
        this.player = 0;
        reset();                                        // Reset method here is used to set the initial conditions of the game (-1 to all elements of the double array)
    }

    /****************************************************************************************************************************************************/
    /*  Will swap the value of the last move from user to computer for blocking move  */
    public void setBlockingMove(){
        board[lastRow][lastColumn] = COMPUTER;
    }

    /****************************************************************************************************************************************************/
    /*  Switches player from the current player to the other.*/
    public int nextPlayer(int pPlayer){
        player = pPlayer;        
        return player;
    }

    /****************************************************************************************************************************************************/
    /*  Resets last move: Assigns -1 to the last assignment on the board*/
    public void undo() {
        if (lastRow != -1) {
            board[lastRow][lastColumn] = -1;
        }
    }

    /****************************************************************************************************************************************************/
    /*  Resets the board for a new game. This method is called from the following ConnectFourPanel class.*/
    public void reset() {
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                board[i][j] = -1;
            }
        }
    }

    /****************************************************************************************************************************************************/
    /*  Determines which column a chip will fall into. This method is called from the following ConnectFourPanel class, whenever the user clicks a JButton.*/
    public int selectCol (int pCol) {
        lastColumn = pCol;
        for (int row = size - 1; row >= 0; row--){
            if (board[row][pCol] == -1) {
                board[row][pCol] = player;
                lastRow = row;
                return row;
            }
        }
        return FULLCOLUMN;
    }
    
    /****************************************************************************************************************************************************/

    public void setUserScore(int move){
        if(move == 0){
            userPoints = 0;
        }
        if(move == 1){
            userPoints += 5;
        }
        if(move == 2){
            userPoints += 50;
        }
    }
    
    /****************************************************************************************************************************************************/
    public void setComputerScore(int move){
        if(move == 0){
            computerPoints = 0;
        }
        if(move == 1){
            computerPoints += 2;
        }
        if(move == 2){
            computerPoints += 10;
        }
        if(move == 3){
            computerPoints += 50;
        }
    }

    /****************************************************************************************************************************************************/
    public int getUserScore(){
        return userPoints;
    }
    
    /****************************************************************************************************************************************************/
    public int getComputerScore(){
        return userPoints;
    }

    /****************************************************************************************************************************************************/
    public String toString(){
        String result = "";
        if(player == 0){
            result = result + userPoints; 
        }
        else{
            result = result + computerPoints;
        }
        return result;
    }

    /****************************************************************************************************************************************************/
    /*  Called mulitple times from the panel class to determine winners and find the blocking move from the computer  */
    public boolean isWinner() {
        return isWinner(player);
    }

    /****************************************************************************************************************************************************/
    /*  Calls methods which contain specific if statements; each method checks a specific way to win from the last chip played  */
    private boolean isWinner(int player) {
        if (checkVerticalWin(player)) 
            return true;
        if (checkHorizontalWin(player)) 
            return true;
        if (checkDiagonal1Win(player))
            return true;
        if (checkDiagonal2Win(player))
            return true;
        return false;
    }

    /****************************************************************************************************************************************************/
    /*  Checks the 4 positions values to see if they are all the same which would indicate a win  */
    private boolean checkVerticalWin(int player) {
        for(int i = 0;i < size-3;i++) {
            for(int j=0;j < size;j++) {
                if (player == board[i][j] &&
                board[i][j] == board[i+1][j] &&
                board[i][j] == board[i+2][j] &&
                board[i][j] == board[i+3][j] ) {
                    System.out.print("V");

                    return true;
                }
            }
        }
        return false;

    }

    /****************************************************************************************************************************************************/
    /*  Checks the 4 positions values to see if they are all the same which would indicate a win  */
    private boolean checkHorizontalWin(int player) {
        for(int i = 0; i < size; i++) {
            for(int j=0; j < size-3; j++) {
                if (player == board[i][j] &&
                board[i][j] == board[i][j+1] &&
                board[i][j] == board[i][j+2] &&
                board[i][j] == board[i][j+3] ) {
                    System.out.print("H");
                    return true;
                }
            }
        }
        return false;

    }

    /****************************************************************************************************************************************************/
    /* Checking for descending diagonals (from left to right) */
    private boolean checkDiagonal1Win(int player) {
        for(int i = 0; i < size - 3; i++) {
            for(int j = 0;j < size - 3;j++) {
                if (player == board[i][j] &&
                board[i][j] == board[i+1][j+1] &&
                board[i][j] == board[i+2][j+2] &&
                board[i][j] == board[i+3][j+3] ) {
                    System.out.print("D1");
                    return true;
                }
            }
        }
        return false;

    }

    /****************************************************************************************************************************************************/
    /* Checking for ascending diagonals (from left to right)  */
    private boolean checkDiagonal2Win(int player) {
        for(int i = 3; i < size; i++) {
            for(int j = 0;j < size - 3;j++) {
                if (player == board[i][j] &&
                board[i][j] == board[i-1][j+1] &&
                board[i][j] == board[i-2][j+2] &&
                board[i][j] == board[i-3][j+3] ) {
                    System.out.print("D2");
                    return true;
                }
            }
        }
        return false;

    }

    /****************************************************************************************************************************************************/
    /*  Passed a string from the panel class which is checked by the switch statement and returns the users desire difficulty level as an intiger in milliseconds*/
    public int setDifficultyLevel(String dLevel){
        System.out.print("LevelMethod");
        int delay = 10;
        switch (dLevel){
            case "1":
            System.out.print("case1");
            delay = 10000;
            break;

            case "2":
            delay = 9000;
            break;

            case "3":
            delay = 8000;
            break;

            case "4":
            delay = 7000;
            break;

            case "5":
            delay = 6000;
            break;

            case "6":
            delay = 5000;
            break;

            case "7":
            delay = 4000;
            break;

            case "8":
            delay = 3000;
            break;

            case "9":
            delay = 2000;
            break;

            case "10":
            delay = 1000;
            break;
        }
        return delay;
    }

    /****************************************************************************************************************************************************/
    /*  Allows the Panel Class to get the current strategy of the computer*/
    public int getTrackStrategy(){
        return trackStrategy;
    }

    /****************************************************************************************************************************************************/
    /*  Allows the strategy to be changed depending on the availability of open spaces relative to the last move made */
    public void setTrackStrategy(int strategy){
        trackStrategy = strategy;
    }

    /****************************************************************************************************************************************************/
    /* Passed the row and column of the last move the computer made. Depending on the strategy of the computer, priorities will be made for checking and placing the next move */
    public int smartMoves(int bRow, int bCol){
        /*  If there is a problem with the indexing of any of the cases or methods called in the switch case, the program will catch the error preventing a crash  */
        System.out.println("Smart Move");
        try{
            switch (trackStrategy){ 
                case 0:
                randomMove();
                break;

                //Last Move was horizontal
                case 1:
                if(checkSides(bRow, bCol))      return nextCol;
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkUpperRight(bRow, bCol)) return nextCol;

                //Last move was verticle
                case 2:
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkUpperRight(bRow, bCol)) return nextCol;  
                if(checkSides(bRow, bCol))      return nextCol;

                //Last move was lower left 
                case 3:
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkSides(bRow, bCol))      return nextCol;
                if(checkUpperRight(bRow, bCol)) return nextCol; 
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;        

                //Last move was lower Right
                case 4:
                if(checkUpperRight(bRow, bCol)) return nextCol;
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkSides(bRow, bCol))      return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;

                //Last move was upper right
                case 5:
                if(checkSides(bRow, bCol))      return nextCol;
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkUpperRight(bRow, bCol)) return nextCol;            

                //Last move was upper left
                case 6:
                if(checkSides(bRow, bCol))      return nextCol;
                if(checkAboveBelow(bRow, bCol)) return nextCol;
                if(checkLowerLeft(bRow, bCol))  return nextCol;
                if(checkLowerRight(bRow, bCol)) return nextCol;
                if(checkUpperLeft(bRow, bCol))  return nextCol;
                if(checkUpperRight(bRow, bCol)) return nextCol;           

            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Dumb Computer");
        }
        trackStrategy = 0;                                              //  If there isnt a smart move the stratigy becomes a random selection
        return -2;                                                      //  -2 is arbitrary, only used to move past an "if" condition in the Panels class 
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a horizontal move (tracked by trackStrategy variable) we want the next move to follow a horizontal strategy*/
    public boolean checkSides(int bRow, int bCol){
        if((bCol-1 >= 0) && (board[bRow][bCol-1] == -1)){                               // Check left of chip
            trackStrategy = 1;
            nextCol = bCol-1;
            return true;
        }
        else if((bCol+1 <= size-1) && (board[bRow][bCol+1] == -1)){                     // Check Right of chip
            trackStrategy = 1;
            nextCol = bCol+1;
            return true;
        }
        return false;
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a verticle move (tracked by trackStrategy variable) we want the next move to follow a verticle strategy*/
    public boolean checkAboveBelow(int bRow, int bCol){
        if((bRow-1 >= 0) && (board[bRow-1][bCol] == -1)){                               // Check above the chip
            trackStrategy = 2;
            nextCol = bCol;
            return true;
        }
        else if((bRow+1 <= size-1) && (board[bRow+1][bCol] == -1)){                     // Check below the chip
            trackStrategy = 2;
            nextCol = bCol;
            return true;
        }
        return false;                                                                   // There was no space available to play the chip
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a horizontal move (tracked by trackStrategy variable) we want the next move to follow a horizontal strategy*/
    public boolean checkUpperLeft(int bRow, int bCol){
        if((bRow-1 >= 0) && (bCol-1 >= 0) && (board[bRow-1][bCol-1] == -1)){            // Check Upper left of chip  
            trackStrategy = 3;
            nextCol = bCol-1;
            return true;
        }
        return false;
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a horizontal move (tracked by trackStrategy variable) we want the next move to follow a horizontal strategy*/
    public boolean checkUpperRight(int bRow, int bCol){
        if((bRow-1 >= 0) && (board[bRow-1][bCol+1] == -1)){                             //Check Upper Right diagonal from chip  
            trackStrategy = 4;
            nextCol = bCol+1;
            return true;
        }
        return false;                                                                   // There was no space available to play the chip
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a lower left diagonal move (tracked by trackStrategy variable) we want the next move to follow a left diagonal strategy*/
    public boolean checkLowerLeft(int bRow, int bCol){
        if((bRow+1 <= size-1) && (bCol-1 >= 0) && (board[bRow+1][bCol-1] == -1)){       //Check Lower left diagonal from chip        
            trackStrategy = 4;
            nextCol = bCol-1;
            return true;
        }
        return false;                                                                   // There was no space available to play the chip
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a Lower Right diagonal move (tracked by trackStrategy variable) we want the next move to follow a Lower Right diagonal strategy*/ 
    public boolean checkLowerRight(int bRow, int bCol){
        if((bRow+1 <= size-1) && (bCol+1 <= size-1) && (board[bRow+1][bCol+1] == -1)){  //Check Lower Right diagonal from chip     
            trackStrategy = 3;
            nextCol = bCol+1;
            return true;
        }
        return false;                                                                   // There was no space available to play the chip
    }
    
    /****************************************************************************************************************************************************/
    public int setFirstMove(){
        System.out.println("First Move Made");
        nextPlayer(COMPUTER);

        trackStrategy = 1;

        if(lastColumn == 0){
            //board[lastRow][lastColumn + 4] = COMPUTER;
            nextCol = (lastColumn + 3);
        }
        else if((lastColumn > 0) && (lastColumn < 4)){
            //board[lastRow][lastColumn + 1] = COMPUTER;
            nextCol = (lastColumn + 1);     
        }
        else{
            //board[lastRow][lastColumn -1] = COMPUTER;
            nextCol = (lastColumn - 1);
        }
        return nextCol;
    }

    /****************************************************************************************************************************************************/
    public int getLastCol(){
        return lastColumn;
    }

    /****************************************************************************************************************************************************/
    public int getLastRow(){
        return lastRow;
    }

    /****************************************************************************************************************************************************/
    /*If the last move made by the computer was a horizontal move (tracked by trackStrategy variable) we want the next move to follow a horizontal strategy*/
    /*For the first move a chip will be placed at random*/
    public int randomMove(){
        nextPlayer(COMPUTER);
        Random ran = new Random();
        int col = (0 + ran.nextInt(10));
        trackStrategy = 1;
        return col;
    }
}
