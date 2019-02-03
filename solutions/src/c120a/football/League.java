package c120a.football;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class League {


  private final ReadWriteLock rwlock;

  // all teams
  private final Team[] teams;

  // same as before, but sorted by their points/goal difference/name (to enforce a total ordering)
  private final Team[] rankings;

  //for validation purposes only
  private final List<Match> matches;

  public League(Team[] teams) {
    this.teams = new Team[teams.length];
    this.rankings = new Team[teams.length];
    System.arraycopy(teams, 0, this.teams, 0, teams.length);
    System.arraycopy(teams, 0, this.rankings, 0, teams.length);
    this.matches = new ArrayList<>();
    this.rwlock = new ReentrantReadWriteLock();
  }

  public void postMatch(Match match) {
    matches.add(match);
    match.updateTeams(match);
    updateRanking();
  }


  private void updateRanking() {
    Arrays.sort(rankings);
  }


  public Team[] getTeams() {
    return teams;
  }

  public Team[] getRankings() {
    return rankings;
  }

  public List<Match> getMatches() {
    return matches;
  }

  public ReadWriteLock getRwlock() {
    return rwlock;
  }

}
