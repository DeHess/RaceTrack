package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.game.Track;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderStrategyTest {

  /**
   * tests if the distances from the finish line is calculated correctly
   */
  @Test
  public void calculates_distance_from_finish_line() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/easiest-track.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinderStrategy = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    assertEquals(5, pathFinderStrategy.getDistanceGrid(0, 0));
    assertEquals(4, pathFinderStrategy.getDistanceGrid(1, 0));
    assertEquals(3, pathFinderStrategy.getDistanceGrid(2, 0));
    assertEquals(2, pathFinderStrategy.getDistanceGrid(3, 0));
    assertEquals(1, pathFinderStrategy.getDistanceGrid(4, 0));
  }

  /**
   * prints out a visual depiction of the distances per point away from the finish line
   */
  @Test
  public void prints_distanceGrid() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/oval-clock-up.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinderStrategy = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    System.out.println(pathFinderStrategy);
  }

  /**
   * Tests whether the algorithm moves in the correct direction towards the finish line
   * when given a choice of movement
   */
  @Test
  public void finds_path_to_finish_on_straight_track_which_goes_both_ways() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/both-ways.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinder = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    assertEquals(PositionVector.Direction.RIGHT, pathFinder.nextMove());
  }

  /**
   * Tests whether the algorithm moves in the correct direction towards the finish line
   * when given only one way which doesn't crash
   */
  @Test
  public void finds_path_to_finish_on_straight_track_which_goes_only_right() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/one-way.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinder = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    assertEquals(PositionVector.Direction.RIGHT, pathFinder.nextMove());

  }

  /**
   * Tests whether the algorithm moves in the correct direction towards the finish line
   * when it only needs to take one step to finish the race
   */
  @Test
  public void finds_path_to_finish_on_baby_step_track() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/baby-step.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinder = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    assertEquals(PositionVector.Direction.RIGHT, pathFinder.nextMove());
  }

  /**
   * Tests whether the algorithm tries to cheat and move over the finish line
   * the wrong way.
   */
  @Test
  public void does_not_roll_backwards_over_finish_line() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/backtrack-forbidden.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinder = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));
    assertEquals(PositionVector.Direction.DOWN_LEFT, pathFinder.nextMove());

  }

  /**
   * Tests whether the algorithm finds a set of moves towards the finish lines that dont
   * crash on a very narrow map
   */
  @Test
  public void narrow() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/narrow.txt");
    Track track = new Track(file);
    PathFinderStrategy pathFinder = new PathFinderStrategy(track.getGrid(), track.getCarPos(0));

  }
}
