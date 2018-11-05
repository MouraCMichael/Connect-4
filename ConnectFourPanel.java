
/****************************************************************************************************************************************************
 *  @Author:         Corey M. Moura
 *  @Date:           February 17, 2018
 *  @Professor:      Dr. Trafftz
 *  @Project:        Project 2 of CS163: Connect-4 Game, player vs computer 
 *  @Notes:          This GUI will produce a gridbag layout of the board for connect 4.  The size is determined when the user types a gameboard size they 
 *                   wish to play with via a prompt window and text box.  Once selected the gui will run through a double for-loop to create an equal number 
 *                   of columns and rows.  When it is creating these spaces, it will also set the space boarders with a black outline, and set the default 
 *                   board apearence.  Once this occurs and the board appears, the game begins.  The user will have a predetermined amount of time to play 
 *                   a move before the computer will skip them, and play agin.  The player and computer are both limited to one move per-iteration.  The, 
 *                   player-move-program, is initiated once the user presses "select" above any column.  The, computer-move-program, is initiated by the 
 *                   internal java timer interupt. 
 *                   
 *                   This class works extensively with the ConnectFourGame class.
/****************************************************************************************************************************************************/

/** Libraries used for the creation of this GUI **/
import java.awt.*;
import javax.swing.*;
import java.awt.Button;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionListener;

public class ConnectFourPanel extends JPanel {
    /** Final int variables**/
    public static final int USER = 0;                                               // Value for player  
    public static final int COMPUTER = 1;                                           // Value for Computer 
    private final int SIZE = 10;                                                    // Default board size

    /** Variables USed in GUI **/
    private int bSize, baseRow, baseCol;
    private int lastPlayedRow, lastPlayedCol;
    private int turnTracker = 1;
    private Boolean person = true;
    
    private int resetPoints = 0;
    private int singlePoints = 1;
    private int blockingPoints = 2;
    private int winningPoints = 3;
    
    /** Arrays used in GUI  **/
    private JLabel[][] matrix;                                                      // GUI visual for the game board model 
    private JButton[] selection;                                                    // buttons to select game columns 

    /** Object used throughout GUI class **/
    private ConnectFourGame game;           
    private JLabel label;                   
    private JMenuItem gameItem;
    private JMenuItem quitItem;
    private ImageIcon iconBlank;
    private ImageIcon iconPlayer1;
    private ImageIcon iconPlayer2;

    /**  Strings for Prompt window message storage  **/
    private String dLevel, promptPlayAgain;                                         //Stores the difficulty level enterd as a string
    
    
    
    /***************/
    
    private ConnectFourGame userScore;
    private ConnectFourGame computerScore;
    
    
    /** labels */
    private JLabel scoreOfUser;      //Used to create a text box to eneter the countdown time manually for t1
    private JLabel scoreOfComputer;      //Used to create a text box to eneter the countdown time manually for t2
    private JLabel labelUserScore;     //A variable for showing the time as it counts down  
    private JLabel labelComputerScore;     //A variable for showing the time as it counts down 
    
    private JFrame scoreBoard;      //Will be used to  create an object of JFrame

    
    
    
    /***************/
    
    
    
    private javax.swing.Timer timer;                                                //Will be used to create an object timer for interupts
    private int delay = 5000;                                                       //DELAY represents the number of milliseconds the swing timer will wait between interupts

