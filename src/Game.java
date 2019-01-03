// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main game class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Chinese Chess");
        frame.setLocation(50, 50);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Player 1's Turn");
        status.setFont(new Font("Arial", Font.BOLD, 20));
        status_panel.add(status);

        // Main playing area
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        // create the button and give it an action listener
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	board.undo();
            }
        });
        control_panel.add(undo);

        // create the button and give it an action listener
        final JButton reset = new JButton("New Game");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	board.reset();
            }
        });
        control_panel.add(reset);
        
        // add an action listener to the reset button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	board.viewInstructions();
            }
        });
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        board.reset();
    }

    // draw the board
    /**
     * Main method run to start and run the game. Initializes the GUI 
     * elements specified in Game and runs it.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}