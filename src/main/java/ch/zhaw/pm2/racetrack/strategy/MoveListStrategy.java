package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.game.PositionVector.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Moves are read from a given list of moves
 */
public class MoveListStrategy implements MoveStrategy {
  private final Scanner scanner;

  /**
   * Creates a new MoveList strategy with the moves in a file
   *
   * @param moveList File containing moves
   * @throws FileNotFoundException if file does not exist
   */
  public MoveListStrategy(File moveList) throws FileNotFoundException {
    scanner = new Scanner(moveList);
  }

  @Override
  public Direction nextMove() {
    Direction nextDirection;
    if (scanner.hasNext()) {
      nextDirection = getDirection();
    } else {
      nextDirection = null;
    }
    return nextDirection;
  }

  /**
   * Gets the next direction from a scanner
   *
   * @return Direction
   */
  private Direction getDirection() {
    Direction nextDirection;
    try {
      nextDirection = Direction.valueOf(scanner.next());
    } catch (Exception e) {
      nextDirection = null;
    }
    return nextDirection;
  }
}
