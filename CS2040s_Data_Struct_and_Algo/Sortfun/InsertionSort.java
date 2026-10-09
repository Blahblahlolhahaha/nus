public class InsertionSort extends SortAlgo{

    public InsertionSort(String name, int[] data){
        super(name, data);
    }

    public void sort(){
        for(int i = 1;i < data.length;i++){
            for(int j = i; j > 0; j--){
                if(data[j] < data[j - 1]){
                    int temp = data[j];
                    data[j] = data[j-1];
                    data[j-1] = temp;
                }
                else{
                    break;
                }
            }
        } 
    }
}
