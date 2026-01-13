import java.util.Scanner; 

/**
 * This class implements a coffee shop simulation.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */ 
class CoffeeSimulation extends Simulation  {
  /** 
   * The availability of counters in the coffee shop. 
   */
  public static CoffeeShop coffeeShop = new CoffeeShop();  
  public static boolean emptyBaristaQueue = false;
  /** 
   * The list of customer arrival events to populate
   * the simulation with.
   */
  public Event[] initEvents; 

  /** 
   * Constructor for  a coffee shop simulation. 
   *
   * @param sc A scanner to read the parameters from.  The first
   *           integer scanned is the number of customers;  followed
   *           by the number of service counters.  Next is a 
   *           sequence of (arrival time,  service time) pair,  each
   *           pair represents a customer.
   */
  public CoffeeSimulation(Scanner sc)  {
    initEvents = new Event[sc.nextInt()]; 
    int numOfCounters = sc.nextInt(); 
    int queueLength = sc.nextInt();
    if (queueLength == 0) {
      CoffeeSimulation.emptyBaristaQueue = true;
    }
    CoffeeSimulation.coffeeShop.initCounters(numOfCounters, queueLength);  
    CoffeeSimulation.coffeeShop.initQueue(sc.nextInt()); 
    int id = 0; 
    while (sc.hasNextDouble())  {
      double arrivalTime = sc.nextDouble(); 
      double serviceTime = sc.nextDouble(); 
      int orderId = sc.nextInt();
      String size = sc.next();
      Order order = CoffeeSimulation.coffeeShop.getOrder(orderId, size);
      Customer customer = new Customer(id, order, serviceTime); 
      initEvents[id] = new ArrivalEvent(arrivalTime, customer); 
      id += 1; 
    }
  }

  /**
   * Retrieve an array of events to populate the 
   * simulator with.
   *
   * @return An array of events for  the simulator.
   */
  @Override
  public Event[] getInitialEvents()  {
    return initEvents; 
  }
}
