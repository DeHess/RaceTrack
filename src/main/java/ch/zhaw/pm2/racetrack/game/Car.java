package ch.zhaw.pm2.racetrack.game;

import ch.zhaw.pm2.racetrack.given.CarSpecification;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

/**
 * Class representing a car on the racetrack.
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.
 * Each car has an identifier character which represents the car on the race track board.
 * Also keeps the state, if the car is crashed (not active anymore). The state can not be changed back to uncrashed.
 * The velocity is changed by providing an acelleration vector.
 * The car is able to calculate the endpoint of its next position and on request moves to it.
 */

public class Car implements CarSpecification {

  /**
   * Car identifier used to represent the car on the track
   */
  private char id;

  /**
   * Current position of the car on the track grid using a {@link PositionVector}
   */
  private PositionVector position;

  /**
   * Current velocity of the car using a {@link PositionVector}
   */
  private PositionVector velocity = new PositionVector(0, 0);

  /**
   * Indicator if the car has crashed
   */
  private boolean crashed = false;


  /**
   * Current move strategy
   */
  private MoveStrategy moveStrategy;

  /**
   * Counts how many rounds the Car has made.
   */
  private int roundCount = 0;

  /**
   * Constructor for class Car
   *
   * @param id       unique Car identification
   * @param position initial position of the Car
   */
  public Car(char id, PositionVector position) {
    this.id = id;
    this.position = position;
  }

  /**
   * Constructor for the Car instance.
   *
   * @param id       the id for the Car.
   * @param position the starting position.
   * @param velocity the velocity of the Car.
   */
  public Car(char id, PositionVector position, PositionVector velocity) {
    this.id = id;
    this.velocity = velocity;
    this.position = position;

  }

  /**
   * Gets the position of the Car on the racetrack.
   *
   * @return A {@link PositionVector} containing the position of the Car.
   */
  public PositionVector getPosition() {
    return position;
  }

  /**
   * Set this Car position directly, regardless of current position and velocity.
   * This should only be used by the game controller in rare cases to set the crash or winning position.
   * The next position is normaly automatically calculated and set in the {@link #move()} method.
   *
   * @param position The new position to set the car directly to.
   */
  @Override
  public void setPosition(final PositionVector position) {
    this.position = position;
  }

  /**
   * Return the position that will apply after the next move at the current velocity.
   * Does not complete the move, so the current position remains unchanged.
   *
   * @return Expected position after the next move
   */
  @Override
  public PositionVector nextPosition() {
    return PositionVector.add(position, velocity);
  }

  /**
   * Add the specified amounts to this cars's velocity.
   * The only acceleration values allowed are -1, 0 or 1 in both axis
   * There are 9 possible acceleration vectors, which are defined in {@link PositionVector.Direction}.
   * Changes only velocity, not position.
   *
   * @param acceleration A Direction vector containing the amounts to add to the velocity in x and y dimension
   */
  @Override
  public void accelerate(PositionVector.Direction acceleration) {
    velocity.add(acceleration.vector);
  }

  /**
   * Update this Car's position based on its current velocity.
   */
  @Override
  public void move() {
    this.position = nextPosition();
  }

  /**
   * Mark this Car as being crashed.
   */
  @Override
  public void crash() {
    crashed = true;

    int xVelocity = velocity.getX();
    int yVelocity = velocity.getY();

    if (xVelocity > 0) {
      xVelocity = 1;
    }
    if (xVelocity < 0) {
      xVelocity = -1;
    }
    if (yVelocity > 0) {
      yVelocity = 1;
    }
    if (yVelocity < 0) {
      yVelocity = -1;
    }
  }

  /**
   * Returns whether this Car has been marked as crashed.
   *
   * @return Returns true if crash() has been called on this Car, false otherwise.
   */
  @Override
  public boolean isCrashed() throws UnsupportedOperationException {
    return crashed;
  }

  /**
   * Gets the ID of the Car.
   *
   * @return A char containing the id of the Car.
   */
  public char getId() {
    return id;
  }

  /**
   * Gets the velocity of the Car on the {@link Track}.
   *
   * @return A {@link PositionVector} containing the velocity of the Car.
   */
  public PositionVector getVelocity() {
    return velocity;
  }

  /**
   * changes the RoundCount.
   *
   * @param amount the amount the lapCount is changed by.
   */
  public void changeRoundCount(int amount) {
    roundCount += amount;
  }

  /**
   * @return roundCount, amount of rounds done by a Car.
   */
  public int getRoundCount() {
    return roundCount;
  }

  /**
   * Set move strategy
   *
   * @param moveStrategy The move strategy that you want to set
   */
  public void setMoveStrategy(MoveStrategy moveStrategy) {
    this.moveStrategy = moveStrategy;
  }

  /**
   * Get current move strategy
   *
   * @return MoveStrategy
   */
  public MoveStrategy getMoveStrategy() {
    return this.moveStrategy;
  }
}
