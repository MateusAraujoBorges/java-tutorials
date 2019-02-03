package c120a.football;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FootballFan extends Thread {

  private final int MAX_WAIT_MS = 30;

  public final League league;
  public final Random rng;
  private volatile boolean stop = false; // Extra question: why stop must be volatile?

  public FootballFan(League league) {
    this.league = league;
    this.rng = new Random();
  }

  @Override
  public void run() {
    int nTeams = league.getTeams().length;

    while (!stop) {
      try {
        // wait a bit between requests
        int sleepMs = rng.nextInt(MAX_WAIT_MS + 1);
        TimeUnit.MILLISECONDS.sleep(sleepMs);
        // Fans only read stuff, so we grab the read lock
        league.getRwlock().readLock().lock();

        //look up the position of a random team
        int rankingPosition = rng.nextInt(nTeams);
        Team team = league.getRankings()[rankingPosition];

        System.out.println(
            "Fan " + this.getId() + " saw that " + team.getName() + " was in position no. "
                + rankingPosition + " with " + team.getTotalPoints() + " points and " + team
                .getGoalDifference() + " goal difference!");

      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        league.getRwlock().readLock().unlock();
      }
    }
  }

  public void pleaseStop() {
    this.stop = true;
  }
}
