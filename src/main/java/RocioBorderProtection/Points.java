package RocioBorderProtection;

import java.util.ArrayList;
import java.util.List;

public class Points {

  int n;
  List<Point> pointList;

  public Points(List<Point> pointList) {
    n = pointList.size() - 1;
    assert n > 0;
    assert pointList.get(0).equals(pointList.get(n));
    this.pointList = pointList;
  }

  public static Points getInstance(List<Point> pointList) {
    return new Points(pointList);
  }

  public static Points movePoints(Points points, int delta) {
    int n = points.size();
    List<Point> ans = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      ans.add(points.get((i + delta) % n));
    }
    ans.add(points.get(delta));
    return Points.getInstance(ans);
  }

  public Point get(int index) {
    assert index < pointList.size();
    return pointList.get(index);
  }

  public int size() {
    return n;
  }

  public List<Point> getPointList() {
    return pointList;
  }

  @Override
  public String toString() {
    return "Points{" +
        "n=" + n +
        ", pointList=" + pointList +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Points)) {
      return false;
    }

    Points points = (Points) o;

    if (n != points.n) {
      return false;
    }
    return pointList.equals(points.pointList);
  }

  @Override
  public int hashCode() {
    int result = n;
    result = 31 * result + pointList.hashCode();
    return result;
  }
}
