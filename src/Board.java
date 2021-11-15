package src;

import java.util.TreeMap;
import java.util.TreeSet;

public class Board {
  public static final int DIMENSION = 3;
  public int[][] board = new int[DIMENSION][DIMENSION];
  public int filled = DIMENSION * DIMENSION;
  public boolean finished = false;
  public int winner = 0;

  public Board() {
  }

  // This is the combined move of player and computer,
  // yes I know it's not a great idea to couple it this way.
  public boolean makeMove(int row, int col) {

    row--;
    col--;
    if (this.finished || row < 0
        || row >= DIMENSION || col < 0 
        || col >= DIMENSION || this.board[row][col] != 0)
      return false;
    this.filled--;
    boolean temp = this.Evaluate(row, col, 1);
    this.board[row][col] = 1;
    if (temp) {
      this.winner = 1;
      this.finished = true;
      return true;
    } 
    else if (this.filled == 0) {
      this.finished = true;
      this.winner = 0;
      return true;
    }

    TreeMap<Integer, int[]> order = new TreeMap<>();
    for (int i = 0; i < DIMENSION; i++) {
      for (int j = 0; j < DIMENSION; j++) {
        if (this.board[i][j] == 0) {
          int[] arr = { i, j };
          order.put(makeMoveRec(i, j, -1, this.filled), arr);
        }
      }
    }
    // testing the result, sadly you will never see a negative key because algo
    // can't beat itself :'(
    /*
     * for (Map.Entry<Integer, int[]> entry : order.entrySet()) {
     * System.out.println(Arrays.toString(entry.getValue()) + " key: " +
     * entry.getKey()); }
     */
    this.filled--;
    boolean tempC = this.Evaluate(order.get(order.firstKey())[0], order.get(order.firstKey())[1], -1);
    this.board[order.get(order.firstKey())[0]][order.get(order.firstKey())[1]] = -1;
    if (tempC) {
      this.winner = -1;
      this.finished = true;
      return true;
    } 
    else if (this.filled == 0) {
      this.finished = true;
      this.winner = 0;
      return true;
    }
    return true;
  }

  // Recursive search for best next move with this player
  public int makeMoveRec(int row, int col, int player, int depth) {

    if (Evaluate(row, col, player) == true) return player * depth;
    if (depth == 1) return 0;

    this.board[row][col] = player;
    TreeSet<Integer> order = new TreeSet<>();
    for (int i = 0; i < DIMENSION; i++) {
      for (int j = 0; j < DIMENSION; j++) {
        if (this.board[i][j] == 0)
          order.add(makeMoveRec(i, j, -player, depth - 1));
      }
    }
    this.board[row][col] = 0;
    // we want to give the weight of the strongest possible counter-attack by enemy
    // against current player, should current player not win with current move
    if (player > 0) return order.first();
    else return order.last();
  }

  public void Reset() {

    this.board = new int[DIMENSION][DIMENSION];
    this.filled = DIMENSION * DIMENSION;
    this.finished = false;
  }

  /**
   * Evaluates if move to row, col with this player is a win. Half of normal eval.
   * Got the idea to do it this way from StackOverflow user, Osama Al-Maadeed:
   * https://stackoverflow.com/questions/1056316/algorithm-for-determining-tic-tac-toe-game-over
   */
  public boolean Evaluate(int row, int col, int player) {
    this.board[row][col] = player;
    int c = 0, r = 0, d = 0, rd = 0;
    for (int i = 0; i < DIMENSION; i++) {
      if (this.board[row][i] == player) c++;
      if (this.board[i][col] == player) r++;
      if (this.board[i][i]   == player) d++;
      if (this.board[i][DIMENSION - i - 1] == player) rd++;
    }
    this.board[row][col] = 0;
    return c == DIMENSION || r == DIMENSION || d == DIMENSION || rd == DIMENSION;
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
    if (this.finished) {
      switch (winner) {
      case -1 -> result.append("\nO WINS!\n");
      case  1 -> result.append("\nX WINS!\n");
      default -> result.append("\nDRAW!\n");
      }
    }
    return result.toString();
  }
}