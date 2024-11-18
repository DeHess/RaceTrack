package ch.zhaw.pm2.racetrack.game;

/**
 * Holds a position (vector to x,y-position of the car on the track grid)
 * or a velocity vector (x,y-components of the velocity vector of a car).
 */
public final class PositionVector {
  private int x; // horizontal component (position / velocity)
  private int y; // vertical component (position / velocity)

  /**
   * Creates a {@link PositionVector} with given coordinates
   *
   * @param x int vectors x axis value
   * @param y int vectors y axis value
   */
  public PositionVector(final int x, final int y) {
    this.y = y;
    this.x = x;
  }

  /**
   * Creates a new {@link PositionVector} with x and y values from another PositionVector
   *
   * @param givenPos PositionVector to copy
   */
  public PositionVector(final PositionVector givenPos) {
    this.x = givenPos.getX();
    this.y = givenPos.getY();
  }

  /**
   * Creates a null vector (0,0)
   */
  public PositionVector() {
    this.x = 0;
    this.y = 0;
  }

  /**
   * Enum representing a direction on the track grid.
   * Also representing the possible acceleration values.
   */
  public enum Direction {
    DOWN_LEFT(new PositionVector(-1, 1)),
    DOWN(new PositionVector(0, 1)),
    DOWN_RIGHT(new PositionVector(1, 1)),
    LEFT(new PositionVector(-1, 0)),
    NONE(new PositionVector(0, 0)),
    RIGHT(new PositionVector(1, 0)),
    UP_LEFT(new PositionVector(-1, -1)),
    UP(new PositionVector(0, -1)),
    UP_RIGHT(new PositionVector(1, -1));

    public final PositionVector vector;

    Direction(final PositionVector v) {
      vector = v;
    }

    /**
     * Converts a normalized vector to its matching direction
     *
     * @param positionVector PositionVector which should be matched
     * @return Direction matching PositionVector
     * @throws IllegalArgumentException if vector doesnt match a valid direction
     */
    public static Direction fromPositionVector(PositionVector positionVector) {
      for (Direction direction : Direction.values()) {
        if (direction.vector.equals(positionVector)) {
          return direction;
        }
      }
      throw new IllegalArgumentException(positionVector.toString() + " can't be converted to a Direction.");
    }

    /**
     * Converts the given direction
     *
     * @param dir Direction which should be converted
     * @return opposite Direction
     */
    public static Direction getOpposite(Direction dir) {
      PositionVector opposite = new PositionVector(-dir.vector.getX(), -dir.vector.getY());
      return fromPositionVector(opposite);
    }

  }

  /**
   * Adds two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
   *
   * @param vectorA A position or velocity vector
   * @param vectorB A position or velocity vector
   * @return A new PositionVector holding the result of the addition. If both
   * arguments are positions (not velocity), the result is mathematically
   * correct but meaningless.
   */
  public static PositionVector add(final PositionVector vectorA, final PositionVector vectorB) {
    return new PositionVector(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY());
  }

  /**
   * Subtracts two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
   *
   * @param vectorA A position or velocity vector
   * @param vectorB A position or velocity vector
   * @return A new PositionVector holding the result of the addition. If both
   * arguments are positions (not velocity), the result is mathematically
   * correct but meaningless.
   */
  public static PositionVector subtract(final PositionVector vectorA, final PositionVector vectorB) {
    return new PositionVector(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY());
  }

  /**
   * Adds the values of a PositionVector to a PositionVector object.
   *
   * @param vector A PositionVector to be added.
   */
  public void add(PositionVector vector) {
    this.x += vector.getX();
    this.y += vector.getY();
  }

  /**
   * Calculates the length of the PositionVector.
   *
   * @return length of the PositionVector.
   */
  public double getLength() {
    return Math.sqrt(x * x + y * y);
  }

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof PositionVector)) throw new ClassCastException();
    final PositionVector otherPositionVector = (PositionVector) other;
    return this.y == otherPositionVector.getY() && this.x == otherPositionVector.getX();
  }

  @Override
  public int hashCode() {
    return this.x ^ this.y;
  }

  @Override
  public String toString() {
    return "(X:" + this.x + ", Y:" + this.y + ")";
  }
}
