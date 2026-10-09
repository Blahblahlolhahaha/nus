/**
 * CS2030S Ex 0: Point.java
 * Semester 1, 2024/25
 *
 * <p>The Point class encapsulates a point on a 2D plane.
 *
 * @author XXX
 */
import java.lang.Math;

class Point {
  private double x;
  private double y;

  public Point(double x, double y){
    this.x = x;
    this.y = y;
  }
  
  public double getDistSq(Point a){
    return Math.pow(this.x - a.x,2) + Math.pow(this.y-a.y,2);
  }
  
  public void relocate(double newX,double newY){
    this.x = newX;
    this.y = newY;
  }

  @Override
  public String toString(){
    return "(" + String.valueOf(this.x) + ", " + String.valueOf(this.y) + ")";
  }
}
