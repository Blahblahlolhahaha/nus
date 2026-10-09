class ServiceEndEvent extends Event  {

  private Customer customer; 

  private int counterId; 
  
  public ServiceEndEvent(double time,  Customer customer,  int counterId)  {
    super(time); 
    this.customer = customer; 
    this.counterId = counterId; 
  }

  @Override
  public String toString()  {
    String str = ""; 

    str =  ": " + this.customer.toString() + " served "
      + this.customer.getDrink() + " (by B"  + this.counterId + ")"; 

    return super.toString() + str; 
  }

  @Override
  public Event[] simulate()  {
    CoffeeSimulation.coffeeShop.toggleCounter(this.counterId); 
    DepartureEvent event = new DepartureEvent(this.getTime(),   this.customer); 
    Customer queueCust = CoffeeSimulation.coffeeShop.getNextCustomer(); 
    if  (queueCust != null)  {
      ServiceBeginEvent serviceEvent = new ServiceBeginEvent(this.getTime(), 
          queueCust, this.counterId); 
      return new Event[]  {event,  serviceEvent}; 
    }
    return new Event[]   {event}; 
  }
}


