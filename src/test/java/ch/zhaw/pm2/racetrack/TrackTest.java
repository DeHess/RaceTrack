package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.game.Track;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test all the methods of the track class.
 */
public class TrackTest {
  /**
   * Tests if the track is generated correctly with the given file.
   */
  @Test
  public void can_make_track_with_existing_file() {
    File file = new File("./tracks/oval-clock-up.txt");
    assertTrue(file.exists());
    assertDoesNotThrow(() -> new Track(file));
  }

  /**
   * Tests if the determined size is valid.
   */
  @Test
  public void width_and_height_are_determined_by_file_content() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/oval-clock-up.txt");
    Track oval = new Track(file);
    assertEquals(50, oval.getWidth());
    assertEquals(14, oval.getHeight());
  }

  /**
   * Tests if the exception correctly occurs by size difference.
   */
  @Test
  public void throws_InvalidTrackFormatException_when_width_of_track_differs() {
    File file = new File("./tracks/testingTracks/differing-line-widths.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests if the exception correctly occurs when car id is crash indicator.
   */
  @Test
  public void throws_InvalidTrackFormatException_when_car_id_is_crash_indicator() {
    File file = new File("./tracks/testingTracks/car-has-crash-indicator-as-index.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests the validity of all Space types on the chosen track.
   */
  @Test
  public void recognizes_all_SpaceTypes_on_a_track() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/all-characters.txt");
    Track track = new Track(file);

    assertEquals(
            ConfigSpecification.SpaceType.TRACK,
            track.getSpaceType(new PositionVector(0, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.WALL,
            track.getSpaceType(new PositionVector(1, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.FINISH_LEFT,
            track.getSpaceType(new PositionVector(2, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.FINISH_RIGHT,
            track.getSpaceType(new PositionVector(3, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.FINISH_UP,
            track.getSpaceType(new PositionVector(4, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.FINISH_DOWN,
            track.getSpaceType(new PositionVector(5, 0)));

    assertEquals(
            ConfigSpecification.SpaceType.TRACK,
            track.getSpaceType(new PositionVector(6, 0)));
  }

  /**
   * Tests if the amount of cars is valid.
   */
  @Test
  public void counts_two_cars_on_two_cars_testing_track() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/two-cars.txt");
    Track track = new Track(file);
    assertEquals(2, track.getCarCount());
  }

  /**
   * Tests if the achieved car position is valid.
   */
  @Test
  public void car_positions_are_found() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/two-cars.txt");
    Track track = new Track(file);
    PositionVector expectedPositionA = new PositionVector(0, 2);
    PositionVector expectedPositionB = new PositionVector(4, 2);
    assertEquals(expectedPositionA, track.getCarPos(0));
    assertEquals(expectedPositionB, track.getCarPos(1));
    assertEquals(expectedPositionA, track.getCar(0).getPosition());
    assertEquals(expectedPositionB, track.getCar(1).getPosition());
  }

  /**
   * Tests if the exception correctly occurs if there are more than nine cars.
   */
  @Test
  public void throws_invalidTrackFormatException_when_track_has_more_than_nine_cars() {
    File file = new File("./tracks/testingTracks/too-many-cars.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests if the exception correctly occurs if two cars have the same id.
   */
  @Test
  public void throws_invalidTrackFormatException_when_two_cars_of_same_id_exist() {
    File file = new File("./tracks/testingTracks/duplicate-car-ids.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests if the exception correctly occurs if no car exists.
   */
  @Test
  public void throws_invalidTrackFormatException_when_no_cars_exist_on_track() {
    File file = new File("./tracks/testingTracks/no-cars.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests if the exception correctly occurs if the used file is empty.
   */
  @Test
  public void throws_invalidTrackFormatException_when_using_empty_file() {
    File file = new File("./tracks/testingTracks/empty.txt");
    assertThrows(InvalidTrackFormatException.class, () -> new Track(file));
  }

  /**
   * Tests if character wall is set if the position is out of bounds.
   */
  @Test
  public void getCharAtPosition_returns_wall_character_when_position_out_of_bounds() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/all-characters.txt");
    Track track = new Track(file);
    assertEquals('#', track.getCharAtPosition(40, 40, ConfigSpecification.SpaceType.TRACK));
  }

  /**
   * Tests if the car id is returned correctly after the car is at its position.
   */
  @Test
  public void getCharAtPosition_returns_car_id_when_a_car_is_at_position() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/two-cars.txt");
    Track track = new Track(file);

    Character car1 = track.getCharAtPosition(2, 0, ConfigSpecification.SpaceType.TRACK);
    assertEquals('a', car1);

    Character car2 = track.getCharAtPosition(2, 4, ConfigSpecification.SpaceType.TRACK);
    assertEquals('b', car2);
  }

  /**
   * Tests if character wall is set if position parameter is out of bounds.
   */
  @Test
  public void getSpaceType_returns_wall_if_position_parameter_is_out_of_bounds() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/two-cars.txt");
    Track track = new Track(file);
    assertEquals(ConfigSpecification.SpaceType.WALL, track.getSpaceType(new PositionVector(30, 30)));
  }

  /**
   * Tests the validity of the car characters at the track.
   */
  @Test
  public void toString_returns_String_containing_cars() throws InvalidTrackFormatException, FileNotFoundException {
    File file = new File("./tracks/testingTracks/all-characters-and-cars.txt");
    Track track = new Track(file);
    assertEquals(" #<>^vxab\n #<>^vycd", track.toString());
  }
}
