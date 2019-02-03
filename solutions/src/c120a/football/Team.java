package c120a.football;

public class Team implements Comparable {

  private final String name;
  private int totalPoints;
  private int goalDifference;


  public Team(String name) {
    if (name == null) {
      throw new RuntimeException("Null name!");
    }
    this.name = name;
    this.totalPoints = 0;
    this.goalDifference = 0;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Team team = (Team) o;

    return name.equals(team.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  public int getTotalPoints() {
    return totalPoints;
  }

  public void updateTotalPoints(int matchPoints) {
    this.totalPoints += matchPoints;
  }

  public int getGoalDifference() {
    return goalDifference;
  }

  public void updateGoalDifference(int matchGoalDifference) {
    this.goalDifference += matchGoalDifference;
  }

  @Override
  public String toString() {
    return "Team{" +
        "name='" + name + '\'' +
        ", totalPoints=" + totalPoints +
        ", goalDifference=" + goalDifference +
        '}';
  }

  // order by points/goal difference/name (to enforce a total ordering)
  @Override
  public int compareTo(Object o) {
    if (this == o) {
      return 0;
    }
    if (o == null || getClass() != o.getClass()) {
      throw new RuntimeException("Not comparable");
    }

    Team team = (Team) o;

    if (this.getTotalPoints() > team.getTotalPoints()) {
      return -1;
    } else if (this.getTotalPoints() < team.getTotalPoints()) {
      return 1;
    }

    if (this.getGoalDifference() > team.getGoalDifference()) {
      return -1;
    } else if (this.getGoalDifference() < team.getGoalDifference()) {
      return 1;
    }

    return this.getName().compareTo(team.getName());
  }
}
