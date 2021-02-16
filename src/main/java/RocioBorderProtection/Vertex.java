package RocioBorderProtection;

public class Vertex {

  private final Point point;
  private final int subdivisionId;
  private final int id;

  public Vertex(int id, Point point, int subdivisionId) {
    this.point = point;
    this.subdivisionId = subdivisionId;
    this.id = id;
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

  @Override
  public String toString() {
    return "Vertex{" +
        "point=" + point +
        ", id=" + id +
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
    return point.equals(vertex.point);
  }

  @Override
  public int hashCode() {
    int result = point.hashCode();
    result = 31 * result + subdivisionId;
    result = 31 * result + id;
    return result;
  }
}
