package RocioBorderProtection;

import static OrderTypeGraph.IpeDraw.drawIpeMark;
import static OrderTypeGraph.IpeDraw.drawIpePath;
import static OrderTypeGraph.IpeDraw.getIpeConf;
import static OrderTypeGraph.IpeDraw.getIpeEnd;
import static OrderTypeGraph.IpeDraw.getIpePreamble;
import static OrderTypeGraph.IpeDraw.writeIpeText;

import OrderTypeGraph.MyFileWriter;
import RocioBorderProtection.Edge.EdgeType;
import java.util.List;

public class MapDrawer {

  final double drawSize = 700;
  final double o = 200;

  double scaleFactor;
  double minX;
  double minY;
  double maxX;
  double maxY;

  StringBuilder output;

  List<Point> pointList;
  List<List<Edge>> path;
  boolean addText;


  public MapDrawer(List<Point> pointList, boolean addText) {
    this.pointList = pointList;
    this.addText = addText;

    scaleFactor = computeScaleFactor();
    output = new StringBuilder();

    output.append(getIpePreamble());
    output.append(getIpeConf());
    drawPoints();
    drawEdges();
//    output.append(getIpeEnd());
  }

  public MapDrawer(List<Point> pointList, List<List<Edge>> path, boolean addText) {
    this(pointList, addText);
    this.path = path;
    drawPath();
  }

  public static String draw(List<Point> pointList, boolean addText) {
    MapDrawer mapDrawer = new MapDrawer(pointList, addText);
    return mapDrawer.output();
  }

  public static void drawAndWrite(List<Point> pointList, String suffixPathOutput, boolean addText) {
    MyFileWriter.write(suffixPathOutput + ".ipe", MapDrawer.draw(pointList, addText));
  }

  public static String draw(List<Point> pointList, List<List<Edge>> path, boolean addText) {
    MapDrawer mapDrawer = new MapDrawer(pointList, path, addText);
    return mapDrawer.output();
  }

  public static void drawAndWrite(List<Point> pointList, List<List<Edge>> path,
      String suffixPathOutput, boolean addText) {
    MyFileWriter.write(suffixPathOutput + ".ipe", MapDrawer.draw(pointList, path, addText));
  }

  private double computeScaleFactor() {
    minX = Long.MAX_VALUE;
    minY = Long.MAX_VALUE;
    maxX = Long.MIN_VALUE;
    maxY = Long.MIN_VALUE;
    for (Point point : pointList) {
      minX = Math.min(minX, point.getX());
      minY = Math.min(minY, point.getY());

      maxX = Math.max(maxX, point.getX());
      maxY = Math.max(maxY, point.getY());
    }

    double deltaX = maxX - minX;
    double deltaY = maxY - minY;
    return drawSize / Math.max(deltaX, deltaY);
//    return Math.min(1.0, drawSize / (maxCoordinate - minCoordinate));
//    return drawSize / (maxCoordinate - minCoordinate);

  }

  private int scaleX(double t) {
    return (int) (((t - minX) * scaleFactor) + o);
//    return (int) (t / 3 + o);
  }

  private int scaleY(double t) {
    return (int) (((t - minY) * scaleFactor) + o);
  }

  private void drawEdge(Point u, Point v, String color, String pen, String dash) {
    int ux = scaleX(u.getX());
    int uy = scaleY(u.getY());

    int vx = scaleX(v.getX());
    int vy = scaleY(v.getY());

    output.append(drawIpePath(new int[]{ux, vx}, new int[]{uy, vy}, color, pen, dash));
  }

  private void drawPoint(Point point, String shape, String color, String size) {
    int x = scaleX(point.getX());
    int y = scaleY(point.getY());
    output.append(drawIpeMark(x, y, shape, color, size));
  }

  private void drawText(String string, Point point, String color, String size) {
    int eps = 3;
    int x = scaleX(point.getX());
    int y = scaleY(point.getY());
    output.append(writeIpeText(string, x - eps, y + eps, color, size));
  }

  private void drawPath() {
    String shape = "disk";
    String color = "red";
    String size = "normal";

    for (List<Edge> edgeList : path) {
      Vertex u = edgeList.get(0).getU();
      Point point = u.getPoint();
      drawPoint(point, shape, color, size);
    }

    String pen = "normal";
    String dash_normal = "normal";
    String dash_dashed = "dashed";
    String dash_dotted = "dotted";

    for (List<Edge> edgeList : path) {
      for (Edge edge : edgeList) {
        Point u = edge.getU().getPoint();
        Point v = edge.getV().getPoint();
        if (edge.getEdgeType().equals(EdgeType.BOUNDARY)) {
//         String dash = dash_normal;
//         drawEdge(u, v, color, pen, dash);
        } else {
          String dash = dash_normal;
          drawEdge(u, v, color, pen, dash);
        }
      }
    }

  }

  private void drawEdges() {

    String color = "black";
    String pen = "normal";
    String dash = "normal";

    for (int i = 0; i < pointList.size() - 1; i++) {
      Point u = pointList.get(i);
      Point v = pointList.get(i + 1);

      drawEdge(u, v, color, pen, dash);
    }
    System.out.printf("here pointlist %s\n", pointList.get(0));
    System.out.printf("here pointlist %s\n", pointList.get(pointList.size() - 1));

  }

  private void drawPoints() {
    String shape = "disk";
    String color = "black";
    String size = "normal";

    int index = 0;
    for (Point point : pointList) {
      drawPoint(point, shape, color, size);
      if (addText) {
        drawText(Integer.toString(index++), point, color, size);
      }
    }
  }

  public String output() {
    StringBuilder ans = new StringBuilder(output);
    ans.append(getIpeEnd());
    return ans.toString();
  }

}
