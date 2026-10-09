class ServiceBeginEvent extends Event {
  private Customer customer; 
  
  private int counterId; 
  
  public ServiceBeginEvent(double time, Customer customer,  int counterId) {
    super(time); 
    this.customer = customer; 
    this.counterId = counterId; 
  }

  @Override
  public String toString() {
    String str = ""; 

    str =  ": " + this.customer.toString() + " ordered " 
      + this.customer.getDrink() + " (by B"  + this.counterId + ")"; 

    return super.toString() + str; 
  }

  @Override
  public Event[] simulate() {
    CoffeeSimulation.coffeeShop.toggleCounter(this.counterId); 
    double endTime = this.customer.calcEndTime(this.getTime()); 
    ServiceEndEvent event = new ServiceEndEvent(endTime,  this.customer,  this.counterId); 
    return new Event[]  {event}; 
  }
}


