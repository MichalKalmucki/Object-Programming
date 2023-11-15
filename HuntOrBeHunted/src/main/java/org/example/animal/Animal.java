package org.example.animal;

import org.example.console.Board;
import org.example.console.Point;
import org.example.drawable.Drawable;

/**
 * Class that represents each animal
 */

public abstract class Animal extends Drawable{
    private String name;
    private int speed;
    private int strength;
    private String species_name;
    protected Board board;
    private boolean dead = false;
    
    /**
     * Constructor of Animal class
     * @param _name name of an animal
     * @param _speed speed of an animal
     * @param _strength animal's strength
     * @param _species_name animal's species name
     * @param _x_cord x cordinate
     * @param _y_cord y cordinate
     * @param _board board on which animal is supposed to move
     */
    public Animal(String _name, int _speed, int _strength, String _species_name, int _x_cord, int _y_cord, Board _board){
        super(_x_cord, _y_cord);
        this.name = _name;
        this.speed = _speed;
        this.strength = _strength;
        this.species_name = _species_name;
        this.board = _board;
    }

    
    /** 
     * Method to move animal from one square to another
     * @param destination target square where animal wants to get
     */
    public void move(Point destination){
        board.removeImageFromLabel(new Point(getX(), getY()));
        setX(destination.getX());
        setY(destination.getY());
        draw_self();
    }
      
    public int getStrength(){
        return strength;
    }

    public int getSpeed(){
        return speed;
    }

    /**
     * Method to get animal's name
     * @return animal's name
     */
    public String getname() {
        return name;
    }

    /**
     * Method to check if animal is dead
     * @return true if animal is dead false if animal is alive
     */
    public boolean isdead(){
        return dead;
    }

    /**
     * Method to set animal dead
     */
    public void setDead(){
        dead = true;
    }

    /**
     * Method to remove animal from board
     */
    public void dying_operations(){
        Point p = new Point(getX(), getY());
        board.removeAnimal(p, this);
    }

    public String getSpecies_name() {
        return species_name;
    }

    
    /** 
     * Method to get information about selected animal
     * @return certain values of object's fields put into String
     */
    @Override
    public String getInfo(){
        return String.format("""
                Animal
                ------------
                name: %s
                ------------
                Species name: %s
                ------------
                speed: %d
                ------------
                strength: %d
                ------------
                """, name, species_name, speed, strength);
    }
}
