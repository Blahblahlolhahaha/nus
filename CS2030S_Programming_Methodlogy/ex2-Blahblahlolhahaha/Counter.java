class Counter {
  private boolean available = true; 
  
  private int id; 

  public Counter(int id) {
    this.id = id; 
  }

  public void toggleAvailable() {
    this.available = !this.available; 
  }

  public boolean isAvailable() {
    return this.available; 
  }
}
