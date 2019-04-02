/**
 * An abstract class representing a state in the state machine
 * @author Spencer Yoder
 */
public abstract class State {
  /**
   * The action this State will take given this String
   * Fits in with polymorphism
   * @param s, the String which is parsed to perform the action conditionally
   * @return the state to transition to
   */
   public abstract State action(String s);
}