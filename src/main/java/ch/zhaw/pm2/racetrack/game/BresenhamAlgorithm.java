package ch.zhaw.pm2.racetrack.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a path between two points with the given Algorithm of Bresenham.
 *
 * @author Mike Diethelm
 */
public class BresenhamAlgorithm {
  public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
    List<PositionVector> path = new ArrayList<>();
    PositionVector difference = PositionVector.subtract(endPosition, startPosition);
    PositionVector distance = new PositionVector(Math.abs(difference.getX()), Math.abs(difference.getY()));
    PositionVector direction = new PositionVector(Integer.signum(difference.getX()), Integer.signum(difference.getY()));

    // Determine which axis is the fast direction and set parallel/diagonal step values
    PositionVector parallelStep = new PositionVector();
    PositionVector diagonalStep;
    int distanceSlowAxis, distanceFastAxis;
    boolean isXAxisTheFastDirection = distance.getX() > distance.getY();
    if (isXAxisTheFastDirection) {
      parallelStep.setX(direction.getX());
      distanceSlowAxis = distance.getY();
      distanceFastAxis = distance.getX();
    } else {
      // y-axis is the 'fast' direction
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



