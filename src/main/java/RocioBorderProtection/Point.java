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
    double area2 = (b.x-a.x)*(c.y- a.y)-(c.x-a.x)*(b.y-a.y);
    if (DoubleEpsilonCompare.compare(area2, 0.0) == 0) {
      return 0;
    }
    return area2 < 0 ? -1 : +1;
  }

  public double x() { return x; }

  public double y() {
    return y;
  }

  public Point diff(Point other) {
    double x = this.x - other.x;
    double y = this.y - other.y;
    return getInstance(x, y);
  }

  public double len2() {
    return x*x + y*y;
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
