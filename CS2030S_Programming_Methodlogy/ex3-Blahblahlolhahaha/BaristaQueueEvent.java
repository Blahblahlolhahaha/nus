class BaristaQueueEvent extends Event {
  
  private Customer cust; 
  
  private String queueState; 

  private Counter counter;

  public BaristaQueueEvent(double time,  Customer cust, Counter counter) {
    super(time); 
    this.cust = cust; 
    this.counter = counter;
  }
  
  @Override
  public String toString() {
    String str = ": " + this.cust.toString() + " joined barista queue (at " 
        + this.counter.toString() + this.counter.getQueue() + ")"; 
    return super.toString() + str; 
  }
  

  public Event[] simulate() {
    this.counter.addToQueue(this.cust);
    return new Event[] {}; 
  }

}
