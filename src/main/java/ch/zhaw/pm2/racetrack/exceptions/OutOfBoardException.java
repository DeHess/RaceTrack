package ch.zhaw.pm2.racetrack.exceptions;

import ch.zhaw.pm2.racetrack.game.Track;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

/**
 * An Exception to be used when a method wants to select a {@link ConfigSpecification.SpaceType} that's not on the board of a given {@link Track}.
 *
 * @author Lukas Wipf
 */
public class OutOfBoardException extends RuntimeException {
  public OutOfBoardException(String message) {
    super(message);
  }
}
