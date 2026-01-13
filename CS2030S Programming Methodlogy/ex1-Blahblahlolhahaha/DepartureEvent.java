class DepartureEvent extends Event{
  private int customerId;

  private double serviceTime;
  
  private int counter;
  public DepartureEvent(double time,int customerId){
    super(time);
    this.customerId = customerId;
  }

  @Override
  public String toString(){
    String str = ": Customer " + this.customerId + " departed";
    return super.toString() + str;
  }
  
  @Override
  public Event[] simulate(){
    return new Event[0];
  }
}




