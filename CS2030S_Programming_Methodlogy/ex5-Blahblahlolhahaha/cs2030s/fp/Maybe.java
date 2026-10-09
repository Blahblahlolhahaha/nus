/**
 * This class implements Some container.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */ 

package cs2030s.fp;

public abstract class Maybe<T>  {
  
  private static final None<Object>  empty = new None<>(); 

  public abstract <U>  Maybe<U> map(Transformer<? super T, ? extends U> transformer);
  
  public abstract Maybe<T>  filter(BooleanCondition<T> bool);
  
  public abstract <U>  Maybe<U> flatMap(Transformer<? super T, 
      ? extends Maybe<? extends U>> transformer);
  
  public abstract T orElse(Producer<? extends T>  value);
  
  public abstract void ifPresent(Consumer<? super T>  consumer);

  public static <T> Maybe<T> some(T value)  {
    try {
      return new Some<>(value);
    } catch (NullPointerException e) {
      return Maybe.none();
    }
  }
 
  public static <T> Maybe<T> none() {
    //Since we will never use the object stored in None object, it 
    //is safe to return Maybe.empty for all instances of Maybe<T>
    @SuppressWarnings("unchecked")
    Maybe<T> maybe = (Maybe<T>) Maybe.empty;
    return maybe;
  }

  public static <T> Maybe<T> of(T value) {
    if (value != null)  {
      return Maybe.some(value);
    }
    return Maybe.none();
  }

  private static final class Some<T> extends Maybe<T> {
    private final T something;
    
    private Some(T something)  {
      this.something = something;
    }

    public <U> Maybe<U> map(Transformer<? super T, ? extends U> transformer)  {
      return new Some<>(transformer.transform(this.something));
    }
  
    @Override
    public <U> Maybe<U> flatMap(Transformer<? super T, ? extends Maybe<? extends U>> transformer) {
      if (this.something == null)  {
        return Maybe.none();
      }
      //Here the since the type returned by transformer.transform 
      //is Maybe<? extends U>, the item stored in the Maybe 
      //object will be a type that is a subtype of U. Furthermore,
      //the methods used in Some requires either U or a subtype of U.
      //This makes casting to be safe
      @SuppressWarnings("unchecked")
      Maybe<U> res = (Maybe<U>) transformer.transform(this.something);
      return res;
    }

    @Override
    public T orElse(Producer<? extends T>  value) {
      if (this.something == null)  {
        return value.produce();
      }
      return this.something;
    }
    
    @Override
    public void ifPresent(Consumer<? super T>  consumer) {
      if (this.something != null) {
        consumer.consume(this.something); 
      }
    }

    @Override
    public Maybe<T> filter(BooleanCondition<T> bool) {
      if (this.something == null)  {
        return Maybe.none();
      }
      if (bool.test(this.something)) {
        return this;
      } else  {
        return Maybe.none();
      }
    }

    @Override
    public boolean equals(Object obj)  {
      if  (obj instanceof Some<?>  some)  {
        if  (some.something != null)  {
          return some.something.equals(this.something);
        }  else {
          return some.something == null && this.something == null;
        }
      }

      return false;
    }

    @Override
    public String toString()  {
      return "[" + this.something + "]";
    }
  
  
  }

  private static class None<T>  extends Maybe<T> {
    
    private None() {
    }

    @Override
    public <U>  Maybe<U> map(Transformer<? super T, ? extends U> transformer) {
      return Maybe.<U>none();
    }

    @Override
    public Maybe<T> filter(BooleanCondition<T> bool) {
      return this;
    }

    @Override
    public <U> Maybe<U> flatMap(Transformer<? super T, ? extends Maybe<? extends U>> bool) {
      return Maybe.<U>none();
    }
    
    @Override
    public T orElse(Producer<? extends T>  value) {
      return value.produce();
    }
  
    @Override
    public void ifPresent(Consumer<? super T> consumer) {
      
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof None<?>) {
        return true;
      }
      return false;
    }

    @Override
    public String toString() {
      return "[]";
    }
  }
}
