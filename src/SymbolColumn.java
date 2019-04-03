/**
 * Represents a column containing only symbols
 * @author Spencer Yoder
 */
public class SymbolColumn extends Column {
  /** A dynamically resizable array of values */
  private String[] values;
  /** The initial length of the array */
  private static final int INITIAL_CAPACITY = 50;
  
  /**
   * Constructs a new SymbolColumn with the given header
   * @param header the given header
   */
  public SymbolColumn(String header) {
    super(header);
    values = new String[INITIAL_CAPACITY];
  }
  
  /**
   * Add the given symbol to the column
   * @param value the given symbol
   */
  @Override
  public void add(String value) {
    if(size >= values.length) {
      String[] temp = new String[values.length * 2];
      for(int i = 0; i < size; i++) {
        temp[i] = values[i];
      }
      values = temp;
    }
    values[size++] = value;
  }
  
  /**
   * @param index a row number
   * @return the value at the given row number
   */
  @Override
  public String toString(int index) {
    return values[index];
  }
}