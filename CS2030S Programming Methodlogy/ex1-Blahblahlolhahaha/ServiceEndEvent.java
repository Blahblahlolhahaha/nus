class ServiceEndEvent extends Event{
  private int customerId;

  private int counterId;
  
  public ServiceEndEvent(double time,int customerId,int counterId){
    super(time);
    this.customerId = customerId;
    this.counterId = counterId;
  }

  @Override
  public String toString(){
    String str = "";

    str =  ": Customer " + this.customerId + " service done (by Counter "  + this.counterId + ")";

    return super.toString() + str;
  }

  @Override
  public Event[] simulate(){
    CoffeeSimulation.coffeeShop.toggleCounter(this.counterId);
    DepartureEvent event = new DepartureEvent(this.getTime(), this.customerId);
    return new Event[] {event};
  }
}


