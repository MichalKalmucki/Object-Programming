package org.example.drawable;

/**
Abstract class that represents a drawable object on a screen.
*/

public abstract class Drawable extends Thread{
private int x_cord;
private int y_cord;

/**
 * Constructor for Drawable class
 * @param _x_cord the x coordinate of the object
 * @param _y_cord the y coordinate of the object
 */
public Drawable(int _x_cord, int _y_cord){
    this.x_cord = _x_cord;
    this.y_cord = _y_cord;
}

public int getX(){
    return x_cord;
}

public void setX(int x){
    x_cord = x;
}

public int getY(){
    return y_cord;
}

public void setY(int y){
    y_cord = y;
}

/**
 * An abstract method to get the information of the Drawable object
 * @return a string containing the information
 */
public abstract String getInfo();

/**
 * An abstract method to draw the Drawable object
 */
public abstract void draw_self();
}