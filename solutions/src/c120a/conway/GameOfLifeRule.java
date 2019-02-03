package c120a.conway;

/**
 * See wikipedia for more details: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
 */

public class GameOfLifeRule implements AutomataRule {


  @Override
  public boolean isCellAliveNextStep(boolean[][] board, final int x, final int y) {
    int liveNeighbours = 0;
    boolean isAlive = board[x][y];

    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {
        if (i == x && j == y) {
          continue;
        }

        if (board[i][j]) {
          liveNeighbours++;
        }
      }
    }

    boolean result;
    if (isAlive) {
      result = (liveNeighbours == 2 || liveNeighbours == 3);
    } else {
      result = liveNeighbours == 3;
    }
//    if (isAlive || result) {
//      System.out.println("[debug] [" + x + "," + y + "] " + isAlive + " -> " + result);
//    }
    return result;
  }
}
