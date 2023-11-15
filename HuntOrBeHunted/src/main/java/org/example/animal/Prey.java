package org.example.animal;

import java.util.HashSet;
import java.util.Random;

import org.example.console.Board;
import org.example.console.Point;
import org.example.place.Hideout;

/**
 * Class that represents preys
 * <p>
 * This class is responsible for calculating prey's movement and searching for places to go
 * </p>
 */
public class Prey extends Animal{
    private int health;
    private int water_level;
    private int fullness;
    private HashSet<Point> points;
    private boolean is_hidden;
    private boolean is_refilling = false;
    private int crititcal_level = 50;
    private int max_food;
    private int max_water;
    private Random random = new Random();
    private int max_hp;

    /**
     * Constructor of Prey class
     * @param _name name of an animal
     * @param _speed speed of an animal
     * @param _strength animal's strength
     * @param _species_name animal's species name
     * @param _x_cord x cordinate
     * @param _y_cord y cordinate
     * @param _health starting (max) health of an animal
     * @param _water_level starting (max) water level of an animal
     * @param _fullness starting (max) food level of an animal
     * @param _board board on which animal is supposed to move
     * @param is_hidden true if animal is created in hideout, false otherwise
     */
    public Prey(String _name, int _speed, int _strength, String _species_name, int _x_cord, int _y_cord, int _health, int _water_level, int _fullness, Board _board, boolean is_hidden){
        super(_name, _speed, _strength, _species_name, _x_cord, _y_cord, _board);
        this.health = _health;
        this.water_level = _water_level;
        this.fullness = _fullness;
        this.points = _board.get_paths();
        this.max_food = _fullness;
        this.max_water = _water_level;
        this.max_hp = _health;
        this.is_hidden = is_hidden;
        if (!is_hidden){
            _board.add_taken(new Point(_x_cord, _y_cord), this);
        }
    }

    /** 
     * Method to get information about selected animal
     * @return certain values of object's fields put into String
     */
    @Override
    public String getInfo() {
        return super.getInfo() + String.format("""
                health/max health: %d/%d
                ------------
                water level/max level: %d/%d
                ------------
                food level/max level: %d/%d
                ------------
                """, health, max_hp, water_level, max_water, fullness, max_food);
    }

    /**
     * Method to increase preys water level
     * @param ammount ammount to increase it by
     */
    public synchronized void drink(int ammount){
        water_level = Math.min(water_level + ammount, max_water);
    }

    /**
     * Method to fully restore preys health to starting value
     */
    public void heal(){
        health = max_hp;
    }

    /**
     * Method to increase preys food level
     * @param ammount ammount to increase it by
     */
    public void eat(int ammount){
        fullness = Math.min(fullness + ammount, max_food);
    }

    public void setIs_refilling(boolean is){
        is_refilling = is;
    }

    public void setIs_hidden(boolean is){
        is_hidden = is;
    }

