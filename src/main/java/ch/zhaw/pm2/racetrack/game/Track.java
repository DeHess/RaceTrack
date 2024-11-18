package ch.zhaw.pm2.racetrack.game;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.given.TrackSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link ConfigSpecification.SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:</p>
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 *
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The the tracks ends with the first empty line or the file end.<br>
 * An {@link InvalidTrackFormatException} is thrown, if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 */
public class Track implements TrackSpecification {
  public static final char CRASH_INDICATOR = 'X';
  private int width;
  private int height;
  private ConfigSpecification.SpaceType[][] grid;
  private List<Car> cars = new ArrayList<>();

  /**
   * Initialize a Track from the given track file.
   *
   * @param trackFile Reference to a file containing the track data
   * @throws FileNotFoundException       if the given track file could not be found
   * @throws InvalidTrackFormatException if the track file contains invalid data (no tracklines, ...)
   */
  public Track(File trackFile) throws FileNotFoundException, InvalidTrackFormatException {
    grid = determineDimensions(trackFile);
    fillGridAndCars(trackFile);
  }

  /**
   * this method fills the grid of SpaceTypes.
   * Converts chars from file to corresponding SpaceTypes,
   * or sets the Track SpaceType if the char has no corresponding spaceType,
   * This method also fills the cars ArrayList
   *
   * @param trackFile Reference to a file containing the track data
   * @throws FileNotFoundException       if the given track file could not be found
   * @throws InvalidTrackFormatException if the track file contains invalid data
   */
  private void fillGridAndCars(File trackFile) throws FileNotFoundException, InvalidTrackFormatException {
    Scanner scanner = new Scanner(trackFile);
    for (int y = 0; y < height; y++) {
      String line = scanner.nextLine();
      for (int x = 0; x < width; x++) {
        setGridAtPosition(x, y, line);
      }
    }
    scanner.close();

    if (cars.size() > ConfigSpecification.MAX_CARS) {
      throw new InvalidTrackFormatException(
              String.format("There are more than %d (maximum) cars on the track.", ConfigSpecification.MAX_CARS));
    }
    if (cars.size() == 0) {
      throw new InvalidTrackFormatException("There are no cars on the Track.");
    }
  }

  /**
   * Sets one SpaceType element in the spaceType grid at given position
   * @param x Position in the grid
   * @param y Position in the grid
   * @param line String representing one line of the raceTrack
   * @throws InvalidTrackFormatException
   */
  private void setGridAtPosition(int x, int y, String line) throws InvalidTrackFormatException {
    char character = line.charAt(x);
    ConfigSpecification.SpaceType spaceType = ConfigSpecification.SpaceType.fromChar(character);
    if (spaceType == ConfigSpecification.SpaceType.TRACK && character != ' ') {
      checkIfCarHasCrashIndicatorAsId(y, x, character);
      addCar(x, y, character);
    }
    grid[x][y] = spaceType;
  }

  /**
   * @throws InvalidTrackFormatException if a car has the same id as the crash indicator
   */
  private void checkIfCarHasCrashIndicatorAsId(int y, int x, char character) throws InvalidTrackFormatException {
    if (carHasCrashIndicatorAsId(character)) {
      throw new InvalidTrackFormatException(
              String.format("Car at X: %d, Y: %d has the Crash-Indicator (%s) as index", x, y, CRASH_INDICATOR));
    }
  }

  private boolean carHasCrashIndicatorAsId(char carId) {
    return carId == CRASH_INDICATOR;
  }

