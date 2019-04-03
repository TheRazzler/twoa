/**
 * A class for columns containing numeric values
 * @author Spencer Yoder
 */
public class NumberColumn extends Column {
  /** A dynamically resizable array of values */
  private double[] values;
  /** The initial capacity for the array */
  private static final int INITIAL_CAPACITY = 50;
  /** The number of values in the column */
  private int size;
  /** The largest value in the column */
  private double max;
  /** The smallest value in the column */
  private double min;
  /** If the column is dependent, whether it should be maximized or minimized */
  private int weight;
  
  /**
   * Constructs a new NumberColumn with the given header and weight
   * @param header the column header
   * @param weight the weight for maximization/minimization
   */
  public NumberColumn(String header, int weight) {
    super(header);
    this.weight = weight;
    max = Double.MIN_VALUE;
    min = Double.MAX_VALUE;
    values = new double[INITIAL_CAPACITY];
  }
  
  /**
   * Adds the double represented by s to the column
   * @param s a String which can either hold a numeric value or an unknown value (?)
   */
  @Override
  public void add(String s) {
    double value;
    if(s.equals("?"))
      value = Double.POSITIVE_INFINITY;
    else
      value = Double.parseDouble(s);
    if(size >= values.length) {
      double[] temp = new double[values.length * 2];
      for(int i = 0; i < size; i++) {
        temp[i] = values[i];
      }
      values = temp;
    }
    values[size++] = value;
    if(!isUnknown(value)) {
      if(value > max)
        max = value;
      if(value < min)
        min = value;
    }
  }
  
  /**
   * @param i the row of the column
   * @return the value at row i
   */
  public double get(int i) {
    return values[i];
  }
  
  /**
   * Returns the given value normalized against the list
   * @param value the value to be normalized
   * @return the normalized value
   */
  public double norm(double value) {
    if(isUnknown(value))
      return 0.5;
    return (value - min) / (max - min + Math.pow(10, -32));
  }
  
  /**
   * @param index the given row
   * @return the value at the given row formatted to a string
   */
  @Override
  public String toString(int index) {
    double val = values[index];
    if(isUnknown(val))
      return "?";
    if(val == (long) val)
      return "" + (long) val;
    return "" + val;
  }
  
  /**
   * @param value a numeric value
   * @return true if the value represents an unknown value
   */
  private boolean isUnknown(double value) {
    return value == Double.POSITIVE_INFINITY;
  }
  
  /**
   * @return the weight of this column
   */
  public int getWeight() {
    return weight;
  }
}