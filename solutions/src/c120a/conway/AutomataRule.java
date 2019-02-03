package c120a.conway;

public interface AutomataRule {

  /**
   * Precondition: board[x][y] will never be at one of the edges of the board, i.e. we can always
   * access all of its 8 neighbours without having to worry about ArrayIndexOutOfBounds.
   */
  boolean isCellAliveNextStep(boolean[][] board, int x, int y);
}
