package src;

import java.util.TreeMap;
import java.util.Collections;
import java.util.ArrayList;

public class Board {

  public int[][] board = new int[3][3];
  public int empty = 9;
  public boolean finished = false;

  public Board() {
  }

  // This is the combined move of player and computer, yes I know it's a bad idea
  // to couple it this way.
  public boolean makeMove(int row, int col) {

    row--;
    col--;
    if (this.finished || this.board[row][col] != 0 || row < 0 || row >= board.length || col < 0
        || col >= board[row].length)
      return false;
    else {
      this.empty--;
      boolean temp = this.Evaluate(row, col, 1);
      this.board[row][col] = 1;
      if (temp) {
        System.out.println("X WINS");
        this.finished = true;
        return true;
      } else if (this.empty == 0) {
        this.finished = true;
        System.out.println("DRAW");
        return true;
      }

      TreeMap<Integer, int[]> order = new TreeMap<>();
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          if (this.board[i][j] == 0) {
            int[] arr = { i, j };
            order.put(makeMoveRec(i, j, -1), arr);
          }
        }
      }
      this.empty--;
      // testing the result, sadly you will never see a negative key because algo
      // can't beat itself :'(
      /*
       * for (Map.Entry<Integer, int[]> entry : order.entrySet()) {
       * System.out.println(Arrays.toString(entry.getValue()) + " key: " +
       * entry.getKey()); }
       */
      boolean tempC = this.Evaluate(order.get(order.firstKey())[0], order.get(order.firstKey())[1], -1);
      this.board[order.get(order.firstKey())[0]][order.get(order.firstKey())[1]] = -1;
      if (tempC) {
        System.out.println("O WINS");
        this.finished = true;
        return true;
      } else if (this.empty == 0) {
        this.finished = true;
        System.out.println("DRAW");
        return true;
      }
      return true;
    }
  }

  // Recursive search for best next move with this player
  public int makeMoveRec(int row, int col, int player) {

    if (Evaluate(row, col, player) == true) {
      return player * this.empty;
    }
    if (this.empty == 1)
      return 0;
    this.empty--;
    this.board[row][col] = player;
    ArrayList<Integer> order = new ArrayList<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (this.board[i][j] == 0) {
          order.add(makeMoveRec(i, j, -player));
        }
      }
    }
    unMove(row, col);
    Collections.sort(order);
    if (player > 0)
      return order.get(0);
    else
      return order.get(order.size() - 1);
  }

  public boolean unMove(int row, int col) {

    if (this.board[row][col] == 0 || row < 0 || row >= board.length || col < 0 || col >= board[row].length)
      return false;
    else {
      this.empty++;
      this.board[row][col] = 0;
      this.finished = false;
      return true;
    }
  }

  public void Reset() {

    if (board != null) {
      this.board = new int[board.length][board[0].length];
    }
  }

  /**
   * Evaluates if move to row, col with player of such set is a win. Got the idea to it this way from StackOverflow
   */
  public boolean Evaluate(int row, int col, int player) {
    this.board[row][col] = player;
    int c = 0, r = 0, d = 0, rd = 0;
    int len = this.board.length;
    for (int i = 0; i < len; i++) {
      if (this.board[row][i] == player) c++;
      if (this.board[i][col] == player) r++;
      if (this.board[i][i]   == player)   d++;
      if (this.board[i][len - i - 1] == player) rd++;
    }
    this.board[row][col] = 0;
    return c == len || r == len || d == len || rd == len;
  }

  public String toString() {
    StringBuilder result = new StringBuilder("");
    for (int[] row : this.board) {
      for (int square : row) {
        switch (square) {
        case -1 -> result.append(" O ");
        case  1 -> result.append(" X ");
        default -> result.append(" - ");
        }
      }
      result.append("\n");
    }
    return result.toString();
  }
}