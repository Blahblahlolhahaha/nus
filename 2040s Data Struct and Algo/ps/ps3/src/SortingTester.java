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
        KeyValuePair[] test = generateArray(size);
        sorter.sort(test);
        for(int i = 0;i < test.length - 1;i++){
            if(test[i].getValue() > test[i+1].getValue()){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // TODO: implement this
    }
}
