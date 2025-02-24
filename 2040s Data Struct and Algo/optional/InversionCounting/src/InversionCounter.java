class InversionCounter {

    public static long countSwaps(int[] arr) {
        int size = 1;
        long swaps = 0;
        while(size < arr.length){
            for(int i = 0;i < size;i+= size*2){
                int first = i;
                while(first < arr.length){
                    int second = first + size; 
                    swaps += mergeAndCount(arr,first,first+size - 1,second, second + size - 1);
                    first += size*2;
                }
            }
            size *= 2;
        }
        return swaps;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        if(right1 >= arr.length){
            right1 = arr.length - 1;
        }
        if(right2 >= arr.length){
            right2 = arr.length - 1;
        }
        long swaps = 0;
        int count = 0;
        int initLeft = left1;
        boolean firstDone = left1 > right1;
        boolean secondDone = left2 > right2 || left2 >= arr.length;
        int[] temp = new int[right2 - left1 + 1];
        while(!firstDone || !secondDone){
            if(!secondDone && (firstDone || arr[left2] < arr[left1])){
                temp[count] = arr[left2];
                left2++;
                swaps += right1 - left1 + 1;
                secondDone = left2 > right2;
            }
            else{
                temp[count] = arr[left1];
                left1++;
                firstDone = left1 > right1;
            }
            count++;
        }
        for(int i = 0;i < count;i++){
            arr[initLeft + i] = temp[i];
        }
        return swaps;

    }
}
