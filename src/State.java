import java.util.Scanner;
import java.io.PrintStream;

/**
 * An abstract class representing a state in the state machine
 * @author Spencer Yoder
 */
public abstract class State {
  /**
   * The action this State will take given this String
   * Fits in with polymorphism
   * @param input, the input which this state reads
   * @param output the output to which this state writes
   * @return the state to transition to
   */
   public abstract State action(Scanner input, PrintStream output);
}