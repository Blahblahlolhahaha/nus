import java.util.Random;
public class QuickSort extends SortAlgo {
    
    public QuickSort(String name, int[] data) {
        super(name, data);
    }

    public int partition(int start, int end){
        Random random = new Random();
        int pivot = random.nextInt(start, end);
        int exchange = data[end-1];
        data[end-1] = data[pivot];
        data[pivot] = exchange;
        int small = start;
        for(int i = 0;i < end - start - 1;i++){
            if(data[i + start] < data[end-1]){
                exchange = data[i + start];
                data[i + start] = data[small];
                data[small] = exchange;
                small++;
            }
        }
        exchange = data[end-1];
        data[end-1] = data[small];
        data[small] = exchange;
        return small;
    }

    public void quickSort(int start, int end){
        if(start >= end){
            return;
        }
        int pos = partition(start,end);
        quickSort(start,pos);
        quickSort(pos + 1,end);
    }

    public void sort() {
        quickSort(0,data.length);
    }

    class Pair{
        int first;
        int second;

        Pair(int first, int second){
            this.first = first;
            this.second = second;
        }
    }    
}
