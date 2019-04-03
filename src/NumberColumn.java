public class NumberColumn extends Column {
  private double[] values;
  private static final int INITIAL_CAPACITY = 50;
  private int size;
  private double max;
  private double min;
  private int weight;
  
  public NumberColumn(String header, int weight) {
    super(header);
    this.weight = weight;
    max = Double.MIN_VALUE;
    min = Double.MAX_VALUE;
    values = new double[INITIAL_CAPACITY];
  }
  
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
  
  public double get(int i) {
    return values[i];
  }
  
  public double norm(double value) {
    if(isUnknown(value))
      return 0.5;
    return (value - min) / (max - min + Math.pow(10, -32));
  }
  
  @Override
  public String toString(int index) {
    double val = values[index];
    if(isUnknown(val))
      return "?";
    if(val == (long) val)
      return "" + (long) val;
    return "" + val;
  }
  
  private boolean isUnknown(double value) {
    return value == Double.POSITIVE_INFINITY;
  }
  
  public int getWeight() {
    return weight;
  }
}