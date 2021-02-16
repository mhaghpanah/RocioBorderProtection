package RocioBorderProtection;

public class Edge {

  private final Vertex u;
  private final Vertex v;
  private final double dist;
  private final EdgeType edgeType;
  public Edge(Vertex u, Vertex v, double dist, EdgeType edgeType) {
    this.u = u;
    this.v = v;
    this.dist = dist;
    this.edgeType = edgeType;
  }

  public Vertex getU() {
    return u;
  }

  public EdgeType getEdgeType() {
    return edgeType;
  }

  public Vertex getV() {
    return v;
  }

  public double getDist() {
    return dist;
  }

  @Override
  public String toString() {
    return "Edge{" +
        "u=" + u +
        ", v=" + v +
        ", dist=" + dist +
        ", edgeType=" + edgeType +
        '}';
  }

  public enum EdgeType {
    INTERIOR, EXTERIOR, BOUNDARY
  }
}
