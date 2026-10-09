import java.util.Scanner;

/**
 * CS2030S Ex 0: Estimating Pi with Monte Carlo
 * Semester 1, 2024/25
 *
 * <p>This program takes in two command line arguments: the 
 * number of points and a random seed.  It runs the
 * Monte Carlo simulation with the given argument and print
 * out the estimated pi value.
 *
 * @author XXX 
 */

class Ex0 {

  // TODO estimatePi(int numOfPoints, int seed) {
  // }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int numOfPoints = sc.nextInt();
    int seed = sc.nextInt();

    double pi = estimatePi(numOfPoints, seed);

    System.out.println(pi);
    sc.close();
  }

  public static double estimatePi(int numOfPoints, int seed){
    RandomPoint.setSeed(seed);
    double insideCircle = 0;
    Circle circle = new Circle(new Point(0.5,0.5),0.5);
    for(int i = 0; i<numOfPoints;i++){
      if(circle.contains(new RandomPoint(0,1,0,1))){
        insideCircle++;
      }
    }
    return (4*insideCircle)/(double)numOfPoints;
  }


}
