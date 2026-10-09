/**
 * The Combiner interface that can combine
 * two types S and T to type R.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */

package cs2030s.fp;

@FunctionalInterface
public interface Combiner<S, T, R> {
  R combine(S s, T t);
}
