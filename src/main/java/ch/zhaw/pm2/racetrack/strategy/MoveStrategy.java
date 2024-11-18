package ch.zhaw.pm2.racetrack.strategy;

import static ch.zhaw.pm2.racetrack.game.PositionVector.Direction;

/**
 * Defines the strategy to decide the next move
 */
public interface MoveStrategy {
  Direction nextMove();
}
