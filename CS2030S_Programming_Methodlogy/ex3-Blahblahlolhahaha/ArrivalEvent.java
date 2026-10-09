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
    Counter counter = customer.goToCounter(); 
    Event event; 
    if (counter == null) {
      if (!CoffeeSimulation.coffeeShop.isQueueFull()) {
        event = new QueueEvent(this.getTime(), customer); 
      } else {
        event = new DepartureEvent(this.getTime(), this.customer); 
      }
    } else {
      if (!counter.isAvailable()) {
        event = new BaristaQueueEvent(this.getTime(), this.customer, counter);      
        return new Event[] {event};
      }
      event = new ServiceBeginEvent(this.getTime(), this.customer, counter); 
    }
    return new Event[]  {event}; 
  }
  
}

