package RocioBorderProtection;

import static RocioBorderProtection.IpeDraw.drawIpeMark;
import static RocioBorderProtection.IpeDraw.drawIpePath;
import static RocioBorderProtection.IpeDraw.getIpeConf;
import static RocioBorderProtection.IpeDraw.getIpeEnd;
import static RocioBorderProtection.IpeDraw.getIpePreamble;
import static RocioBorderProtection.IpeDraw.writeIpeText;

import java.util.Collection;
import java.util.List;

public class MapDrawer {

  private static final String blackColor = "black";
  private static final String blueColor = "blue";
  private static final String redColor = "red";
  private static final String seagreenColor = "seagreen";
  private static final String greenColor = "green";

  private static final String normalDash = "normal";
  private static final String dashedDash = "dashed";
  private static final String dottedDash = "dotted";

  private static final String defaultShape = "disk";
  private static final String defaultColor = blackColor;
  private static final String defaultSize = "normal";
  private static final String defaultPen = "normal";
  private static final String defaultDash = normalDash;

  final double drawSize = 700;
  final double o = 200;

  private final double scaleFactor;
  private double minX;
  private double minY;

  StringBuilder output;

  Points points;
  List<List<Edge>> path;
  Graph graph;
  boolean addText;


  public MapDrawer(Points points, boolean addText) {
    this.points = points;
    this.addText = addText;

    scaleFactor = computeScaleFactor(points);
    output = new StringBuilder();

    output.append(getIpePreamble());
    output.append(getIpeConf());
    drawPoints(points);
    drawPolygonEdges(points);

//    output.append(getIpeEnd());
  }

  public MapDrawer(Points points, List<List<Edge>> path, boolean addText) {
    this(points, addText);
    this.path = path;
    drawPath(path);
  }

  public MapDrawer(Graph graph, List<List<Edge>> path, boolean addText) {
    this.graph = graph;
    this.path = path;
    this.addText = addText;
    this.points = graph.getPoints();

    scaleFactor = computeScaleFactor(points);
    output = new StringBuilder();

    output.append(getIpePreamble());
    output.append(getIpeConf());
//    drawVertexes(graph.getVertexList());
    drawPolygonEdges(points);

//    drawEdges(graph.getAdjacencyMap().values());
    drawPath(path);

//    MyPolygon myPolygon = new MyPolygon(points);
//    drawConvexHull(myPolygon.generateConvexHull());

  }

  public static String draw(Points points, boolean addText) {
    MapDrawer mapDrawer = new MapDrawer(points, addText);
    return mapDrawer.output();
  }

  public static void drawAndWrite(Points points, String suffixPathOutput, boolean addText) {
    MyFileWriter.write(suffixPathOutput + ".ipe", MapDrawer.draw(points, addText));
  }

  public static String draw(Points points, List<List<Edge>> path, boolean addText) {
    MapDrawer mapDrawer = new MapDrawer(points, path, addText);
    return mapDrawer.output();
  }

  public static void drawAndWrite(Points points, List<List<Edge>> path,
      String suffixPathOutput, boolean addText) {
    MyFileWriter.write(suffixPathOutput + ".ipe", MapDrawer.draw(points, path, addText));
  }

  public static String draw(Graph graph, List<List<Edge>> path, boolean addText) {
    MapDrawer mapDrawer = new MapDrawer(graph, path, addText);
    return mapDrawer.output();
  }

  public static void drawAndWrite(Graph graph, List<List<Edge>> path, String suffixPathOutput,
      boolean addText) {
    MyFileWriter.write(suffixPathOutput + ".ipe", MapDrawer.draw(graph, path, addText));
  }

  private double computeScaleFactor(Points points) {
    minX = Long.MAX_VALUE;
    minY = Long.MAX_VALUE;
    double maxX = Long.MIN_VALUE;
    double maxY = Long.MIN_VALUE;
    for (Point point : points.getPointList()) {
      minX = Math.min(minX, point.x());
      minY = Math.min(minY, point.y());

      maxX = Math.max(maxX, point.x());
      maxY = Math.max(maxY, point.y());
    }

    double deltaX = maxX - minX;
    double deltaY = maxY - minY;
    return drawSize / Math.max(deltaX, deltaY);
  }

