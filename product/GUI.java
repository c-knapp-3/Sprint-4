package sprint4;

import javax.swing.JFrame;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import sprint4.GUI;
import sprint4.Board.Cell;
import sprint4.Board.GameState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private SimpleGame simpleGame; 
    private GeneralGame generalGame; 
    private Computer computerBlue; 
    private Computer computerRed; 
    protected Board board; 
	protected int boardSize;
	
	public static final int SYMBOL_STROKE_WIDTH = 8; 
	public static final int CANVAS_SIZE = 300;
	public int cellSize;
	public int cellPadding;
	public int symbolSize;
	public char currentPlayerTurn;
	Cell currentPlayerSymbol;
		
	protected GameBoardCanvas gameBoardCanvas; 
	protected JTextField textFieldCurrentTurn;
	
	protected JRadioButton buttonBlueS;
	protected JRadioButton buttonBlueO;
	protected ButtonGroup bluePlayerSelection = new ButtonGroup(); 
	protected ButtonGroup bluePlayerSymbolSelection;
	
	protected JRadioButton buttonRedS;
	protected JRadioButton buttonRedO;	
    protected ButtonGroup redSelectionGroup = new ButtonGroup(); 
    protected ButtonGroup redPlayerSymbolSelection;
    
    protected ButtonGroup gameModeSelectionGroup; 
    protected JSpinner spinnerBoardSize; 
    private JTextField textFieldScore;
        
    public GUI() {  // Initialize GUI components and layout
    	
    	setTitle("SOS GAME");
    	setResizable(false); 
    	setVisible(true);
    	setSize(350, 300); 
    	setBackground(Color.WHITE);		
		getContentPane().setBackground(Color.WHITE);
		setContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); 
		
		//Initialize game objects
		generalGame = new GeneralGame();
        simpleGame = new SimpleGame();
        board = simpleGame;
        computerBlue = new Computer(board, 'B');
        computerRed = new Computer(board, 'R');
	}
		
	// Setup content pane with game components
	protected void setContentPane(){	
		
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
        Border gridBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        gameBoardCanvas.setBorder(gridBorder);
        
        JPanel panelNorth = new JPanel();
        JPanel panelEast = new JPanel();
        JPanel panelSouth = new JPanel();
        JPanel panelWest = new JPanel();
                        
        panelNorth.setBackground(Color.WHITE);
        panelEast.setBackground(Color.WHITE);        
        panelSouth.setBackground(Color.WHITE);        
        panelWest.setBackground(Color.WHITE);
              
        // Setup game mode selection  
        JLabel labelGameMode = new JLabel("Mode:");
        labelGameMode.setFont(new Font("Consolas", Font.BOLD, 14));  
        labelGameMode.setHorizontalAlignment(SwingConstants.CENTER);
        JRadioButton buttonSimpleGame = new JRadioButton("Simple", true);
        buttonSimpleGame.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonSimpleGame.setHorizontalAlignment(SwingConstants.CENTER);
        buttonSimpleGame.setBackground(Color.WHITE);
        JRadioButton buttonGeneralGame = new JRadioButton("General");
        buttonGeneralGame.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonGeneralGame.setHorizontalAlignment(SwingConstants.CENTER);
        buttonGeneralGame.setBackground(Color.WHITE);
        
        gameModeSelectionGroup = new ButtonGroup();
        gameModeSelectionGroup.add(buttonSimpleGame);
        gameModeSelectionGroup.add(buttonGeneralGame);
        
        buttonSimpleGame.addActionListener(e -> {
            board = simpleGame;
            buttonGeneralGame.setEnabled(false);
        });

        buttonGeneralGame.addActionListener(e -> {
            board = generalGame;
            buttonSimpleGame.setEnabled(false);
        });
        
        panelNorth.add(labelGameMode);
        panelNorth.add(buttonSimpleGame);
        panelNorth.add(buttonGeneralGame);        
            
        // Setup board size selection
        JLabel labelBlank = new JLabel("                                ");
        JLabel labelBoardSize = new JLabel("Board Size:");
        labelBoardSize.setFont(new Font("Consolas", Font.BOLD, 14));
        labelBoardSize.setHorizontalAlignment(SwingConstants.CENTER);
        labelBoardSize.setBackground(Color.WHITE);
        
        spinnerBoardSize = new JSpinner();
        spinnerBoardSize.setModel(new SpinnerNumberModel(3, 3, 8, 1));
        spinnerBoardSize.addChangeListener(e -> {
            int newSize = (int) spinnerBoardSize.getValue();
            board.setBoardSize(newSize); // Change the board size
            board.newGame();
            gameBoardCanvas.repaint();
        });
        
        panelNorth.add(labelBlank);
        panelNorth.add(labelBoardSize);    
        panelNorth.add(spinnerBoardSize);
    
        // Setup blue player selection
        JLabel labelBluePlayer = new JLabel("<html><font color='blue'>Blue Player:</font></html>");        
        labelBluePlayer.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonBlueS = new JRadioButton("S", true);
        buttonBlueS.setFont(new Font("Lucida Console", Font.BOLD, 14));
        buttonBlueS.setActionCommand("S");
        buttonBlueS.setBackground(Color.WHITE);
        buttonBlueO = new JRadioButton("O");
        buttonBlueO.setFont(new Font("Lucida Console", Font.BOLD, 14));
        buttonBlueO.setActionCommand("O");
        buttonBlueO.setBackground(Color.WHITE);
        
        JRadioButton buttonBlueHuman = new JRadioButton("Human", true);
        buttonBlueHuman.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonBlueHuman.setBackground(Color.WHITE);
        JRadioButton buttonBlueComputer = new JRadioButton("Computer");
        buttonBlueComputer.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonBlueComputer.setForeground(new Color(75, 0, 130));
        buttonBlueComputer.setBackground(Color.WHITE);
        
        bluePlayerSymbolSelection = new ButtonGroup();
        bluePlayerSymbolSelection.add(buttonBlueS);
        bluePlayerSymbolSelection.add(buttonBlueO);
        bluePlayerSelection.add(buttonBlueHuman);
        bluePlayerSelection.add(buttonBlueComputer);
        
        panelWest.add(labelBluePlayer);
        panelWest.add(buttonBlueHuman);
        panelWest.add(buttonBlueS);
        panelWest.add(buttonBlueO);         
        panelWest.add(buttonBlueComputer);
        
        buttonBlueComputer.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {            	
            	computerBlue = new Computer(board, 'B');
                bluePlayerSymbolSelection.clearSelection();
                buttonBlueS.setEnabled(false);
                buttonBlueO.setEnabled(false);
                buttonBlueHuman.setEnabled(false);
	            if (board.getCurrentPlayer() == 'B') {
	                computerBlue.makeAutoMove();
	                gameBoardCanvas.repaint();
	                handleGameResult();
	            }
            }
        });
        
        // Setup red player selection
        JLabel labelRedPlayer = new JLabel("<html><font color='red'>Red Player:</font></html>");
        labelRedPlayer.setFont(new Font("Consolas", Font.BOLD, 14)); 
        buttonRedS = new JRadioButton("S", true);
        buttonRedS.setFont(new Font("Lucida Console", Font.BOLD, 14));
        buttonRedS.setActionCommand("S");
        buttonRedS.setBackground(Color.WHITE);
        buttonRedO = new JRadioButton("O");   
        buttonRedO.setFont(new Font("Lucida Console", Font.BOLD, 14));
        buttonRedO.setActionCommand("O");
        buttonRedO.setBackground(Color.WHITE);
        
        JRadioButton buttonRedHuman = new JRadioButton("Human", true);
        buttonRedHuman.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonRedHuman.setBackground(Color.WHITE);
        JRadioButton buttonRedComputer = new JRadioButton("Computer");
        buttonRedComputer.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonRedComputer.setForeground(new Color(75, 0, 130));
        buttonRedComputer.setBackground(Color.WHITE);
        
        redPlayerSymbolSelection = new ButtonGroup();
        redPlayerSymbolSelection.add(buttonRedS);
        redPlayerSymbolSelection.add(buttonRedO);
        redSelectionGroup = new ButtonGroup();
        redSelectionGroup.add(buttonRedHuman);
        redSelectionGroup.add(buttonRedComputer);
        
        panelEast.add(labelRedPlayer);
        panelEast.add(buttonRedHuman);
        panelEast.add(buttonRedS);
        panelEast.add(buttonRedO);          
        panelEast.add(buttonRedComputer);
        
        buttonRedComputer.addActionListener(e -> {
        	
            computerRed = new Computer(board, 'R');
            redPlayerSymbolSelection.clearSelection();
            buttonRedS.setEnabled(false);
            buttonRedO.setEnabled(false);
            buttonRedHuman.setEnabled(false);
            if (board.getCurrentPlayer() == 'R') {
                computerRed.makeAutoMove();
                gameBoardCanvas.repaint();
                handleGameResult();
            }
        });
          
        // Configure main GUI layout
        Container ContentPane = getContentPane();
        ContentPane.setLayout(new BorderLayout());
        ContentPane.add(panelNorth, BorderLayout.NORTH);
        ContentPane.add(panelEast, BorderLayout.EAST);
        ContentPane.add(panelSouth, BorderLayout.SOUTH);
        ContentPane.add(panelWest, BorderLayout.WEST);        
        ContentPane.add(gameBoardCanvas, BorderLayout.CENTER);
        panelNorth.setPreferredSize(new Dimension(150, 100));
        panelEast.setPreferredSize(new Dimension(100, 100));        
        panelSouth.setPreferredSize(new Dimension(150, 100));  
        panelWest.setPreferredSize(new Dimension(100, 100));
        
        // Setup score-keeping and current turn
        textFieldScore = new JTextField();
        textFieldScore.setFont(new Font("Calibri", Font.PLAIN, 15));  
        textFieldScore.setBorder(null);
        textFieldScore.setColumns(16);        
        textFieldCurrentTurn = new JTextField();
        textFieldCurrentTurn.setFont(new Font("Consolas", Font.BOLD, 16));
        textFieldCurrentTurn.setBorder(null);
        textFieldCurrentTurn.setBackground(Color.WHITE);
        textFieldCurrentTurn.setColumns(15);
        panelSouth.add(textFieldScore);
        panelSouth.add(textFieldCurrentTurn);
       
        // Setup button to select Computer vs Computer mode
        JRadioButton buttonForCompVsComp = new JRadioButton("Click for Computer vs Computer Mode");
        buttonForCompVsComp.setFont(new Font("Consolas", Font.BOLD, 14));
        buttonForCompVsComp.setForeground(new Color(75, 0, 130));
        buttonForCompVsComp.setBackground(Color.WHITE);
        buttonForCompVsComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable all player option buttons
                buttonBlueS.setEnabled(false);
                buttonBlueO.setEnabled(false);
                buttonBlueHuman.setEnabled(false);
                buttonBlueComputer.setEnabled(false);
                buttonRedS.setEnabled(false);
                buttonRedO.setEnabled(false);
                buttonRedHuman.setEnabled(false);
                buttonRedComputer.setEnabled(false);

                bluePlayerSymbolSelection.clearSelection();
                redPlayerSymbolSelection.clearSelection();

                // Log current game mode and board size to the console
                System.out.println(board.getGameMode());
                System.out.println(board.getBoardSize());

                playComputerVsComputerGame(board);
                gameBoardCanvas.repaint();
            }
        });
        
        panelNorth.add(buttonForCompVsComp);
        
        // Initialize reset button (start new game, restore defaults)
        JButton buttonNewGame = new JButton();        
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("refresh.png")); // Replace with your image path
        Image originalImage = originalIcon.getImage();
        int newWidth = 24; 
        int newHeight = 24;
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(scaledImage);
        buttonNewGame.setIcon(newIcon);
        buttonNewGame.setToolTipText("Start New Game");

        buttonNewGame.addActionListener(e -> {
            
            board.newGame();
            spinnerBoardSize.setValue(3); 
            
            buttonBlueS.setEnabled(true);
            buttonBlueO.setEnabled(true);
            buttonBlueHuman.setEnabled(true);
            buttonBlueComputer.setEnabled(true);
            buttonRedS.setEnabled(true);
            buttonRedO.setEnabled(true);
            buttonRedHuman.setEnabled(true);
            buttonRedComputer.setEnabled(true);
                       
            buttonSimpleGame.setEnabled(true);
            buttonGeneralGame.setEnabled(true);
            
            buttonForCompVsComp.setSelected(false);
            buttonBlueS.setSelected(true);
            buttonRedS.setSelected(true);
            buttonSimpleGame.setSelected(true);
            
            board = new SimpleGame();
            buttonBlueHuman.setSelected(true);
            buttonRedHuman.setSelected(true);
            
            textFieldCurrentTurn.setText(""); 
            textFieldScore.setText(""); 
            gameBoardCanvas.repaint(); 
        });
        panelSouth.add(buttonNewGame);  
	}       

    class GameBoardCanvas extends JPanel {	
    	
    	GameBoardCanvas() {
    	    addMouseListener(new MouseAdapter() {
    	        @Override
    	        public void mouseClicked(MouseEvent e) {
    	            if (board.getCurrentGameState() != GameState.PLAYING) {
    	                return;
    	            }

    	            int rowSelected = e.getY() / cellSize;
    	            int colSelected = e.getX() / cellSize;

    	            if (board.getCell(rowSelected, colSelected) != Cell.EMPTY) {
    	                return;
    	            }

    	            currentPlayerTurn = board.getCurrentPlayer();
    	            ButtonModel selection = currentPlayerTurn == 'B' ? bluePlayerSymbolSelection.getSelection() : redPlayerSymbolSelection.getSelection();

    	            if (selection != null && selection.getActionCommand() != null) {
    	                currentPlayerSymbol = Cell.valueOf(selection.getActionCommand());
    	                board.makeMove(rowSelected, colSelected, currentPlayerSymbol);
    	                gameBoardCanvas.repaint();
    	                handleGameResult();
    	            }
    	        }
    	    });
    	}

		@Override
		public void paintComponent(Graphics g) { 
			super.paintComponent(g);   
			setBackground(Color.WHITE);
			
		    int canvasSize = Math.min(getWidth(), getHeight());
		    cellSize = canvasSize / board.getBoardSize();
		    cellPadding = cellSize / 20;
		    symbolSize = cellSize - cellPadding * 2;
		    
			drawGridLines(g);
			drawBoard(g);
			drawSOSLines(g);
		}
		
		// Draws grid lines according to board size
	    private void drawGridLines(Graphics g) {
	        g.setColor(Color.BLACK);
	        int gridSize = getSize().width / board.getBoardSize();
	        
	        for (int i = 1; i < board.getBoardSize(); i++) {
	            int pos = i * gridSize;
	            g.drawLine(pos, 0, pos, getSize().height);
	            g.drawLine(0, pos, getSize().width, pos);
	        }
	    }
	
	    // Draws S and O symbols on the board
	    private void drawBoard(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
	        for (int row = 0; row < board.getBoardSize(); row++) {
	            for (int col = 0; col < board.getBoardSize(); col++) {
	                int x1 = col * cellSize + cellPadding;
	                int y1 = row * cellSize + cellPadding;
	                Cell cellValue = board.getCell(row, col);
	                if (cellValue == Cell.S || cellValue == Cell.O) {
	                    drawSymbol(g2d, cellValue.name().charAt(0), x1, y1);
	                }
	            }
	        }
	    }
	}
    
    private void playComputerVsComputerGame(Board board) {
	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() { // create new swing worker
	        @Override
	        protected Void doInBackground() throws Exception {
	        	computerBlue = new Computer(board, 'B');
	        	computerRed = new Computer(board, 'R');
	            while (board.getCurrentGameState() == GameState.PLAYING) {
	                // Make the blue computer's move
	                if (board.getCurrentPlayer() == 'B') {
	                    computerBlue.makeAutoMove();
	                }

	                // Make the red computer's move
	                if (board.getCurrentGameState() == GameState.PLAYING && board.getCurrentPlayer() == 'R') {
	                    computerRed.makeAutoMove();
	                }

	                // Publish intermediate results (this triggers the process method)
	                publish();
	                
	                // Delay for a short period to visualize the moves
	                try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }

	            return null;
	        }

	        @Override
	        protected void process(List<Void> chunks) {
	            // Update GUI
	            gameBoardCanvas.repaint();
	        }

	        @Override
	        protected void done() {
	            // Display game result
	            gameBoardCanvas.repaint();
	            handleGameResult();
	        }
	    };

	    // Execute the SwingWorker
	    worker.execute();
	} 
	
	//Update GUI based on game's current state and scores
    public void handleGameResult() {
        // Update the status message based on the game state
        switch (board.getCurrentGameState()) {
            case BLUE_WINS:
                textFieldCurrentTurn.setText("Blue Wins!");
                break;
            case RED_WINS:
                textFieldCurrentTurn.setText("Red Wins!");
                break;
            case DRAW:
                textFieldCurrentTurn.setText("Tie Game!");
                break;
            default:
                // Show which player's turn it is during an ongoing game.
                textFieldCurrentTurn.setText("Play: " + (board.getCurrentPlayer() == 'B' ? "Blue" : "Red"));
                break;
        }	        
        // Update scores for both players.
        textFieldScore.setText(String.format("Blue Score: %d, Red Score: %d", board.getBlueScore(), board.getRedScore()));
        repaint();
    }

	private void drawSOSLines(Graphics g) {
	    List<SOSEvent> sosEventList = board.getSOSEventList();
	    Map<SOSEvent, Character> playerSOSMap = board.getPlayerSOSMap();

	    for (SOSEvent event : sosEventList) {
	        Color playerColor = getPlayerColor(playerSOSMap.get(event));
	        g.setColor(playerColor);
	        drawLine(g, event);
	    }
	}	
	
	private void drawLine(Graphics g, SOSEvent event) {
	        
	        int startRow = event.getRow()* cellSize + cellSize/2;
	        int startCol = event.getColumn()* cellSize + cellSize/2;
	        String direction = event.getDirection();

	        int lineLength = (int) (cellSize * 2.3);
	        ((Graphics2D) g).setStroke(new BasicStroke(5));

	        // Draw a line based on the type of the SOS event
	        switch (direction) {
	        case "row":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow);
	            break;
	        case "column":
	            g.drawLine(startCol, startRow, startCol, startRow + lineLength);
	            break;
	        case "diagTlBr":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow + lineLength);
	            break;
	        case "diagTrBl":
	            g.drawLine(startCol, startRow, startCol - lineLength, startRow + lineLength);
	            break;
	        }
	 }

	 // Draw a symbol at specific cell coordinates
	 private void drawSymbol(Graphics2D g2d, char symbol, int x, int y) {
		    String letter = String.valueOf(symbol);
		    
		    int fontSize = cellSize > 30 ? symbolSize : 24;
		    Font font = new Font("Lucida Console", Font.BOLD, fontSize);
		    g2d.setFont(font);

		    FontMetrics fm = g2d.getFontMetrics(font);
		    int textWidth = fm.stringWidth(letter);
		    int textHeight = fm.getHeight();
		    
		    int centerX = x + (cellSize - textWidth) / 2; // Calculate the center position for the text
		    int centerY = y + (cellSize - textHeight) / 2 + fm.getAscent();

		    g2d.setColor(Color.BLACK);
		    g2d.drawString(letter, centerX, centerY);
	}
	    
    private Color getPlayerColor(char player) {
	    return player == 'B' ? Color.BLUE : Color.RED;
	}
    
    public Board getBoard(){
		return board;
	}
		
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
                gui.board.initializeBoard();
            }
		});
	}
}
