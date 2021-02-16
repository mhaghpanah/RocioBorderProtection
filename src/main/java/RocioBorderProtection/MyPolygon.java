package RocioBorderProtection;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class MyPolygon {

  int n;
  List<Point> pointList;

  GeometryFactory geometryFactory;

  Polygon polygon;
  List<Segment> border;

  public MyPolygon(List<Point> pointList) {
    n = pointList.size() - 1;
    this.pointList = pointList;

    geometryFactory = new GeometryFactory();

    generatePolygon();
    generateBorder();

//    assert isSimple();
  }

  private void generatePolygon() {
    Coordinate[] coordinates = new Coordinate[n + 1];
    for (int i = 0; i < n + 1; i++) {
      Point point = pointList.get(i);
      coordinates[i] = new Coordinate(point.getX(), point.getY());
    }
    polygon = geometryFactory.createPolygon(coordinates);
    System.out.println(polygon);

//    return coordinates;
  }

  private void generateBorder() {
    border = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      Point p0 = pointList.get(i);
      Point p1 = pointList.get(i + 1);
      Segment segment = new Segment(p0, p1);
      border.add(segment);
    }
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%");
    System.out.println(border);
  }

  public int size() {
    return n;
  }

  public List<Point> subdivision(int index, double epsilon) {
//    Segment segment = new Segment(pointList.get(index), pointList.get(index + 1));
//    return segment.subdivision(epsilon);
    return border.get(index).subdivision(epsilon);
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

  public boolean isSegmentOutside(Segment segment) {
    Coordinate midPoint = segment.getLineSegment().midPoint();
    org.locationtech.jts.geom.Point point = geometryFactory.createPoint(midPoint);
    return !point.within(polygon);
  }

}
