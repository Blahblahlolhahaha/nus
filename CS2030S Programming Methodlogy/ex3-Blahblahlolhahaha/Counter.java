class Counter implements Comparable<Counter> {
  private boolean available = true; 
  private CoffeeQueue<Customer> queue;  
  private int id; 
  private String queueState;

  public Counter(int id, int length) {
    this.id = id; 
    this.queue = new CoffeeQueue<Customer>(length);
    this.queueState = this.queue.toString();
  }

  public void toggleAvailable() {
    this.available = !this.available; 
  }

  public boolean isAvailable() {
    return this.available; 
  }

  public String addToQueue(Customer cus) {
    this.queueState = this.queue.toString();
    if (this.queue.enq(cus)) {
      return queueState;
    }
    return null;
  }

  public Customer getNextCustomer() {
    Customer cust = this.queue.deq();
    this.queueState = this.queue.toString();
    return cust;
  }
  
  public boolean isQueueFull() {
    return this.queue.isFull();
  }

  @Override
  public int compareTo(Counter counter) {
    if (this.available && !counter.available) {
      return -1;
    }
    if (this.available && counter.available) {
      return this.id - counter.id;
    }
    return this.queue.compareTo(counter.queue);
  }
   
  public String getQueue() {
    return this.queue.toString();
  }

  @Override
  public String toString() {
    return "B" + this.id + " ";
  }
}
