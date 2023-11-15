package org.example.place;

import java.util.HashSet;

import org.example.animal.Prey;
import org.example.console.Board;
import org.example.drawable.Drawable;

/**
 * Class representing any place
 */
public abstract class Place extends Drawable {
    private int max_animals;
    private int current = 0;

    protected Board board;

    HashSet<Prey> animals = new HashSet<Prey>();

    public Place(int _max_animals, int _x_cord, int _y_cord, Board board) {
        super(_x_cord, _y_cord);
        this.max_animals = _max_animals;
        this.board = board;
    }

    /**
     * method to get information about object
     *
     * @return string containing information
     */
    @Override
    public String getInfo() {
        String result = String.format("""
                ------------
                maximum number of vessels: %d
                ------------
                current number of vessels: %d
                ------------
                List of vessels:
                """, max_animals, current);
        int i = 1;
        for (Prey animal : animals) {
            result += String.format("%d: %s\n", i++, animal.getname());
        }
        result += "------------";
        return result;
    }

    /**
     * Method to check whether place is enterable or not, if it is adds prey to list of contained preys
     *
     * @param prey prey that checks to enter
     * @return true if place is enterable, false otherwise
     */
    public synchronized boolean is_enterable(Prey prey) {
        if (current < max_animals) {
            current += 1;
            animals.add(prey);
            return true;
        }
        return false;
    }

    /**
     * Method to remove animal from place when it is leaving
     *
     * @param prey prey that is leaving place
     */
    public synchronized void exit_operations(Prey prey) {
        current -= 1;
        animals.remove(prey);
    }

    /**
     * method to change current number of animals
     *
     * @param i ammount to add
     */
    public synchronized void change_current(int i) {
        current += i;
    }

    public synchronized int getCurrent() {
        return current;
    }

    public HashSet<Prey> getset() {
        return animals;
    }

    public int getMax_animals() {
        return max_animals;
    }

    /**
     * Method to add prey to list of contained preys
     *
     * @param prey prey to add
     */
    public void add_animal(Prey prey) {
        animals.add(prey);
    }
}