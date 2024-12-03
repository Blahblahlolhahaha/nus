class Customer{
  private int id;

  public Customer(int id){
    this.id = id;
  }

  public int goToCounter(){
    return CoffeeSimulation.coffeeShop.getFirstCounter();
  }

  @Override
  public String toString(){
    return "Customer " + this.id;
  }
}

