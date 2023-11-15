package org.example.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import org.example.animal.Animal;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;
import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * Action listener killing animal when button is clicked
 */
public class KillAnimal implements ActionListener{
    JButton button;
    Board board;
    Animal animal;

    /**
     * Constructor of KillAnimal class
     * @param _button button responsible for killing animals
     * @param board board on which to kill animal
     * @param animal animal to be killed
     */
    public KillAnimal(JButton button, Board board,  Animal animal){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/org/example/assets/TrajanPro-Regular.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.button = button;
        this.animal = animal;
        this.board = board;
        int width = board.panel_width;
        button.setPreferredSize(new Dimension(width - 10, width/4));
        button.setOpaque(true);
        button.setFont(new Font("TrajanPro-Regular", Font.PLAIN, 15));
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(new Color(230, 230, 230));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Method to set selected animal dead
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button){
            animal.setDead();
        }
    }
    
}
