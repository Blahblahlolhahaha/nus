public class BubbleSort extends SortAlgo {
    
    public BubbleSort(String name, int[] data) {
        super(name, data);
    }

    public void sort() {
        for(int i = data.length - 1; i > 0; i--) {
            for(int j = 0; j < i; j++) {
                if(data[j] > data[j+1]) {
                    int temp = data[j];
                    data[j] = data[j+1];
                    data[j+1] = temp;
                }
            }
        }
    }    
}