  private double scaleX(double t) {
    return (((t - minX) * scaleFactor) + o);
  }

  private double scaleY(double t) {
    return (((t - minY) * scaleFactor) + o);
  }

  private void drawText(String string, Point point, String color, String size) {
    double eps = 3;
    double x = scaleX(point.x());
    double y = scaleY(point.y());
    output.append(writeIpeText(string, x - eps, y + eps, color, size));
  }

  private void drawPoint(Point point, String shape, String color, String size) {
    double x = scaleX(point.x());
    double y = scaleY(point.y());
    output.append(drawIpeMark(x, y, shape, color, size));
  }

  private void drawPoints(Points points, String shape, String color, String size) {
    int index = 0;
    for (Point point : points.getPointList()) {
      drawPoint(point, shape, color, size);
      if (addText) {
        if (index % 5 == 0) {
          drawText(Integer.toString(index), point, color, size);
        }
        index++;
      }
    }
  }

  private void drawPoints(Points points) {
    drawPoints(points, defaultShape, defaultColor, defaultSize);
  }


  private void drawVertexes(List<Vertex> vertexList) {
    int index = 0;
    for (Vertex vertex : vertexList) {
      Point point = vertex.getPoint();
      String color = vertex.isOnConvexHull() ? blueColor : blackColor;

      drawPoint(point, defaultShape, color, defaultSize);
      if (addText) {
        if (index % 5 == 0) {
          drawText(Integer.toString(index), point, color, defaultSize);
        }
        index++;
      }
    }
  }

  private void drawEdge(Point u, Point v, String color, String pen, String dash) {
    double ux = scaleX(u.x());
    double uy = scaleY(u.y());

    double vx = scaleX(v.x());
    double vy = scaleY(v.y());

    output.append(drawIpePath(new double[]{ux, vx}, new double[]{uy, vy}, color, pen, dash));
  }

  private void drawPolygonEdges(Points points, String color, String pen, String dash) {
    int n = points.size();
    for (int i = 0; i < n; i++) {
      Point u = points.get(i);
      Point v = points.get(i + 1);
//      if (i % 2 == 0) {
//        color = greenColor;
//      } else {
//        color = redColor;
//      }
      drawEdge(u, v, color, pen, dash);
    }
  }

  private void drawPolygonEdges(Points points) {
    drawPolygonEdges(points, defaultColor, defaultPen, defaultDash);
  }


  private void drawEdges(Collection<List<Edge>> edges, String color, String pen, String dash) {
    for (List<Edge> edgeList : edges) {
      for (Edge edge : edgeList) {
        Point u = edge.getU().getPoint();
        Point v = edge.getV().getPoint();

        drawEdge(u, v, color, pen, dash);
      }
    }
  }

  private void drawEdges(Collection<List<Edge>> edges) {
    drawEdges(edges, defaultColor, defaultPen, defaultDash);
  }

  private void drawPath(List<List<Edge>> path) {
    String color = redColor;

    int index = 0;
    for (List<Edge> edgeList : path) {
      Vertex u = edgeList.get(0).getU();
      Point point = u.getPoint();
      drawPoint(point, defaultShape, color, defaultSize);

      if (addText) {
//        if (index % 10 == 0) {
//          drawText(Integer.toString(index), point, color, defaultSize);
//        }
        drawText(Integer.toString(index), point, color, defaultSize);
        index++;
      }
    }

    for (List<Edge> edgeList : path) {
      for (Edge edge : edgeList) {
        Point u = edge.getU().getPoint();
        Point v = edge.getV().getPoint();

//        String dash = edge.getEdgeType().equals(EdgeType.BOUNDARY) ? dottedDash : normalDash;
        String dash = defaultDash;

        drawEdge(u, v, color, defaultPen, dash);
      }
    }

  }


  private void drawConvexHull(Points points) {
    String color = seagreenColor;
    String dash = dashedDash;

//    drawPoints(points, defaultShape, color, defaultSize);

    drawPolygonEdges(points, color, defaultPen, dash);

  }

  public String output() {
    return output + getIpeEnd();
  }

}
