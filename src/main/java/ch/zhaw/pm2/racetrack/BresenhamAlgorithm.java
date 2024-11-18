package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.PositionVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a path between two points with the given Algorithm of Bresenham.
 */
public class BresenhamAlgorithm {
  /**
   * Returns all grid positions in the path between two positions, for use in determining the line of sight.
   * Determine the 'nodes/positions' on a grid using Bresenham's line algorithm.
   * Basic steps are
   * - Detect which axis of the distance vector is longer (faster movement).
   * - for each node on the 'faster' axis calculate the position on the 'slower' axis.
   * Direction of the movement has to be correctly considered.
   *
   * @param startPosition Starting position as a {@link PositionVector}.
   * @param endPosition   Ending position as a {@link PositionVector}.
   * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
   */
  public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
    List<PositionVector> path = new ArrayList<>();
    PositionVector difference = PositionVector.subtract(endPosition, startPosition);
    PositionVector distance = new PositionVector(Math.abs(difference.getX()), Math.abs(difference.getY()));
    PositionVector direction = new PositionVector(Integer.signum(difference.getX()), Integer.signum(difference.getY()));

    PositionVector parallelStep = new PositionVector();
    PositionVector diagonalStep;
    int distanceSlowAxis, distanceFastAxis;
    boolean isXAxisTheFastDirection = distance.getX() > distance.getY();
    if (isXAxisTheFastDirection) {
      parallelStep.setX(direction.getX());
      distanceSlowAxis = distance.getY();
      distanceFastAxis = distance.getX();
    } else {
      parallelStep.setY(direction.getY());
      distanceSlowAxis = distance.getX();
      distanceFastAxis = distance.getY();
    }
    diagonalStep = new PositionVector(direction);

    PositionVector currentStep = new PositionVector(startPosition);
    path.add(new PositionVector(currentStep));
    int error = distanceFastAxis / 2;

    for (int step = 0; step < distanceFastAxis; step++) {
      error -= distanceSlowAxis;
      if (error < 0) {
        error += distanceFastAxis;
        currentStep.add(diagonalStep);
      } else {
        currentStep.add(parallelStep);
      }
      path.add(new PositionVector(currentStep));
    }
    return path;
  }
}
