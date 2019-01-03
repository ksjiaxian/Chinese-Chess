import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Chariot game piece
 * 
 * A game piece displayed by an image.
 * 
 * Note that the image is read from the file when the object is constructed, 
 * the image can be red or black depending on the argument in the constructor
 */
public class Chariot extends Piece {
	// fields
    public static final String IMG_FILE_BLACK = "files/chariotblack.png";
    public static final String IMG_FILE_RED = "files/chariotred.png";
    private BufferedImage img;

    public Chariot(int x, int y, boolean isRed, int num) {
    	// initialize the super class constructor
        super(x, y, 9, num, isRed);

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
		
		// check if there's already a piece of the player's there
		if (board[x][y] != null && !(this.getColor() ^ board[x][y].getColor())){
			return false;
		}
		
		// check that there is a clear path between start and destination
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
		return new Chariot(this.getX(), this.getY(), 
						   this.getColor(), this.getNum());
	}
}