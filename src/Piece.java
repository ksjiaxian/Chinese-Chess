import java.awt.Graphics;



/** 
 * An piece object for the game
 *
 * Game objects exist in the game board. They can only move from valid 
 * coordinates to valid coordinates.
 */

public abstract class Piece implements Comparable<Piece> {

    /*
     * Current coordinates of the piece (in terms of the game board array)
     */
    private int x; 
    private int y;
    
    /* Size of object, in pixels. All pieces will be the same dimension */
    final private int WIDTH = 70;
    final private int HEIGHT = 70;
    
    // strength of the piece
    private final int strength;
    
    // number of the piece
    private final int num;
    
    // the color of the piece
    private final boolean isRed;


    /**
     * Constructor
     */

    public Piece(int x, int y, int strength, int num, boolean isRed) {

    	this.num = num;
    	this.strength = strength;
        this.x = x;
        this.y = y;
        this.isRed = isRed;
    }

    /*** GETTERS ***********************************************/

    public int getX() {
        return this.x;
    }

    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.WIDTH;
    }

    
    public int getHeight() {
        return this.HEIGHT;
    }
    
    public boolean getColor() {
    	return isRed;
    }
    
    /*** SETTERS ***********************************************/
    
    public void setX(int x) {
        this.x = x;
    }

    
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Checks if the move is legal for a given piece.
     * 
     * @param x coor
     * @param y coor
     * @return boolean is legal
     */

    public abstract boolean canMove(int x, int y, Piece[][] board);

    
    /**
     * Moves the object by giving the object a new set of coordinates.
     * Implementations of each type of piece will check if move is legal, 
     * returns true if so
     * 
     * @param x coor
     * @param y coor
     * @return boolean is legal
     */

    public abstract void move(int x, int y, Piece[][] board);


    /**
     * Default draw method that provides how the object should be drawn in the 
     * GUI. This method does not draw anything. Subclass should 
     * override this method based on how their object should appear.
     * 
     * @param g graphics context
     */

    public abstract void draw(Graphics g);
    
	// return the strength of the piece
	public int getStrength() {
		return strength;
	}
	
	// return the num of the piece
	public int getNum() {
		return num;
	}
	
	// duplicate the piece to help with saving state
	public abstract Piece dup();
	
	
	// for comparable
	@Override
	public int compareTo(Piece p) {
		return (this.getStrength() - p.getStrength()) * 10 +
				(this.num - p.getNum());
	}
}