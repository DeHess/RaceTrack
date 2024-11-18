package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.BresenhamAlgorithm;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.game.PositionVector.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The PathFollowerMoveStrategy class determines the next move based on a file containing points on a path.
 */
public class PathFollowerMoveStrategy implements MoveStrategy {
  List<Direction> directions;
  List<Direction> speedyDirections;
  ListIterator<Direction> directionListIterator;

  /**
   * Creates a PathFollower strategy with a File containing positions and the start position
   *
   * @param positionList File which contains a list of positions
   * @param start        {@link PositionVector} at which the car starts
   * @throws FileNotFoundException If the file is not valid
   */
  public PathFollowerMoveStrategy(File positionList, PositionVector start) throws FileNotFoundException {
    List<PositionVector> path = parseFile(positionList);
    directions = setup(path, start);
    speedyDirections = optimizeAcceleration(directions);
    directionListIterator = speedyDirections.listIterator();
  }

  /**
   * Creates a PathFollower strategy with a list of positions and the start position
   *
   * @param positionVectors List of {@link PositionVector} containing the targeted positions
   * @param start           {@link PositionVector} at which the car starts
   */
  public PathFollowerMoveStrategy(List<PositionVector> positionVectors, PositionVector start) {
    directions = setup(positionVectors, start);
    speedyDirections = optimizeAcceleration(directions);
    directionListIterator = speedyDirections.listIterator();
  }

  /**
   * File parser for {@link PositionVector} coordinates
   *
   * @param positionList File to be parsed
   * @return List of {@link PositionVector}
   * @throws FileNotFoundException    If file does not exist
   * @throws IllegalArgumentException If file contains invalid entries
   */
  private List<PositionVector> parseFile(File positionList) throws FileNotFoundException, IllegalArgumentException {
    List<PositionVector> positionVectors = new ArrayList<>();
    try (Scanner scanner = new Scanner(positionList)) {
      Pattern pattern = Pattern.compile("\\(X:(\\d+), Y:(\\d+)\\)");
      while (scanner.hasNextLine()) {
        Matcher matcher = pattern.matcher(scanner.nextLine());
        if (matcher.matches()) {
          positionVectors.add(new PositionVector(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
        } else {
          throw new IllegalArgumentException();
        }
      }
    }
    return positionVectors;
  }

  /**
   * Calculates the directions to drive the car to the given points with optimised acceleration and braking.
   * Car accelerates more than 1 if it can break in time
   * <p>
   * The method itself calculates a path to the next targeted position with the {@link BresenhamAlgorithm}
   * Each point to this target is steered to with a max speed of 1
   * The resulting list of directions is then optimised using the applySpeed method
   *
   * @param path          List of positions containing every targeted position. Contains the finish position but not the start position.
   * @param startPosition Position at which the car starts the race.
   * @return List of Directions containing the calculated direction sequence
   */
  private List<Direction> setup(List<PositionVector> path, PositionVector startPosition) {
    List<Direction> moves = new ArrayList<>();
    PositionVector velocity = new PositionVector(0, 0);
    PositionVector position = startPosition;

    for (PositionVector pathPoint : path) {
      BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
      List<PositionVector> positionVectorList = bresenhamAlgorithm.calculatePath(position, pathPoint);
      positionVectorList.remove(0);

      for (PositionVector pos : positionVectorList) {
        PositionVector way = PositionVector.subtract(pos, position);

        if (velocity.getLength() == 0) {
          velocity.add(way);
          moves.add(PositionVector.Direction.fromPositionVector(velocity));
        } else if (!way.equals(velocity)) {

          PositionVector diff = PositionVector.subtract(way, velocity);
          velocity.add(diff);

          moves.add(PositionVector.Direction.fromPositionVector(diff));
        } else {
          moves.add(Direction.NONE);
        }

        position = pos;

        if (position.equals(pathPoint)) {
          PositionVector oppositeVelocity = new PositionVector();
          oppositeVelocity.setX(-velocity.getX());
          oppositeVelocity.setY(-velocity.getY());
          velocity.add(oppositeVelocity);

          moves.add(PositionVector.Direction.fromPositionVector(oppositeVelocity));
        }

      }

      position = pathPoint;
    }

    return moves;
  }

  /**
   * Optimises a sequence of movements to allow acceleration greater than 1
   *
   * @param directions List of {@link Direction} to be optimized
   * @return optimized list of {@link Direction}
   */
  private List<Direction> optimizeAcceleration(List<Direction> directions) {
    List<Direction> fastDirections = new ArrayList<>();

    for (int i = 0; i < directions.size(); i++) {
      if (directions.get(i) == Direction.NONE) {
        Direction dir = directions.get(i - 1);
        Direction oppDir = Direction.getOpposite(dir);
        int identicalDirectionDistance = 0;
        for (int y = i; y < directions.size(); y++) {
          if (directions.get(y) == Direction.NONE) {
            identicalDirectionDistance++;
          } else {
            break;
          }
        }
        if (maxSpeed(identicalDirectionDistance) >= 2) {
          int maxSpeed = maxSpeed(identicalDirectionDistance);
          for (int u = 1; u < maxSpeed; u++) {
            fastDirections.add(dir);
            i++;
          }
          for (int u = 1; u < maxSpeed; u++) {
            fastDirections.add(oppDir);
            i++;
          }
          i = (i + maxSpeed + maxSpeed - 3);
        } else {
          fastDirections.add(directions.get(i));
        }

      } else {
        fastDirections.add(directions.get(i));
      }
    }

    return fastDirections;
  }

  /**
   * Calculates the maximum speed a car can reach while still being able to brake before the target
   *
   * @param distance int length of the distance
   * @return int speed for the distance
   */
  public static int maxSpeed(int distance) {
    int prevSpeed = 0;
    int speed = 0;
    int length = 1;
    while (length <= distance) {
      prevSpeed = speed;
      speed++;
      length = accelerationDistance(speed) * 2 + speed;
    }
    return prevSpeed;
  }

  /**
   * Calculates the distance traveled to accelerate to a given speed
   *
   * @param speed int target speed
   * @return int distance traveled
   */
  public static int accelerationDistance(double speed) {
    int x = (int) Math.round(speed);
    int d = 0;
    for (int i = x - 1; i >= 1; i--) {
      d += i;
    }
    return d;
  }

  @Override
  public Direction nextMove() {
    if (directionListIterator.hasNext()) return directionListIterator.next();
    throw new IllegalArgumentException();
  }
}
