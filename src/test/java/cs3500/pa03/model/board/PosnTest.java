package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the posn class
 */
class PosnTest {

  @Test
  void equals() {
    Posn p1 = new Posn(0, 0);
    Posn p2 = new Posn(1, 1);
    assertNotEquals(p1, p2);
  }

  @Test
  void equals2() {
    Posn p1 = new Posn(1, 1);
    Posn p2 = new Posn(1, 1);
    assertEquals(p1, p2);
  }

  @Test
  void equals3() {
    Posn p1 = new Posn(0, 0);
    ArrayList<String> p2 = new ArrayList<>();
    assertNotEquals(p1, p2);
  }

  @Test
  void equals4() {
    Posn p1 = new Posn(0, 0);
    Posn p2 = null;
    assertNotEquals(p1, p2);
  }



  /**
   * tests the distance method
   */
  @Test
  void distance() {
    Posn p1 = new Posn(0, 0);
    Posn p2 = new Posn(1, 1);
    assertEquals(2, p1.distance(p2));
  }

  @Test
  void distance2() {
    Posn p1 = new Posn(0, 0);
    Posn p2 = new Posn(0, 5);
    assertEquals(6, p1.distance(p2));
  }

  @Test
  void distance3() {
    Posn p1 = new Posn(0, 0);
    Posn p2 = new Posn(0, 0);
    assertEquals(1, p1.distance(p2));
  }

  @Test
  void distance4() {
    Posn p1 = new Posn(2, 4);
    Posn p2 = new Posn(2, 0);
    assertEquals(5, p1.distance(p2));
  }

  /**
   * tests the toString method
   */
  @Test
  void testToString() {
    Posn p1 = new Posn(2, 4);
    assertEquals("(2, 4)", p1.toString());
  }

  /**
   * tests the randomPosn method
   */
  @Test
  void testRandomPosn() {
    assertNotEquals(new Posn(0, 0), Posn.randomPosn(100, 100));

    Posn randomPosn = Posn.randomPosn(10, 10);
    assertTrue(randomPosn.getX() < 10 && randomPosn.getY() < 10
        && randomPosn.getX() >= 0 && randomPosn.getY() >= 0);
  }

  /**
   * tests that a random coordinate was generated from a given coordinate,
   * if the coordinate is in the middle of the given range
   */
  @Test
  void testRandomPosnFrom() {
    Posn start = new Posn(10, 10);
    Posn end = Posn.randomPosnFrom(start, 5, 20, 20);
    boolean option1 = Math.abs(start.getX() - end.getX()) == 4
        && Math.abs(start.getY() - end.getY()) == 0;
    boolean option2 = Math.abs(start.getX() - end.getX()) == 0
        && Math.abs(start.getY() - end.getY()) == 4;
    assertTrue(option1 || option2);
  }

  /**
   * tests that a random coordinate was generated from a given coordinate,
   * if the coordinate is in the middle of the given range
   */
  @Test
  void testRandomPosnFrom2() {
    Posn start = new Posn(10, 10);
    Posn end = Posn.randomPosnFrom(start, 7, 20, 20);
    boolean option1 = Math.abs(start.getX() - end.getX()) == 6
        && Math.abs(start.getY() - end.getY()) == 0;
    boolean option2 = Math.abs(start.getX() - end.getX()) == 0
        && Math.abs(start.getY() - end.getY()) == 6;
    assertTrue(option1 || option2);
  }

  /**
   * tests that a random coordinate was generated from a given coordinate,
   * if there's only enough room in certain directions
   */
  @Test
  void testRandomPosnFrom3() {
    Posn start = new Posn(10, 10);
    Posn end = Posn.randomPosnFrom(start, 7, 11, 11);
    assertTrue((end.getX() == 4 && end.getY() == 10) || (end.getX() == 10 && end.getY() == 4));
  }

  /**
   * tests that a random coordinate was generated from a given coordinate,
   * if there's only enough room in one direction
   */
  @Test
  void testRandomPosnFrom4() {
    Posn start = new Posn(0, 10);
    Posn end = Posn.randomPosnFrom(start, 7, 2, 11);
    assertEquals(4, end.getY());
  }

  /**
   * tests that a random coordinate can't be generated from a given coordinate,
   * if there's no room from this coordinate
   */
  @Test
  void testRandomPosnFrom5() {
    Posn start = new Posn(10, 10);
    assertThrows(IllegalArgumentException.class, () -> Posn.randomPosnFrom(start, 20, 11, 11));
  }
}