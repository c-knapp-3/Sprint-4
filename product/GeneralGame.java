package sprint4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralGame extends Board {
	private List<SOSEvent> sosEventList;   // Tracks all SOS events found during game
	private Map<SOSEvent, Character> playerSOSMap = new HashMap<>();   // Associates each SOS event w/ player that made it
	private Cell[][] board;  // 2D array to represent the game board
	private GameState currentGameState;   // Tracks current state of game
	private int boardSize;  // Dimension of the square board
    private int blueScore;   // Blue player's score
    private int redScore;     // Red player's score
    private char currentPlayer;  // 'B' for Blue player, 'R' for Red player
    
    // Constructor for default 3x3 game board
	public GeneralGame() {
		this.board = new Cell[3][3]; 
		this.boardSize = 3; 
		this.sosEventList = new ArrayList<>();	
        initializeBoard();
        sosEventList.clear();
	}
	
	// Constructor accepting custom game board size
	public GeneralGame(int size) {
		this.board = new Cell[size][size]; 
		this.boardSize = size;
		this.sosEventList = new ArrayList<>();   	 
        initializeBoard();
        sosEventList.clear();
    }
	
	public Cell[][] getBoard() {
        return board;
    }
	
	// Setup game board for new game
    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING; // Reset game state & scores
        currentPlayer = 'B';	// Blue starts game
        blueScore = redScore = 0;    // Reset both player's scores
    }   
    
    public int getBoardSize() {
    	return boardSize;
    }
    
    // Set new size for game board and reinitialize
    public void setBoardSize(int size) {
    	this.boardSize = size;
    	this.board = new Cell[size][size];
    	initializeBoard();
    	sosEventList.clear();   // Reset list of found SOS's for new game
    }  
	    
    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }
    
    public GameState getCurrentGameState() {
		return currentGameState;
	}
    
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return board[row][column];
        else
            return Cell.EMPTY;
    }

    public Cell getSymbol(int row, int column) {
        return board[row][column];
    }
    
    public int getBlueScore() {
    	return blueScore;
    }
    
    public int getRedScore() {
    	return redScore;
    }
    public List<SOSEvent> getSOSEventList() {
        return sosEventList;
    }
    
    public Map<SOSEvent, Character> getPlayerSOSMap() {
        return playerSOSMap;
    }   
    
    @Override
    public void newGame() {
    	initializeBoard();
    	sosEventList.clear();
    } 
    
    @Override
    public boolean makeMove(int row, int column, Cell cell) {
        // No move if game is already over
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }    
        // Check for valid move
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || board[row][column] != Cell.EMPTY) {
            System.out.println("Invalid move");
            return false; // Invalid move
        }
        board[row][column] = cell;  // Place the cell

        if (!hasSOS() && currentGameState == GameState.PLAYING) {  // If no SOS is made, switch the player
        	switchPlayers();       
        } 
        else {  
        	System.out.println(currentPlayer + " made an SOS");
        	printScores();  
            printList();    
        }      
        
        updateGameState(currentPlayer);        
        return true;
    }
    
    @Override
    public void switchPlayers() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        System.out.println("Switching players... Current Player is " + currentPlayer);
    }

    private void printScores() {  // Print current scores for both players
        System.out.println("Blue Score: " + blueScore);
        System.out.println("Red Score: " + redScore);
    }

    public void printList() {  // Print list of SOS events found
        sosEventList.forEach(event -> System.out.println(event));
    }
    
    public void countSOS() {
    	if (currentPlayer == 'B') {
    		blueScore++;
    	}
    	else {
    		redScore++;
    	}
    }
    
    public void updateGameState(char turn) {
        boolean boardFull = isBoardFull();

        if (boardFull) {
            if (blueScore > redScore) {  // Blue wins
                currentGameState = GameState.BLUE_WINS;
                logWinner("Blue");
            } 
            else if (redScore > blueScore) {   // Red wins
                currentGameState = GameState.RED_WINS;
                logWinner("Red");
            } 
            else {                            // Tie game
                currentGameState = GameState.DRAW;
                logDraw();
            }
        }
    }

    private void logWinner(String winner) {  
        System.out.println(winner + " WINS");
    }

    private void logDraw() {               
        System.out.println("Tie Game");
    }
    
    public boolean isBoardFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (board[row][column] == Cell.EMPTY) {   
                    return false;	// Board is not full because an empty cell was found
                }
            }
    	}
    	return true;  // No empty cells are found, board is full
    }
    
    
    private boolean sosEventExistsInList(SOSEvent event, List<SOSEvent> eventList) {
        for (SOSEvent existingEvent : eventList) {
            if (existingEvent.equals(event)) {
                return true; 
            }
        }
        return false; 
    }
       
    public boolean hasSOS() {   
    	
    	Cell[] symbols = { Cell.S, Cell.O, Cell.S }; 
    	boolean sosFound = false;

    	// Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row][column + 1] == symbols[1] &&
                    board[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}               	
                }
            }
        }

        // Find vertical SOS Events
        for (int column = 0; column < boardSize; column++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column] == symbols[1] &&
                    board[row + 2][column] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "column");
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
                }
            }
        }

        // Find top-left to bottom-right diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column + 1] == symbols[1] &&
                    board[row + 2][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTlBr");
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
                }
            }
        }

        // Find top-right to bottom-left diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = boardSize - 1; column >= 2; column--) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column - 1] == symbols[1] &&
                    board[row + 2][column - 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTrBl");
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
                }
            }
        }
        return sosFound;
    }
}
