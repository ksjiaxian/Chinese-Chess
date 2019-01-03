import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Cannon game piece
 * 
 * A game piece displayed by an image.
 * 
 * Note that the image is read from the file when the object is constructed, 
 * the image can be red or black depending on the argument in the constructor
 */
public class Cannon extends Piece {
	// fields
    public static final String IMG_FILE_BLACK = "files/cannonblack.png";
    public static final String IMG_FILE_RED = "files/cannonred.png";
    private BufferedImage img;
    
    // name of the piece

    public Cannon(int x, int y, boolean isRed, int num) {
    	// initialize the super class constructor
        super(x, y, 5, num, isRed);

        try {
            if (img == null) {
            	if (isRed) {
            		img = ImageIO.read(new File(IMG_FILE_RED));
            	} else {
            		img = ImageIO.read(new File(IMG_FILE_BLACK));
            	}
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
    	// must displace the piece given the dimensions of the array
    	int px = this.getX() * 70;
    	int py = 630 - this.getY() * 70;
    	
    	// draw the image
        g.drawImage(img, px, py, this.getWidth(), this.getHeight(), null);
    }

	@Override
	public void move(int x, int y, Piece[][] board) {
		// move the piece
		super.setX(x);
		super.setY(y);
	}

	@Override
	public boolean canMove(int x, int y, Piece[][] board) {
		// check to make sure in the bounds of the board
		if (x < 0 || x > 8 || y < 0 || y > 9) {
			return false;
		}
		
		// if there is a piece of the player on the board, can't move there
		if (board[x][y] != null && !(board[x][y].getColor()^this.getColor())) {
			return false;
		}
		
		// check if the cannon will be attacking
		if (board[x][y] != null && (this.getColor() ^ board[x][y].getColor())){
			
			// check that there is one piece between
			if (x - this.getX() == 0) {
				// piece counter
				int ctr = 0;
				for (int i = 1; i < Math.abs(y - this.getY()); i++) {
					if (board[x][Math.min(y, this.getY()) + i] != null) {
						// there is a piece here
						ctr++;
					}
				}
				// if attacking and one piece between, then move is possible
				return ctr == 1;
			} else if (y - this.getY() == 0) {
				// piece counter
				int ctr = 0;
				for (int i = 1; i < Math.abs(x - this.getX()); i++) {
					if (board[Math.min(x, this.getX()) + i][y] != null) {
						// there is a piece in the way
						ctr++;
					}
				}
				// if attacking and one piece between, then move is possible
				return ctr == 1;
			}
			// then it's just an illegal move
			return false;
		}
		
		// just moving, check that there is a clear path
		if (x - this.getX() == 0) {
			for (int i = 1; i < Math.abs(y - this.getY()); i++) {
				if (board[x][Math.min(y, this.getY()) + i] != null) {
					// there is a piece in the way
					return false;
				}
			}
			// there is a clear path
			return true;
		} else if (y - this.getY() == 0) {
			for (int i = 1; i < Math.abs(x - this.getX()); i++) {
				if (board[Math.min(x, this.getX()) + i][y] != null) {
					// there is a piece in the way
					return false;
				}
			}
			// there is a clear path
			return true;
		} else {
			// otherwise assume that it's an illegal move
			return false;
		}
	}
	
	public Piece dup() {
		return new Cannon(this.getX(), this.getY(), 
						   this.getColor(), this.getNum());
	}
}