package c120a.conway;

public class Runner {

  final static boolean x = true;
  final static boolean o = false;
  final static boolean[][] glider = new boolean[][]{
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, x, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, x, o, o, o, o, o, o},
      new boolean[]{o, x, x, x, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o}
  };
  final static boolean[][] pulsar = new boolean[][]{
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, x, x, x, o, o, o, x, x, x, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, x, x, x, o, o, o, x, x, x, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, x, x, x, o, o, o, x, x, x, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, x, o, o, o, o, x, o, x, o, o, o, o, x, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, x, x, x, o, o, o, x, x, x, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
      new boolean[]{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
  };

  public static void main(String[] args) {
    LifeSimulator simulator = new LifeSimulator(pulsar);
    simulator.setPrintAllStates(true);
    simulator.simulate(60, 4, new GameOfLifeRule());
  }
}
