package cs3500.pa03.model.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a coordinate pair
 */
public class Posn {
  private final int x;
  private final int y;

  /**
   * Instantiate a position object
   *
   * @param x coordinate
   * @param y coordinate
   */
  public Posn(@JsonProperty("x") int x,
              @JsonProperty("y") int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Generate a string representation of the position
   *
   * @return string representation of the position
   */
  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  /**
   * Is this other posn equal to this posn?
   *
   * @param o other posn
   * @return if they are the same posn
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    Posn otherPosn = (Posn) o;

    if (x != otherPosn.x) {
      return false;
    }

    return y == otherPosn.y;
  }

  /**
   * gets the x coordinate of this ship
   *
   * @return the x coordinate of this ship
   */
  public int getX() {
    return this.x;
  }

  /**
   * gets the y coordinate of this ship
   *
   * @return the y coordinate of this ship
   */
  public int getY() {
    return this.y;
  }

  /**
   * Return the INCLUSIVE distance between two positions
   *
   * @param other other posn to use
   * @return inclusive distance between two positions
   */
  public int distance(Posn other) {
    return Integer.max(Math.abs(other.y - this.y) + 1,
        Math.abs(other.x - this.x) + 1);
  }

  /**
   * adds this coordinate to the given coordinate
   *
   * @param other the given coordinate to add
   * @return the result of the added coordinates
   */
  public Posn add(Posn other) {
    return new Posn(x + other.getX(), y + other.getY());
  }

  /**
   * a list of coordinates offset in the y direction by a given distance from this coordinate
   *
   * @param distance offset distance
   * @return a list of coordinates offset by a given distance
   */
  public List<Posn> offsetY(int distance) {
    ArrayList<Posn> posns = new ArrayList<>();
    if (distance > 0) {
      for (int i = 1; i <= distance; i++) {
        posns.add(new Posn(x, y + i));
      }
    } else {
      for (int i = -1; i >= distance; i--) {
        posns.add(new Posn(x, y + i));
      }
    }

    return posns;
  }

  /**
   * a list of coordinates offset in the x direction by a given distance from this coordinate
   *
   * @param distance offset distance
   * @return a list of coordinates offset by a given distance
   */
  public List<Posn> offsetX(int distance) {
    ArrayList<Posn> posns = new ArrayList<>();
    if (distance > 0) {
      for (int i = 1; i <= distance; i++) {
        posns.add(new Posn(x + i, y));
      }
    } else {
      for (int i = -1; i >= distance; i--) {
        posns.add(new Posn(x + i, y));
      }
    }
    return posns;
  }


  /**
   * Generate a random posn within the 0 and the given X and Y bounds
   *
   * @param maxX max X value (exclusive)
   * @param maxY max Y value (exclusive)
   * @return random posn
   */
  public static Posn randomPosn(int maxX, int maxY) {
    Random random = new Random();
    return new Posn(random.nextInt(maxX), random.nextInt(maxY));
  }

  /**
   * Generate a random posn within the given X and Y bounds
   * that is exactly the given distance from the original posn
   *
   * @param start    starting posn
   * @param distance distance to find a new posn
   * @param maxX     max X value (exclusive)
   * @param maxY     max Y value (exclusive)
   * @return random posn from the original posn
   */
  public static Posn randomPosnFrom(Posn start, int distance, int maxX, int maxY) {
    distance--;

    int[] directions = {1, 2, 3, 4};
    int[] randomDirections = randomizeArray(directions); // Randomize the order of directions

    Posn posn = null;
    for (int randomNumber : randomDirections) {
      switch (randomNumber) {
        case (1) -> posn = new Posn(start.x + distance, start.y);
        case (2) -> posn = new Posn(start.x, start.y + distance);
        case (3) -> posn = new Posn(start.x - distance, start.y);
        case (4) -> posn = new Posn(start.x, start.y - distance);
      }

      if (posn.x >= 0 && posn.y >= 0 && posn.x < maxX && posn.y < maxY) {
        break;
      } else {
        posn = null;
      }
    }

    if (posn == null) {
      throw new IllegalArgumentException("No possible positions found");
    }

    return posn;
  }

  /**
   * Randomize the order of the given array
   *
   * @param array array to be randomized
   * @return randomized array
   */
  private static int[] randomizeArray(int[] array) {
    Random rand = new Random();
    for (int i = 0; i < array.length; i++) {
      int randomIndex = rand.nextInt(array.length);
      int temp = array[randomIndex];
      array[randomIndex] = array[i];
      array[i] = temp;
    }
    return array;
  }
}
