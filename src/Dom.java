import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

import java.io.PrintStream;
/**
 * A class which calculates the dom scores given a table of values
 * @author Spencer Yoder
 * @author Thea Wall
 * @author Josef Dewberry
 */
public class Dom {
  /** The number of lines to randomly sample to calculate the dom score */
  private static final int DOM_COUNT = 100;
  
  /** The table of columns */
  private static ArrayList<Column> table;
  /** The list of dependent columns */
  private static ArrayList<NumberColumn> dependents;
  /** The list of independent columns */
  private static ArrayList<NumberColumn> independents;
  /** The list of symbol columns */
  private static ArrayList<SymbolColumn> symbols;
  
  /** The number of rows in the table */
  private static int tableLength = 0;
  /** The initial state which initializes all the column lists */
  private static final State INITIAL = new State() {
    /**
     * Initializes each of the lists and transitions to the READ state
     */
    @Override
    public State action(Scanner input, PrintStream output) {
      table = new ArrayList<Column>();
      dependents = new ArrayList<NumberColumn>();
      independents = new ArrayList<NumberColumn>();
      symbols = new ArrayList<SymbolColumn>();
      
      return READ;
    }
  };
  
  /** The state to read in the row of column headers */
  private static final State READ = new State() {
    /**
     * Reads in each column header and appropriately adds the correct type of column constructed 
     * from that header. Transitions to the ADD_ROW state.
     */
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
  
  /** A state to add the data in each row to the appropriate column. */
  private static final State ADD_ROW = new State() {
    /**
     * Adds the data in each row to the appropriate column. While there are columns left, loops.
     * Once there are no more, transitions to OUTPUT_HEADER state
     */
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
        return this;
      }
      return OUTPUT_HEADER;
    }
  };
  
  /** A state which prints the output column headers */
  public static final State OUTPUT_HEADER = new State() {
    /**
     * Prints each column header and adds the header for dom. Transitions to OUTPUT_ROW state.
     */
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
  
  /** A state for outputting each row of the data */
  public static final State OUTPUT_ROW = new State() {
    /** The row currently being output */
    private int rowNum = 0;
    
    /**
     * Calculates the dom score for each row and outputs the full row. Loops until no more rows.
     * Transitions to SUCCESS state.
     */
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
        output.printf(",%.2f\n", domScore);
      rowNum++;
      return this;
    }
    
    /**
     * Returns true if r1 dominates r2
     * @param r1 the index of the row to check
     * @param r2 the index of the row to check against
     * @return true if the row at r1 dominates r2
     */
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
    
    /**
     * Calculates the dom score for the current row against a random assortment of other rows
     * @return the dom score
     */
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
    /**
     * Unused action method, the program will end upon transition to this state.
     */
    @Override
    public State action(Scanner input, PrintStream output) {
      return null;
    }
  };
  
  /**
   * Runs the state machine
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