    public ConnectFourPanel(JMenuItem pquitItem, JMenuItem pgameItem){
        bSize = 10;                                                                 // Default board size;
        String strBdSize;                                                           // Creates a string variable
        strBdSize = JOptionPane.showInputDialog(null, "Board Size?", bSize);        // Stores the board size enterd as a string
        bSize = Integer.parseInt(strBdSize);                                        // Converts a string to an intiger

        game = new ConnectFourGame(bSize);                                          // Object reffering to the ConnectFourGame Class
        gameItem = pgameItem;                                                       // Menu objects
        quitItem = pquitItem;

        dLevel = JOptionPane.showInputDialog(null, "Easy:1  Impossible:10");        // Stores the difficulty level typed by user as a string
        delay = game.setDifficultyLevel(dLevel);                                    // Calls a method in ConnectFourGame class to assign delay in milliseconds

        setLayout(new GridLayout(bSize+1,bSize));                                   // Sets the size of the grid layout via bSize variable

        iconBlank = new ImageIcon ("BB.png");                                       // Icon of blue square blck dot              
        iconPlayer1 = new ImageIcon ("RBBR.png");                                   // Icon of blue square red dot
        iconPlayer2 = new ImageIcon ("YBBY.png");                                   // Icon of blue square yellow dot

        ButtonListener listener = new ButtonListener();                             // Creates a button listener object
        quitItem.addActionListener(listener);                                       // Adds quititem and game item as buttons
        gameItem.addActionListener(listener);
        
        matrix = new JLabel[bSize][bSize];                                          // The double array used to set icons in specific positions

        /**Makes all of the select buttons for a board size determined by the variable size*/
        selection = new JButton[bSize];                                             // Makes a number of buttons called select
        for (int col = 0; col < bSize; col++) {         
            selection[col] = new JButton ("Select");
            selection[col].addActionListener(listener);                             // Add the button to the list of action listeners
            add(selection[col]);                                                    // Add the visual button to the gui
        }

        /**Creates the matrix for the board display*/
        for (int row = 0; row < bSize; row++) {
            for (int col = 0; col < bSize; col++) {
                matrix[row][col] = new JLabel("",iconBlank,SwingConstants.CENTER);                  // Sets the format to center 
                matrix[row][col].setBorder(new LineBorder(Color.black));                            // Sets the grid as black
                matrix[row][col].setIcon(iconBlank);                                                // Sets the default board via iconBlank 
                //matrix[row][col].setBackground(Color.darkGray);
                matrix[row][col].setOpaque(true);                                                   // Must be set to true
                add(matrix[row][col]);                                                              // Adds the cell to the board
            }
        }
        
        
        /***************/
        
        /**JFrame Initialization*/
        JFrame scoreBoard = new JFrame ("Score Board");             //Score Board Window Sepreate from the game window
        scoreBoard.setSize(500, 200);
        
        
        scoreBoard.setVisible(true);
        scoreBoard.setBackground(Color.black);
        scoreBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Default exit procedures vs a menu exit button
        scoreBoard.setLayout(new GridBagLayout());                  //Sets the structure of the GUI to follow gridBagLayout format
        GridBagConstraints layoutConst;                 //Variable layoutConst is of data type GridBagConstraints
        layoutConst = constraints(10,10,10,10);         //assigns the size of the GridBagConstraints
        
        
        
        /**  Labels  **/
        labelUserScore = new JLabel("Your Score");        //Creates a Label Assigned the toString methods output for timer 1
        layoutConst = constraintsLabels(0,1,2,1);       //Location of Label
        scoreBoard.add(labelUserScore, layoutConst);           //Adds label to the JFrame

        labelComputerScore = new JLabel("Computer's Score");        //Creates a Label Assigned the toString methods output for timer 2
        layoutConst = constraintsLabels(1,1,1,1);       //Location of Label
        scoreBoard.add(labelComputerScore, layoutConst);           //Adds label to the JFrame
        
        scoreOfUser = new JLabel("0");                 //New text box
        layoutConst = constraintsText(0,3,1,1);     //Location of textbox
        scoreBoard.add(scoreOfUser, layoutConst);            //Adds text box to the layout
        
        scoreOfComputer= new JLabel("0");                 //New text box
        layoutConst = constraintsText(1,3,1,1);     //Location of textbox
        scoreBoard.add(scoreOfComputer, layoutConst);            //Adds text box to the layout
        
        
        /***************/
        
        
        
        /**Timer Initialization*/
        timer = new javax.swing.Timer(delay, new TimerListener());                                  //Passes DELAY, and TimerListener 
        timer.start();                                                                              //Starts the Java Swing internal timer with the above peramiters
    }
    
    private GridBagConstraints constraints(int x, int y, int h, int w){
        GridBagConstraints rtn=new GridBagConstraints();
        rtn.gridx=x;
        rtn.gridy=y;
        rtn.gridheight=h;
        rtn.gridwidth=w;
        rtn.insets=new Insets(5,5,5,5);
        rtn.anchor=GridBagConstraints.LINE_START;
        return rtn;
    }
    /**************************************************************************************************************************************/

    /*Assigns the Labels which called the function a location in the JFrame via grid points*/
    private GridBagConstraints constraintsLabels(int x, int y, int h, int w){
        GridBagConstraints rtn=new GridBagConstraints();
        rtn.gridx=x;
        rtn.gridy=y;
        rtn.gridheight=h;
        rtn.gridwidth=w;
        rtn.insets=new Insets(5,28,5,5);
        rtn.anchor=GridBagConstraints.LINE_START;
        return rtn;
    }
    /**************************************************************************************************************************************/

