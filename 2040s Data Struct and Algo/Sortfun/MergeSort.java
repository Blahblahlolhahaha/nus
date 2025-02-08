public class MergeSort extends SortAlgo {
    
    public MergeSort(String name, int[] data) {
        super(name, data);
    }

    public void mergeySorty(int first, int second,int length){
        int count = 0;
        int[] temp = new int[length*2];
        int initFirst = first;
        int initSecond = second;
        boolean secondDone = second >= data.length;
        boolean firstDone = false;
        while(!secondDone || !firstDone){
            if(!secondDone && (firstDone || data[second] < data[first])){
                temp[count] = data[second];
                second++;
            }
            else{
                temp[count] = data[first];
                first++;
            
            }
            if(first - initFirst == length || first == data.length){
                firstDone = true;
            }
            if(second == data.length || second - initSecond == length){
                secondDone = true;
            }
            count++;

        }
        for(int i = 0;i < count;i++){
            data[initFirst + i] = temp[i];
        }
    }

    public void sort() {
        int length = 1;
        while(length < data.length){
            int first = 0;
            int second = first + length;
            while(first < data.length){
                mergeySorty(first,second,length);
                first += length * 2;
                second += length * 2;

            }
            length *= 2;
        }
    }    
}
