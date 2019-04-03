import java.util.Scanner;
import java.io.PrintStream;

/**
 * An abstract class representing a state in the state machine
 * @author Spencer Yoder
 */
public abstract class State {
  /**
   * Handles the transition as well as I/O and side effects
   * @param input the input for the program
   * @param output the output for the program
   */
   public abstract State action(Scanner input, PrintStream output);
}