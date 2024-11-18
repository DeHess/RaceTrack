package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.game.BresenhamAlgorithm;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**The Pathfinder class finds a set of moves to find a path towards the finish line. There must be
 * a way for the car to find its way to the finish line. */
public class PathFinderStrategy implements MoveStrategy {

  private int[][] distanceGrid;
  int width;
  int height;
  PositionVector velocity = new PositionVector(0, 0);
  private ConfigSpecification.SpaceType[][] grid;
  PositionVector location;
  BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
  final int WALL = Integer.MAX_VALUE;
  final int FINISH_LINE = Integer.MAX_VALUE - 1;
  final int END_NODE = 1;

  List<PositionVector.Direction> moves = new ArrayList<>();
  private int moveCounter = 0;

  /**Once the object is created, a set of moves is ready to be called with nextMove()
   * The strategy requires the grid from track to determine the distance to the finish line for
   * each point in the grid.
   */
  public PathFinderStrategy(ConfigSpecification.SpaceType[][] grid, PositionVector carLocation) {
    this.grid = grid;
    this.location = carLocation;
    width = grid.length;
    height = grid[0].length;
    distanceGrid = new int[width][height];
    blockFinishLine(grid);
    assignEndNodes(grid);
    assignWalls(grid);
    fillInDistancesToFinishLine();
    calculateRoute(carLocation, velocity);
  }

  /**
   * Calls the next move from the moves (list) and increments
   * moveCounter, so that the next method call will return
   * the next move in the sequence.
   *
   * @return next move from the move list
   * */
  @Override
  public PositionVector.Direction nextMove() {
    return moves.get(moveCounter++);
  }

