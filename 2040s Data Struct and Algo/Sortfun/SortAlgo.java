public abstract class SortAlgo{
    protected int[] data;
    
    public SortAlgo(int[] data){
        this.data = data;
    }

    public abstract void sort();

    @Override
    public String toString(){
        String s = "{";
        for(int i = 0; i < data.length; i++){
            if(i == data.length - 1){
                s += data[i] + "}";
                continue;
            }
            s += data[i] + ",";
        }
        return s;
    }
}
