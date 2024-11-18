package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.BresenhamAlgorithm;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BresenhamTest {

  /**
   * Tests that the algorithm does not count start and end points twice
   * when start and end point are the same
   */
  @Test
  public void counts_one_point_if_start_and_end_are_the_same() {
    BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
    List<PositionVector> points = bresenhamAlgorithm.calculatePath(new PositionVector(0, 0), new PositionVector(0, 0));
    assertEquals(1, points.size());
    assertEquals(new PositionVector(0, 0), points.get(0));
  }

  /**
   * Tests that the algorithm works correctly for two points horizontally next to
   * each other (should only count start and end points)
   */
  @Test
  public void counts_two_point_if_start_and_end_are_horizontally_adjacent_to_each_other() {
    BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
    List<PositionVector> points = bresenhamAlgorithm.calculatePath(new PositionVector(0, 0), new PositionVector(1, 0));
    assertEquals(2, points.size());
    assertEquals(new PositionVector(0, 0), points.get(0));
    assertEquals(new PositionVector(1, 0), points.get(1));
  }

  /**
   * Tests that the algorithm works correctly for two points vertically next to
   * each other (should only count start and end points)
   */
  @Test
  public void counts_two_point_if_start_and_end_are_vertically_adjacent_to_each_other() {
    BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
    List<PositionVector> points = bresenhamAlgorithm.calculatePath(new PositionVector(0, 0), new PositionVector(0, 1));
    assertEquals(2, points.size());
    assertEquals(new PositionVector(0, 0), points.get(0));
    assertEquals(new PositionVector(0, 1), points.get(1));
  }

  /**
   * Tests that the algorithm works when using two points which are positioned diagonally
   * from each other
   */
  @Test
  public void diagonal_45_degrees() {
    BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
    List<PositionVector> points = bresenhamAlgorithm.calculatePath(new PositionVector(2, 2), new PositionVector(0, 0));
    assertEquals(3, points.size());
    assertEquals(new PositionVector(2, 2), points.get(0));
    assertEquals(new PositionVector(1, 1), points.get(1));
    assertEquals(new PositionVector(0, 0), points.get(2));
  }

  /**
   * Tests the official wikipedia example:
   * //https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/BresenhamLine2.png/480px-BresenhamLine2.png
   */
  @Test
  public void wikipedia_example() {
    BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm();
    List<PositionVector> points = bresenhamAlgorithm.calculatePath(new PositionVector(0, 0), new PositionVector(18, 7));
    assertEquals(19, points.size());
    assertEquals(new PositionVector(0, 0), points.get(0));
    assertEquals(new PositionVector(1, 0), points.get(1));
    assertEquals(new PositionVector(2, 1), points.get(2));
    assertEquals(new PositionVector(3, 1), points.get(3));
    assertEquals(new PositionVector(4, 2), points.get(4));
    assertEquals(new PositionVector(5, 2), points.get(5));
    assertEquals(new PositionVector(6, 2), points.get(6));
    assertEquals(new PositionVector(7, 3), points.get(7));
    assertEquals(new PositionVector(8, 3), points.get(8));
    assertEquals(new PositionVector(9, 3), points.get(9));
    assertEquals(new PositionVector(10, 4), points.get(10));
    assertEquals(new PositionVector(11, 4), points.get(11));
    assertEquals(new PositionVector(12, 5), points.get(12));
    assertEquals(new PositionVector(13, 5), points.get(13));
    assertEquals(new PositionVector(14, 5), points.get(14));
    assertEquals(new PositionVector(15, 6), points.get(15));
    assertEquals(new PositionVector(16, 6), points.get(16));
    assertEquals(new PositionVector(17, 7), points.get(17));
    assertEquals(new PositionVector(18, 7), points.get(18));
  }


}
