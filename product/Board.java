package sprint4;

import java.util.List;
import java.util.Map;

public abstract class Board {    
          
    private Cell[][] board;    
    private int boardSize;
    private int blueScore;
    private int redScore;
    private char currentPlayer;
    private boolean simpleGame;
    
    public static enum Cell {EMPTY, S, O}
    
    public enum GameState {PLAYING, DRAW, BLUE_WINS, RED_WINS}
    
    protected GameState currentGameState;
    
    public Board() {
    	 this.board = new Cell[3][3];
    	 this.boardSize = 3;
    	 this.simpleGame = true;
         this.currentPlayer = 'B';   // Blue player starts by default    
         this.currentGameState = GameState.PLAYING;
         initializeBoard();
    }
    
    public Board(int size) {
    	this.board = new Cell[size][size];
    	this.boardSize = size;
    	this.simpleGame = true;
        this.currentPlayer = 'B';        
        this.currentGameState = GameState.PLAYING;        
        initializeBoard();
    }
        
    public Board(int size, boolean mode) {
    	this.board = new Cell[size][size];
    	this.boardSize = size;
        this.simpleGame = mode;
        this.currentPlayer = 'B';        
        this.currentGameState = GameState.PLAYING;
        initializeBoard();
    }
    
    public void initializeBoard() {
   	 for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
   }
    
    public Cell[][] getBoard() {
        return board;
    }
    
    public void setBoardSize(int size) {
    	 this.boardSize = size;
         this.board = new Cell[size][size];
         initializeBoard();
    }
    
    public int getBoardSize() {
    	return boardSize;
    }        
             
    public void setGameMode(boolean mode) {
    	this.simpleGame = mode;
    }
    
    public boolean getGameMode() {
    	return simpleGame;
    }
    
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }
    
    public char getCurrentPlayer() {
		return currentPlayer;
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
      
    public boolean makeMove(int row, int column, Cell cell) {
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || board[row][column] != Cell.EMPTY) {
            return false; // Invalid move
        }
        board[row][column] = cell;
        switchPlayers();        
        return true;
    }
    
    public void switchPlayers() {        
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';   // Toggle current player from 'B' to 'R', or from 'R' to 'B'
    }
    
    public void countSOS() {
    	if (currentPlayer == 'B') {
    		blueScore++;
    	}
    	else {
    		redScore++;
    	}
    }
    
    public abstract void newGame();
    public abstract boolean isBoardFull();     
    protected abstract void updateGameState(char turn);
    protected abstract int getBlueScore();
    protected abstract int getRedScore();
    protected abstract boolean hasSOS();    
    protected abstract List<SOSEvent> getSOSEventList();
    protected abstract Map<SOSEvent, Character> getPlayerSOSMap();
}
