package cs3500.pa03;

/**
 * Utility class for testing
 */
public class TestUtils {

  /**
   * Removes ANSI color codes from a string
   *
   * @param s the string to remove ANSI color codes from
   * @return the string without ANSI color codes
   */
  public static String removeColors(String s) {
    return s.replaceAll("\u001B\\[[;\\d]*m", "");
  }
}