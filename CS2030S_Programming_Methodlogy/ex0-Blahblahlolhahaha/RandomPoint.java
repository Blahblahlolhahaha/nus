import java.util.Random;

public class RandomPoint extends Point{
  private static Random rng = new Random(1);

  public RandomPoint(double minX,double maxX, double minY, double maxY){
    super(0,0);

    double x = rng.nextDouble()*(maxX-minX) + minX;
    double y = rng.nextDouble()*(maxY-minY) + minY;
    
    this.relocate(x,y);
  }

  public static void setSeed(int seed){
    rng = new Random(seed);
  }
}
