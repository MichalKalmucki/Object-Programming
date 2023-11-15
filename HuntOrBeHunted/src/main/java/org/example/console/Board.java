package org.example.console;

import org.example.animal.Animal;
import org.example.animal.Prey;
import org.example.place.Hideout;
import org.example.place.Place;
import org.example.place.resource.Plant;
import org.example.place.resource.Resource;
import org.example.place.resource.Water;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Class responsible for creating and visualising board
 * <p>
 * Board is not randomly generated
 * </p>
 */
public class Board extends JFrame {
JFrame frame = new JFrame("Zote boat");
JPanel board_panel = new JPanel();
JPanel control_panel = new JPanel();
JButton Prey_button;
JButton Predator_button;

/**
 * Board has nxn squares
 */
public final int n = 21;
/**
 * size of a square mxm
 */
public final int m = 40;
public final int panel_width = 200;
Random random = new Random();
private HashMap<Point, Place> places = new HashMap<Point, Place>();
private HashSet<Point> crossings = new HashSet<Point>();
private HashMap<Point, Animal> taken = new HashMap<Point, Animal>();

/**
 * Constructor of Board class creating board and adding places on it
 * <p> 
 * Also creates control panel to the right of the board
 * </p>
 */
public Board(){

    board_panel.setLayout(new GridLayout(n, n));
    board_panel.setPreferredSize(new Dimension(n*m, n*m));
    Color path_color = new Color(100,100,100);
    for (int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(m,m));
        label.setOpaque(true);
        label.addMouseListener(new ShowInfo(this));
        if(((i == 1 || i == 4 || i == 7 || i == 10 || i == 13 || i == 13 || i == 16 || i == 19) 
            || (j == 1 || j == 4 || j == 7 || j == 10 || j == 13 || j == 13 || j == 16 || j == 19))
            && !(i == 0 || i == n - 1 || j == 0 || j == n - 1)){
            label.setBackground(path_color);
        }
        else if ((i+j) % 2 == 0) {
            label.setBackground(new Color(39, 68, 105));
        } else {
            label.setBackground(new Color(31, 53, 92));
        }
        if(((i == 1 || i == n - 2) && (j == 1 || j == n - 2) ) || (i == 10 && j == 10)){
            label.setOpaque(true);
            ImageIcon icon = new ImageIcon("src/main/java/org/example/assets/uafka.png");
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(m, m, Image.SCALE_SMOOTH); 
            icon.setImage(scaledImage); 
            label.setIcon(icon);
            places.put(new Point(i, j), new Hideout(random.nextInt(3, 7), i, j, this));
        }
        if((i == 4 && j == 7) || (j == 4 && i == 7) || ((i == 13 && j == 16) || (j == 13 && i == 16))){
            label.setOpaque(true);
            ImageIcon icon = new ImageIcon("src/main/java/org/example/assets/soultotem.png");
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(m, m, Image.SCALE_SMOOTH); 
            icon.setImage(scaledImage); 
            label.setIcon(icon);
            places.put(new Point(i, j), new Plant(random.nextInt(3,7), i, j, random.nextInt(5, 20), this));
        }
        if((i == 4 && j == 13) || (i == 7 && j ==16) || (i == 13 && j == 4) || (i == 16 && j ==7)){
            label.setOpaque(true);
            ImageIcon icon = new ImageIcon("src/main/java/org/example/assets/HotSpring.png");
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(m, m, Image.SCALE_SMOOTH); 
            icon.setImage(scaledImage); 
            label.setIcon(icon);
            places.put(new Point(i, j), new Water(random.nextInt(3,7), i, j, random.nextInt(5, 20), this));
        }
        if(i % 3 == 1 && j % 3 == 1){
            crossings.add(new Point(i, j));
        }
        board_panel.add(label);
        }
    }
    Prey_button = new JButton("Add Vessel");
    Prey_button.addActionListener(new CreatePrey(Prey_button, this));
    Predator_button = new JButton("Add Zote");
    Predator_button.addActionListener(new CreatePredator(Predator_button, this));
    control_panel.setPreferredSize(new Dimension(panel_width, n*m));
    control_panel.add(Prey_button);
    control_panel.add(Predator_button);
    control_panel.setBackground(new Color(60,60,60));

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.setResizable(false);
    ImageIcon icon = new ImageIcon("src/main/java/org/example/assets/zotehead.png");
    Image image = icon.getImage();
    frame.setIconImage(image);
    frame.add(board_panel, BorderLayout.CENTER);
    frame.add(control_panel, BorderLayout.EAST);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    }

    /**
     * Method to add image on a selected square of board
     * @param x x cordinate of chosen square
     * @param y y cordinate of chosen square
     * @param file path to image
     */
    public void addImageToLabel(int x, int y, String file) {
    ArrayList<JLabel> labels = new ArrayList<>();
    for (int i = 0; i < board_panel.getComponentCount(); i++) {
        Component c = board_panel.getComponent(i);
        if (c instanceof JLabel) {
        labels.add((JLabel) c);
        }
    }
    int index = x + n*y;
    JLabel label = labels.get(index);
    label.setOpaque(true);
    ImageIcon icon = new ImageIcon(file);
    Image image = icon.getImage();
    Image scaledImage = image.getScaledInstance(m, m, Image.SCALE_SMOOTH); 
    icon.setImage(scaledImage); 
    label.setIcon(icon);
    }

    /**
     * Method to remove image from a selected square of board
     * @param point point from which image is to be deleted
     */
    public void removeImageFromLabel(Point point) {
        int x = point.getX();
        int y = point.getY();
        ArrayList<JLabel> labels = new ArrayList<>();
        for (int i = 0; i < board_panel.getComponentCount(); i++) {
            Component c = board_panel.getComponent(i);
            if (c instanceof JLabel) {
            labels.add((JLabel) c);
            }
        }
        int index = x + n*y;
        JLabel label = labels.get(index);
        label.setIcon(null);
    }

    /**
     * Method to return HashSet of points paths were created on 
     * @return HashSet of points paths were created on 
     */
    public HashSet<Point> get_paths(){
        HashSet<Point> points = new HashSet<Point>();
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if(((i == 1 || i == 4 || i == 7 || i == 10 || i == 13 || i == 13 || i == 16 || i == 19) 
                || (j == 1 || j == 4 || j == 7 || j == 10 || j == 13 || j == 13 || j == 16 || j == 19))
                && !(i == 0 || i == n - 1 || j == 0 || j == n - 1)){
                points.add(new Point(i, j));
            }
            }
        }
        return points;
    }

    /**
     * Method to add Animal and it's taken point to set of taken Points
     * @param p point where animal is
     * @param a animal that occupies point
     */
    public void add_taken(Point p, Animal a){
        taken.put(p, a);
    }

    /**
     * Method to check whether given point is taken by an animal
     * @param p Point to check
     * @return true if point is taken false otherwise
     */
    public boolean is_taken(Point p){
        return taken.containsKey(p);
    }

    /**
     * Method to get Animal that occupies chosen point
     * @param target Point we want to retrieve animal from
     * @return  Animal that occupies given point
     */
    public Animal get_taken(Point target){
        return taken.get(target);
    }

    /**
     * Method to change information displayed on control panel, it deletes components that were previously displayed and adds up to 2 new ones
     * @param info text information 
     * @param button button for special actions
     */
    public void add_toPanel(JComponent info, JComponent button){
        int lastIndex = control_panel.getComponentCount() - 1;
        if (lastIndex >= 0) {
        Component lastComponent = control_panel.getComponent(lastIndex);
            if (lastComponent instanceof JButton && lastComponent != Prey_button && lastComponent != Predator_button) {
                control_panel.remove(lastComponent);
                lastIndex --;
                if (lastIndex >= 0){
                    lastComponent = control_panel.getComponent(lastIndex);
                }
            }
            if (lastIndex >= 0 && lastComponent instanceof JTextArea){
                control_panel.remove(lastComponent);
            }
        }
        if (info != null){
            control_panel.add(info);
        }
        if (button != null){
            control_panel.add(button);
        }
        control_panel.revalidate();
        control_panel.repaint();
    }

    /**
     * Synchronized method to change point that is taken by animal to a different one
     * @param start point currently occupied by an animal
     * @param end destination point 
     * @param animal animal that moves
     * @return true if animal was moved on board to a different square false if selected square was taken
     */
    public synchronized boolean swap_taken(Point start, Point end, Animal animal){
        if (crossings.contains(end) && taken.containsKey(end)){
            return false;
        }
        taken.remove(start);
        taken.put(end, animal);
        return true;
    }

    /**
     * Synchronized method for prey to enter place
     * @param start starting point of an prey
     * @param target location of place
     * @param prey prey that wants to enter
     * @return true if place was entered false otherwise
     */
    public synchronized boolean enter_place(Point start, Point target, Prey prey){
        if(prey.isdead()){
            return false;
        }
        Place place = places.get(target);
        if (place.is_enterable(prey)){
            removeImageFromLabel(start);
            taken.remove(start);
            prey.setX(place.getX());
            prey.setY(place.getY());
            prey.heal();
            return true;
        }
        return false;
    }

    /**
     * Method for prey to leave place
     * @param start location of place
     * @param target point to which prey wants to leave place
     * @param prey prey that wants to leave
     */
    public synchronized void exit_place(Point start, Point target, Prey prey){
        Place place = places.get(start);
        taken.put(target, prey);
        place.exit_operations(prey);
        prey.setX(target.getX());
        prey.setY(target.getY());
        prey.draw_self();
        }

        /**
         * Method to check whether there is a place on a given square
         * @param p square to check
         * @return true if there is place false otherwise
         */
        public boolean is_place(Point p){
        return places.containsKey(p);
    }

    /**
     * Method to get place from given square
     * @param p square to get place from
     * @return place on a given point
     */
    public Place getPlace(Point p){
        return places.get(p);
    }

    /**
     * Synchronized method to remove dead animal from board
     * @param point point from which to remove animal from
     * @param animal animal to remove
     */
    public synchronized void removeAnimal(Point point, Animal animal){
        taken.remove(point);
        if(places.containsKey(point)){
            places.get(point).exit_operations((Prey) animal);
            return;
        }
        removeImageFromLabel(point);
        taken.remove(point);
    }

    /**
     * Method to give resource to prey
     * @param start point on which resource is located
     * @param prey prey to give resource to
     */
    public void give_resource(Point start, Prey prey){
        Resource resource = (Resource) places.get(start);
        resource.get_resource(prey);
    }

    /**
     * Method to calculate distance between 2 points
     * @param p1 point1
     * @param p2 point2
     * @return manhattan distance between points
     */
    public int dist (Point p1, Point p2){
        return Math.abs(p1.getX()-p2.getX()) + Math.abs(p1.getY()-p2.getY());
    }

    /**
     * Method to find nearest place from starting point
     * @param p starting point
     * @param place name of place to search for
     * @return point on which place is located
     */
    public Point find_nearest_place(Point p, String place){
        Point nearest = p;
        int shortest_route = 4000;
        ArrayList<Point> places_list = new ArrayList<Point>(places.keySet());
        Collections.shuffle(places_list);
            for(Point key : places_list){
                int distance = dist(key, p);
                try {
                    if (Class.forName(place).isInstance(places.get(key)) && distance < shortest_route){
                    nearest = key;
                    shortest_route = distance;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        return nearest;
    }
}