public class SelectionSort extends SortAlgo{

    public SelectionSort(String name, int[] data){
        super(name, data);
    }

    public void sort(){
        for(int i = 0; i < data.length -1; i++){
            int min = data[i];
            int index = i;
            for(int j = i; j < data.length; j++){
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
