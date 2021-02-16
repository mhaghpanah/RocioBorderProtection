package RocioBorderProtection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Hop {

  Graph graph;
  double capacity;
  int n;
  Vertex start;
  Vertex end;

  public Hop(Graph graph, double capacity) {
    this.graph = graph;
    this.capacity = capacity;

    n = graph.vertexSize();
    start = graph.getVertex(0);
    end = graph.getVertex(n - 1);
  }

  public Map<Vertex, Edge> reachableVertexSet(Vertex v0) {
    PriorityQueue<Pair<Vertex, Double>> pq = new PriorityQueue<>((a, b) -> Double.compare(a.getValue(), b.getValue()));
    Map<Vertex, Edge> parent = new HashMap<>();
    parent.put(v0, null);
    pq.add(new Pair<>(v0, Double.valueOf(0.0)));
    while (!pq.isEmpty()) {
      Pair<Vertex, Double> top = pq.remove();
      Vertex u = top.getKey();

      for (Edge edge : graph.neighbours(u)) {
        double dist = edge.getDist() + top.getValue();
        Vertex v = edge.getV();

        assert u.getId() >= v0.getId();
        assert v.getId() > u.getId();

        if (DoubleEpsilonCompare.compare(dist, capacity) <= 0 && !parent.containsKey(v)) {
          parent.put(v, edge);
          pq.add(new Pair<>(v, dist));
        }
      }
    }

    assert (v0.equals(end) ? parent.size() == 1 : parent.size() > 1);
    return parent;
  }

  public Vertex farthestVertex(Vertex v) {
    Set<Vertex> set = reachableVertexSet(v).keySet();
    Vertex ans = null;
    for (Vertex vertex : set) {
      if (ans == null || vertex.getId() > ans.getId()) {
        ans = vertex;
      }
    }

    assert (v.equals(end) ? ans.getId() == v.getId() : ans.getId() > v.getId());
    return ans;
  }

  public Vertex selectNextVertex(Vertex curr, Map<Vertex, Edge> parent) {
    Set<Vertex> set = parent.keySet();
    Vertex next = null;
    Vertex nextWitness = null;

    for (Vertex v : set) {
      if (curr.equals(v)) continue;
      Vertex vWitness = farthestVertex(v);
      if (next == null || vWitness.getId() > nextWitness.getId()) {
        next = v;
        nextWitness = vWitness;
      }
    }
    return next;
  }

  public List<Edge> generatePath(Vertex s, Vertex e, Map<Vertex, Edge> parent) {
    List<Edge> ans = new ArrayList<>();
    while (!e.equals(s)) {
      Edge edge = parent.get(e);
      ans.add(edge);
      e = edge.getU();
    }
    Collections.reverse(ans);
    return ans;
  }

  public List<List<Edge>> hopAlgorithm() {
    int n = graph.vertexSize();
    Vertex curr = graph.getVertex(0);
//    Vertex last = graph.getVertex(n - 1);

    List<List<Edge>> path = new ArrayList<>();

    while (!curr.equals(end)) {
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      System.out.printf("curr: %s\n", curr);
      Map<Vertex, Edge> parent = reachableVertexSet(curr);

      System.out.printf("parent: %s\n", parent);

      Vertex next = selectNextVertex(curr, parent);

      System.out.printf("next: %s\n", next);

      List<Edge> p = generatePath(curr, next, parent);
      path.add(p);

      assert !curr.equals(next);
      curr = next;
    }
    return path;
  }

  public static List<List<Edge>> run(Graph graph, double capacity) {
    Hop hop = new Hop(graph, capacity);
    return hop.hopAlgorithm();
  }

}
