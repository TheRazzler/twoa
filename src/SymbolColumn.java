public class SymbolColumn extends Column {
  private String header;
  private String[] values;
  private static final int INITIAL_CAPACITY = 50;
  
  public SymbolColumn(String header) {
    super(header);
    values = new String[INITIAL_CAPACITY];
  }
  
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
  
  @Override
  public String toString(int index) {
    return values[index];
  }
}