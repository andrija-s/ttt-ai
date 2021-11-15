package src;

import java.util.*;

public class Board {
  public static final int WINSTREAK = 3;
  public int[][] board;
  public int empty;
  public boolean finished = false;

  public Board(int row, int col) {
    this.empty = row * col;
    this.board = new int[row][col];
  }

  public boolean makeMove(int row, int col) {

    if (this.finished || this.board[row][col] != 0 || row < 0 || row >= board.length || col < 0
        || col >= board[row].length)
      return false;
    else {
      this.empty--;
      this.board[row][col] = 1;
      boolean temp = this.Evaluate(row, col, 1);
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
            int res = makeMoveRec(i, j, -1);
            order.put(res, arr);
          }
        }
      }
      this.empty--;
      for (Map.Entry<Integer, int[]> entry : order.entrySet()) {
        System.out.println(Arrays.toString(entry.getValue()) + " key: " + entry.getKey());
      }
      this.board[order.get(order.firstKey())[0]][order.get(order.firstKey())[1]] = -1;
      boolean tempC = this.Evaluate(order.get(order.firstKey())[0], order.get(order.firstKey())[1], -1);
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

  public int makeMoveRec(int row, int col, int set) {
    this.empty--;
    this.board[row][col] = set;
    if (Evaluate(row, col, set) == true) {
      unMove(row, col);
      return set * this.empty;
    }
    ArrayList<Integer> order = new ArrayList<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (this.board[i][j] == 0) {
          order.add(makeMoveRec(i, j, -set));
        }
      }
    }
    unMove(row, col);
    Collections.sort(order);
    if (order.isEmpty()) return 0;
    if (set > 0) return order.get(0);
    else return order.get(order.size() - 1);
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

  public boolean Evaluate(int row, int col, int set) {
    int c = 0, r = 0, d = 0, rd = 0;
    int len = this.board.length;
    for (int i = 0; i < len; i++) {
      if (this.board[row][i] == set)
        c++;
      if (this.board[i][col] == set)
        r++;
      if (this.board[i][i] == set)
        d++;
      if (this.board[i][len - i - 1] == set)
        rd++;
    }
    return c == len || r == len || d == len || rd == len;
  }

  public String toString() {
    StringBuilder result = new StringBuilder("");
    for (int[] row : this.board) {
      for (int square : row) {
        switch (square) {
        case -1 -> result.append(" O ");
        case 1 -> result.append(" X ");
        default -> result.append(" - ");
        }
      }
      result.append("\n");
    }
    return result.toString();
  }

}