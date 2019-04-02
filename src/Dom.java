/**
 * Right now, just contains some state machine parts of the program
 * @author Spencer Yoder
 */
public class Dom {
  
  private static int count = 1;
  /** Example initial state, part of machine that counts from 1 to 10 */
  private static final State INITIAL = new State() {
    @Override
    public State action(String s) {
      System.out.println(count);
      if(count < 10) {
        count++;
        return this;
      } else {
        return SUCCESS;
      }
    }
  };
  
  /** Success state, state machine will end after this */
  private static final State SUCCESS = new State() {
    @Override
    public State action(String s) {
      return null;
    }
  };
  
  /**
   * Will run any state machine, behavior will be defined in the states
   * When we actually implement the program, we will make sure to pass the correct string
   * @param args command line arguments
   */
  public static void main(String[] args) {
    State currentState = INITIAL;
    while(currentState != SUCCESS) {
      currentState = currentState.action("");
    }
    System.out.println("Done");
  }
}