package org.example.place.resource;

import org.example.animal.Prey;
import org.example.console.Board;
import org.example.place.Place;

public abstract class Resource extends Place {
    private int replenish_rate;
    private int type;
    protected Board board;


    public Resource(int _max_animals, int _x_cord, int _y_cord, int _replenish_rate, Board board) {
        super(_max_animals, _x_cord, _y_cord, board);
        this.replenish_rate = _replenish_rate;
        this.board = board;
    }

    public abstract void get_resource(Prey prey);

    public int get_type() {
        return type;
    }

    @Override
    public synchronized void exit_operations(Prey prey) {
        super.exit_operations(prey);
        prey.setIs_refilling(false);
    }

    public int getReplenish_rate() {
        return replenish_rate;
    }
}
