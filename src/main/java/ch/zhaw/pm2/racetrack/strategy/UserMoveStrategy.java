package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.game.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.io.UserInterface;

/**
 * Let the user decide the next move.
 */
public class UserMoveStrategy implements MoveStrategy {
  private final UserInterface ui;

  /**
   * Initializes the strategy.
   * The user-interface allows it to ask the user for the next move
   *
   * @param userInterface {@link UserInterface} to get an input from the user
   */
  public UserMoveStrategy(UserInterface userInterface) {
    ui = userInterface;
  }

  @Override
  public Direction nextMove() {
    return ui.getEnum(Direction.class, "Enter direction:");
  }
}
