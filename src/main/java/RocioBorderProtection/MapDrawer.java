package RocioBorderProtection;

import static RocioBorderProtection.IpeDraw.drawIpeMark;
import static RocioBorderProtection.IpeDraw.drawIpePath;
import static RocioBorderProtection.IpeDraw.getIpeConf;
import static RocioBorderProtection.IpeDraw.getIpeEnd;
import static RocioBorderProtection.IpeDraw.getIpePreamble;
import static RocioBorderProtection.IpeDraw.writeIpeText;

import RocioBorderProtection.Edge.EdgeType;
import java.util.Collection;
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

//    String color = "red";
//    String pen = "normal";
//    String dash = "normal";
//
//    drawEdge(points.get(591), points.get(592), color, pen, dash);
//    drawEdge(points.get(592), points.get(593), color, pen, dash);

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
    drawVertexes(graph.getVertexList());
    drawPolygonEdges(points);

//    List<List<Edge>> ll = new ArrayList<>();
//    ll.add(graph.getAdjacencyMap().get(graph.getVertex(1369)));
//    drawEdges(ll);
    drawEdges(graph.getAdjacencyMap().values());
    drawPath(path);

    MyPolygon myPolygon = new MyPolygon(points);
    drawConvexHull(myPolygon.generateConvexHull());

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
    maxX = Long.MIN_VALUE;
    maxY = Long.MIN_VALUE;
    for (Point point : points.getPointList()) {
      minX = Math.min(minX, point.getX());
      minY = Math.min(minY, point.getY());

      maxX = Math.max(maxX, point.getX());
      maxY = Math.max(maxY, point.getY());
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
    double x = scaleX(point.getX());
    double y = scaleY(point.getY());
    output.append(writeIpeText(string, x - eps, y + eps, color, size));
  }

  private void drawPoint(Point point, String shape, String color, String size) {
    double x = scaleX(point.getX());
    double y = scaleY(point.getY());
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
    String shape = "disk";
    String color = "black";
    String size = "normal";

    drawPoints(points, shape, color, size);
  }


  private void drawVertexes(List<Vertex> vertexList) {
    String shape = "disk";
    String colorBlack = "black";
    String colorBlue = "blue";
    String size = "normal";

    int index = 0;
    for (Vertex vertex : vertexList) {
      Point point = vertex.getPoint();

      String color = vertex.isOnConvexHull() ? colorBlue : colorBlack;
//      if (vertex.getId() == 1369 || (1380 <= vertex.getId() && vertex.getId() <= 1384)) color = "red";

      drawPoint(point, shape, color, size);
      if (addText) {
        if (index % 5 == 0) {
          drawText(Integer.toString(index), point, color, size);
        }
        index++;
      }
    }
  }

  private void drawEdge(Point u, Point v, String color, String pen, String dash) {
    double ux = scaleX(u.getX());
    double uy = scaleY(u.getY());

    double vx = scaleX(v.getX());
    double vy = scaleY(v.getY());

    output.append(drawIpePath(new double[]{ux, vx}, new double[]{uy, vy}, color, pen, dash));
  }

  private void drawPolygonEdges(Points points, String color, String pen, String dash) {
    int n = points.size();
    for (int i = 0; i < n; i++) {
      Point u = points.get(i);
      Point v = points.get(i + 1);
//      if (i % 2 == 0) {
//        color = "green";
//      } else {
//        color = "red";
//      }
      drawEdge(u, v, color, pen, dash);
    }
  }

  private void drawPolygonEdges(Points points) {
    String color = "black";
    String pen = "normal";
    String dash = "normal";
    drawPolygonEdges(points, color, pen, dash);
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
    String color = "black";
    String pen = "normal";
    String dash = "normal";
    drawEdges(edges, color, pen, dash);
  }

  private void drawPath(List<List<Edge>> path) {
    String shape = "disk";
    String color = "red";
    String size = "normal";

    int index = 0;
    for (List<Edge> edgeList : path) {
      Vertex u = edgeList.get(0).getU();
      Point point = u.getPoint();
      drawPoint(point, shape, color, size);

      if (addText) {
        if (index % 10 == 0) {
          drawText(Integer.toString(index), point, color, size);
        }
        index++;
      }
    }

    String pen = "normal";
    String dash_normal = "normal";
//    String dash_dashed = "dashed";
    String dash_dotted = "dotted";

    for (List<Edge> edgeList : path) {
      for (Edge edge : edgeList) {
        Point u = edge.getU().getPoint();
        Point v = edge.getV().getPoint();
        if (edge.getEdgeType().equals(EdgeType.BOUNDARY)) {
          drawEdge(u, v, color, pen, dash_dotted);
        } else {
          drawEdge(u, v, color, pen, dash_normal);
        }
      }
    }

  }


  private void drawConvexHull(Points points) {
//    String shape = "disk";
//    String colorPoints = "seagreen";
//    String size = "normal";
//    drawPoints(points, shape, colorPoints, size);

    String colorEdges = "seagreen";
    String pen = "normal";
    String dash = "dashed";
    drawPolygonEdges(points, colorEdges, pen, dash);

  }

  public String output() {
    return output + getIpeEnd();
  }

}
