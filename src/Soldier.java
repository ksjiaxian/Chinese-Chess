import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Soldier game piece
 * 
 * A game piece displayed by an image.
 * 
 * Note that the image is read from the file when the object is constructed, 
 * the image can be red or black depending on the argument in the constructor
 */
public class Soldier extends Piece {
	// fields
	// soldier gains more mobility after crossing river
    private boolean crossedRiver;
    public static final String IMG_FILE_BLACK = "files/soldierblack.png";
    public static final String IMG_FILE_RED = "files/soldierred.png";
    private BufferedImage img;

    public Soldier(int x, int y, boolean isRed, int num) {
    	// initialize the super class constructor
        super(x, y, 1, num, isRed);
        
        // on your side of the river
        crossedRiver = false;

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
			
		// check if crossed the river
		if ((getColor() && y >= 5) || (!getColor() && y < 5) ) {
			crossedRiver = true;
		}
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
		
		// this piece can not move sideways until it has crossed the river
		if (getColor() && !crossedRiver && x - this.getX() == 0 && 
			y - this.getY() == 1) {
			return true;
		} else if (!getColor() && !crossedRiver && (x - this.getX() == 0 && 
			this.getY() - y == 1)) {
			return true;
		} 
		
		// if crossed river, can only move one space horizontal or one space
		// vertical forward
		if (crossedRiver && Math.abs(x - this.getX()) == 1 && 
			y - this.getY() == 0) {
			return true;
		} else if (getColor() && crossedRiver && y - this.getY() == 1 && 
				   x - this.getX() == 0) {
			return true;
		} else if (!getColor() && crossedRiver && this.getY() - y == 1 && 
				   x - this.getX() == 0) {
			return true;
		} 
		
		// otherwise assume that it's an invalid move
		return false;
	}
	
	// return whether or not this piece has crossed the river
	public boolean hasCrossedRiver() {
		return crossedRiver;
	}
	
	public Piece dup() {
		return new Soldier(this.getX(), this.getY(), 
						   this.getColor(), this.getNum());
	}
}