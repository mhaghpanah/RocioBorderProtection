package RocioBorderProtection;

public class Point {

  private final double x;
  private final double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public String toString() {
    return "Point{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

  public static int orientation(Point p0, Point p1, Point p2) {
    double d = (p1.getY() - p0.getY()) * (p2.getX() - p1.getX()) - (p2.getY() * p1.getY()) * (p1.getX() - p0.getX());
    if (DoubleEpsilonCompare.compare(d, 0.0) == 0) return 0;
    return d < 0 ? -1 : +1;
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
