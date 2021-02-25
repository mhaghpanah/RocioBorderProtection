package RocioBorderProtection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private final List<Vertex> vertexList;
  private final Map<Vertex, List<Edge>> adjacencyMap;

  private int n;
  private int m;

  public Graph() {
    vertexList = new ArrayList<>();
    adjacencyMap = new HashMap<>();
    n = 0;
    m = 0;
  }

  public List<Vertex> getVertexList() {
    return vertexList;
  }

  public Map<Vertex, List<Edge>> getAdjacencyMap() {
    return adjacencyMap;
  }

  public Points getPoints() {
    List<Point> ans = new ArrayList<>();
    for (Vertex vertex : getVertexList()) {
      ans.add(vertex.getPoint());
    }
    return Points.getInstance(ans);
  }

  public Vertex getVertex(int index) {
    assert index < vertexSize();
    return vertexList.get(index);
  }

  public int vertexSize() {
    return n;
  }

  public int edgeSize() {
    return m;
  }

  public void addVertex(Vertex vertex) {
    vertexList.add(vertex);
    n++;
  }

  public void addEdge(Edge edge) {
    Vertex vertex = edge.getU();
    adjacencyMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(edge);
    m++;
  }

  public List<Edge> neighbours(Vertex vertex) {
    return adjacencyMap.getOrDefault(vertex, new ArrayList<>());
  }

  @Override
  public String toString() {
    return "Graph{" +
        "n=" + n +
        ", m=" + m +
        ", vertexList=" + vertexList +
        ", adjacencyMap=" + adjacencyMap +
        '}';
  }
}
