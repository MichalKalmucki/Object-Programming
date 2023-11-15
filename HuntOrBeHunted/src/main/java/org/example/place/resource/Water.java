package org.example.place.resource;

import org.example.animal.Prey;
import org.example.console.Board;

public class Water extends Resource{

    public Water (int _max_animals, int _x_cord, int _y_cord, int _replenish_rate, Board board){
        super(_max_animals, _x_cord, _y_cord, _replenish_rate, board);
    }

    @Override
    public void get_resource(Prey prey){
        prey.drink(getReplenish_rate());
    }

    @Override
    public void draw_self() {
        board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/HotSpring.png");
    }

    @Override
    public String getInfo() {
        return "Hot Spring\n" + super.getInfo();
    }
}