class Menu {

  private String[] menu;

  public Menu(String[] menu) {
    this.menu = menu;
  }

  public String getItem(int id) {
    return menu[id];
  }

}
