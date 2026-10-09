class Order {
  String order;
  String size;

  public Order(String order, String size) {
    this.order = order;
    this.size = size;
  }
  
  @Override
  public String toString() {
    return "(" + this.size + ") " + this.order + " ";
  }
}

