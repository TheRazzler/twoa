public abstract class Column {
  private String header;
  protected int size;
  
  public Column(String header) {
    this.header = header;
    size = 0;
  }
  
  public abstract String toString(int index);
  public abstract void add(String data);
  public int size() {
    return size;
  }
  
  public String getHeader() {
    return header;
  }
}