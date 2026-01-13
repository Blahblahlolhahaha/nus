class Customer {
 
  private static String[] drinkArray =  {"Coffee Espresso", "Coffee Latte"}; 
  private double serviceTime; 
  private int id; 
  private Order order;
  private String size;
  
  public Customer(int id, Order order, double serviceTime) {
    this.id = id; 
    this.order = order; 
    this.serviceTime = serviceTime; 
    this.size = "(" + size + ") ";
  }

  public Counter goToCounter() {
    Counter counter = CoffeeSimulation.coffeeShop.getFirstCounter(); 
    if (!counter.isAvailable() && counter.isQueueFull()) {
      return null;
    }
    return counter;
  }

  @Override
  public String toString() {
    return "C" + this.id; 
  }

  public String getOrder() {
    return this.order.toString(); 
  }

  public double calcEndTime(double time) {
    return this.serviceTime + time; 
  }
}

