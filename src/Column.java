/**
 * Represents a column in the table
 * @author Spencer Yoder
 */
public abstract class Column {
  /** The column header */
  private String header;
  /** The number of elements in the column */
  protected int size;
  
  /**
   * Constructs a new Column with the given header
   * @param header the given header
   */
  public Column(String header) {
    this.header = header;
    size = 0;
  }
  
  /**
   * @param index a row number
   * @return a String representation of the value at the given row number
   */
  public abstract String toString(int index);
  
  /**
   * Add the data represented by the given string to the column
   * @param data the given string
   */
  public abstract void add(String data);
  
  /**
   * @return the number of elements in the column
   */
  public int size() {
    return size;
  }
  
  /**
   * @return the column header
   */
  public String getHeader() {
    return header;
  }
}