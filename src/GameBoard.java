import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameBoard
 * 
 * This class holds the primary game board model and handles the piece movement
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {
	/* FIELDS */
	
	// a deque of the board states - allows for undoing
	private Deque<Piece[][]> states;
	
    // the state of the game logic
    private Piece[][] board;
    
    // to keep track of the turns
    private boolean redTurn;

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int BOARD_WIDTH = 630;
    public static final int BOARD_HEIGHT = 700;
    
    // board
 	final String IMG_FILE_BOARD = "files/board.png";
 	private BufferedImage boardImage;
 	
 	// instructions
 	final String IMG_FILE_INSTRUCT = "files/instructions.png";
 	private BufferedImage instructionsImage;
 	
 	// highlighting dots
 	final String IMG_FILE_PIECELIGHT = "files/pieceHighlight.png";
 	private BufferedImage highlightImage;
 	
 	final String IMG_FILE_MOVELIGHT = "files/moveHighlight.png";
 	private BufferedImage moveImage;
    
    // placement mode
    private Mode mode = null;
    
    // give references to the players' generals
    private Piece redGeneral;
    private Piece blackGeneral;
    
    private Piece selectedPiece = null;

    public GameBoard (JLabel status) {
    	// start playing
    	playing = true;
    
    	// initialize the sets of pieces and the board
    	startGame();
    	
    	// red goes first
    	redTurn = true;
    	
    	// pick up mode
    	mode = Mode.PickUpMode;
    	
    	// load the board image
        try {
            if (boardImage == null) {
            	boardImage = ImageIO.read(new File(IMG_FILE_BOARD));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        // load the instructions image
        try {
            if (instructionsImage == null) {
            	instructionsImage = ImageIO.read(new File(IMG_FILE_INSTRUCT));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        // load the highlighting dots
        try {
            if (highlightImage == null) {
            	highlightImage = ImageIO.read(new File(IMG_FILE_PIECELIGHT));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        try {
            if (moveImage == null) {
            	moveImage = ImageIO.read(new File(IMG_FILE_MOVELIGHT));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    	
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // create mouse listener
        Mouse mouse = new Mouse();
        addMouseListener(mouse);

        this.status = status;
    }
    
    // create the pieces in the correct places
    private void startGame() {
    	// reset mode
    	mode = Mode.PickUpMode;
    	
    	// initialize the board
    	board = new Piece[9][10];
    	
    	// add all pieces
    	redGeneral = new General(4,0, true, 1);   	
    	board[4][0] = redGeneral;
    	board[0][0] = new Chariot(0,0, true, 1);
    	board[8][0] = new Chariot(8,0, true, 2);
    	board[1][0] = new Horse(1,0, true, 1);
    	board[7][0] = new Horse(7,0, true, 2);
    	board[2][0] = new Elephant(2,0, true, 1);
    	board[6][0] = new Elephant(6,0, true, 2);
    	board[3][0] = new Advisor(3,0, true, 1);
		board[5][0] = new Advisor(5,0, true, 2);
		board[1][2] = new Cannon(1,2, true, 1);
		board[7][2] = new Cannon(7,2, true, 2);
		board[0][3] = new Soldier(0,3, true, 1);
		board[2][3] = new Soldier(2,3, true, 2);
		board[4][3] = new Soldier(4,3, true, 3);
		board[6][3] = new Soldier(6,3, true, 4);
		board[8][3] = new Soldier(8,3, true, 5);
    					
    	
    	blackGeneral = new General(4,9, false, 1);   	
    	board[4][9] = blackGeneral;
    	board[0][9] = new Chariot(0,9, false, 1);
    	board[8][9] = new Chariot(8,9, false, 2);
    	board[1][9] = new Horse(1,9, false, 1);
    	board[7][9] = new Horse(7,9, false, 2);
    	board[2][9] = new Elephant(2,9, false, 1);
    	board[6][9] = new Elephant(6,9, false, 2);
    	board[3][9] = new Advisor(3,9, false, 1);
		board[5][9] = new Advisor(5,9, false, 2);
		board[1][7] = new Cannon(1,7, false, 1);
		board[7][7] = new Cannon(7,7, false, 2);
		board[0][6] = new Soldier(0,6, false, 1);
		board[2][6] = new Soldier(2,6, false, 2);
		board[4][6] = new Soldier(4,6, false, 3);
		board[6][6] = new Soldier(6,6, false, 4);
		board[8][6] = new Soldier(8,6, false, 5);
		  	
		// create the save states of the board
		states = new LinkedList<Piece[][]>();
		  	
		repaint();
    }
    
	/** Current placement mode */
	private enum Mode {
		PickUpMode, 
		PlaceDownMode,
		InstructionsMode
	}
	
	// given a point get the associated x array coordinate
	private int getXCoor(Point point) {
		return (int) Math.floor(point.getX() / 70);
	}
	
	// given a point get the associated y array coordinate
	private int getYCoor(Point point) {
		return (int) Math.floor(point.getY() / 70);
	}
	
    // to add actions to clicks
	private class Mouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent click) {
			// only continue playing if still playing
			if (!playing) {
				return;
			}
			
			// get the associated x and y coordinates
			Point p = click.getPoint();
			int x = getXCoor(p);
			int y = 9 - getYCoor(p);
			
			switch (mode) {
			case InstructionsMode:
				// want to have a way to exit out of the instructions panel
				if (x > 6 && y == 9) {
					mode = Mode.PickUpMode;
					repaint();
				}
				break;
				
			case PickUpMode: 
				// check if there's a piece and is that player's piece
				if (board[x][y] == null || 
					(redTurn ^ board[x][y].getColor())){
					return;
					
				} else {
					// select the piece and go to place down mode
					selectedPiece = board[x][y];
					mode = Mode.PlaceDownMode;
				}
				
				repaint();
				
				break;
				
			case PlaceDownMode:
				// check if piece can legally move there, and that space isn't
				// occupied by another one of player's pieces
				if (selectedPiece.canMove(x, y, board)) {
					// check move the piece or capture another piece
					if (board[x][y] == null) {
						// move the piece
						int oldX = selectedPiece.getX();
						int oldY = selectedPiece.getY();
						
						// save before making changes
						save();
								
						board[oldX][oldY] = null;
						board[x][y] = selectedPiece;
						
						// change the coordinates in the piece object
						selectedPiece.move(x, y, board);
						
						redTurn = !redTurn;
						
					} else if (selectedPiece.getColor() ^
								board[x][y].getColor()) {
						
						// save before making changes
						save();
						
						// get a reference to the captured piece
						Piece captured = board[x][y];
						
						// move the piece
						int oldX = selectedPiece.getX();
						int oldY = selectedPiece.getY();
								
						board[oldX][oldY] = null;
						board[x][y] = selectedPiece;
						
						// change the coordinates in the piece object
						selectedPiece.move(x, y, board);
						
						// see if the game is over
						if (captured.getClass().getName().equals("General")) {
							playing = false;
							
							selectedPiece = null;
							
							if (redTurn) {
								status.setText("Player 1 Wins!!!");
							} else {
								status.setText("Player 2 Wins!!!");
							}
							// paint the final state of the board and exit
							repaint();
							return;
						}
						
						redTurn = !redTurn;
						
					}
				}
				// reset the movement logic
				selectedPiece = null;
				mode = Mode.PickUpMode;
				
				break;
				
			}
			// keep track of the turns
			if (redTurn) {
				status.setText("Player 1's Turn");
			} else {
				status.setText("Player 2's Turn");
			}
			
			// repaint the board
			repaint();
		}
	}

    /**
     * Reset the game to its initial state.
     */
    public void reset() {
        startGame();

        playing = true;
        
        // red's turn
        redTurn = true;
        
        selectedPiece = null;
        
        status.setText("Player 1's Turn");
        
        repaint();
    }
    
    /**
     * View the instructions of the game
     */
    public void viewInstructions() {
    	// put the board into instructions display mode
    	mode = Mode.InstructionsMode;
        
        repaint();
    }

    // paint the game board
    @Override
    public void paintComponent(Graphics g) {
    	switch(mode) {
    	case InstructionsMode:
    		// want to draw the instructions over the board
    		
    	case PickUpMode:
    		
    	case PlaceDownMode:
    		// draw the board
	        super.paintComponent(g);
	        
	        // draw the background
	        g.drawImage(boardImage, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
	    	
    		// highlight the selected piece
    		if (selectedPiece != null) {
        		g.drawImage(highlightImage, selectedPiece.getX() * 70, 
        					630 - selectedPiece.getY() * 70, 70, 70, null);
    		}
    		
    		
	        // iterate through the board and paint
			for (int i = 0; i < 9;i++) {
				for (int j = 0; j < 10; j++) {
					if (board[i][j] == null ) {
						continue;
					} else {
						board[i][j].draw(g);;
					}
				}
			}
			
			// highlight the move options
			if (selectedPiece != null) {
				boolean[][] moves = availableMoves();
        		
        		// iterate through all spaces and see where piece can move
        		for (int i = 0; i < 9; i++) {
        			for (int j = 0; j < 10; j++) {
        				if (moves[i][j] && board[i][j] == null) {
        	        		g.drawImage(moveImage, 20 + i * 70, 660 - j * 71, 
        	        					25, 25, null);
        				}
        				if (moves[i][j] && board[i][j] != null) {
        					g.drawImage(moveImage, i * 70, 630 - j * 70, 
                						70, 70,null);
        				}
        			}
        		}
			}
			
			 
	    	// if in instruction mode, draw instructions over board
	    	if (mode != Mode.InstructionsMode) {
	    		break;
	    	} else {
	    		g.drawImage(instructionsImage, 65, 25, 500, 657, null);
	    	}
    	}
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
    
    // save the state of the game into the deque
	private void save() {
    	
		// replace the board
		Piece[][] savedBoard = new Piece[9][10];
		
		// iterate through the board and copy over to the save state
		for (int i = 0; i < 9;i++) {
			for (int j = 0; j < 10; j++) {
				if (board[i][j] == null ) {
					continue;
				} else {
					savedBoard[i][j] = board[i][j].dup();
				}
			}
		}
		
		states.add(savedBoard);
	}

    // to undo turns in-game
	public void undo() {
		try {
			// get the previous state and load it in
			board = states.removeLast();
			
			// other player's turn
			redTurn = !redTurn;
			
			// keep track of the turns
			if (redTurn) {
				status.setText("Player 1's Turn");
			} else {
				status.setText("Player 2's Turn");
			}
			
			// go back to pick up mode
			mode = Mode.PickUpMode;
			
			repaint();
			
		} catch (NoSuchElementException e) {
			System.out.println("No More Undos");
		}
	}
	
	// this method gets all of the available moves of the highlighted piece
	private boolean[][] availableMoves() {
		// error if no selected piece
		if (selectedPiece == null) {
			return null;
		}
		
		// available moves
		boolean[][] moves = new boolean[9][10];
		
		// iterate through all spaces and see if this piece can move there
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				if (selectedPiece.canMove(i, j, board)) {
					moves[i][j] = true;
				} else {
					moves[i][j] = false;
				}
			}
		}
		
		return moves;
	}
}