    /**
     * Method called by predators to atack prey
     * @param str predator's strength
     * @return true if prey was killed false otherwise
     */
    public synchronized boolean lose_health(int str){
        int hp = health;
        if (hp > 0){
            int atack_val = str - getStrength();
            if (atack_val > 0){
                hp -= atack_val;
                this.health = hp;
                if (hp <= 0){
                    setDead();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to remove animal from board
     * <p> 
     * for preys it also draws very shortly dead prey on screen
     * </p>
     */
    @Override
    public void dying_operations(){
        Point p = new Point(getX(), getY()); 
        if (!board.is_place(p)){
            draw_self();
        } 
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.dying_operations();
    }

    /**
     * Method responsible for drawing predator at square it currently is on
     */
    @Override
    public void draw_self() {
        if (!isdead()){
            board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/knighthead.png");
        }
        else{
            board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/shade.png");
        }
        
    }

    /**
     * Run method overriden from Thread, while alive prey will:
     * -die if water or food level below zero
     * -go to hideout/stay in one if not hungry and not thirsty
     * -go to resource/stay in one if hungry or thirsty 
     */
    @Override
    public void run(){
        while(!isdead()){
            if(water_level < 0 || fullness < 0){
                setDead();
                break;
            }
            if (is_hidden && water_level > crititcal_level && fullness > crititcal_level){
                stay_in();
            }

            else if(!is_hidden && !is_refilling){
                find_place();
            }

            else if(is_refilling && (fullness == max_food || water_level == max_water)){
                exit_place();
            }

            else if(is_hidden && (water_level <= crititcal_level || fullness <= crititcal_level)){
                exit_place();
            }

            else if(is_refilling && (fullness < max_food || water_level < max_water)){
                refill();
            }
        }
        dying_operations();
    }

    /**
     * Method to stay in hideout
     */
    private void stay_in(){
        fullness -= 3;
        water_level -= 3;
        try {
            sleep(getSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to refill in resource source
     */
    private void refill(){
        board.give_resource(new Point(getX(), getY()), this);
        try {
            sleep(getSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to find and enter Hideout/Plant/Water source
     * Hideout if water and food level above critical value
     * Plant/Water source if respective level below critical value 
     */
    private void find_place(){
        draw_self();
        Point start = new Point(getX(), getY());
        Point nearest;
        if(fullness > crititcal_level && water_level > crititcal_level){
            nearest = board.find_nearest_place(start, "org.example.place.Hideout");
        }else{
            Point nearestplant = board.find_nearest_place(start, "org.example.place.resource.Plant");
            int skip = random.nextInt(2);
            Point nearestwater = board.find_nearest_place(start, "org.example.place.resource.Water");
            nearest = nearestplant;
            int dist1 = board.dist(start, nearestplant);
            int dist2 = board.dist(start, nearestwater); 
            if(dist2 + water_level/10 < dist1 + fullness/10 || (dist1 + water_level/10 == dist1 + fullness/10 && skip == 0)){
                nearest = nearestwater;
            }
        }
        
        Point end = new Point(getX() + Integer.signum(nearest.getX() - getX()), getY());
        Point end2 = new Point(getX(), getY() + Integer.signum(nearest.getY() - getY()));
        if (Math.abs(nearest.getX() - start.getX()) >= Math.abs(nearest.getY() - start.getY()) || !points.contains(end2)){
            if (points.contains(end)){

                if (board.is_place(end)){
                    while (!board.enter_place(start, end, this)){
                        fullness -= 1;
                        water_level -= 1;
                        if(isdead() || water_level < 0 || fullness < 0){
                            return;
                        }
                        try {
                            sleep(getSpeed());
                            draw_self();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (board.getPlace(end) instanceof Hideout){
                        is_hidden = true;
                    }
                    else{
                        is_refilling = true;
                    }
                    return;
                }
                if (nearest.getX() != getX()){
                    while (!board.swap_taken(start, end, this)){
                        fullness -= 1;
                        water_level -= 1;
                        if(isdead() || water_level < 0 || fullness < 0){
                            return;
                        }
                        try {
                            sleep(getSpeed());
                            draw_self();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    move(end);
                    try {
                        fullness -= 1;
                        water_level -= 1;
                        sleep(getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
         
        end = end2;
        if (points.contains(end)){
            
            if (nearest.getY() != getY()){

                if (board.is_place(end)){
                    while (!board.enter_place(start, end, this)){
                        fullness -= 1;
                        water_level -= 1;
                        if(isdead() || water_level < 0 || fullness < 0){
                            return;
                        }
                        try {
                            sleep(getSpeed());
                            draw_self();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (board.getPlace(end) instanceof Hideout){
                        is_hidden = true;
                    }
                    else{
                        is_refilling = true;
                    }
                    return;
                }
                
                while (!board.swap_taken(start, end, this)){
                    if(isdead() || water_level < 0 || fullness < 0){
                        return;
                    }
                    try {
                        fullness -= 1;
                        water_level -= 1;
                        sleep(getSpeed());
                        draw_self();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                move(end);
                try {
                    fullness -= 1;
                    water_level -= 1;
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        fullness -= 1;
        water_level -=1;
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to exit place in which prey is currently in
     */
    private void exit_place(){
        Point start = new Point(getX(), getY());
        
        Point nearest = board.find_nearest_place(start, "org.example.place.Hideout");
        if (fullness < crititcal_level + 3){
            nearest = board.find_nearest_place(start, "org.example.place.resource.Plant");
        }
        if (water_level < crititcal_level + 3){
            nearest = board.find_nearest_place(start, "org.example.place.resource.Water");
        }
        
        int skip = random.nextInt(2);
        if (skip == 0 && Math.abs(nearest.getX() - start.getX()) >= Math.abs(nearest.getY() - start.getY())){
            Point end = new Point(getX() + Integer.signum(nearest.getX() - getX()), getY());
            if (nearest.getX() != getX() && points.contains(end)){
                board.exit_place(start, end, this);
                try {
                    sleep(getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        Point end = new Point(getX(), getY() + Integer.signum(nearest.getY() - getY()));
        if(nearest.getY() != getY() && points.contains(end)){
            board.exit_place(start, end, this);
            try {
                sleep(getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}