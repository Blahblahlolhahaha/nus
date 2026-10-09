package cs2030s.fp;

import java.util.ArrayList;
import java.util.List;

/**
 * The InfiniteList class that may contain an arbitrary number of
 * elements.  The class is a generic class.
 * 
 * @author XXX (Your Group)
 * @param <T> The type to be stored in the InfiniteList.
 */
public class InfiniteList<T> {
  private final Lazy<Maybe<T>> head;
  private final Lazy<InfiniteList<T>> tail;
  private static final InfiniteList<?> SENTINEL = (InfiniteList<?>) new Sentinel();

  /**
   * Private constructor to set all fields null.
   */
  private InfiniteList() {
    this.head = null;
    this.tail = null;
  }

  /**
   * Private constructor given head and tail.
   * 
   * @param head The Lazy instance containing the head of the InfiniteList.
   * @param tail The Lazy instance to produce the tail of the InfiniteList.
   */
  private InfiniteList(Lazy<Maybe<T>> head, Lazy<InfiniteList<T>> tail) {
    this.head = head;
    this.tail = tail;
  }
  /**
   * Generates an InfiniteList.  Given a producer that produces
   * a value x, generate the list as [x, x, x, ...]
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @param prod The producer to produce the value in the InfiniteList.
   * @return The created InfiniteList.
   */

  public static <T> InfiniteList<T> generate(Producer<T> prod) {
    return new InfiniteList<>(
        Lazy.of(() -> Maybe.some(prod.produce())),
        Lazy.of(() -> InfiniteList.generate(prod))
        );
  }

  /**
   * Generate an InfiniteList.  Given x and a lambda f, 
   * generate the list as [x, f(x), f(f(x)), f(f(f(x))), ...]
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @param init The first element.
   * @param next The transformation function on the element.
   * @return The created InfiniteList.
   */
  public static <T> InfiniteList<T> iterate(T init,
      Transformer<? super T, ? extends T> next) {
    return new InfiniteList<>(
        Lazy.of(Maybe.some(init)),
        Lazy.of(() -> InfiniteList.iterate(next.transform(init), next))
        );
  }

  /**
   * Generate an InfiniteList.  This is an empty InfiniteList.
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @return The created InfiniteList.
   */
  public static <T> InfiniteList<T> sentinel() {
    //Since we will never use any element stored in sentinel, it 
    //is safe to return Sentinel for all instances of InfiniteList<T>
    @SuppressWarnings("unchecked")
    InfiniteList<T> res = (InfiniteList<T>) SENTINEL;
    return res;
  }

  /**
   * Lazily search for the first element of the InfiniteList.
   * Then return the value of the first element of the InfiniteList.
   * 
   * @return the head of the InfiniteList.
   */
  public T head() {
    return this.head.get().orElse(() -> this.tail.get().head());
  }

  /**
   * Lazily search for the first element of the InfiniteList.
   * Then return the tail of the first element of the InfiniteList.
   * 
   * @return the tail of the InfiniteList.)
   */
  public InfiniteList<T> tail() {
    return this.head.get()
      .map(x -> this.tail.get())
      .orElse(() -> this.tail.get().tail());
  }

  /**
   * Transform each element in the InfiniteList using
   * the given Transformer and return the resulting InfiniteList.
   * 
   * @param <U> The type of the resulting InfiniteList.
   * @param fn  The Transformer to transform 
   *            the element of the InfiniteList.
   * @return    A lazily evaluated InfiniteList with each
   *            element transformed using fn.
   */
  public <U> InfiniteList<U> map(Transformer<? super T, ? extends U> fn) {
    return new InfiniteList<>(
        this.head.map(mHead -> mHead.map(fn)),
        this.tail.map(mTail -> mTail.map(fn))
        );
  }

  /**
   * Check each element of the InfiniteList and filter out
   * elements that evaluate to `false` using the given
   * BooleanCondition.
   * 
   * @param pred The predicate to check element.
   * @return     A lazily evaluated InfiniteList with element
   *             failing the check removed.
   */
  public InfiniteList<T> filter(BooleanCondition<? super T> pred) {
    return new InfiniteList<>(
        this.head.map(mHead -> mHead.filter(pred)),
        this.tail.map(mTail -> mTail.filter(pred))
        );
  }

