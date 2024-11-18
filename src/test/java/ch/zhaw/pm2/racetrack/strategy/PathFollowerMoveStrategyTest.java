package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.game.PositionVector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link PathFollowerMoveStrategy} class.
 * Types: Represent every possible braking behaviour. Tests if the most optimal acceleration and braking curve is calculated.
 * 1: x9-steps: Acceleration to max speed followed by braking
 * 2: x0-steps Acceleration to max speed followed by holding speed after braking
 * 3: x1-steps Acceleration to max speed followed by holding speed while braking
 * 4: x2-steps Acceleration to max speed followed by holding speed before braking
 */
class PathFollowerMoveStrategyTest {

  /**
   * 9-step movement on a single axis
   * Speed per step: 0,1,2,3,2,1,0
   */
  @Test
  void singleAxisMovementType1() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(10, 1));
    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 10-step movement on a single axis
   * Speed per step: 0,1,2,3,2,1,1,0
   */
  @Test
  void singleAxisMovementType2() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(11, 1));

    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 11-step movement on a single axis
   * Speed per step: 0,1,2,3,2,2,1,0
   */
  @Test
  void singleAxisMovementType3() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(12, 1));

    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 12-step movement on a single axis
   * Speed per step: 0,1,2,3,3,2,1,0
   */
  @Test
  void singleAxisMovementType4() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(13, 1));

    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 9-step 45 degree movement on two axis
   * Speed per step: 0,1,2,3,2,1,0
   */
  @Test
  void dualAxisMovementType1() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(10, 10));
    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 10-step 45 degree movement on two axis
   * Speed per step: 0,1,2,3,2,1,1,0
   */
  @Test
  void dualAxisMovementType2() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(11, 11));
    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 11-step 45 degree movement on two axis
   * Speed per step: 0,1,2,3,2,2,1,0
   */
  @Test
  void dualAxisMovementType3() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(12, 12));
    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * 12-step 45 degree movement on two axis
   * Speed per step: 0,1,2,3,3,2,1,0
   */
  @Test
  void dualAxisMovementType4() {
    List<PositionVector> positions = new ArrayList<>();
    PositionVector startPosition = new PositionVector(1, 1);
    positions.add(new PositionVector(13, 13));
    PathFollowerMoveStrategy pathFollowerMoveStrategy = new PathFollowerMoveStrategy(positions, startPosition);
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.DOWN_RIGHT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.NONE, pathFollowerMoveStrategy.nextMove());
    assertEquals(PositionVector.Direction.UP_LEFT, pathFollowerMoveStrategy.nextMove());

    assertThrows(IllegalArgumentException.class, pathFollowerMoveStrategy::nextMove);
  }

  /**
   * Tests if the maxSpeed method returns the correct speed for a given distance
   */
  @Test
  void testMaxSpeedMethod() {
    assertEquals(3, PathFollowerMoveStrategy.maxSpeed(9));
  }

}
