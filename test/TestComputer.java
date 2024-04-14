package sprint4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sprint4.Board.Cell;

class TestComputer {
    private Computer computerPlayer;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new SimpleGame();
        computerPlayer = new Computer(board, 'B');
    }

    @AfterEach
    void tearDown() {
        board = null;
        computerPlayer = null;
    }

    @Test
    public void testMakeWinningMove() {
        // Precondition: Set up the board for a guaranteed winning move.
        board.makeMove(0, 0, Cell.S);
        board.makeMove(0, 2, Cell.S);
        // Action: Trigger the winning move.
        boolean moveMade = computerPlayer.makeWinningMove();
        // Verification: Check if the move was successfully made.
        assertTrue(moveMade, "Expected winning move was not made.");
    }
    
    @Test
    public void testMakeAutoMove() {
        // Setup: The board is in a winnable state by the computer.
        board.makeMove(0, 0, Cell.S);
        board.makeMove(0, 2, Cell.S);
        // Action: Auto-move should detect and complete the win.
        computerPlayer.makeAutoMove();
        // Verification: Check if the game state changes to BLUE_WINS.
        assertEquals(Board.GameState.BLUE_WINS, board.getCurrentGameState(), "Expected game state to be BLUE_WINS");

        // Reset the board for a new scenario.
        board.newGame();
        // Setup: Board configured where no immediate win or loss should occur.
        board.makeMove(0, 0, Cell.S);
        board.makeMove(0, 1, Cell.S);
        board.makeMove(1, 2, Cell.O);
        // Action: Execute auto-move.
        computerPlayer.makeAutoMove();
        // Verification: Ensure that the game state does not indicate a win or draw.
        assertNotEquals(Board.GameState.RED_WINS, board.getCurrentGameState(), "Unexpected RED_WINS state");
        assertNotEquals(Board.GameState.DRAW, board.getCurrentGameState(), "Unexpected DRAW state");

        // Reset the board for a non-winning move test.
        board.newGame();
        // Action: Execute auto-move when no winning moves are possible.
        int initialEmptyCells = computerPlayer.getNumEmptyCells();
        computerPlayer.makeAutoMove();
        // Verification: Check if a random move was made by verifying the number of empty cells decreased.
        assertEquals(initialEmptyCells - 1, computerPlayer.getNumEmptyCells(), "Expected one less empty cell after a random move.");
    }
    
    @Test
    public void testMakeFirstMove() {
        // Precondition: Verify the initial number of empty cells.
        int initialEmptyCells = computerPlayer.getNumEmptyCells();
        // Action: Make the first move.
        computerPlayer.makeFirstMove();
        // Verification: Ensure the number of empty cells is reduced by one.
        assertEquals(initialEmptyCells - 1, computerPlayer.getNumEmptyCells(), "Expected the number of empty cells to decrease by one.");
    }
}

