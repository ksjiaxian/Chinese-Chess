import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Horse game piece
 * 
 * A game piece displayed by an image. The horse can only move like the knight
 * in international chess
 * 
 * Note that the image is read from the file when the object is constructed, 
 * the image can be red or black depending on the argument in the constructor
 */
public class Horse extends Piece {
	// fields
    public static final String IMG_FILE_BLACK = "files/horseblack.png";
    public static final String IMG_FILE_RED = "files/horsered.png";
    private BufferedImage img;

    public Horse(int x, int y, boolean isRed, int num) {
    	// initialize the super class constructor
        super(x, y, 4, num, isRed);

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
		
		// unlike international chess, the horse can be blocked by a piece
		// directly in front of where it wants to go
		// get the potential coordinates of this location
		int blockX = Math.min(x, this.getX()) + 1;
		int blockY = Math.min(y, this.getY()) + 1;
		
		// can only move 2 spaces horizontal and 1 space vertical or 
		// 1 space horizontal and 2 spaces vertical
		if (Math.abs(x - this.getX()) == 1 && 
			Math.abs(y - this.getY()) == 2 && 
			board[this.getX()][blockY] == null) {
			return true;
		} else if (Math.abs(x - this.getX()) == 2 && 
				   Math.abs(y - this.getY()) == 1 &&
				   board[blockX][this.getY()] == null) {
			return true;
		}
		
		// otherwise assume that it's an invalid move
		return false;
	}
	
	public Piece dup() {
		return new Horse(this.getX(), this.getY(), 
						   this.getColor(), this.getNum());
	}
}