  /**
   * determines the dimensions (x, y) of the Track and the grid representing it.
   * @param trackFile given File
   * @throws InvalidTrackFormatException
   * @throws FileNotFoundException
   */
  private ConfigSpecification.SpaceType[][] determineDimensions(File trackFile) throws FileNotFoundException, InvalidTrackFormatException {
    Scanner scanner = new Scanner(trackFile);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (height == 0) {
        width = line.length();
      }
      if (width != line.length()) {
        throw new InvalidTrackFormatException(
                String.format("line %d has a length of %d, but should have length %d.", height + 1, line.length(), width));
      }
      height++;
    }
    scanner.close();
    if (height == 0 || width == 0) {
      throw new InvalidTrackFormatException("File contains an empty track");
    }
    return new ConfigSpecification.SpaceType[width][height];
  }

  /**
   * adds a car to the cars ArrayList
   * @param x Position in the grid
   * @param y Position in the grid
   * @param character which represents the car as an id
   * @throws InvalidTrackFormatException if car id appears more than once
   */
  private void addCar(int x, int y, char character) throws InvalidTrackFormatException {
    for (Car car : cars) {
      if (car.getId() == character) {
        throw new InvalidTrackFormatException(
                String.format("The car with id %c appears more than once.", character));
      }
    }
    cars.add(new Car(character, new PositionVector(x, y)));
  }

  /**
   * Return the type of space at the given position.
   * If the location is outside the track bounds, it is considered a wall.
   *
   * @param position The coordinates of the position to examine
   * @return The type of track position at the given location
   */
  @Override
  public ConfigSpecification.SpaceType getSpaceType(PositionVector position) {
    if (!positionIsInGrid(position)) {
      return ConfigSpecification.SpaceType.WALL;
    }
    return grid[position.getX()][position.getY()];
  }

  /**
   * Return the number of cars.
   *
   * @return Number of cars
   */
  @Override
  public int getCarCount() {
    return cars.size();
  }

  /**
   * Get instance of specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return The car instance at the given index
   */
  @Override
  public Car getCar(int carIndex) {
    return cars.get(carIndex);
  }

  /**
   * Get the id of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A char containing the id of the car
   */
  @Override
  public char getCarId(int carIndex) {
    return cars.get(carIndex).getId();
  }

  /**
   * Get the position of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A PositionVector containing the car's current position
   */
  @Override
  public PositionVector getCarPos(int carIndex) {
    return cars.get(carIndex).getPosition();
  }

  /**
   * Get the velocity of the specified car.
   *
   * @param carIndex The zero-based carIndex number
   * @return A PositionVector containing the car's current velocity
   */
  @Override
  public PositionVector getCarVelocity(int carIndex) {
    return cars.get(carIndex).getVelocity();
  }

  /**
   * Gets character at the given position.
   * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is returned.
   * If given x, y values result in a position that is out of bounds returns a '#' (wall) character
   *
   * @param y            position Y-value
   * @param x            position X-vlaue
   * @param currentSpace char to return if no car is at position (x,y)
   * @return character representing position (x,y) on the track
   */
  @Override
  public char getCharAtPosition(int y, int x, Config.SpaceType currentSpace) {
    PositionVector position = new PositionVector(x, y);
    if (!positionIsInGrid(new PositionVector(x, y))) {
      return '#';
    }
    for (Car car : cars) {
      if (car.getPosition().equals(position)) {
        if (car.isCrashed()) {
          return CRASH_INDICATOR;
        }
        return car.getId();
      }
    }
    ConfigSpecification.SpaceType spaceType = this.getSpaceType(position);
    return spaceType.value;
  }

  /**
   * Counts the amount of uncrashed cars.
   *
   * @return the amount of uncrashed cars.
   */
  public int uncrashedCarCount() {
    int uncrashedCars = 0;
    for (Car car : cars) {
      if (!car.isCrashed()) {
        uncrashedCars++;
      }
    }
    return uncrashedCars;
  }

  /**
   * Return a String representation of the track, including the car locations.
   *
   * @return A String representation of the track
   */
  @Override
  public String toString() {
    StringBuilder track = new StringBuilder();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        char currentChar = determineChar(x, y);
        track.append(currentChar);
      }
      if (y != height - 1) {
        track.append('\n');
      }
    }
    return track.toString();
  }

  /**
   * Check if a different {@link Car} is already at a given position.
   *
   * @param position The position to check.
   * @param car      The {@link Car} that should be ignored.
   * @return true if another car is already at the position.
   */
  public boolean hasDifferentCarAtPosition(PositionVector position, Car car) {
    for (Car differentCar : cars) {
      if (differentCar.getPosition().equals(position) && car.getId() != differentCar.getId()) {
        return true;
      }
    }
    return false;
  }

  /**
   * determines the character at given location (can be either a car or a SpaceType character)
   * Cars are "laid over" the grid, which means if a car is at the given Position, the cars id
   * will be returned, and not the SpaceType beneath it.
   * @param x Position in the grid
   * @param y Position in the grid
   * @return char at given Position
   */
  private char determineChar(int x, int y) {
    ConfigSpecification.SpaceType currentSpaceType = this.getSpaceType(new PositionVector(x, y));
    return this.getCharAtPosition(y, x, currentSpaceType);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public ConfigSpecification.SpaceType[][] getGrid() {
    return grid;
  }

  private boolean positionIsInGrid(PositionVector position) {
    return position.getX() <= width && position.getY() <= height;
  }
}
