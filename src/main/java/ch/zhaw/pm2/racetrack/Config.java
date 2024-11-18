package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import java.io.File;
import java.util.Objects;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;

/**
 * Class which expands the interface ConfigSpecification for global
 * definitions of the game.
 */
public class Config implements ConfigSpecification {

  public static final int MIN_CARS = 2;
  private File trackDirectory = new File("tracks");
  private File moveDirectory = new File("moves");
  private File followerDirectory = new File("follower");

  /**
   * Method to get the possible move directions.
   *
   * @return possible moves.
   */
  public File getMoveDirectory() {
    return moveDirectory;
  }

  /**
   * Method to set the possible moves directions.
   *
   * @param moveDirectory possible moves.
   */
  public void setMoveDirectory(File moveDirectory) {
    Objects.requireNonNull(moveDirectory);
    this.moveDirectory = moveDirectory;
  }

  /**
   * Method to get the follower direction.
   *
   * @return followerDirectory Follower direction.
   */
  public File getFollowerDirectory() {
    return followerDirectory;
  }

  /**
   * Method to set the follower direction.
   *
   * @param followerDirectory Follower direction.
   */
  public void setFollowerDirectory(File followerDirectory) {
    Objects.requireNonNull(followerDirectory);
    this.followerDirectory = followerDirectory;
  }

  /**
   * Method to get the direction of the track.
   *
   * @return Direction of the track.
   */
  public File getTrackDirectory() {
    return trackDirectory;
  }

  /**
   * Method to set the direction of the track.
   *
   * @param trackDirectory direction of the track.
   */
  public void setTrackDirectory(File trackDirectory) {
    Objects.requireNonNull(trackDirectory);
    this.trackDirectory = trackDirectory;
  }

  /**
   * Method to check if the given character is a
   * finish line character.
   *
   * @param character specific character from the track.
   * @return valid character for finish line.
   */
  public boolean isFinishLine(SpaceType character) {
    return character == FINISH_UP ||
            character == FINISH_DOWN ||
            character == FINISH_LEFT ||
            character == FINISH_RIGHT;
  }
}

