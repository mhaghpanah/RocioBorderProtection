package RocioBorderProtection;

public class Vertex {

  private final Point point;
  private final int subdivisionId;
  private final int id;
  private final boolean isOnConvexHull;

  public Vertex(int id, Point point, int subdivisionId, boolean isOnConvexHull) {
    this.id = id;
    this.point = point;
    this.subdivisionId = subdivisionId;
    this.isOnConvexHull = isOnConvexHull;
  }

  public int getSubdivisionId() {
    return subdivisionId;
  }

  public Point getPoint() {
    return point;
  }

  public int getId() {
    return id;
  }

  public boolean isOnConvexHull() {
    return isOnConvexHull;
  }

  @Override
  public String toString() {
    return "Vertex{" +
//        "point=" + point +
        "id=" + id +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Vertex)) {
      return false;
    }

    Vertex vertex = (Vertex) o;

    if (subdivisionId != vertex.subdivisionId) {
      return false;
    }
    if (id != vertex.id) {
      return false;
    }
    if (isOnConvexHull != vertex.isOnConvexHull) {
      return false;
    }
    return point.equals(vertex.point);
  }

  @Override
  public int hashCode() {
    int result = point.hashCode();
    result = 31 * result + subdivisionId;
    result = 31 * result + id;
    result = 31 * result + (isOnConvexHull ? 1 : 0);
    return result;
  }
}
