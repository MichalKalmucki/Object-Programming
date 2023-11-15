package org.example.animal;
import java.util.Random;

import org.example.console.Board;
import org.example.console.Point;

/**
 * Class that represents predators
 * <p>
 * This class is responsible for calculating predators movement and searching for preys
 * </p>
 */
public class Predator extends Animal{
    private boolean hunt_mode = true;
    Random random = new Random();

    /**
     * Constructor of Predator class
     * @param _name name of an animal
     * @param _speed speed of an animal
     * @param _strength animal's strength
     * @param _species_name animal's species name
     * @param _x_cord x cordinate
     * @param _y_cord y cordinate
     * @param _board board on which animal is supposed to move
     */
    public Predator(String _name, int _speed, int _strength, String _species_name, int _x_cord, int _y_cord, Board _board){
        super(_name, _speed, _strength, _species_name, _x_cord, _y_cord, _board);
        _board.add_taken(new Point(_x_cord, _y_cord), this);
        draw_self();
    }

    /** 
     * Method to get information about selected animal
     * @return certain values of object's fields put into String
     */
    @Override
    public String getInfo() {
        return super.getInfo() + String.format("""
                hunt mode: %b
                """, hunt_mode);
    }

    /**
     * Method responsible for decreasing health of nearby prey
     * <p>
     * Also changes Predator's image on board for duration of atack
     * </p>
     * @param target Point where Prey should be
     */
    public void atack(Point target){
        Prey prey = (Prey) board.get_taken(target);
        if(prey == null){
            return;
        }
        board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/lifeender.png");
        
        if (prey.lose_health(getStrength())){
            //take a break after killing prey
            hunt_mode = false;
            for (int i = 0; i < 20; i ++){
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                draw_self();
            } 
            hunt_mode = true;
        }
        try {
            sleep(getSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        draw_self();
    }
    
    /**
     * Method responsible to finding nearest prey
     * <p>
     * If found prey is next to Predator it is atacked
     * </p>
     * @return point where predator should move in order to reach prey, or current point of predator if Prey was not found or prey was atacked
     */
    public Point hunt_prey(){
        int range = 1;
        int x = getX();
        int y = getY();
        Point point = new Point(x, y);
        for(int helper = 0; helper < board.n; helper++){
            for(int i = - range; i <= range; i++){
                for(int j = - range; j <= range; j++){
                    if (i == -range || i == range || j == -range || j == range){
                        point = new Point(x + i, y + j);
                        if (board.get_taken(point) != null && board.get_taken(point).getSpecies_name().equals("Vessel") && !board.is_place(point)){
                            if (range == 1){
                                atack(point);
                                return new Point(x, y);
                            }
                            return point;
                        }
                    }
                }
            }
            range += 1;
        }
        return new Point(x, y);
    }

    /**
     * Method responsible for drawing predator at square it currently is on
     */
    @Override
    public void draw_self() {
        board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/zotehead.png");
    }

    /**
     * Run method overriden from Thread, when started predator will call hunting() method while it is alive 
     */
    @Override
    public void run() {
        while(!isdead()){
            hunting();
        }
        dying_operations();
    }

    /**
     * Method responsible for predators actions
     * <p>
     * It chooses closest prey tries to get closer to it or atack if next to it
     * Predators are in each step randomly moving moving closer to correct row or column of hunted prey,
     * Also in specific case where predator is on correct row/column but it cannot move closer to prey because of some object on the way it moves out of the way
     * <p>
     */
    private void hunting(){
        Point start = new Point(getX(), getY());
        Point target = hunt_prey();
        while (target.equals(start)){
            if(isdead()){
                return;
            }
            target = hunt_prey();
            try {
                sleep(getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int skip = random.nextInt(2);
        Point end = new Point(getX() + Integer.signum(target.getX() - getX()), getY());
        if(board.is_place(end)){
            skip = 1;
        }
        if (skip == 0 && target.getX() != getX()){   
            if(!board.is_taken(end)){
                while (!board.swap_taken(start, end, this)){
                    try {
                        sleep(getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                move(end);
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        Point end2 = new Point(getX(), getY() + Integer.signum(target.getY() - getY()));
        if (!board.is_place(end2) && target.getY() != getY()){
            
            if(!board.is_taken(end2)){
                while (!board.swap_taken(start, end2, this)){
                    try {
                        sleep(getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                move(end2);
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        if (target.getY() == getY() && (board.is_place(end) || board.is_taken(end))){
            end = new Point(getX(), getY() + 1);
            if (board.is_place(end) || board.is_taken(end)){
                end = new Point(getX(), getY() - 1);
                if (board.is_taken(end) || board.is_place(end)){
                    try {
                        sleep(getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            while (!board.swap_taken(start, end, this)){
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            move(end);
            try {
                sleep(getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
    
        if (target.getX() == getX() && (board.is_place(end2) || board.is_taken(end2))){
            end2 = new Point(getX() + 1, getY());
            if (board.is_taken(end2) || board.is_place(end2) || board.is_taken(end2)){
                end2 = new Point(getX() - 1, getY());
                if (board.is_place(end2) || board.is_taken(end2)){
                    try {
                        sleep(getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            while (!board.swap_taken(start, end2, this)){
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            move(end2);
            try {
                sleep(getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}