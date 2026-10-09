class Menu {

  private String[] menu;

  public Menu(String[] menu) {
    this.menu = menu;
  }
 
  public Order getOrder(int orderId, String size) {
    return new Order(this.menu[orderId], size);
  }
}
