package RocioBorderProtection;

public class DoubleEpsilonCompare {

  private static final double EPSILON = 1e-18;

  public static boolean equals(final double a, final double b) {
    if (a == b) {
      return true;
    }
    return Math.abs(a - b) < EPSILON;
  }

  public static int compare(final double a, final double b) {
    if (equals(a, b)) {
      return 0;
    }
    return a < b ? -1 : +1;
  }

}
