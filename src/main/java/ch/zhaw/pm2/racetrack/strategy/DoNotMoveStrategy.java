package ch.zhaw.pm2.racetrack.strategy;

import static ch.zhaw.pm2.racetrack.game.PositionVector.Direction;

/**
 * Do not accelerate in any direction.
 */
public class DoNotMoveStrategy implements MoveStrategy {

  @Override
  public Direction nextMove() {
    return Direction.NONE;
  }
}
