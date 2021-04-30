package RocioBorderProtection;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class Segment {

  private final Point p0;
  private final Point p1;

  private final LineSegment lineSegment;

  public Segment(Point p0, Point p1) {
    this.p0 = p0;
    this.p1 = p1;
    lineSegment = new LineSegment(p0.x(), p0.y(), p1.x(), p1.y());
  }

  public boolean isEmpty() {
    return DoubleEpsilonCompare.equals(lineSegment.getLength(), 0.0);
  }

  public double getLength() {
    return lineSegment.getLength();
  }

  public boolean isIntersect(Segment segment) {
    LineIntersector lineIntersector = new RobustLineIntersector();

    LineSegment l0 = this.lineSegment;
    LineSegment l1 = segment.lineSegment;

    Coordinate[] cs = new Coordinate[]{l0.p0, l0.p1, l1.p0, l1.p1};

    lineIntersector.computeIntersection(cs[0], cs[1], cs[2], cs[3]);

    if (!lineIntersector.hasIntersection()) {
      return false;
    }

    Coordinate intersection = lineIntersector.getIntersection(0);
    Point point = Point.getInstance(intersection.getX(), intersection.getY());
    double dist = Double.MAX_VALUE;
    for (int i = 0; i < 4; i++) {
      Point point1 = Point.getInstance(cs[i].getX(), cs[i].getY());
      dist = Math.min(dist, point.dist(point1));
    }
//    System.err.println(dist);
    return DoubleEpsilonCompare.compare(dist, 0.0) != 0;

//    return lineIntersector.isInteriorIntersection();

//    Segment a = this;
//    Segment b = segment;
//    if (Point.ccw(a.p0, a.p1, b.p0) * Point.ccw(a.p0, a.p1, b.p1) >= 0) return false;
//    if (Point.ccw(b.p0, b.p1, a.p0) * Point.ccw(b.p0, b.p1, a.p1) >= 0) return false;
//    return true;
  }


  public List<Point> subdivision(double eps) {
    List<Point> ans = new ArrayList<>();

    double length = getLength();
    int m = (int) Math.ceil(length / eps);
    for (int i = 0; i < m; i++) {
      double segmentLengthFraction = (double)i / (double)m;
      Coordinate coordinate = lineSegment.pointAlong(segmentLengthFraction);
      ans.add(Point.getInstance(coordinate.getX(), coordinate.getY()));
    }
    return ans;
  }

  public int side(Point point) {
    return Point.ccw(p0, p1, point);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Segment)) {
      return false;
    }

    Segment segment = (Segment) o;

    return lineSegment.equals(segment.lineSegment);
  }

  @Override
  public int hashCode() {
    return lineSegment.hashCode();
  }

  @Override
  public String toString() {
    return "Segment{" +
        "lineSegment=" + lineSegment +
        '}';
  }
}
