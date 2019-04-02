import java.util.Scanner;
import java.io.PrintStream;
/**
 * Right now, just contains some state machine parts of the program
 * @author Spencer Yoder
 */
public class Dom {
  
  private static int count = 1;
  /** Example initial state, part of machine that prints everything from input */
  private static final State INITIAL = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
      if(input.hasNext()) {
        output.println(input.next());
        return this;
      }
      return SUCCESS;
    }
  };
  
  /** Success state, state machine will end after this */
  private static final State SUCCESS = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
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
    Scanner input = new Scanner("A B C D E F G");
    while(currentState != SUCCESS) {
      currentState = currentState.action(input, System.out);
    }
    System.out.println("Done");
  }
}