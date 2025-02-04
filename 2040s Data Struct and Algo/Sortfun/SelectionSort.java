public class SelectionSort extends SortAlgo{

    public SelectionSort(int[] data){
        super(data);
    }

    public void sort(){
        for(int i = 0; i < data.length; i++){
            int min = data[i];
            int index = i;
            for(int j = i; j < data.length - 1; j++){
                if(min > data[j]){
                    min = data[j];
                    index = j;
                }
            }
            if(index != i){
               data[index] = data[i];
               data[i] = min;
            } 
        }
    }
}