  /**
   * Truncates the list by a given length and returns an InfiniteList
   * ending with a Sentinel
   * @param n     Desired length of the InfiniteList 
   * @return      A truncated InfiniteList with given 
   *              length ending with a sentinel
   */
  public InfiniteList<T> limit(long n) {
    if (n < 1) {
      return InfiniteList.<T>sentinel();
    } else if (this.head.toString() == "?") {
      return new InfiniteList<T>(
          Lazy.<Maybe<T>>of(() -> Maybe.<T>some(this.head())),
          this.tail.map(x -> this.tail().limit(n - 1))
          );
    } else {
      return new InfiniteList<T>(
          Lazy.<Maybe<T>>of(Maybe.<T>some(this.head())),
          this.tail.map(x -> this.tail().limit(n - 1))
          );
    }
  }

  /**
   * Converts the InfiniteList to a List. However, if List
   * does not end with a Sentinel using InfiniteList.limit 
   * or InfiniteList.takeWhile, will cause the code to run forever
   * @return      A list containing all the elements in the InfiniteList 
   */
  public List<T> toList() { 
    ArrayList<T> list = new ArrayList<T>();
    this.head.get().ifPresent(x -> list.add(x));
    list.addAll(this.tail.get().toList());
    return list;
  }

  /**
   * Takes in a predicate which tests the elements in the InfiniteList.
   * Truncates the list immediately after an element fails the predicate
   * @param pred    A BooleanCondition to test the element
   * @return        An truncated InfiniteList containing all the elements 
   *                that passes the predicate until the first element that 
   *                failed
   */
  public InfiniteList<T> takeWhile(BooleanCondition<? super T> pred) {
    if (this.head.get() != Maybe.<T>none()) {
      Lazy<Boolean> test = this.head.filter(x -> pred.test(x.orElse(() -> null)));
      return new InfiniteList<>(
          Lazy.<Maybe<T>>of(() -> test.get() ? 
            Maybe.<T>some(this.head()) : Maybe.<T>none()), 
          Lazy.<InfiniteList<T>>of(() -> test.get() ? 
            this.tail.get().takeWhile(pred) : InfiniteList.<T>sentinel())
      );
    } else {
      return this.tail.get().takeWhile(pred);
    }
  }

  /**
   *  Takes in an initial value and accumulator which accumulates all elements
   *  in the List from the last element to the first and returns the accumulated value
   *  Note that the method will run forever if the InfiniteList does not end with a
   *  InfiniteList.SENTINEL.
   *  @param <U>    Type of accumulated value
   *  @param id     Initial value to be used in the operation
   *  @param acc    Accumulator to process the elements  
   *  @return       Accumulated value after running the accumulator on 
   *                all elements of the list
   */
  public <U> U foldRight(U id, Combiner<? super T, U, U> acc) {
    return acc.combine(this.head(), this.tail().foldRight(id, acc)); // TODO: Implement
  }

  @Override
  public String toString() {
    return "[" + this.head + " " + this.tail + "]";
  }

  // TODO: Write JavaDoc
  public boolean isSentinel() {
    return false;
  }

  /**
   * A nested static class that represents the end of the list.
   * The class contains nothing and performs no operation.
   */
  private static class Sentinel extends InfiniteList<Object> {
    @Override
    public Object head() {
      throw new java.util.NoSuchElementException();
    }

    @Override
    public InfiniteList<Object> tail() {
      throw new java.util.NoSuchElementException();
    }

    @Override
    public <R> InfiniteList<R> map(Transformer<Object, ? extends R> mapper) {
      return InfiniteList.<R>sentinel();
    }

    @Override
    public InfiniteList<Object> filter(BooleanCondition<Object> predicate) {
      return InfiniteList.<Object>sentinel();
    }

    @Override
    public InfiniteList<Object> limit(long n) {
      return this; // TODO: Implement
    }

    @Override
    public List<Object> toList() {
      return new ArrayList<>(); // TODO: Implement
    }

    @Override
    public InfiniteList<Object> takeWhile(BooleanCondition<Object> pred) {
      return this; // TODO: Implement
    }

    @Override
    public <U> U foldRight(U id, Combiner<Object, U, U> acc) {
      return id; // TODO: Implement
    }

    @Override
    public String toString() {
      return "~";
    }

    @Override
    public boolean isSentinel() {
      return true;
    }
  }
}
