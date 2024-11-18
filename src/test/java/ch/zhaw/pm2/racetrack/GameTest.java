package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.game.Track;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test the functionality of the class Game.
 */
public class GameTest {
  private static Game game;
  private static Track track;


  @BeforeEach
  void init() throws InvalidTrackFormatException, FileNotFoundException {
    game = new Game();
    track = new Track(new File("./tracks/testingTracks/quarter-mile-with-five-people.txt"));
    game.setTrack(track);
  }

  /**
   * Tests if the car will crash with a wall.
   */
  @Test
  void testWilLCarCrashWithWall() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/wall-in-front-of-car.txt"));
    game.setTrack(track);
    assertFalse(track.getCar(0).isCrashed());
    game.doCarTurn(PositionVector.Direction.RIGHT);
    assertTrue(track.getCar(0).isCrashed());
  }

  /**
   * Tests if the car will crash with another car.
   */
  @Test
  void testWilLCarCrashWithCar() {
    track.getCar(0).setPosition(new PositionVector(10, 4));
    track.getCar(1).setPosition(new PositionVector(8, 4));
    track.getCar(2).setPosition(new PositionVector(6, 4));
    assertTrue(game.willCarCrash(0, new PositionVector(8, 4)));
    assertTrue(game.willCarCrash(1, new PositionVector(6, 4)));
    assertFalse(game.willCarCrash(1, new PositionVector(8, 4)));
  }

  /**
   * Tests if the car will crash with a crashed car.
   */
  @Test
  void testWilLCarCrashWithCrashedCar() {
    track.getCar(0).setPosition(new PositionVector(10, 4));
    track.getCar(1).setPosition(new PositionVector(8, 4));
    track.getCar(1).crash();
    assertTrue(game.willCarCrash(0, new PositionVector(8, 4)));
  }

  /**
   * Tests if all cars have a valid id.
   */
  @Test
  public void shouldReturnTheCorrectIdOfEveryCar() {
    List<Car> cars = new ArrayList<>();
    for (int i = 0; i < track.getCarCount(); i++) {
      cars.add(track.getCar(i));
    }
    for (int i = 0; i < cars.size(); i++) {
      assertEquals(cars.get(i).getId(), game.getCarId(i));
    }
  }

  /**
   * Tests if the current car index is valid.
   */
  @Test
  public void shouldReturnTheCorrectCurrentCarIndex() {
    assertEquals(0, game.getCurrentCarIndex());
  }

  /**
   * Tests if the exception correctly occurs if the car index is out of bounds.
   */
  @Test
  void testGetCarIdIndexOutOfBounds() {
    assertThrows(IndexOutOfBoundsException.class, () -> game.getCarId(5));
  }

  /**
   * Tests if the exception correctly occurs if the car index is negative.
   */
  @Test
  void testGetCarIdIndexNegativeValue() {
    assertThrows(IndexOutOfBoundsException.class, () -> game.getCarId(-1));
  }

  /**
   * Tests the validity of the chosen track.
   */
  @Test
  void testSetTrackAndGetTrack() {
    assertEquals(track, game.getTrack());
  }

  /**
   * Tests the correctness of the actual velocity.
   */
  @Test
  void testGetCarVelocity() {
    assertEquals(new PositionVector(0, 0), game.getCarVelocity(0));
    game.doCarTurn(PositionVector.Direction.RIGHT);
    assertEquals(new PositionVector(1, 0), game.getCarVelocity(0));
  }

  /**
   * Tests if the velocity stacks up correctly.
   */
  @Test
  void testGetCarVelocityStacking() {
    game.doCarTurn(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, 1), game.getCarVelocity(0));
    game.doCarTurn(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(0, 2), game.getCarVelocity(0));
    game.doCarTurn(PositionVector.Direction.UP);
    assertEquals(new PositionVector(0, 1), game.getCarVelocity(0));
    game.doCarTurn(PositionVector.Direction.UP);
    assertEquals(new PositionVector(0, 0), game.getCarVelocity(0));
  }

  /**
   * Tests the validity of the current car position.
   */
  @Test
  void testGetCarPosition() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    assertEquals(new PositionVector(56, 3), game.getCarPosition(1));
    assertEquals(new PositionVector(56, 4), game.getCarPosition(2));
    assertEquals(new PositionVector(56, 5), game.getCarPosition(3));
    assertEquals(new PositionVector(56, 6), game.getCarPosition(4));
  }

  /**
   * Tests if the car turns correctly to the right.
   */
  @Test
  void testDoCarTurnRight() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.RIGHT);
    assertEquals(new PositionVector(57, 2), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly to the left.
   */
  @Test
  void testDoCarTurnLeft() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.LEFT);
    assertEquals(new PositionVector(55, 2), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly downwards.
   */
  @Test
  void testDoCarTurnDown() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.DOWN);
    assertEquals(new PositionVector(56, 3), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly upwards.
   */
  @Test
  void testDoCarTurnUp() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.UP);
    assertEquals(new PositionVector(56, 1), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly diagonal up to the right.
   */
  @Test
  void testDoCarTurnUpRight() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.UP_RIGHT);
    assertEquals(new PositionVector(57, 1), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly diagonal up to the left.
   */
  @Test
  void testDoCarTurnUpLeft() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.UP_LEFT);
    assertEquals(new PositionVector(55, 1), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly diagonal down to the right.
   */
  @Test
  void testDoCarTurnDownRight() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.DOWN_RIGHT);
    assertEquals(new PositionVector(57, 3), game.getCarPosition(0));
  }

  /**
   * Tests if the car turns correctly diagonal down to the left.
   */
  @Test
  void testDoCarTurnDownLeft() {
    assertEquals(new PositionVector(56, 2), game.getCarPosition(0));
    game.doCarTurn(PositionVector.Direction.DOWN_LEFT);
    assertEquals(new PositionVector(55, 3), game.getCarPosition(0));
  }

  /**
   * Tests if the switch to another car works correctly.
   */
  @Test
  void testSwitchToNextActiveCar() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/nine-cars.txt"));
    game.setTrack(track);
    for (int i = 0; i < track.getCarCount() - 1; i++) {
      track.getCar(i).setMoveStrategy(new DoNotMoveStrategy());
    }

    assertEquals(0, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(1, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(2, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(3, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(4, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(5, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(6, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(7, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(8, game.getCurrentCarIndex());
    game.switchToNextActiveCar();

    assertEquals(0, game.getCurrentCarIndex());
  }

  /**
   * Tests if the switch to another car skips crashed cars.
   */
  @Test
  void testSwitchToNextActiveCarSkippingCrashedCars() {
    for (int i = 0; i < track.getCarCount() - 1; i++) {
      track.getCar(i).setMoveStrategy(new DoNotMoveStrategy());
    }
    track.getCar(1).crash();
    track.getCar(2).crash();
    track.getCar(3).crash();
    track.getCar(4).crash();
    game.switchToNextActiveCar();
    assertEquals(0, game.getCurrentCarIndex());
  }

  /**
   * Tests if the last non crashed car wins the game.
   */
  @Test
  void testGetWinnerThroughCrash() {
    for (int i = 0; i < track.getCarCount() - 1; i++) {
      do {
        if (game.getCurrentCarIndex() != 0) {
          game.doCarTurn(PositionVector.Direction.RIGHT);
        }
        game.switchToNextActiveCar();
      } while (track.uncrashedCarCount() != 1);
    }
    assertTrue(track.getCar(1).isCrashed());
    assertTrue(track.getCar(2).isCrashed());
    assertTrue(track.getCar(3).isCrashed());
    assertTrue(track.getCar(4).isCrashed());
    assertFalse(track.getCar(0).isCrashed());
    game.switchToNextActiveCar();
    game.doCarTurn(PositionVector.Direction.NONE);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the correct winner is chosen.
   */
  @Test
  void testGetWinnerInitialWinner() {
    assertEquals(-1, game.getWinner());
  }

  /**
   * Tests if the car wins by a right directed finish line.
   */
  @Test
  void car_wins_when_crossing_finish_line_right() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/finish-line-in-all-directions.txt"));
    game.setTrack(track);
    assertEquals(-1, game.getWinner());
    game.doCarTurn(PositionVector.Direction.RIGHT);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the car wins by a left directed finish line.
   */
  @Test
  void car_wins_when_crossing_finish_line_left() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/finish-line-in-all-directions.txt"));
    game.setTrack(track);
    assertEquals(-1, game.getWinner());
    game.doCarTurn(PositionVector.Direction.LEFT);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the car wins by a downwards directed finish line.
   */
  @Test
  void car_wins_when_crossing_finish_line_down() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/finish-line-in-all-directions.txt"));
    game.setTrack(track);
    assertEquals(-1, game.getWinner());
    game.doCarTurn(PositionVector.Direction.DOWN);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the car wins by a upwards directed finish line.
   */
  @Test
  void car_wins_when_crossing_finish_line_up() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/finish-line-in-all-directions.txt"));
    game.setTrack(track);
    assertEquals(-1, game.getWinner());
    game.doCarTurn(PositionVector.Direction.UP);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the car wins when coming from below.
   */
  @Test
  void car_wins_when_crossing_finish_line_up_diagonally_from_below() throws InvalidTrackFormatException, FileNotFoundException {
    track = new Track(new File("./tracks/testingTracks/diagonal-finish-line.txt"));
    game.setTrack(track);
    assertEquals(-1, game.getWinner());
    game.doCarTurn(PositionVector.Direction.UP_RIGHT);
    assertEquals(0, game.getWinner());
  }

  /**
   * Tests if the position of the car is valid.
   */
  @Test
  public void shouldReturnTheCorrectCarPosition() {
    assertEquals(56, game.getCarPosition(0).getX());
    assertEquals(2, game.getCarPosition(0).getY());
    assertEquals(56, game.getCarPosition(1).getX());
    assertEquals(3, game.getCarPosition(1).getY());
  }
}
