class QueueEvent extends Event {
  
  private Customer cust; 
  
  private String queueState; 

  public QueueEvent(double time,  Customer cust) {
    super(time); 
    this.cust = cust; 
    this.queueState = CoffeeSimulation.coffeeShop.addToQueue(cust); 
    
  }
  
  @Override
  public String toString() {
    String str = ": " + this.cust.toString() + " joined queue " + this.queueState; 
    return super.toString() + str; 
  }
  

  public Event[] simulate() {
    return new Event[] {}; 
  }

}
