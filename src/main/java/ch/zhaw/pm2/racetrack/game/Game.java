package ch.zhaw.pm2.racetrack.game;


import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.exceptions.OutOfBoardException;
import ch.zhaw.pm2.racetrack.given.GameSpecification;

import java.util.List;

import static ch.zhaw.pm2.racetrack.game.PositionVector.Direction;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game implements GameSpecification {
  public static final int NO_WINNER = -1;
  /**
   * The actual SpaceType
   */
  private final Config config;
  /**
   * The index of the winner.
   */
  private int winner;
  /**
   * The index of the {@link Car} that the Game class is currently looking at.
   */
  private int currentCarIndex;
  /**
   * The {@link Track} that is being played.
   */
  private final BresenhamAlgorithm bresenham;

  private Track track;

  /**
   * Constructor for the Game class.
   */
  public Game() {
    bresenham = new BresenhamAlgorithm();
    this.winner = NO_WINNER;
    this.currentCarIndex = 0;
    this.track = null;
    config = new Config();
  }

  /**
   * Return the index of the current active car.
   * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
   *
   * @return The zero-based number of the current car
   */
  @Override
  public int getCurrentCarIndex() {
    return currentCarIndex;
  }

  /**
   * Gets the char id the of the currently active car.
   *
   * @return Char identifying the car
   */
  public char getCurrentCarId() {
    return track.getCarId(currentCarIndex);
  }

  public Track getTrack() {
    return track;
  }

  /**
   * Sets the Track in the Game.
   *
   * @param track The track that is used in the game.
   */
  public void setTrack(Track track) {
    this.track = track;
  }

  /**
   * Get the id of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A char containing the id of the car
   */
  @Override
  public char getCarId(int carIndex) {
    return track.getCarId(carIndex);
  }

  /**
   * Get the position of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A PositionVector containing the car's current position
   */
  @Override
  public PositionVector getCarPosition(int carIndex) {
    return track.getCarPos(carIndex);
  }

  /**
   * Get the velocity of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A PositionVector containing the car's current velocity
   */
  @Override
  public PositionVector getCarVelocity(int carIndex) {
    return track.getCarVelocity(carIndex);
  }

  /**
   * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
   *
   * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
   */
  @Override
  public int getWinner() {
    return winner;
  }

  /**
   * Execute the next turn for the current active car.
   * <p>This method changes the current car's velocity and checks on the path to the next position,
   * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
   * <p>The steps are as follows</p>
   * <ol>
   *   <li>Accelerate the current car</li>
   *   <li>Calculate the path from current (start) to next (end) position
   *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
   *   <li>Verify for each step what space type it hits:
   *      <ul>
   *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
   *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
   *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
   *      </ul>
   *   </li>
   *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
   *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
   *   <li>Otherwise move the car to the end position</li>
   * </ol>
   * <p>The calling method must check the winner state and decide how to go on. If the winner is different
   * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
   *
   * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
   *                     for this turn
   */
  @Override
  public void doCarTurn(Direction acceleration) {
    Car car = track.getCar(currentCarIndex);
    car.accelerate(acceleration);
    List<PositionVector> pathPositions = calculatePath(car.getPosition(), car.nextPosition());

    for (PositionVector pathPosition : pathPositions) {
      try {
        if (willCarCrash(currentCarIndex, pathPosition)) {
          crashCar(car, pathPosition);
          return;
        }
        if (config.isFinishLine(track.getSpaceType(pathPosition))) {
          setWinner(car, pathPosition);
          return;
        }
      } catch (OutOfBoardException fail) {
        car.crash();
        car.setPosition(pathPosition);
        return;
      }
    }
    car.move();
  }

  private void setWinner(Car car, PositionVector pathPosition) {
    if (calculateRoundCount(track.getSpaceType(pathPosition), car) > 0) {
      winner = currentCarIndex;
      car.setPosition(pathPosition);
    }
  }

  private void crashCar(Car car, PositionVector pathPosition) {
    car.crash();
    car.setPosition(pathPosition);
    if (track.uncrashedCarCount() < Config.MIN_CARS) {
      setLastUncrashedCarAsWinner();
    }
  }

  private int calculateRoundCount(Config.SpaceType finishType, Car car) {
    switch (finishType) {
      case FINISH_UP:
        if (car.getVelocity().getY() < 0) {
          addOneRound(car);
        } else if (car.getVelocity().getY() > 0) {
          subtractOneRound(car);
        }
        break;
      case FINISH_LEFT:
        if (car.getVelocity().getX() < 0) {
          addOneRound(car);
        } else if (car.getVelocity().getX() > 0) {
          subtractOneRound(car);
        }
        break;
      case FINISH_DOWN:
        if (car.getVelocity().getY() > 0) {
          addOneRound(car);
        } else if (car.getVelocity().getY() < 0) {
          subtractOneRound(car);
        }
        break;
      case FINISH_RIGHT:
        if (car.getVelocity().getX() > 0) {
          addOneRound(car);
        } else if (car.getVelocity().getX() < 0) {
          subtractOneRound(car);
        }
        break;
    }
    return car.getRoundCount();
  }

  private void addOneRound(Car car) {
    car.changeRoundCount(1);
  }

  private void subtractOneRound(Car car) {
    car.changeRoundCount(-1);
  }

  /**
   * Let the "Last Car Standing" win
   */
  private void setLastUncrashedCarAsWinner() {
    for (int i = 0; i < track.getCarCount(); i++) {
      if (!track.getCar(i).isCrashed()) {
        winner = i;
        return;
      }
    }
  }

  /**
   * Switches to the next car who is still in the game. Skips crashed cars.
   */
  @Override
  public void switchToNextActiveCar() {
    do {
      currentCarIndex = (currentCarIndex + 1) % track.getCarCount();
    } while (track.getCar(currentCarIndex).isCrashed() && track.uncrashedCarCount() != 0);
  }

  /**
   * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
   * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
   * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
   * Basic steps are
   * - Detect which axis of the distance vector is longer (faster movement)
   * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
   * Direction of the movement has to correctly considered
   *
   * @param startPosition Starting position as a PositionVector
   * @param endPosition   Ending position as a PositionVector
   * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
   */
  @Override
  public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
    return bresenham.calculatePath(startPosition, endPosition);
  }

  /**
   * Does indicate if a car would have a crash with a WALL space or another car at the given position.
   *
   * @param carIndex The zero-based carIndex number
   * @param position A PositionVector of the possible crash position
   * @return A boolean indicator if the car would crash with a WALL or another car.
   */
  public boolean willCarCrash(int carIndex, PositionVector position) {
    Config.SpaceType positionSpaceType;

    try {
      positionSpaceType = track.getSpaceType(position);
    } catch (OutOfBoardException e) {
      return true;
    }
    if (crashesWithSpaceType(carIndex, positionSpaceType)) return true;
    return track.hasDifferentCarAtPosition(position, track.getCar(carIndex));
  }

  private boolean crashesWithSpaceType(int carIndex, Config.SpaceType positionSpaceType) {
    switch (positionSpaceType) {
      case WALL -> {
        return true;
      }
      case FINISH_UP -> {
        return track.getCar(carIndex).getVelocity().getY() >= 0;
      }
      case FINISH_DOWN -> {
        return track.getCar(carIndex).getVelocity().getY() <= 0;
      }
      case FINISH_LEFT -> {
        return track.getCar(carIndex).getVelocity().getX() >= 0;
      }
      case FINISH_RIGHT -> {
        return track.getCar(carIndex).getVelocity().getX() <= 0;
      }
    }
    return false;
  }
}
