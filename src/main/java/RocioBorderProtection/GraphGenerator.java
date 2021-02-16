package RocioBorderProtection;

import RocioBorderProtection.Edge.EdgeType;
import java.util.List;

public class GraphGenerator {

  List<Point> pointList;
  double capacity;
  double epsilon;

  MyPolygon myPolygon;

  Graph graph;

  public GraphGenerator(List<Point> pointList, double capacity, double epsilon) {
    int n = pointList.size();
    assert pointList.get(0).equals(pointList.get(n - 1));

    this.pointList = pointList;
    this.capacity = capacity;
    this.epsilon = epsilon;

//    border = new ArrayList<>();
    myPolygon = new MyPolygon(pointList);
    graph = new Graph();

//    generateBorder();
//    assert isSimple();
    generateVertices();
    generateEdges();
  }

//
//  public boolean isSimple() {
//    int n = border.size();
//    for (int i = 0; i < n; i++) {
//      if (border.get(i).isEmpty()) {
//        return false;
//      }
//    }
//
//    for (int i = 0; i < n; i++) {
//      for (int j = i + 1; j < n; j++) {
//        Segment s0 = border.get(i);
//        Segment s1 = border.get(j);
//        if (s0.isIntersect(s1)) {
//          return false;
//        }
//      }
//    }
//    return true;
//  }

  public static Graph GraphGenerator(List<Point> pointList, double unitDist, double eps) {
    GraphGenerator gg = new GraphGenerator(pointList, unitDist, eps);
    return gg.graph;
  }

  private void generateVertices() {
    int n = myPolygon.size();
    int t = 0;
    for (int i = 0; i < n; i++) {
      List<Point> decompose = myPolygon.subdivision(i, epsilon);
      System.out.printf("Subdivide %d into %d parts\n", i, decompose.size());
      assert decompose.size() > 0;
      for (Point point : decompose) {
        Vertex vertex = new Vertex(t++, point, i);
        graph.addVertex(vertex);
      }
      System.out.printf("size: %d\n", graph.vertexSize());
    }

    Point point = pointList.get(n);
    Vertex vertex = new Vertex(t++, point, n - 1);
    graph.addVertex(vertex);
    System.out.printf("size: %d\n", graph.vertexSize());

    System.out.println(pointList.get(0));
    System.out.println(pointList.get(n));

    System.out.println(graph.getVertex(0).getPoint());
    System.out.println(graph.getVertex(graph.vertexSize() - 1).getPoint());

    assert graph.getVertex(0).getPoint().equals(
        graph.getVertex(graph.vertexSize() - 1).getPoint());
  }

  public boolean isLeftSide(int i, int j, Segment segment) {
    for (int k = i + 1; k < j; k++) {
      Vertex v = graph.getVertex(k);
      if (segment.side(v.getPoint()) <= 0) {
        return false;
      }
    }
    return true;
  }

  private void generateEdges() {
    int n = graph.vertexSize();
    for (int i = 0; i < n; i++) {
      Vertex u = graph.getVertex(i);
      for (int j = i + 1; j < n; j++) {
        Vertex v = graph.getVertex(j);
        Segment segment = new Segment(u.getPoint(), v.getPoint());
        double len = segment.getLength();
        if (u.getSubdivisionId() == v.getSubdivisionId() ||
            (u.getSubdivisionId() + 1 == v.getSubdivisionId() && i + 1 == j)) {
          if (j == i + 1) {
            Edge edge = new Edge(u, v, len, EdgeType.BOUNDARY);
            graph.addEdge(edge);
          }
        } else if (DoubleEpsilonCompare.compare(len, 0) > 0 && 
            DoubleEpsilonCompare.compare(len, capacity) < 0 && 
            myPolygon.isSegmentVisible(segment) && 
            myPolygon.isSegmentOutside(segment) && 
            isLeftSide(i, j, segment)) {
          Edge edge = new Edge(u, v, len, EdgeType.EXTERIOR);
          graph.addEdge(edge);
        }

      }
    }
    System.out.printf("sz: %d\n", graph.edgeSize());
  }

}
