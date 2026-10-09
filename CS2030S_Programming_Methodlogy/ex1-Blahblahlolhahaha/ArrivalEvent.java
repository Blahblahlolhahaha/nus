class ArrivalEvent extends Event{
  private int customerId;

  private double serviceTime;
  
  public ArrivalEvent(double time,int customerId,double serviceTime){
    super(time);
    this.customerId = customerId;
    this.serviceTime = serviceTime;
  }

  @Override
  public String toString(){
    String str = ": Customer " + this.customerId + " arrives";
    return super.toString() + str;
  }
  
  @Override
  public Event[] simulate(){
    boolean counterFound = false;
    int counterId = CoffeeSimulation.coffeeShop.getFirstCounter();
    Event event;
    if(counterId == -1){
      event = new DepartureEvent(this.getTime(),this.customerId);
    }
    else{
      event = new ServiceBeginEvent(this.getTime(),this.customerId,this.serviceTime,counterId);
    }
    return new Event[] {event};
  }
  
}

