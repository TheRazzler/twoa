import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

import java.io.PrintStream;
/**
 * Right now, just contains some state machine parts of the program
 * @author Spencer Yoder
 */
public class Dom {
  private static final int DOM_COUNT = 100;
  
  private static ArrayList<Column> table;
  
  private static ArrayList<NumberColumn> dependents;
  private static ArrayList<NumberColumn> independents;
  private static ArrayList<SymbolColumn> symbols;
  
  private static int tableLength = 0;
  /** Example initial state, part of machine that prints everything from input */
  private static final State INITIAL = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
      table = new ArrayList<Column>();
      dependents = new ArrayList<NumberColumn>();
      independents = new ArrayList<NumberColumn>();
      symbols = new ArrayList<SymbolColumn>();
      
      return READ;
    }
  };
  
  private static final State READ = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
      String[] cols = input.nextLine().split(",");
      for(int i = 0; i < cols.length; i++) {
        String val = cols[i].trim();
        char type = val.charAt(0);
        if(type == '<') {
          dependents.add(new NumberColumn(val, -1));
          table.add(dependents.get(dependents.size() -1));
        } else if(type == '>') {
          dependents.add(new NumberColumn(val, 1));
          table.add(dependents.get(dependents.size() -1));
        } else if(type == '$') {
          independents.add(new NumberColumn(val, 0));
          table.add(independents.get(independents.size() - 1));
        } else if(type == '?') {
          table.add(null);
        } else {
          symbols.add(new SymbolColumn(val));
          table.add(symbols.get(symbols.size() -1));
        }
      }
      return ADD_ROW;
    }
  };
  
  private static final State ADD_ROW = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
      if(input.hasNextLine()) {
        tableLength++;
        String line = input.nextLine();
        String[] cells = line.split(",");
        for(int i = 0; i < cells.length; i++) {
          Column col = table.get(i);
          if(col != null)
            col.add(cells[i].trim());
        }
        return ADD_ROW;
      }
      return OUTPUT_HEADER;
    }
  };
  
  public static final State OUTPUT_HEADER = new State() {
    @Override
    public State action(Scanner input, PrintStream output) {
      int initIdx = 0;
      while(table.get(initIdx) == null)
        initIdx++;
      output.print(table.get(initIdx).getHeader());
      for(int i = 1; i < table.size(); i++) {
        Column col = table.get(i);
        if(col != null)
          output.print("," + col.getHeader());
      }
      output.println(",>dom");
      return OUTPUT_ROW;
    }
  };
  
  public static final State OUTPUT_ROW = new State() {
    private int rowNum = 0;
    
    @Override
    public State action(Scanner input, PrintStream output) {
      if(rowNum >= tableLength)
        return SUCCESS;
      int initIdx = 0;
      while(table.get(initIdx) == null)
        initIdx++;
      output.print(table.get(initIdx).toString(rowNum));
      
      for(int i = 1; i < table.size(); i++) {
        Column col = table.get(i);
        if(col != null)
          output.print("," + col.toString(rowNum));
      }
      double domScore = domScore();
      if(domScore == (long) domScore)
        output.println("," + (long) domScore);
      else
        output.println("," + domScore);
      rowNum++;
      return this;
    }
    
    private boolean dom(int r1, int r2) {
      int n = dependents.size();
      double s1 = 0;
      double s2 = 0;
      for(int i = 0; i < n; i++) {
        NumberColumn col = dependents.get(i);
        double a = col.norm(col.get(r1));
        double b = col.norm(col.get(r2));
        s1 -= Math.pow(10, col.getWeight() * (a - b) / n);
        s2 -= Math.pow(10, col.getWeight() * (b - a) / n);
      }
      return s1 / n < s2 / n;
    }
    
    private double domScore() {
      double domScore = 0;
      Random r = new Random();
      for(int i = 0; i < DOM_COUNT; i++) {
        if(dom(rowNum, r.nextInt(tableLength)))
          domScore += 1.0 / DOM_COUNT;
      }
      return domScore;
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
    Scanner input = new Scanner(System.in);
    while(currentState != SUCCESS) {
      currentState = currentState.action(input, System.out);
    }
  }
}