  /**
   * Creates a string representation of the distanceGrid
   * The distanceGrid shows the distances from the finish line
   * for every point in the grid of the track.
   *
   * @return the String representation
   * */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        appendDistanceGrid(stringBuilder, y, x);
        stringBuilder.append("|");
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }


  private void appendDistanceGrid(StringBuilder stringBuilder, int y, int x) {
    if (distanceGrid[x][y] > 999) {
      stringBuilder.append("  ");
    } else {
      stringBuilder.append(String.format("%02d", distanceGrid[x][y]));
    }
  }


  public int getDistanceGrid(int x, int y) {
    return distanceGrid[x][y];
  }

  /**
   * The finish line is blocked in the distanceGrid, so that the algorithm
   * does not go over the finish line the wrong way if the finish line is right
   * behind the car starting location on a circular map.
   *
   * @param grid from the track
   * */
  private void blockFinishLine(ConfigSpecification.SpaceType[][] grid) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        switch (grid[x][y]) {
          case FINISH_UP:
          case FINISH_DOWN:
          case FINISH_LEFT:
          case FINISH_RIGHT:
            distanceGrid[x][y] = FINISH_LINE;
        }
      }
    }
  }

  /**
   * The last points before the finish line are assigned as end nodes
   * with the level 1 (algorithm will later seek to go here)
   * */
  private void assignEndNodes(ConfigSpecification.SpaceType[][] grid) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        switch (grid[x][y]) {
          case FINISH_UP -> distanceGrid[x][y + 1] = END_NODE;
          case FINISH_DOWN -> distanceGrid[x][y - 1] = END_NODE;
          case FINISH_LEFT -> distanceGrid[x + 1][y] = END_NODE;
          case FINISH_RIGHT -> distanceGrid[x - 1][y] = END_NODE;
        }
      }
    }
  }

  /**
   * The walls in the track grid are assigned as Int_max_values (WALL) in the distanceGrid
   */
  private void assignWalls(ConfigSpecification.SpaceType[][] grid) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (grid[x][y] == ConfigSpecification.SpaceType.WALL) {
          distanceGrid[x][y] = WALL;
        }
      }
    }
  }

  /**
   * The distances to the finish lines are filled in step by step.
   * method starts from end nodes and stops when it finds no more zero values
   * to change.
   * */
  private void fillInDistancesToFinishLine() {
    int level = END_NODE;
    while (fillLevel(++level)) ;
  }


  /**
   * This method fills the moves (list)
   *
   * @param location of car
   * @param velocity of car
   *
   * @return first best move available
   * @return null if there is no move available that does not crash
   */
  private PositionVector.Direction calculateRoute(PositionVector location, PositionVector velocity) {
    List<Alternative> alternatives = new ArrayList<>();

    PositionVector neutralPosition = PositionVector.add(location, velocity);

    PositionVector.Direction move = getNextMove(location, velocity, alternatives, neutralPosition);
    if (move != null) return move;

    boolean willCrash = alternatives.isEmpty();
    if (willCrash) return null;
    alternatives.sort(Comparator.comparingInt(a -> a.distance));

    for (Alternative alternative : alternatives) {
      PositionVector.Direction direction = this.calculateRoute(alternative.nextPosition, alternative.velocity);
      if (direction != null) {
        moves.add(0, alternative.direction);
        return alternative.direction;
      }

    }

    return null;
  }

  /**
   * This method loops over all nine direction options,
   * and checks if the car will crash, go out of grid
   * or will win when considering the current location
   * and velocity of the car.
   *
   * @return null if all alternatives crash or go out of grid
   * @return best move, or winning move
   * */
  private PositionVector.Direction getNextMove(PositionVector location, PositionVector velocity, List<Alternative> alternatives, PositionVector neutralPosition) {
    PositionVector.Direction[] allMoves = PositionVector.Direction.values();
    for (PositionVector.Direction move : allMoves) {
      PositionVector nextPosition = PositionVector.add(neutralPosition, move.vector);
      PositionVector nextVelocity = PositionVector.add(velocity, move.vector);
      if (!isInGrid(nextPosition)) continue;
      if (willCrash(location, nextPosition)) continue;
      if (willFinish(location, nextPosition)) {
        moves.add(0, move);
        return move;
      }
      alternatives.add(new Alternative(
              move,
              nextPosition,
              nextVelocity,
              distanceGrid[nextPosition.getX()][nextPosition.getY()]
      ));
    }
    return null;
  }

  /**This method fills in the specified level (distance to finish line)*/
  private boolean fillLevel(int level) {
    boolean levelExists = false;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (distanceGrid[x][y] == 0 && hasNeighbouringNode(x, y, level - 1)) {
          distanceGrid[x][y] = level;
          levelExists = true;
        }
      }
    }
    return levelExists;
  }

  /**This method checks if the neighbouring distances have been filled out already with the specified level
   * @param x position
   * @param y position
   * @param level specified level (distance)
   * @return true if has neighbouring node
   * */
  private boolean hasNeighbouringNode(int x, int y, int level) {
    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {
        if (i == x && j == y) continue;
        if (!isInGrid(i, j)) continue;
        if (distanceGrid[i][j] == level) return true;
      }
    }
    return false;
  }

  private boolean isInGrid(int x, int y) {
    return !(x < 0 || y < 0 || x >= width || y >= height);
  }

  private boolean isInGrid(PositionVector positionVector) {
    return isInGrid(positionVector.getX(), positionVector.getY());
  }

  /**This method checks whether there is a finish line in line with a start and end vector
   * @param startPosition start vector
   * @param endPosition end vector
   * @return true if there is finish line between the two points
   * */
  private boolean willFinish(PositionVector startPosition, PositionVector endPosition) {
    List<PositionVector> wayPoints = bresenhamAlgorithm.calculatePath(startPosition, endPosition);
    for (PositionVector wayPoint : wayPoints) {
      if (distanceGrid[wayPoint.getX()][wayPoint.getY()] == FINISH_LINE) {
        switch (grid[wayPoint.getX()][wayPoint.getY()]) {

          case FINISH_UP -> {
            return endPosition.getY() < startPosition.getY();
          }

          case FINISH_DOWN -> {
            return endPosition.getY() > startPosition.getY();
          }

          case FINISH_LEFT -> {
            return endPosition.getX() < startPosition.getX();
          }

          case FINISH_RIGHT -> {
            return endPosition.getX() > startPosition.getX();
          }

        }
      }
    }
    return false;
  }

  /**
   * method that will check if there is a wall between two start end end vectors
   * does not check for cars!
   * @param startPosition start vector
   * @param endPosition end vector
   * @return if will crash with wall
   * */
  private boolean willCrash(PositionVector startPosition, PositionVector endPosition) {
    List<PositionVector> wayPoints = bresenhamAlgorithm.calculatePath(startPosition, endPosition);
    for (PositionVector wayPoint : wayPoints) {
      if (distanceGrid[wayPoint.getX()][wayPoint.getY()] == WALL) {
        return true;
      }
    }
    return false;
  }

  /**
   * An Alternative object holds a direction value,
   * a Positionvector describing its next location
   * a Positionvector describing the velocity
   * a distance int which holds the distance to the finish line
   * */
  class Alternative {

    private PositionVector.Direction direction;
    private PositionVector nextPosition;
    private PositionVector velocity;
    private int distance;

    public Alternative(PositionVector.Direction direction, PositionVector nextPosition, PositionVector velocity, int distance) {
      this.direction = direction;
      this.nextPosition = nextPosition;
      this.velocity = velocity;
      this.distance = distance;

    }

    public PositionVector.Direction getDirection() {
      return direction;
    }

    public int getDistance() {
      return distance;
    }

    public PositionVector getNextPosition() {
      return nextPosition;
    }

    public PositionVector getVelocity() {
      return velocity;
    }
  }

}
