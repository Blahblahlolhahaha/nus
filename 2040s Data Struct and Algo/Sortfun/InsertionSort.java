public class InsertionSort extends SortAlgo{

    public InsertionSort(int[] data){
        super(data);
    }

    public void sort(){
        for(int i = 1;i < data.length;i++){
            for(int j = i; j > 0; j--){
                if(data[j] < data[j - 1]){
                    int temp = data[j];
                    data[j] = data[j-1];
                    data[j-1] = temp;
                }
            }
        } 
    }
}
