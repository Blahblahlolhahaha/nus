class ServiceBeginEvent extends Event {
  private Customer customer; 
  
  private Counter counter; 
  
  public ServiceBeginEvent(double time, Customer customer,  Counter counter) {
    super(time); 
    this.customer = customer; 
    this.counter = counter; 
  }

  @Override
  public String toString() {
    String str = ""; 

    str =  ": " + this.customer.toString() + " ordered " 
       + this.customer.getOrder() + " (by "  
       + this.counter.toString() + this.counter.getQueue() + ")";

    return super.toString() + str; 
  }

  @Override
  public Event[] simulate() {
    this.counter.toggleAvailable(); 
    double endTime = this.customer.calcEndTime(this.getTime()); 
    ServiceEndEvent event = new ServiceEndEvent(endTime,  this.customer,  this.counter); 
    return new Event[]  {event}; 
  }
}


