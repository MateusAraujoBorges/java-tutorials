package c120a.football;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FootballEditor extends Thread {

  private final int MAX_WAIT_MS = 60;
  private final int MAX_GOALS = 7;

  public final League league;
  public final Random rng;
  private volatile boolean stop = false; // Extra question: why stop must be volatile?

  public FootballEditor(League league) {
    this.league = league;
    this.rng = new Random();
  }

  @Override
  public void run() {
    while (!stop) {
      // Select two distinct random teams and simulate a football match
      int nTeams = league.getTeams().length;
      int homeTeamIndex = rng.nextInt(nTeams);
      int awayTeamIndex = homeTeamIndex;

      while (awayTeamIndex == homeTeamIndex) {
        awayTeamIndex = rng.nextInt(nTeams);
      }

      Team home = league.getTeams()[homeTeamIndex];
      Team away = league.getTeams()[awayTeamIndex];

      int homeScore = rng.nextInt(MAX_GOALS + 1);
      int awayScore = rng.nextInt(MAX_GOALS + 1);

      Match match = new Match(home, away, homeScore, awayScore);

      try {
        // wait a bit between requests
        int sleepMs = rng.nextInt(MAX_WAIT_MS + 1);
        TimeUnit.MILLISECONDS.sleep(sleepMs);

        // Editors need to post the latest results, so we grab the write lock
        league.getRwlock().writeLock().lock();
        league.postMatch(match);
        System.out.println("Editor " + this.getId() + " posted  " + match);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        league.getRwlock().writeLock().unlock();
      }
    }
  }

  public void pleaseStop() {
    this.stop = true;
  }

}
