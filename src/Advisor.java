import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Advisor game piece 
 * 
 * A game piece displayed by an image. The Advisor can only move diagonally in a 
 * 3x3 square on the player's side of the board.
 * 
 * Note that the image is read from the file when the object is constructed, 
 * the image can be red or black depending on the argument in the constructor
 */
public class Advisor extends Piece {
	// fields
    public static final String IMG_FILE_BLACK = "files/advisorblack.png";
    public static final String IMG_FILE_RED = "files/advisorred.png";
    private BufferedImage img;

    public Advisor(int x, int y, boolean isRed, int num) {
    	// initialize the super class constructor
        super(x, y, 2, num, isRed);

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
		// check to make sure in the bounds of the palace
		if (getColor() && ( x < 3 || x > 5 || y < 0 || y > 2)) {
			return false;
		} else if (!getColor() && ( x < 3 || x > 5 || y < 7 || y > 9)) {
			return false;
		} 
		
		// check if there's already a piece of the player's there
		if (board[x][y] != null && !(this.getColor() ^ board[x][y].getColor())){
			return false;
		}
		
		// can only move one space diagonal
		if (Math.abs(x - this.getX()) == 1 && Math.abs(y - this.getY()) == 1) {
			return true;
		}
		
		// otherwise assume that it's an invalid move
		return false;
	}
	
	public Piece dup() {
		return new Advisor(this.getX(), this.getY(), 
						   this.getColor(), this.getNum());
	}
}