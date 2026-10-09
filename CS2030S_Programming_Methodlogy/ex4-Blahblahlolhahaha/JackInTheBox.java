/**
 * This class implements a Jack in the Box Transformer.
 * Takes in an item and returns an item in Some container.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */
public class JackInTheBox<T> implements Transformer<T, Some<T>> {
  
  @Override
  public Some<T> transform(T arg) {
    return Some.some(arg);
  }
}

