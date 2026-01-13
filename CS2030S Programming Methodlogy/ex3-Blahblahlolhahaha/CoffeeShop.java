class CoffeeShop {
  private Seq<Counter> counters; 
  private CoffeeQueue<Customer> queue; 
  private String queueState; 
  private Menu menu;

  public CoffeeShop() {
    String[] drinkMenu =  {"Coffee Espresso", "Coffee Latte"}; 
    this.menu = new Menu(drinkMenu);
  }

  public Counter getFirstCounter() {
    return counters.min(); 
  }

  public void initCounters(int noOfCounters, int length) {
    counters = new Seq<Counter>(noOfCounters); 
    for (int i = 0; i < noOfCounters; i++) {
      counters.set(i, new Counter(i, length)); 
    }
  }
  
  public void initQueue(int queueSize) {
    this.queue = new CoffeeQueue<Customer>(queueSize); 
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

  public boolean isQueueFull() {
    return this.queue.isFull();
  }

  public Customer getNextCustomer() {
    return this.queue.deq();
  }

  public Customer getNextCustomer(Counter counter) {
    Customer cust = counter.getNextCustomer();
    if (cust == null) {
      return this.queue.deq();
    }
    return cust;
  }
  
  public Order getOrder(int orderId, String size) {
    return this.menu.getOrder(orderId, size);
  }
  
  //public Counter getUpdatedCounter(Counter counter) {
  //return counter.updateSelf(this.counters);
  //}

  @Override
  public String toString() {
    return this.queue.toString(); 
  }
}

