/**
 * The Seq<T> for CS2030S 
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */
public class Seq<T extends Comparable<T>> { // TODO: Change to bounded type parameter
  private T[] array;

  public Seq(int size) {
    //The only way we can put an object into array is through
    // the method set() and we only put object of type T inside.
    // So it is safe to cast `Comparable[]` to `T[]`.
    @SuppressWarnings({"unchecked", "rawtypes"})
    T[] a = (T[]) new Comparable[size];
    this.array = a;
  }

  public void set(int index, T item) {
    // TODO: add implementation
    this.array[index] = item;
  }

  public T get(int index) {
    return this.array[index];
  }

  public T min() {
    T smallest = this.array[0];
    for (int i = 1; i < this.array.length; i++) {
      if (this.array[i].compareTo(smallest) < 0) {
        smallest = this.array[i];
      }
    }
    return smallest;
  }

  @Override
  public String toString() {
    String out = "[ ";
    for (int i = 0; i < array.length; i++) {
      out = out + i + ":" + array[i];
      if (i != array.length - 1) {
        out = out + ", ";
      }
    }
    return out + " ]";
  }
}
