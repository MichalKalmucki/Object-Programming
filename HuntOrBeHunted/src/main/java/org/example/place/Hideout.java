package org.example.place;

import java.util.Random;
import org.example.console.Board;
import org.example.animal.Prey;

/**
 * Class representing hideouts for preys
 */
public class Hideout extends Place {
    private static int idx = 0;
    Random random = new Random();

    public Hideout(int _max_animals, int _x_cord, int _y_cord, Board board){
        super(_max_animals, _x_cord, _y_cord, board);
    }

    /**
     * Method to breed more animals when they enter hideout
     */
    private void breed(){
        Prey prey = new Prey("Hideout breeding" + idx++, random.nextInt(400, 601), random.nextInt(10, 26) , "Vessel", getX(), getY(), random.nextInt(75, 156), random.nextInt(100, 201), random.nextInt(100, 201), board, true);
        add_animal(prey);
        prey.start();
        change_current(1);
    }

    /**
     * Overriden method to check if place is enterable
     * <p>
     * also if there are at least 2 preys and there is space for it preys will breed
     * </p> 
     */
    @Override
    public synchronized boolean is_enterable(Prey prey) {
        if (getCurrent() == 0){
            board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/knightonbench.png");
        }
        else if (getCurrent() >= 1 && getCurrent() < getMax_animals() - 1){
            breed();
        }
        return super.is_enterable(prey);
    }

    @Override
    public void exit_operations(Prey prey){
        super.exit_operations(prey);
        if (getCurrent() == 0){
            draw_self();
        }
        prey.setIs_hidden(false);
    }

    @Override
    public void draw_self() {
        board.addImageToLabel(getX(), getY(), "src/main/java/org/example/assets/uafka.png");
    }

    /**
     * method to get information about object
     * @return string containing information
     */
    @Override
    public String getInfo() {
        return "Bench\n" + super.getInfo();
    }
}