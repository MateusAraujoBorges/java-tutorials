package c120a.football;

public class Match {

  private static final int POINTS_WINNER = 3;
  private static final int POINTS_TIE = 1;
  private static final int POINTS_LOSER = 0;

  private final Team home;
  private final Team away;
  private final int homeScore;
  private final int awayScore;


  public Match(Team home, Team away, int homeScore, int awayScore) {
    if (home == null
        || away == null
        || home.equals(away)
        || homeScore < 0
        || awayScore < 0) {
      throw new RuntimeException("Invalid Match details!");
    }
    this.home = home;
    this.away = away;
    this.homeScore = homeScore;
    this.awayScore = awayScore;
  }


  public void updateTeams(Match match) {
    updateTeams(match, match.getHome(), match.getAway());
  }

  /**
   * This might look convoluted at first - you can access home/away from the match object after all,
   * so why are we passing the teams as parameters? The reason is to help us validate the rankings
   * computed using a parallel algorithm with the precise answer computed sequentially.
   */

  public static void updateTeams(Match match, Team home, Team away) {
    int awayGD = match.getAwayScore() - match.getHomeScore();
    int homeGD = awayGD * -1;
    int awayPoints;
    int homePoints;
    if (awayGD > 0) {
      awayPoints = POINTS_WINNER;
      homePoints = POINTS_LOSER;
    } else if (awayGD == 0) {
      awayPoints = POINTS_TIE;
      homePoints = POINTS_TIE;
    } else {
      awayPoints = POINTS_LOSER;
      homePoints = POINTS_WINNER;
    }

    home.updateGoalDifference(homeGD);
    home.updateTotalPoints(homePoints);
    away.updateGoalDifference(awayGD);
    away.updateTotalPoints(awayPoints);
  }


  public Team getHome() {
    return home;
  }

  public Team getAway() {
    return away;
  }

  public int getHomeScore() {
    return homeScore;
  }

  public int getAwayScore() {
    return awayScore;
  }

  @Override
  public String toString() {
    return "Match{" +
        "home=" + home.getName() +
        ", away=" + away.getName() +
        ", homeScore=" + homeScore +
        ", awayScore=" + awayScore +
        '}';
  }

  public boolean isTie() {
    return getHomeScore() == getAwayScore();
  }
}
