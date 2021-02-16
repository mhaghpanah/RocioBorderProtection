package RocioBorderProtection;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class Segment {

  Point p0;
  Point p1;

  LineSegment lineSegment;
  public Segment(Point p0, Point p1) {
    this.p0 = p0;
    this.p1 = p1;
    lineSegment = new LineSegment(p0.getX(), p0.getY(), p1.getX(), p1.getY());
  }

  public boolean isEmpty() { return DoubleEpsilonCompare.equals(lineSegment.getLength(), 0.0); }

  public double getLength() {
    return lineSegment.getLength();
  }

  public LineSegment getLineSegment() {
    return lineSegment;
  }

//  public Point midPoint() {
//    Coordinate c = lineSegment.midPoint();
//    return new Point(c.getX(), c.getY());
//  }

  public boolean isIntersect(Segment segment) {
    LineIntersector lineIntersector = new RobustLineIntersector();

    LineSegment l0 = this.lineSegment;
    LineSegment l1 = segment.lineSegment;

    Coordinate p0 = l0.p0;
    Coordinate p1 = l0.p1;
    Coordinate p2 = l1.p0;
    Coordinate p3 = l1.p1;

    lineIntersector.computeIntersection(p0, p1, p2, p3);
    return lineIntersector.isInteriorIntersection();
  }


  public List<Point> subdivision(double eps) {
    List<Point> ans = new ArrayList<>();

    double length = getLength();
    int m = (int) Math.floor(length / eps);
    for (int i = 0; i <= m; i++) {
      double segmentLengthFraction = i * eps / length;
      Coordinate coordinate = lineSegment.pointAlong(segmentLengthFraction);
      Point point = new Point(coordinate.getX(), coordinate.getY());
      ans.add(point);
    }
    return ans;
  }

  public int side(Point point) {
//    Coordinate coordinate = new Coordinate(point.getX(), point.getY());
//    return lineSegment.orientationIndex(coordinate);
    return Point.orientation(p0, point, p1);
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

//  public static void main(String[] args) {
//    double eps = 1.0;
//    Point p0 = new Point(0, 0);
//    Point p1 = new Point(10.1, 0);
//    Segment segment = new Segment(p0, p1);
//    List<Point> pointList = segment.subdivision(eps);
//    System.out.println(pointList);
//  }

  @Override
  public String toString() {
    return "Segment{" +
        "lineSegment=" + lineSegment +
        '}';
  }
}
