package ch.zhaw.pm2.racetrack.given;

import java.io.File;

/**
 * This interface specifies stuff we use to test Racetrack for grading. It shall not be altered! <br/>
 * It defines how the Game can be configured.
 */
public interface ConfigSpecification {
  int MAX_CARS = 9;

  File getMoveDirectory();

  void setMoveDirectory(File moveDirectory);

  File getFollowerDirectory();

  void setFollowerDirectory(File followerDirectory);

  File getTrackDirectory();

  void setTrackDirectory(File trackDirectory);

  /**
   * Possible Move Strategies selected by the Console to configure the Cars. (This shall not be altered!)
   */
  enum StrategyType {
    DO_NOT_MOVE, USER, MOVE_LIST, PATH_FOLLOWER, PATH_FINDER
  }

  /**
   * Possible space types of the grid. (This shall not be altered!)
   * The char value is used to parse from the track file and represents
   * the space type in the text representation created by toString().
   */
  enum SpaceType {
    WALL('#'),
    TRACK(' '),
    FINISH_UP('^'),
    FINISH_DOWN('v'),
    FINISH_LEFT('<'),
    FINISH_RIGHT('>');


    public final char value;

    SpaceType(final char c) {
      value = c;

    }


    public char getValue() {
      return value;
    }

    public static SpaceType fromChar(char input) {
      for (SpaceType currentField : values()) {
        if (currentField.value == input) {
          return currentField;
        }
      }
      return TRACK;
    }

  }
}
