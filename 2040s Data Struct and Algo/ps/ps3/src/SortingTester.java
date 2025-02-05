import java.util.Random;

public class SortingTester {

    public static KeyValuePair[] generateArray(int size){
        KeyValuePair[] test = new KeyValuePair[size];
        Random random = new Random();
        for(int i = 0; i < size;i++){
            test[i] = new KeyValuePair(random.nextInt(),i);
        }
        return test;
    }
    
    public static KeyValuePair[] generateStoopidArray(int size){
        KeyValuePair[] test = new KeyValuePair[size];
        Random random = new Random();
        for(int i = 0; i < size;i++){
            test[i] = new KeyValuePair(random.nextInt() % 3,i);
        }
        return test;
    }

    public static KeyValuePair[] generateWorstCase(int size){
        //Reverse order
        KeyValuePair[] test = new KeyValuePair[size];
        Random random = new Random();
        for(int i = 0; i < size;i++){
            test[i] = new KeyValuePair(size - i,i);
        }
        return test;
    }
    
    public static KeyValuePair[] generateBestCase(int size){
        //sortedArray
        KeyValuePair[] test = new KeyValuePair[size];
        Random random = new Random();
        for(int i = 0; i < size;i++){
            test[i] = new KeyValuePair(i,i);
        }
        return test;
    }


    public static boolean checkSort(ISort sorter, int size) {
        // TODO: implement this
        KeyValuePair[] test = generateArray(size);
        sorter.sort(test);
        for(int i = 0;i < test.length - 1;i++){
            if(test[i].getKey() > test[i+1].getKey()){
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        // TODO: implement this
        KeyValuePair[] test = generateStoopidArray(size);
        sorter.sort(test);
        for(int i = 0;i < test.length - 1;i++){
            if(test[i].getKey() == test[i+1].getKey() &&  test[i].getValue() > test[i+1].getValue()){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // TODO: implement this
        ISort[] sorters = {new SorterA(), new SorterB(), new SorterD(), new SorterE(), new SorterF()};
        String[] names = {"SorterA", "SorterB", "SorterD", "SorterE", "SorterF"};
        for(int i = 0; i < sorters.length; i++){
            ISort sort = sorters[i];
            String out = "Sorter: " + names[i] + "\n";
            out += "Good Sort: " + checkSort(sort,10000) + "\n";
            out += "Stable: " + isStable(sort,10000) + "\n";
            out += "Avg: " + sort.sort(generateArray(10000)) + "\n";
            out += "Worst: " + sort.sort(generateWorstCase(10000)) + "\n";
            out += "Best: " + sort.sort(generateBestCase(10000)) + "\n";
            System.out.println(out);
        }
    }
}
