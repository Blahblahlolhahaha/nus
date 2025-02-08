import java.util.Random;
import java.util.stream.Stream;
public class Main{

    public static int[] generateArray(int size){
        Random random = new Random();
        return random.ints().limit(size).toArray();
    }

    public static int[] generateSorted(int size){
        return Stream.iterate(0, x -> x < size, x -> x + 1)
            .mapToInt(Integer::valueOf).toArray();
    }

    public static int[] generateReverse(int size){
        return Stream.iterate(size, x -> x > 0, x -> x - 1)
            .mapToInt(Integer::valueOf).toArray();

    }

    public static void time(SortAlgo algo){
        final long startTime = System.nanoTime();
        algo.sort();
        final long duration = (System.nanoTime() - startTime) / 1000000;
        System.out.println(algo);
        System.out.println("Time Taken: " + duration + "ms\n");
    }

    public static void main(String[] args){
        BubbleSort sort = new BubbleSort("Bubble", generateArray(300000));
        SelectionSort sorty = new SelectionSort("Selection", generateArray(300000));
        InsertionSort sortyy = new InsertionSort("Insertion", generateArray(300000));
        MergeSort sortyyy = new MergeSort("Merge", generateArray(300000));
        SortAlgo[] algos = new SortAlgo[]{sorty,sortyy,sortyyy};
        for(SortAlgo algo: algos){
            time(algo);
            algo.setData(generateSorted(300000));
            time(algo);
            algo.setData(generateReverse(300000));
            time(algo);
        }
    }
}
