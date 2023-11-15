package org.example.console;

import java.awt.GraphicsEnvironment;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import org.example.animal.Predator;

/**
 * Action listener creating predators when button is clicked
 */
public class CreatePredator implements ActionListener {

    JButton button;
    Board board;
    private int idx = 0;
    Random random = new Random();
    
    /**
     * Constructor of CreatePredator class
     * @param _button button responsible for creating predators
     * @param board board on which to create predator
     */
    CreatePredator(JButton _button, Board board){

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/org/example/assets/TrajanPro-Regular.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.button = _button;
        this.board = board;
        int width = board.panel_width;
        button.setPreferredSize(new Dimension(width - 10, width/4));
        button.setFont(new Font("TrajanPro-Regular", Font.BOLD, 20));
        button.setFocusable(false);
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(new Color(230, 230, 230));
        button.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Method to create predator on button clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button){
            int i = random.nextInt(board.n);
            int j = random.nextInt(board.n);
            Point p = new Point(i, j);
            while (board.is_taken(p) || board.is_place(p)){
                i = random.nextInt(board.n);
                j = random.nextInt(board.n);
                p = new Point(i, j);
            }
            new Predator("predator button" + idx++, random.nextInt(100, 201), random.nextInt(30, 61), "Zote the mighty", i, j, board).start();
        }
    }
}