class Customer {
 
  private static String[] drinkArray =  {"Coffee Espresso", "Coffee Latte"}; 
  private double serviceTime; 
  private int id; 
  private int drink; 
  
  public Customer(int id, int drink, double serviceTime) {
    this.id = id; 
    this.drink = drink; 
    this.serviceTime = serviceTime; 
  }

  public int goToCounter() {
    return CoffeeSimulation.coffeeShop.getFirstCounter(); 
  }

  @Override
  public String toString() {
    return "C" + this.id; 
  }

  public String getDrink() {
    return CoffeeSimulation.coffeeShop.getDrink(this.drink); 
  }

  public double calcEndTime(double time) {
    return this.serviceTime + time; 
  }
}

