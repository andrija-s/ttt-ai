import src.Board;
import java.util.Scanner;

/**
 * Main
 */
public class Main {

  public static void main(String[] args) {
    Board test = new Board();
    Scanner myObj = new Scanner(System.in);
    while (!test.finished) {
      System.out.println(test.toString());
      System.out.println("Enter row [1, 3]:");
      int posR = myObj.nextInt();
      System.out.println("Enter col [1, 3]:");
      int posC = myObj.nextInt(); 
      
      test.makeMove(posR, posC);
    }
    System.out.println(test.toString());
    myObj.close();
  }
}