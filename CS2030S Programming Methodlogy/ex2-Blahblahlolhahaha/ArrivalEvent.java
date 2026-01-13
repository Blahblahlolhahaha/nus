class ArrivalEvent extends Event {
  private Customer customer; 

  
  public ArrivalEvent(double time, Customer customer) {
    super(time); 
    this.customer = customer; 
  }

  @Override
  public String toString() {
    String str = ": " + this.customer.toString() 
        + " arrives " + CoffeeSimulation.coffeeShop.toString(); 
    return super.toString() + str; 
  }
  
  @Override
  public Event[] simulate() {
    boolean counterFound = false; 
    int counterId = customer.goToCounter(); 
    Event event; 
    if (counterId == -1) {
      String prevQueueState = CoffeeSimulation.coffeeShop.addToQueue(customer); 
      if (prevQueueState != null) {
        event = new QueueEvent(this.getTime(), customer, prevQueueState); 
      } else {
        event = new DepartureEvent(this.getTime(), this.customer); 
      }
    } else {
      event = new ServiceBeginEvent(this.getTime(), this.customer, counterId); 
    }
    return new Event[]  {event}; 
  }
  
}

