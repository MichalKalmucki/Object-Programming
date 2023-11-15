package org.example.console;


/**
 * Class representing point on board
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Point)) return false;
      Point p = (Point) o;
      return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
    int result = 0;
    result += x;
    result += 100*y;
    return result;
  }
}
