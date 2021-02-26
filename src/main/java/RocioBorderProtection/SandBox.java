package RocioBorderProtection;

import java.util.List;

public class SandBox {

  public static Graph generateAndDraw(Points points, double capacity, double epsilon,
      String suffixPath) {

    MapDrawer.drawAndWrite(points, String.format("%s_original_points", suffixPath), true);

    Graph graph = GraphGenerator.generate(points, capacity, epsilon);
    MapDrawer.drawAndWrite(graph.getPoints(),
        String.format("%s_graph_cap%f_eps%f", suffixPath, capacity, epsilon), false);

    return graph;
  }

  public static void runHop(Graph graph, double capacity, String suffixPath) {

    List<List<Edge>> path = Hop.run(graph, capacity);

    MapDrawer.drawAndWrite(graph.getPoints(), path,
        String.format("%s_2hop_cap%f_sz%d_old", suffixPath, capacity, path.size()), true);

    MapDrawer.drawAndWrite(graph, path,
        String.format("%s_2hop_cap%f_sz%d", suffixPath, capacity, path.size()), true);

  }

  public static boolean isOptimal(Points points, double capacity, double epsilon) {
    Graph graph0 = GraphGenerator.generate(points, capacity, epsilon);
    List<List<Edge>> path0 = Hop.run(graph0, capacity);

    Graph graph1 = GraphGenerator.generate(points, capacity + 2 * epsilon, epsilon);
    List<List<Edge>> path1 = Hop.run(graph1, capacity);

    assert path1.size() >= path0.size();

    System.out.printf("Capacity: %f Epsilon: %f Path0 sz: %d Path1 sz: %d\n", capacity, epsilon, path0.size(), path1.size());

    return path1.size() == path0.size();
  }

  public static void solve(Points points, double capacity, String suffixPath) {
    double epsilon = 0.001;
    while (!isOptimal(points, capacity, epsilon)) {
      System.out.printf("Capacity: %f Epsilon: %f\n", capacity, epsilon);
      epsilon = epsilon / 2.0;
    }
    System.out.printf("Capacity: %f Epsilon: %f\n", capacity, epsilon);

    Graph graph = GraphGenerator.generate(points, capacity, epsilon);
    runHop(graph, capacity, suffixPath);
  }

  public static void exp1(Points points, String suffixPath) {
    for (double capacity = 0.02; capacity <= 0.04; capacity += 0.001) {
      solve(points, capacity, suffixPath);
    }
  }

  public static void exp0(Points points, double epsilon, String suffixPath) {
    double c = 5 * epsilon;
    for (int i = 0; i < 12; i++) {
      double capacity = c * Math.pow(2, i);
      Graph graph = generateAndDraw(points, capacity, epsilon, suffixPath);
      runHop(graph, capacity, suffixPath);
    }
  }

  public static void main(String[] args) throws Exception {
    String island = "salamis";
    String suffixFilePath = String.format("%s.txt", island);

    Points points = PrecisionReadPoints.inexactRead(suffixFilePath);


    double epsilon = 0.001;
    double capacity = 10000 * epsilon;


    Graph graph = generateAndDraw(points, capacity, epsilon, island);
    runHop(graph, capacity, island);
//
//    exp0(points, epsilon, island);

//    MyPolygon myPolygon = new MyPolygon(points);
//    MyPolygon convexHull = new MyPolygon(myPolygon.generateConvexHull());
//    System.out.println(convexHull.perimeter());
//
//
//    for (int i = -2; i <= 2; i++) {
//      capacity = convexHull.perimeter() + i * epsilon;
//      Graph graph = generateAndDraw(points, capacity, epsilon, island);
//      runHop(graph, capacity, island);
//    }

    exp1(points, island);



  }

}
