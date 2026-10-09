class DepartureEvent extends Event {
  private Customer customer; 

  private double serviceTime; 
  
  private int counter; 

  public DepartureEvent(double time, Customer customer) {
    super(time); 
    this.customer = customer; 
  }

  @Override
  public String toString() {
    String str = ": " + this.customer.toString() + " departed"; 
    return super.toString() + str; 
  }
  
  @Override
  public Event[] simulate() {
    return new Event[0]; 
  }
}




