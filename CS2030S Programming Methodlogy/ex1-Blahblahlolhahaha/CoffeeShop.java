class CoffeeShop{
  private Counter[] counters;

  public int getFirstCounter(){
    for(int i = 0; i< counters.length;i++){
      if(counters[i].isAvailable()){
        return i;
      }
    }
    return -1;
  }

  public void toggleCounter(int id){
    counters[id].toggleAvailable();
  }

  public void initCounters(int noOfCounters){
    counters = new Counter[noOfCounters];
    for(int i = 0;i< noOfCounters;i++){
      counters[i] = new Counter(i);
    }
  }

 
}

