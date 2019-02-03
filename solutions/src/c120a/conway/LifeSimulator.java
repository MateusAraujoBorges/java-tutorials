package c120a.conway;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Possible extensions:
 *
 * - This implementation assumes that cells outside of the board are always dead. This behaviour may
 * lead to incorrect results once a pattern touches the edges of the board. A solution to this
 * problem is to "wrap" the board, i.e. the cells on the first row/column are neighbours with the
 * ones in the last row/column.
 *
 * - Stop the simulation once it has 'converged', i.e. we have visited a previously seen board
 * state.
 *
 * - More ideas can be found on the wikipedia page: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
 * Have fun!
 */

public class LifeSimulator {

  private final boolean[][] board;
  private boolean printAllStates = false;


  public LifeSimulator(int boardLength, int boardHeight) {
    // we add two empty (i.e. all false) columns/rows at the edges of the board
    // to simplify the implementation of the game rules (i.e. no edge cases, literally)
    this.board = new boolean[boardLength + 2][boardHeight + 2];
  }

  /**
   * Useful for loading test scenarios
   *
   * Precondition: the rows/columns at the edge of the board are filled with 'false'.
   */

  public LifeSimulator(boolean[][] board) {
    validateBoard(board);
    this.board = board;
  }

  public void simulate(final int nIterations, int nThreads, AutomataRule rule) {
    final int height = board.length;
    final int width = board[0].length;

    boolean[][] tmpBoard = new boolean[height][width];
    Thread[] workers = new Thread[nThreads];
    final int[] currentIteration = {0}; //must be a final reference due to the lambda function

    if (printAllStates) {
      printBoard(board);
    }

    // We must ensure that all computations made by each thread will
    // be visible after each iteration. The CyclicBarrier class
    // can be used to create a "checkpoint" that all threads must
    // reach before they can perform another action.

    CyclicBarrier barrier = new CyclicBarrier(nThreads, () -> {
      // this will be executed once all threads reach the checkpoint,
      // but before the threads are released.

      // copy the result & clean the temporary board
      Arrays.fill(tmpBoard[0], false);
      for (int i = 1; i < board.length - 1; i++) { // skip the edges
        System.arraycopy(tmpBoard[i], 1, board[i], 1, width - 2);
        Arrays.fill(tmpBoard[i], false);
      }
      Arrays.fill(tmpBoard[board.length - 1], false);

      if (printAllStates) {
        System.out.println("Iteration " + currentIteration[0] + " is done:");
        printBoard(board);
      }

      currentIteration[0]++;
    });

    //Start the worker threads
    for (int i = 0; i < nThreads; i++) {
      final Interval2D slice = computeBoardSlice(i, nThreads, height, width);
      final int workerId = i;
      // System.out.println("[slice] " + workerId + " -> " + slice);
      workers[i] = new Thread(() -> {
        for (int iteration = 0; iteration < nIterations; iteration++) {
          int currentX = slice.xLo;
          int currentY = slice.yLo;

          do {
            if (currentY > 0 && currentY < width - 1) { //skip left/right edges
              tmpBoard[currentX][currentY] = rule.isCellAliveNextStep(board, currentX, currentY);
            }

            currentY++;
            if (currentY >= width - 1) {
              currentY = 0;
              currentX++;
            }
          } while (currentX < slice.xHi || (currentX == slice.xHi && currentY <= slice.yHi));

          // wait until all other threads are done before starting the next iteration.
          try {
            barrier.await();
          } catch (InterruptedException e) {
            System.err.println("Worker " + workerId + " was interrupted - exiting...");
            e.printStackTrace();
            System.exit(1);
          } catch (BrokenBarrierException e) {
            System.err
                .println("Worker " + workerId + " tried to enter a broken barrier - exiting...");
            e.printStackTrace();
            System.exit(2);
          }
        }
      });
      workers[i].start();
    }

    // Wait until all threads are done.
    for (Thread worker : workers) {
      try {
        worker.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void printBoard(boolean[][] board) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      boolean[] row = board[i];
      if (i == 0 || i == board.length - 1) {
        for (int j = 0; j < row.length; j++) {
          sb.append('-');
        }
      } else {
        sb.append('|');
        for (int j = 1; j < row.length - 1; j++) {
          sb.append(row[j] ? 'X' : ' ');
        }
        sb.append('|');
      }
      sb.append('\n');
    }
    System.out.println(sb.toString());
  }


  private Interval2D computeBoardSlice(int threadPosition, int nThreads, int height,
      int width) {
    assert threadPosition >= 0 && threadPosition < nThreads;

    // first and bottom rows are ignored; empty cells at the leftmost/rightmost columns will
    // be handled in the worker threads.
    long nCells = (height - 2) * width;
    if (nThreads > nCells) {
      throw new RuntimeException("The board is too small (or you have too many threads)");
    }

    long cellsPerThread = nCells / nThreads;
    long startCell = (threadPosition * cellsPerThread) + width;
    long endCell = threadPosition == nThreads - 1 ? //leave the remaining cells to the last thread
        nCells - 1 + width : ((threadPosition + 1) * cellsPerThread) - 1 + width;

    Interval2D slice = Interval2D.from1DCoordinates(startCell, endCell, width);
    return slice;
  }

  private void validateBoard(boolean[][] board) {
    for (int i = 0; i < board.length; i++) {
      boolean[] row = board[i];
      if (i == 0 || i == board.length - 1) {
        for (int j = 0; j < row.length; j++) {
          if (row[j]) {
            throw new RuntimeException(
                "Error loading board: non-empty edge column/row (i=" + i + " j=" + j);
          }
        }
      } else if (row[0] || row[row.length - 1]) {
        throw new RuntimeException(
            "Error loading board: non-empty edge column/row (i=" + i + " j=[0," + (row.length - 1)
                + "]");
      }
    }
  }

  public boolean isPrintAllStates() {
    return printAllStates;
  }

  public void setPrintAllStates(boolean printAllStates) {
    this.printAllStates = printAllStates;
  }

  public boolean[][] getBoard() {
    return board;
  }

  static class Interval2D {

    final int xLo;
    final int yLo;
    final int xHi;
    final int yHi;

    Interval2D(int xLo, int yLo, int xHi, int yHi) {
      if (xHi < xLo || (xHi == xLo && yHi < yLo)) {
        throw new RuntimeException(
            "Invalid interval2D: [(" + xLo + "," + yLo + "),(" + xHi + "," + yHi + ")]");
      }
      this.xLo = xLo;
      this.yLo = yLo;
      this.xHi = xHi;
      this.yHi = yHi;
    }

    public static Interval2D from1DCoordinates(long start, long end, int width) {
      int xLo = (int) (start / width);
      int yLo = (int) (start % width);
      int xHi = (int) (end / width);
      int yHi = (int) (end % width);
      return new Interval2D(xLo, yLo, xHi, yHi);
    }

    @Override
    public String toString() {
      return "[(" + xLo + "," + yLo + "),(" + xHi + "," + yHi + ")]";
    }
  }
}