    /*Assigns the Text fields which called the function a location in the JFrame via grid points*/
    private GridBagConstraints constraintsText(int x, int y, int h, int w){
        GridBagConstraints rtn=new GridBagConstraints();
        rtn.gridx=x;
        rtn.gridy=y;
        rtn.gridheight=h;
        rtn.gridwidth=w;
        rtn.insets=new Insets(5,19,5,5);
        rtn.anchor=GridBagConstraints.LINE_START;
        return rtn;
    }
    /**************************************************************************************************************************************/
 

    /****************************************************************************************************************************************************/
    /**   Users Turn Initiated by a button press   ******************************************************************************************************/
    /****************************************************************************************************************************************************/

    private class ButtonListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
           
            
            
            JComponent comp = (JComponent) event.getSource();                                       // Gets the location of the selected button
            game.nextPlayer(USER);                                                                  // Sets the "player" to User

            /**  Turn tracker ensures that the user can only perform one move per turn  **/
            if(turnTracker == 1){

                /**  runs though each column  **/
                for (int col = 0; col < bSize; col++) {

                    /** Compares each column to the location of the selected button press  **/
                    if (selection[col] == comp){
                        int row = game.selectCol(col);                                                      // Get the next available row in that col

                        /**  If the row is full display message : Else, set the users icon  **/
                        if (row == ConnectFourGame.FULLCOLUMN)
                            JOptionPane.showMessageDialog(null, "Column is full!");
                        else{
                            matrix[row][col].setIcon(iconPlayer1);
                            
                            game.setUserScore(1);
                            scoreOfUser.setText(game.toString()); //Displays the output of the "toString" method.
                    
                        }

                        /** Method returns boolean by checking if the user has played a winning move  **/
                        if (game.isWinner()) {
                            timer.stop();                                                                   // Stop the internal timer so the computer doesnt keep playing moves
                            
                            game.setUserScore(2);
                            scoreOfUser.setText(game.toString()); //Displays the output of the "toString" method. 
                            
                            JOptionPane.showMessageDialog(null, "You Win!");                                // Show the user they have won 
                            promptPlayAgain = JOptionPane.showInputDialog(null, "PLAY:1  STOP:0");          // Prompt the user to play again

                            /**  Check the users input; either reset or quit**/
                            if(promptPlayAgain.equals("1")){
                                game.reset();                                                               // Resets the values of the board in game class
                                for  (row = 0; row < bSize; row++) 
                                    for (col = 0; col < bSize; col++){ 
                                        matrix[row][col].setIcon(iconBlank);                                // Resets the icons of the board
                                    }
                               
                                turnTracker = 1;                                                            // The player will get to move first the next game  
                                timer.start();                                                              // Begin the timer
                                for(int i = 0; i <= 3000; i++);                                             //Apply a short delay for the user to see where the computer has played its chip
                                                                                             
                            }
                            else System.exit(1);                // User has selected to quit
                        }
                    }
                    

                    /**  If, durring the palyers turn, they wish to restart **/
                    if (comp == gameItem) {    
                        game.reset();                                                                       // Resets the values of the board in game class
                        for (int row = 0; row < bSize; row++) {
                            for (col = 0; col < bSize; col++){ 
                                matrix[row][col].setIcon(iconBlank); 
                            }
                        }// Resets the icons of the board
                        
                        game.setUserScore(resetPoints);
                        scoreOfUser.setText(game.toString()); //Displays the output of the "toString" method. 
                        game.setComputerScore(resetPoints);
                        scoreOfComputer.setText(game.toString()); //Displays the output of the "toString" method.
                        
                        turnTracker = 1; 
                        game.setTrackStrategy(-1);
                    }

                    /**  If, durring the palyers turn, they wish to quit **/
                    if (comp == quitItem)
                        System.exit(1); 
                }
 
                turnTracker = 0;                                                                            // Signifies the end of the users turn
            }
        }
    }

    /****************************************************************************************************************************************************/
    /**   Computers Turn Initiated by Java interupt   *************************************************************************************************/
    /****************************************************************************************************************************************************/

    private class TimerListener implements ActionListener {
        /**Updates the time
         * @param event a timer interrupt*/
        public void actionPerformed(ActionEvent event) { 

            game.nextPlayer(COMPUTER);                                          // Sets the player to computer in Game class

            if(game.getTrackStrategy() == -1){
                game.setComputerScore(singlePoints);
                scoreOfComputer.setText(game.toString()); //Displays the output of the "toString" method. 
                
                int row, col;
                col = game.setFirstMove();
                row = game.selectCol(col);                                  // If the column returned is within the board get the row
                matrix[row][col].setIcon(iconPlayer2);                      // Set icon
                lastPlayedRow = row;                                        // Store this moves row as lastRow
                lastPlayedCol = col;                                        // Store this moves col as lastCol
                turnTracker = 1;                                        // Allows the user to coduct their turn
                
                return;
            }

            for (int col = 0; col < bSize; col++) {
                int row = game.selectCol(col);                                  // Calls method which will return the number of the next available row in the passed col 

                /** Method returns boolean by checking if the computer has a possible winning move  **/
                if(game.isWinner()){
                    game.setComputerScore(winningPoints);
                    scoreOfComputer.setText(game.toString()); //Displays the output of the "toString" method. 
                    
                    matrix[row][col].setIcon(iconPlayer2);                      // isWinner method determined a winning move; sets icon
                    for(int i = 0; i <= 1500; i++);                             //Apply a short delay for the user to see where the computer has played its chip
                    JOptionPane.showMessageDialog(null, "Computer Wins!");      // Shows the user that the computer has won
                    timer.stop();                                               // Stop the internal timer so the computer doesnt keep playing moves
                    
                    
                    promptPlayAgain = JOptionPane.showInputDialog(null, "PLAY:1  STOP:0");          // Prompt the user to play again

                    /**  Check the users input; either reset or quit**/
                    if(promptPlayAgain.equals("1")){        
                        game.reset();  
                        for  (row = 0; row < bSize; row++) 
                            for (col = 0; col < bSize; col++){ 
                                matrix[row][col].setIcon(iconBlank);
                            }
                        game.setTrackStrategy(0);                           // Resets the computers strategy, so it will now begin again with a random move
                        timer.start();                                      // Computer won, so computer timer will allow the computer to play first move (turnTracker is still 0)
                        turnTracker = 1;                                        // Allows the user to coduct their turn
                        return;                         
                    }
                    else System.exit(1);                                    // Ends the program
                }
                else{
                    game.undo();                                    // If the position checked was not a winning move, then the computer needs to remove its value from the square
                }
            }

            /** Computer will check for a blocking move to prevent the human from winning **/
            game.nextPlayer(USER);                                      // Switch to the perspective of the human
            for (int col = 0; col < bSize; col++) {
                int row = game.selectCol(col);                          // Find the humans winning move

                /**  If the human has a wining move  **/
                if(game.isWinner()){
                    game.setComputerScore(blockingPoints);
                    scoreOfComputer.setText(game.toString()); //Displays the output of the "toString" method. 

                    game.setBlockingMove();                                 // Sets the humans winning location the value of the computer   
                    matrix[row][col].setIcon(iconPlayer2);                  // Sets the computers icon to block the move
                    for(int i = 0; i <= 1500; i++);                         // Apply a short delay for the user to see where the computer has played its chip
                    turnTracker = 1;                                        // Allows the user to coduct their turn
                    return;                                                 // Exits the interupt
                }
                else{
                    game.undo();                                            // If the position checked was not a humans winning move, the value is reset to -1
                }
            }

            game.nextPlayer(COMPUTER);  
            
            /**  If there was no wining or blocking move to play, the computer will check for a smart move  **/
            /**  If the strategy is 0 there is no smart move and computer will play randolmly.  If There is a smart move it will continue playing smart moves**/
            
            
            int row, col;
            if(game.getTrackStrategy() > 0){                 
                //System.out.println("Smart Move");
                col = game.smartMoves(lastPlayedRow, lastPlayedCol);            // Passes the last move of the computer as a "base" to check around
                if(col >= 0){                                                   // Ensures that the column selected is position or zero.  If not the it is out of bounds                      
                    row = game.selectCol(col);                                  // If the column returned is within the board get the row
                    matrix[row][col].setIcon(iconPlayer2);                      // Set icon
                    lastPlayedRow = row;                                        // Store this moves row as lastRow
                    lastPlayedCol = col;                                        // Store this moves col as lastCol
                }

            }  

            /**  Randomly Selects A Column as a Last Option  **/
            else{              
                //System.out.println("Random Move");
                col = game.randomMove();                                        // Random Move method will set "trackStrategy" to 1
                row = game.selectCol(col);

                if (row == ConnectFourGame.FULLCOLUMN){
                    JOptionPane.showMessageDialog(null, "Column is full!");     // Column is full
                }
                else {
                    matrix[row][col].setIcon(iconPlayer2);
                }
                lastPlayedRow = row;                                            // Store this moves row as lastRow    
                lastPlayedCol = col;                                            // Store this moves col as lastCol

            }
            game.setComputerScore(singlePoints);
            scoreOfComputer.setText(game.toString()); //Displays the output of the "toString" method. 
            
            game.nextPlayer(USER);                                              // Sets the player to User
            turnTracker = 1;                                                    // Signifies the end of the computers move
        }
    }
}

