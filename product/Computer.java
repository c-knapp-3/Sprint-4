package sprint4;

import java.util.Random;

import sprint4.Board.Cell;

public class Computer {
	
	Random random;  // Instance variable for generating random numbers
	
	private Board board;  
	private char autoPlayer;
	private int boardSize;
	
	Board.GameState currentGameState;
	
	public Computer(Board board, char autoPlayer) {
        this.board = board;
        this.boardSize = board.getBoardSize();
        this.autoPlayer = autoPlayer;
    }
    
	// Resets game and makes first move if it is computer's turn
	public void newGame() {
		board.newGame();
		if (autoPlayer == 'B') {
			makeFirstMove();
	    }
	}
	
	private static final Random RANDOM = new Random();  // Shared random generator for the class

	// First move is at random position with random symbol (S or O)
	public void makeFirstMove() {
	    int row = RANDOM.nextInt(boardSize);
	    int column = RANDOM.nextInt(boardSize);
	    Cell symbol = RANDOM.nextInt(2) == 0 ? Cell.S : Cell.O;
	    board.makeMove(row, column, symbol);
	}

	// Tries to put a specific symbol at a specific location if valid
	public void makeMove(int row, int column, Cell cell) {
		if (row >= 0 && row < boardSize && column >= 0 && column < boardSize && board.getCell(row, column) == Cell.EMPTY) {
			board.makeMove(row, column, cell);
			if (autoPlayer == 'B' && currentGameState == Board.GameState.PLAYING) {
				makeAutoMove();
			}
		}
	}
	
	// If the game is not over and board not full, automatically make move
	public void makeAutoMove() {
		currentGameState = board.getCurrentGameState();
		if (currentGameState == Board.GameState.PLAYING && !board.isBoardFull()) {
			if (!makeWinningMove()) {				
				makeRandomMove();
			}
		}
	}
	
	public boolean makeWinningMove() {		
		Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 

        // Make SOS horizontally
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board.getCell(row,column) == symbols[0] &&
                    board.getCell(row, column + 1) == Cell.EMPTY &&
                    board.getCell(row, column + 2) == symbols[2]) {
                	board.makeMove(row, column +1, Cell.O);
                    return true;
                }
                if (board.getCell(row,column) == Cell.EMPTY &&
                    board.getCell(row, column + 1) == symbols[1] &&
                    board.getCell(row, column + 2) == symbols[2]) {
                	board.makeMove(row, column, Cell.S);
                    return true;
                }
                if (board.getCell(row,column) == symbols[0] &&
                    board.getCell(row, column + 1) == symbols[1] &&
                    board.getCell(row, column + 2) == Cell.EMPTY) {
                	board.makeMove(row, column + 2, Cell.S);
                    return true;
                }
            }
        }        
        
        // Make SOS vertically
        for (int column = 0; column < boardSize; column++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (board.getCell(row, column) == symbols[0] &&
                    board.getCell(row + 1,column) == Cell.EMPTY &&
                    board.getCell(row + 2, column) == symbols[2]) {
                	board.makeMove(row + 1, column, Cell.O);
                    return true;
                }
                if (board.getCell(row, column) == Cell.EMPTY &&
                    board.getCell(row + 1,column) == symbols[1] &&
                    board.getCell(row + 2, column) == symbols[2]) {
                	board.makeMove(row, column, Cell.S);
                    return true;
                }
                if (board.getCell(row, column) == symbols[0] &&
                    board.getCell(row + 1,column) == symbols[1] &&
                    board.getCell(row + 2, column) == Cell.EMPTY) {
                	board.makeMove(row + 2, column, Cell.S);
                    return true;
                }
            }
        }
        
        // Make SOS diagonally TlBr
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board.getCell(row, column) == symbols[0] &&
            		board.getCell(row + 1, column + 1) == Cell.EMPTY &&
            		board.getCell(row + 2, column + 2) == symbols[2]) {
                	board.makeMove(row + 1, column + 1, Cell.O);
                    return true;
                }
                if (board.getCell(row, column) == Cell.EMPTY &&
            		board.getCell(row + 1, column + 1) == symbols[1] &&
            		board.getCell(row + 2, column + 2) == symbols[2]) {
                	board.makeMove(row, column, Cell.S);
                    return true;
                }
                if (board.getCell(row, column) == symbols[0] &&
            		board.getCell(row + 1, column + 1) == symbols[1] &&
            		board.getCell(row + 2, column + 2) == Cell.EMPTY) {
                	board.makeMove(row + 2, column + 2, Cell.S);
                    return true;
                }
            }
        }
        
        // Make SOS diagonally TrBl
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = boardSize - 1; column >= 2; column--) {
                if (board.getCell(row, column) == symbols[0] &&
                    board.getCell(row + 1, column - 1) == Cell.EMPTY &&
                    board.getCell(row + 2, column - 2) == symbols[2]) {
                	board.makeMove(row + 1, column - 1, Cell.O);
                    return true;
                }
                if (board.getCell(row, column) == Cell.EMPTY &&
                    board.getCell(row + 1, column - 1) == symbols[1] &&
                    board.getCell(row + 2, column - 2) == symbols[2]) {
                	board.makeMove(row, column, Cell.S);
                    return true;
                }
                if (board.getCell(row, column) == symbols[0] &&
                    board.getCell(row + 1, column - 1) == symbols[1] &&
                    board.getCell(row + 2, column - 2) == Cell.EMPTY) {
                	board.makeMove(row + 2, column - 2, Cell.S);
                    return true;
                }
            }
        }
		return false;
	}
	
	public void makeRandomMove() {
	    int numEmptyCells = getNumEmptyCells();
	    if (numEmptyCells == 0) return;  

	    int targetMove = RANDOM.nextInt(numEmptyCells);
	    int symbolIndex = RANDOM.nextInt(2);  
	    Cell symbol = symbolIndex == 0 ? Cell.S : Cell.O; 

	    int index = 0;
	    for (int row = 0; row < boardSize; row++) {
	        for (int column = 0; column < boardSize; column++) {
	            if (board.getCell(row, column) == Cell.EMPTY) {
	                if (targetMove == index) {
	                    board.makeMove(row, column, symbol);
	                    return;
	                }
	                index++;
	            }
	        }
	    }
	}

	public int getNumEmptyCells() {
		int numEmptyCells = 0;
		for (int row = 0; row < boardSize; ++row) {
			for (int column = 0; column < boardSize; ++column) {
				if (board.getCell(row, column) == Cell.EMPTY) {
					numEmptyCells++;
				}
			}
		}
		return numEmptyCells;
	}
}
