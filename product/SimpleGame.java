package sprint4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGame extends Board {
	
    private Cell[][] board;        // 2D array to represent the game board
    private GameState currentGameState;  // Tracks current state of game
    private int boardSize;  // Dimension of the square board
    private int blueScore;   // Blue player's score
    private int redScore;    // Red player's score
    private char currentPlayer;   // 'B' for Blue player, 'R' for Red player   
    private List<SOSEvent> sosEventList;   // List to store found SOS's
	private Map<SOSEvent, Character> playerSOSMap = new HashMap<>(); // Maps found SOS's to player that found them
    
	// Default constructor initializing a 3x3 board
	public SimpleGame() {
    	this.boardSize = 3; 
        this.board = new Cell[3][3]; 
        this.sosEventList = new ArrayList<>();
        initializeBoard();
        sosEventList.clear();
    }
	
	// Constructor with custom board size
    public SimpleGame(int size) {
    	this.boardSize = size; 
        this.board = new Cell[size][size]; 
        this.sosEventList = new ArrayList<>();
        initializeBoard();
        sosEventList.clear();
    }
    
    public Cell[][] getBoard() {
        return board;
    }
    
    // Initializes or resets the board to start a new game
    public void initializeBoard() {    	
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;     // Set all cells to EMPTY
            }
        }
        currentGameState = GameState.PLAYING; // Set status to PLAYING
        currentPlayer = 'B';	// Blue starts the game
        blueScore = redScore = 0;          // Reset both player's scores
    }  
    
    public int getBoardSize() {
    	return boardSize;
    }     
    
    public void setBoardSize(int size) {
    	this.boardSize = size;
    	this.board = new Cell[size][size];
        initializeBoard();
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

    // Resets board to start a new game
    @Override
    public void newGame() {
    	initializeBoard();
    	sosEventList.clear();
    }
    
    // Handles a player's move on the boards
    @Override
    public boolean makeMove(int row, int column, Cell cell) {
        
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize && getCell(row, column) == Cell.EMPTY) {
            board[row][column] = cell;      // Place the cell      
            updateGameState(currentPlayer);   // Check for win/tie status        
            if (currentGameState == GameState.PLAYING) {
                switchPlayers();
            }
            return true; // The move was successfully made.
        } 
        else {
            // The move is invalid (either out of bounds or the cell is not empty).
            return false;
        }
    }
   
    // Switches the turn between players
    @Override
    public void switchPlayers() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        System.out.println("Switching... the current player is " + currentPlayer);
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
    	if (hasSOS()) {        // If SOS Event detected, update game status to indicate winner
            currentGameState = (turn == 'B') ? GameState.BLUE_WINS : GameState.RED_WINS;
            System.out.println(currentGameState + "!");
        } 
    	else if (isBoardFull()) {  // If board is full and no SOS Event detected, game is a draw
            currentGameState = GameState.DRAW;
            System.out.println("Tie game");
        }
    }
    
    public boolean isBoardFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (board[row][column] == Cell.EMPTY) {   
                    return false;  // Board is not full because an empty cell was found	
                }
            }
    	}
    	return true;  // No empty cells are found, board is full
    }
    
    public boolean hasSOS() {   
    	
    	Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 

        // Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row][column + 1] == symbols[1] &&
                    board[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
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
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
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
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
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
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }
        return false;
    }
}
