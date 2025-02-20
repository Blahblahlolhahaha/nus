public abstract class SortAlgo{
    private String name;
    protected int[] data;
   
    public SortAlgo(String name){
        this.name = name;
    } 

    public SortAlgo(String name, int[] data){
        this.data = data;
        this.name = name;
    }

    public abstract void sort();
    
    public void setData(int[] data){
        this.data = data;
    }

    private boolean isSorted(){
        for(int i = 0; i< data.length -1;i++){
            if(data[i] > data[i+1]){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString(){
        String s = this.name + ": " + isSorted();
        return s;
    }
}
