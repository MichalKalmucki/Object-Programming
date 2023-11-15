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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import org.example.animal.Prey;

/**
 * Action listener creating preys when button is clicked
 */
public class CreatePrey implements ActionListener {
    private Random random = new Random();
    private JButton button;
    private Board board;
    private HashSet<Point> points;
    private ArrayList<Point> pointlist;
    private int idx = 0;
    
    /**
     * Constructor of CreatePrey class
     * @param _button button responsible for creating preys
     * @param board board on which to create predator
     */
    CreatePrey(JButton _button, Board board){

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/org/example/assets/TrajanPro-Regular.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.button = _button;
        this.board = board;
        this.points = board.get_paths();
        this.pointlist = new ArrayList<Point>(points);
        int width = board.panel_width;
        button.setPreferredSize(new Dimension(width - 10, width/4));
        button.setFont(new Font("TrajanPro-Regular", Font.BOLD, 20));
        button.setFocusable(false);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setForeground(new Color(240, 240, 240));
        button.setBackground(new Color(45,45,45));
        button.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Method to create prey on button clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == button){
            int i = random.nextInt(pointlist.size());
            Point p = pointlist.get(i);
            while (board.is_taken(p) || board.is_place(p)){
                i = random.nextInt(pointlist.size());
                p = pointlist.get(i);
            }

            new Prey("prey button" + idx++, random.nextInt(400, 601), random.nextInt(10, 26) , "Vessel", p.getX(), p.getY(), random.nextInt(75, 156), random.nextInt(100, 201), random.nextInt(100, 201), board, false).start();
        }
    }
}
