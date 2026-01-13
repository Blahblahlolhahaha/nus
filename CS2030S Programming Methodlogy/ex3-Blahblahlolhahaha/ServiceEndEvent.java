class ServiceEndEvent extends Event  {

  private Customer customer; 

  private Counter counter; 
  
  public ServiceEndEvent(double time,  Customer customer,  Counter counter)  {
    super(time); 
    this.customer = customer; 
    this.counter = counter; 
  }

  @Override
  public String toString()  {
    String str = ""; 

    str =  ": " + this.customer.toString() + " served "
      + this.customer.getOrder() + " (by "  
      + this.counter.toString() + this.counter.getQueue() + ")"; 

    return super.toString() + str; 
  }

  @Override
  public Event[] simulate()  {
    this.counter.toggleAvailable(); 
    DepartureEvent event = new DepartureEvent(this.getTime(),   this.customer); 
    Customer queueCust = CoffeeSimulation.coffeeShop.getNextCustomer(this.counter); 
    if (queueCust != null) {
      ServiceBeginEvent serviceEvent = new ServiceBeginEvent(this.getTime(), 
          queueCust, this.counter);
      if (!this.counter.isQueueFull()) {
        Customer entranceCust = CoffeeSimulation.coffeeShop.getNextCustomer();
        if (entranceCust != null) {   
          BaristaQueueEvent baristaEvent = 
              new BaristaQueueEvent(this.getTime() + 0.05, entranceCust, this.counter);
        
          return new Event[] {event, serviceEvent, baristaEvent};
        }
      }
      return new Event[] {event,  serviceEvent}; 
    }
    return new Event[]   {event}; 
  
  }
}


