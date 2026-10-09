class CoffeeShop {
  private Counter[] counters; 
  private Queue queue; 
  private String queueState; 
  private Menu drinkMenu;

  public CoffeeShop() {
    String[] drinkMenu =  {"Coffee Espresso", "Coffee Latte"}; 
    this.drinkMenu = new Menu(drinkMenu);
  }

  public int getFirstCounter() {
    for (int i = 0;  i < counters.length; i++) {
      if (counters[i].isAvailable()) {
        return i; 
      }
    }
    return -1; 
  }

  public void toggleCounter(int id) {
    counters[id].toggleAvailable(); 
  }

  public void initCounters(int noOfCounters) {
    counters = new Counter[noOfCounters]; 
    for (int i = 0; i < noOfCounters; i++) {
      counters[i] = new Counter(i); 
    }
  }
  
  public void initQueue(int queueSize) {
    this.queue = new Queue(queueSize); 
    this.queueState = this.toString(); 
  }

  public String addToQueue(Customer customer) {
    this.queueState = this.queue.toString(); 
    if (this.queue.enq(customer)) {
      return this.queueState; 
    } else {
      return null; 
    }
  }
  
  public Customer getNextCustomer() {
    return (Customer) this.queue.deq(); 
  }
  
  public String getDrink(int drinkID) {
    return this.drinkMenu.getItem(drinkID);
  }

  @Override
  public String toString() {
    return this.queue.toString(); 
  }
}

