package c120a.football;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Runner {

  private static final int N_FANS = 10;
  private static final int N_EDITORS = 4;
  private static final long DURATION_NS = TimeUnit.NANOSECONDS.convert(30,TimeUnit.SECONDS);
  private static final long WAKEUP_INTERVAL_SEC = 3;

  private static final Team[] teams = {
      new Team("Arsenal"),
      new Team("Bournemouth"),
      new Team("Brighton & Hove Albion"),
      new Team("Burnley"),
      new Team("Cardiff City"),
      new Team("Chelsea "),
      new Team("Crystal Palace"),
      new Team("Everton "),
      new Team("Fulham "),
      new Team("Huddersfield Town"),
      new Team("Leicester City"),
      new Team("Liverpool"),
      new Team("Manchester City"),
      new Team("Manchester United"),
      new Team("Newcastle United "),
      new Team("Southampton "),
      new Team("Tottenham Hotspur"),
      new Team("Watford"),
      new Team("West Ham United"),
      new Team("Wolverhampton Wanderers"),
  };

  public static void main(String[] args) {
    FootballEditor[] editors = new FootballEditor[N_EDITORS];
    FootballFan[] fans = new FootballFan[N_FANS];
    League premier = new League(teams);

    // run system for the specified time interval
    long start = System.nanoTime();

    for (int i = 0; i < N_FANS; i++) {
      fans[i] = new FootballFan(premier);
      fans[i].start();
      System.out.println("Started fan n: " + i);
    }

    for (int i = 0; i < N_EDITORS; i++) {
      editors[i] = new FootballEditor(premier);
      editors[i].start();
      System.out.println("Started editor n: " + i);
    }

    while (System.nanoTime() - start < DURATION_NS) {
      try {
        TimeUnit.SECONDS.sleep(WAKEUP_INTERVAL_SEC);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    //Stop all threads
    for (FootballEditor editor : editors) {
      editor.pleaseStop();
    }
    for (FootballFan fan : fans) {
      fan.pleaseStop();
    }

    for (FootballEditor editor : editors) {
      try {
        editor.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (FootballFan fan : fans) {
      try {
        fan.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Print final results - grab the lock to ensure changes will be visible to this thread
    premier.getRwlock().readLock().lock();
    premier.getRwlock().readLock().unlock();

    System.out.println("-- Parallel rankings --");
    Team[] parallelRanking = premier.getRankings();
    for (Team team : parallelRanking) {
      System.out.println(team);
    }

    Team[] sequentialRanking = processMatchesSequentially(premier.getMatches());
    System.out.println("-- Sequential rankings --");
    for (Team team : sequentialRanking) {
      System.out.println(team);
    }

    for (int i = 0; i < sequentialRanking.length; i++) {
      if (!parallelRanking[i].equals(sequentialRanking[i])) {
        System.out.println(" >> RANKING MISMATCH: " + i);
        System.out.println(" >>      PARALLEL: " + parallelRanking[i]);
        System.out.println(" >>    SEQUENTIAL: " + sequentialRanking[i]);
      }
    }
  }

  // Helper method to simulate the final ranking for a sequence of matches
  public static Team[] processMatchesSequentially(List<Match> matches) {
    // create "clones" of the teams in the matches so we can reuse our algorithms
    Map<Team, Team> teamClones = new HashMap<>();

    for (Match match : matches) {
      Team originalHome = match.getHome();
      Team originalAway = match.getAway();

      if (!teamClones.containsKey(originalAway)) {
        Team clone = new Team(originalAway.getName());
        teamClones.put(originalAway, clone);
      }
      if (!teamClones.containsKey(originalHome)) {
        Team clone = new Team(originalHome.getName());
        teamClones.put(originalHome, clone);
      }

      Team home = teamClones.get(originalHome);
      Team away = teamClones.get(originalAway);

      Match.updateTeams(match, home, away);
    }

    Team[] rankedClones = teamClones.keySet().toArray(new Team[]{});
    Arrays.sort(rankedClones);
    return rankedClones;
  }
}
