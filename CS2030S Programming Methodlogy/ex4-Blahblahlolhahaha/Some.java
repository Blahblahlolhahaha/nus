/**
 * This class implements Some container.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */ 
public class Some<T> {
  private final T something;
  
  private Some(T something) {
    this.something = something;
  }

  public static <T> Some<T> some(T something) {
    return new Some<T>(something);
  }

  public <U> Some<U> map(Transformer<? super T, ? extends U> transformer) {
    return new Some<>(transformer.transform(this.something));
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Some<?> some) {
      if (some.something != null) {
        return some.something.equals(this.something);
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "[" + this.something + "]";
  }
  
  
}
