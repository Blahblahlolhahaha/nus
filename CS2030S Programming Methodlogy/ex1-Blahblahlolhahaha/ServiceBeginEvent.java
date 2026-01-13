class ServiceBeginEvent extends Event{
  private int customerId;

  private double serviceTime;
  
  private int counterId;
  
  public ServiceBeginEvent(double time,int customerId,double serviceTime,int counterId){
    super(time);
    this.customerId = customerId;
    this.serviceTime = serviceTime;
    this.counterId = counterId;
  }

  @Override
  public String toString(){
    String str = "";

    str =  ": Customer " + this.customerId + " service begin (by Counter "  + this.counterId + ")";

    return super.toString() + str;
  }

  @Override
  public Event[] simulate(){
    CoffeeSimulation.coffeeShop.toggleCounter(this.counterId);
    double endTime = this.getTime() + this.serviceTime;
    ServiceEndEvent event = new ServiceEndEvent(endTime, this.customerId, this.counterId);
    return new Event[] {event};
  }
}


