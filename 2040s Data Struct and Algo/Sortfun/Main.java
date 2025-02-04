public class Main{
    public static void main(String[] args){
        int[] data = {8,4,1,2,7,6,5,7};
        BubbleSort sort = new BubbleSort(data);
        SelectionSort sorty = new SelectionSort(data);
        InsertionSort sortyy = new InsertionSort(data);
        sort.sort();
        System.out.println(sort);
        sorty.sort();
        System.out.println(sorty);
        sortyy.sort();
        System.out.println(sortyy);

    }
}
