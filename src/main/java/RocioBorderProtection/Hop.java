package RocioBorderProtection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Hop {

  Graph graph;
  double capacity;
  double INF;
  int n;
  Vertex start;
  Vertex end;

  public Hop(Graph graph, double capacity) {
    this.graph = graph;
    this.capacity = capacity;
    INF = 2 * capacity + 1_000.0;

    n = graph.vertexSize();
    start = graph.getVertex(0);
    end = graph.getVertex(n - 1);
    assert start.getPoint().equals(end.getPoint());
  }

  public static List<List<Edge>> run(Graph graph, double capacity) {
    Hop hop = new Hop(graph, capacity);
    return hop.hopAlgorithm();
  }

  public Map<Vertex, Edge> reachableVertexSet(Vertex v0) {
    TreeSet<Pair<Vertex, Double>> set = new TreeSet<>(
        (a, b) -> Double.compare(a.getValue(), b.getValue()) == 0 ?
            a.getKey().getId() - b.getKey().getId() : Double.compare(a.getValue(), b.getValue()));
    Map<Vertex, Edge> parent = new HashMap<>();
    Map<Vertex, Double> distance = new HashMap<>();

    parent.put(v0, null);
    distance.put(v0, 0.0);
    set.add(Pair.getInstance(v0, 0.0));

    while (!set.isEmpty()) {
      Pair<Vertex, Double> top = set.pollFirst();
      assert top != null;
      Vertex u = top.getKey();
      double d = top.getValue();

      for (Edge edge : graph.neighbours(u)) {
        double len = edge.getDist() + d;
        Vertex v = edge.getV();

        assert u.getId() >= v0.getId();
        assert v.getId() > u.getId();
        if (DoubleEpsilonCompare.compare(len, capacity) > 0) {
          continue;
        }

        if (DoubleEpsilonCompare.compare(len, distance.getOrDefault(v, INF)) <= 0) {
          set.remove(Pair.getInstance(v, distance.getOrDefault(v, INF)));

          distance.put(v, len);
          parent.put(v, edge);
          set.add(Pair.getInstance(v, distance.get(v)));
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
    if (parent.containsKey(end)) {
      return end;
    }
    Vertex next = null;
    Vertex nextWitness = null;

    for (Vertex v : set) {
      if (curr.equals(v)) {
        continue;
      }
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
    Vertex curr = graph.getVertex(0);
    List<List<Edge>> path = new ArrayList<>();

    while (!curr.equals(end)) {
      System.err.println("--------------------------------");
      System.err.printf("curr: %s\n", curr);
      Map<Vertex, Edge> parent = reachableVertexSet(curr);

      Vertex next = selectNextVertex(curr, parent);

      System.err.printf("next: %s\n", next);

      List<Edge> p = generatePath(curr, next, parent);
      path.add(p);

      assert !curr.equals(next);
      curr = next;
    }
    return path;
  }

}
