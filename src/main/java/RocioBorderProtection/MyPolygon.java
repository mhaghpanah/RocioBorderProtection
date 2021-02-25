package RocioBorderProtection;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class MyPolygon {

  int n;
  Points points;

  GeometryFactory geometryFactory;

  Polygon polygon;
  List<Segment> border;

  Points convexHull;

  Double perimeter;

  public MyPolygon(Points points) {
    n = points.size();

    this.points = points;

    geometryFactory = new GeometryFactory();

    generatePolygon();
    generateBorder();

    convexHull = generateConvexHull();

    assert DoubleEpsilonCompare.compare(perimeter(), polygon.getLength()) == 0;
//    assert isSimple();
  }

  private void generatePolygon() {
    Coordinate[] coordinates = new Coordinate[n + 1];
    for (int i = 0; i < n + 1; i++) {
      Point point = points.get(i);
      coordinates[i] = new Coordinate(point.getX(), point.getY());
    }
    polygon = geometryFactory.createPolygon(coordinates);
  }

  private void generateBorder() {
    border = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      Point p0 = points.get(i);
      Point p1 = points.get(i + 1);
      Segment segment = new Segment(p0, p1);
      border.add(segment);
    }
  }

  public int size() {
    return n;
  }

  public double perimeter() {
    if (perimeter == null) {
      perimeter = 0.0;
      for (Segment b : border) {
        perimeter += b.getLength();
      }
    }
    return perimeter;
  }

  public Points generateConvexHull() {
    Polygon convexHull = (Polygon) polygon.convexHull();

    Coordinate[] coordinates = convexHull.getCoordinates();
    List<Point> ans = new ArrayList<>();
    for (Coordinate coordinate : coordinates) {
      ans.add(Point.getInstance(coordinate.getX(), coordinate.getY()));
    }
    return Points.getInstance(ans);
  }

  public List<Point> subdivision(int index, double epsilon) {
    Segment segment = new Segment(points.get(index), points.get(index + 1));
    return segment.subdivision(epsilon);
  }

  public boolean isSimple() {
    throw null;
  }

  public boolean isSegmentVisible(Segment segment) {
    for (Segment b : border) {
      if (segment.isIntersect(b)) {
        return false;
      }
    }
    return true;
  }

//  public boolean isSegmentOutside(Segment segment) {
//    Coordinate midPoint = segment.getLineSegment().midPoint();
//    org.locationtech.jts.geom.Point point = geometryFactory.createPoint(midPoint);
//    return !point.within(polygon);
//  }

  public boolean isOnConvexHull(Point point) {
//    Coordinate coordinate = new Coordinate(point.getX(), point.getY());
//    org.locationtech.jts.geom.Point point1 = geometryFactory.createPoint(coordinate);
//    Geometry intersection = convexHull.intersection(point1);
//    return intersection.isEmpty();
    int m = convexHull.size();
    for (int i = 0; i < m - 1; i++) {
      Point u = convexHull.get(i);
      Point v = convexHull.get(i + 1);
      if (DoubleEpsilonCompare.sign(Point.ccw(point, u, v)) == 0) {
        return true;
      }
    }
    return false;
  }

}
