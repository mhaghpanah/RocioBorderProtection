package RocioBorderProtection;

import RocioBorderProtection.Edge.EdgeType;
import java.util.List;

public class GraphGenerator {

  Points points;
  double capacity;
  double epsilon;

  MyPolygon myPolygon;

  Graph graph;

  public GraphGenerator(Points points, double capacity, double epsilon) {
    this.points = points;
    this.capacity = capacity;
    this.epsilon = epsilon;

    myPolygon = new MyPolygon(points);
    graph = new Graph();

    generateVertices();
    generateEdges();
  }

  public static Graph generate(Points points, double capacity, double epsilon) {
    GraphGenerator gg = new GraphGenerator(points, capacity, epsilon);
    return gg.graph;
  }

//  public static Graph generate(Points points, int delta, double capacity, double epsilon) {
//    Points points1 = Points.movePoints(points, delta);
//    GraphGenerator gg = new GraphGenerator(points1, capacity, epsilon);
//    return gg.graph;
//  }

  private void generateVertices() {
    int n = myPolygon.size();
    int t = 0;
    for (int i = 0; i < n; i++) {
      List<Point> decompose = myPolygon.subdivision(i, epsilon);
//      System.out.printf("Subdivide %d into %d parts\n", i, decompose.size());
      assert decompose.size() > 0;
      for (Point point : decompose) {
        boolean isOnConvexHull = myPolygon.isOnConvexHull(point);
        Vertex vertex = new Vertex(t++, point, i, isOnConvexHull);
        graph.addVertex(vertex);
      }
//      System.out.printf("size: %d\n", graph.vertexSize());
    }

    Point point = points.get(n);
    boolean isOnConvexHull = myPolygon.isOnConvexHull(point);
    Vertex vertex = new Vertex(t, point, n - 1, isOnConvexHull);
    graph.addVertex(vertex);
//    System.out.printf("size: %d\n", graph.vertexSize());

    assert graph.getVertex(0).getPoint().equals(
        graph.getVertex(graph.vertexSize() - 1).getPoint());
  }

//  public boolean isLeftSide(int i, int j, Segment segment) {
//    for (int k = i + 1; k < j; k++) {
//      Vertex v = graph.getVertex(k);
//      if (segment.side(v.getPoint()) < 0) {
//        return false;
//      }
//    }
//    return true;
//  }

  public double curve(Point a, Point b) {
    return (b.x() - a.x()) * (a.y() + b.y());
  }

  public boolean isCCWSubChain(int i, int j) {
    double sum = 0.0;

    for (int k = i; k < j; k++) {
      sum += curve(graph.getVertex(k).getPoint(), graph.getVertex(k + 1).getPoint());
    }
    sum += curve(graph.getVertex(j).getPoint(), graph.getVertex(i).getPoint());

    return DoubleEpsilonCompare.sign(sum) >= 0;
  }


  private Edge generateEdge(Vertex u, Vertex v) {
    Segment segment = new Segment(u.getPoint(), v.getPoint());
    double len = segment.getLength();

    assert (u.getId() == 0 && v.getId() == graph.vertexSize() - 1)
        || DoubleEpsilonCompare.compare(len, 0) > 0;

    if (DoubleEpsilonCompare.compare(len, capacity) >= 0) {
      return null;
    }

    if (u.getId() + 1 == v.getId()) {
      return new Edge(u, v, len, EdgeType.BOUNDARY);
    }

    if (u.getSubdivisionId() == v.getSubdivisionId()) {
      return null;
    }

    if (
//        DoubleEpsilonCompare.compare(len, 0) > 0 &&
//        DoubleEpsilonCompare.compare(len, capacity) <= 0 &&
        myPolygon.isSegmentVisible(segment) &&
//        myPolygon.isSegmentOutside(segment) &&
            isCCWSubChain(u.getId(), v.getId())
//            isLeftSide(u.getId(), v.getId(), segment)
    ) {
      return new Edge(u, v, len, EdgeType.EXTERIOR);
    }

    return null;

  }

  private void generateEdges() {
    int n = graph.vertexSize();
    for (int i = 0; i < n; i++) {
      Vertex u = graph.getVertex(i);
      for (int j = i + 1; j < n; j++) {
        if (i == 0 && j == n - 1) {
          continue;
        }
        Vertex v = graph.getVertex(j);
        Edge edge = generateEdge(u, v);
        if (edge != null) {
          graph.addEdge(edge);
        }

      }
    }
  }

}
