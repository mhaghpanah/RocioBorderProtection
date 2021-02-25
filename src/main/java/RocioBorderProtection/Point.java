package RocioBorderProtection;

public class Point {

  private final double x;
  private final double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static Point getInstance(double x, double y) {
    return new Point(x, y);
  }

  public static int ccw(Point a, Point b, Point c) {
    double d = (b.getX() - a.getX()) * (c.getY() - a.getY())
        -
        (c.getX() - a.getX()) * (b.getY() - a.getY());
    if (DoubleEpsilonCompare.compare(d, 0.0) == 0) {
      return 0;
    }
    return d < 0 ? -1 : +1;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public Point diff(Point other) {
    double x = getX() - other.getX();
    double y = getY() - other.getY();
    return getInstance(x, y);
  }

  public double len2() {
    return getX() * getX() + getY() * getY();
  }

  public double dist(Point other) {
    Point d = diff(other);
    return d.len2();
  }

  @Override
  public String toString() {
    return "Point{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Point)) {
      return false;
    }

    Point point = (Point) o;

    if (Double.compare(point.x, x) != 0) {
      return false;
    }
    return Double.compare(point.y, y) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
