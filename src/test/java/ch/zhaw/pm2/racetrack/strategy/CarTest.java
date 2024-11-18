package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test all the methods of the Car class.
 */
public class CarTest {
  private Car testCar;

  @BeforeEach
  void initial() {
    testCar = new Car('T', new PositionVector(0, 0));
  }

  /**
   * Tests if the carposition is correctly set and give the valid position back when using positive values.
   */
  @Test
  void testSetAndGetPositionWithPositiveValues() {
    assertEquals(new PositionVector(0, 0), testCar.getPosition());
    testCar.setPosition(new PositionVector(1, 1));
    assertEquals(new PositionVector(1, 1), testCar.getPosition());
  }

  /**
   * Tests if the carposition is correctly set and give the valid position back when using negative values.
   */
  @Test
  void testSetAndGetPositionWithNegativeValues() {
    assertEquals(new PositionVector(0, 0), testCar.getPosition());
    testCar.setPosition(new PositionVector(-1, -1));
    assertEquals(new PositionVector(-1, -1), testCar.getPosition());
  }

  /**
   * Tests if the velocity of the car is valid.
   */
  @Test
  void testGetVelocity() {
    assertEquals(new PositionVector(0, 0), testCar.getVelocity());
  }

  /**
   * Tests if the specified amount correctly added to the car velocity.
   */
  @Test
  void testAccelerate() {
    testCar.accelerate(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, 1), testCar.getVelocity());
  }

  /**
   * Tests if the specified positive amounts correctly added to the car velocity's.
   */
  @Test
  void testAccelerateMoreThanOncePositiveValues() {
    testCar.accelerate(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, 1), testCar.getVelocity());
    testCar.accelerate(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, 2), testCar.getVelocity());
    testCar.accelerate(PositionVector.Direction.UP);
    assertEquals(new PositionVector(0, 1), testCar.getVelocity());
  }

  /**
   * Tests if the specified negative amounts correctly added to the car velocity's.
   */
  @Test
  void testAccelerateMoreThanOnceNegativeValues() {
    testCar.accelerate(PositionVector.Direction.UP);
    assertEquals(new PositionVector(0, -1), testCar.getVelocity());
    testCar.accelerate(PositionVector.Direction.UP);
    assertEquals(new PositionVector(0, -2), testCar.getVelocity());
    testCar.accelerate(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, -1), testCar.getVelocity());
  }

  /**
   * Tests if the next position is a valid position.
   */
  @Test
  void testNextPosition() {
    testCar.accelerate(PositionVector.Direction.RIGHT);
    assertEquals(new PositionVector(1, 0), testCar.nextPosition());
  }

  /**
   * Tests if the car correctly move up.
   */
  @Test
  void testMoveUp() {
    testCar.accelerate(PositionVector.Direction.UP);
    testCar.move();
    assertEquals(new PositionVector(0, -1), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move down.
   */
  @Test
  void testMoveDown() {
    testCar.accelerate(PositionVector.Direction.DOWN);
    testCar.move();
    assertEquals(new PositionVector(0, 1), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move to the left.
   */
  @Test
  void testMoveLeft() {
    testCar.accelerate(PositionVector.Direction.LEFT);
    testCar.move();
    assertEquals(new PositionVector(-1, 0), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move to the right.
   */
  @Test
  void testMoveRight() {
    testCar.accelerate(PositionVector.Direction.RIGHT);
    testCar.move();
    assertEquals(new PositionVector(1, 0), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move diagonal up to the right.
   */
  @Test
  void testMoveDiagonalUpRight() {
    testCar.accelerate(PositionVector.Direction.UP_RIGHT);
    testCar.move();
    assertEquals(new PositionVector(1, -1), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move diagonal up to the left.
   */
  @Test
  void testMoveDiagonalUpLeft() {
    testCar.accelerate(PositionVector.Direction.UP_LEFT);
    testCar.move();
    assertEquals(new PositionVector(-1, -1), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move diagonal down to the right.
   */
  @Test
  void testMoveDiagonalDownRight() {
    testCar.accelerate(PositionVector.Direction.DOWN_RIGHT);
    testCar.move();
    assertEquals(new PositionVector(1, 1), testCar.getPosition());
  }

  /**
   * Tests if the car correctly move diagonal down to the left.
   */
  @Test
  void testMoveDiagonalDownLeft() {
    testCar.accelerate(PositionVector.Direction.DOWN_LEFT);
    testCar.move();
    assertEquals(new PositionVector(-1, 1), testCar.getPosition());
  }

  /**
   * Tests if the car crashes.
   */
  @Test
  void testCrash() {
    assertFalse(testCar.isCrashed());
    testCar.crash();
    assertTrue(testCar.isCrashed());
  }

  /**
   * Tests if the car id is valid.
   */
  @Test
  void testGetId() {
    assertEquals('T', testCar.getId());
  }

  /**
   * Tests if the Move strategy is valid.
   */
  @Test
  void testGetMoveStrategyAfterInitial() {
    assertNull(testCar.getMoveStrategy());
  }
}
