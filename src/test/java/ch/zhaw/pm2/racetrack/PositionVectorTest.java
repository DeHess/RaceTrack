package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.PositionVector;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class to test the functionality of the class PositionVector.
 */
class PositionVectorTest {

  /**
   * Tests if two vectors are equal.
   */
  @Test
  void testEquals() {
    PositionVector a = new PositionVector(3, 5);
    PositionVector b = new PositionVector(3, 5);
    assertEquals(a, b);
  }

  /**
   * Tests equality between vector and hashmap.
   */
  @Test
  void testEqualsWithHashMap() {
    Map<PositionVector, Integer> map = new HashMap<>();
    PositionVector a = new PositionVector(3, 5);
    map.put(a, 1);
    PositionVector b = new PositionVector(3, 5);
    assertTrue(map.containsKey(a), "Test with same object");
    assertTrue(map.containsKey(b), "Test with equal object");
  }

  /**
   * Test the {@link PositionVector.Direction} getOpposite method
   * Test if for every valid Direction the correct opposite direction is returned
   */
  @Test
  void testGetOpposite() {
    PositionVector.Direction dirRight = PositionVector.Direction.RIGHT;
    PositionVector.Direction dirLeft = PositionVector.Direction.LEFT;
    PositionVector.Direction dirUp = PositionVector.Direction.UP;
    PositionVector.Direction dirDown = PositionVector.Direction.DOWN;

    PositionVector.Direction dirNone = PositionVector.Direction.NONE;

    PositionVector.Direction dirUpRight = PositionVector.Direction.UP_RIGHT;
    PositionVector.Direction dirUpLeft = PositionVector.Direction.UP_LEFT;
    PositionVector.Direction dirDownRight = PositionVector.Direction.DOWN_RIGHT;
    PositionVector.Direction dirDownLeft = PositionVector.Direction.DOWN_LEFT;

    assertEquals(PositionVector.Direction.LEFT, PositionVector.Direction.getOpposite(dirRight));
    assertEquals(PositionVector.Direction.RIGHT, PositionVector.Direction.getOpposite(dirLeft));
    assertEquals(PositionVector.Direction.DOWN, PositionVector.Direction.getOpposite(dirUp));
    assertEquals(PositionVector.Direction.UP, PositionVector.Direction.getOpposite(dirDown));

    assertEquals(PositionVector.Direction.NONE, PositionVector.Direction.getOpposite(dirNone));

    assertEquals(PositionVector.Direction.DOWN_LEFT, PositionVector.Direction.getOpposite(dirUpRight));
    assertEquals(PositionVector.Direction.DOWN_RIGHT, PositionVector.Direction.getOpposite(dirUpLeft));
    assertEquals(PositionVector.Direction.UP_LEFT, PositionVector.Direction.getOpposite(dirDownRight));
    assertEquals(PositionVector.Direction.UP_RIGHT, PositionVector.Direction.getOpposite(dirDownLeft));
  }
}
