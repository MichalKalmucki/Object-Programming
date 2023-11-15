package org.example.console;

import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.example.animal.Animal;
import org.example.place.Place;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;

/**
 * Class responsible for showing information about animal/place when it is clicked
 */
public class ShowInfo implements MouseListener {
    private Board board;

    public ShowInfo(Board board){
        this.board = board;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/org/example/assets/TrajanPro-Regular.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to pass information about clicked thing to board
     * <p> 
     * when clicked thing was a place there will be information about that place added to board
     * </p>
     * <p> 
     * when clicked thing was an animal additionally button able to remove it will be added
     * </p>
     * <p>
     * if clicked thing was neither of above previous information will be erased
     * </p>
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        int index = label.getParent().getComponentZOrder(label);
        int row = index / board.n;
        int col = index % board.n;
        Point point = new Point(col, row);
        JTextArea info = new JTextArea();
        int width = board.panel_width;
        
        info.setOpaque(true);
        info.setFont(new Font("TrajanPro-Regular", Font.PLAIN, 15));
        info.setBackground(new Color(45, 45, 45));
        info.setForeground(new Color(230, 230, 230));
        info.setBorder(BorderFactory.createEtchedBorder());

        if(board.is_taken(point)){
            Animal animal = board.get_taken(point);
            JButton button = new JButton("Remove selected");
            button.addActionListener(new KillAnimal(button, board, animal));
            info.setPreferredSize(new Dimension(width - 10, board.n * board.m - 3*width/4 - 30));
            info.setWrapStyleWord(true);
            info.setLineWrap(true);

            info.setText(animal.getInfo());
            board.add_toPanel(info, button);
        }
        
        else if(board.is_place(point)){
            info.setPreferredSize(new Dimension(width - 10, board.n * board.m - width/2 - 10));
            info.setWrapStyleWord(true);
            info.setLineWrap(true);
            Place place = board.getPlace(point);
            info.setText(place.getInfo());
            board.add_toPanel(info, null);
        }
        else {
            board.add_toPanel(null, null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
