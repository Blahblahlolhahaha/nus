class QueueEvent extends Event {
  
  private Customer cust; 
  
  private String queueState; 

  public QueueEvent(double time,  Customer cust, String queueState) {
    super(time); 
    this.cust = cust; 
    this.queueState = queueState; 
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
