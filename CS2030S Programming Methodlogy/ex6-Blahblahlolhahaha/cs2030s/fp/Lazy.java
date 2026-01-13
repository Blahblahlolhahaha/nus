package cs2030s.fp;

/**
 * This class implements a lazily evaluated value.
 * The value is computed only when needed and the
 * value is not recomputed.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */

public class Lazy<T> {
  private Producer<? extends T> producer;
  private Maybe<T> value;
  
  private Lazy(T value) {
    this.value = Maybe.some(value);
    this.producer = () -> null;
  }
  
  private Lazy(Producer<? extends T> producer) {
    this.producer = producer;
    this.value = Maybe.none();
  }
  
  public static <T> Lazy<T> of(T value) {
    return new Lazy<>(value);
  }

  public static <T> Lazy<T> of(Producer<? extends T> producer) {
    return new Lazy<>(producer);
  }

  public T get() {
    return value.orElse(() -> (value = Maybe.of(producer.produce())).orElse(producer));
  }

  public <U> Lazy<U> map(Transformer<? super T, ? extends U> trans) {
    return Lazy.<U>of(() -> trans.transform(this.get()));
  }

  public <U> Lazy<U> flatMap(Transformer<? super T, ? extends Lazy<? extends U>> trans) {
    return Lazy.<U>of(() -> trans.transform(this.get()).get());
  }
  
  public Lazy<Boolean> filter(BooleanCondition<? super T> bool) {
    return Lazy.<Boolean>of(() -> bool.test(this.get()));
  }

  public <S, R> Lazy<R> combine(Lazy<S> lazy, 
      Combiner<? super T, ? super S, ? extends R> combiner) {
    return Lazy.<R>of(() -> combiner.combine(this.get(), lazy.get()));
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Lazy<?> lazy) {
      return this.get().equals(lazy.get());
    }
    return false;
  }

  @Override
  public String toString() {
    return value.map(x -> String.valueOf(x)).orElse(() -> "?");
  }



}
