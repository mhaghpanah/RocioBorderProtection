package RocioBorderProtection;

import OrderTypeGraph.MyFile.Address;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SandBox {

  public static List<Point> readPoints(String suffixPath) throws FileNotFoundException {
    File myFile = OrderTypeGraph.MyFile.getInstance(suffixPath, Address.RESOURCE);
    Scanner scanner = new Scanner(myFile);
    int n = scanner.nextInt();
    List<Point> pointList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      double x = scanner.nextDouble();
      double y = scanner.nextDouble();
      Point point = new Point(x, y);
      pointList.add(point);
    }
    System.out.println("################################################");
    System.out.println(pointList);
    return pointList;
  }

  public static void generateAndDraw(String suffixPath) throws FileNotFoundException {
    List<Point> pointList = readPoints(suffixPath);
    System.out.println(pointList);
    MapDrawer.drawAndWrite(pointList, "salamis", true);

    double epsilon = 0.01;
    double capacity = 10 * epsilon;
    Graph graph = GraphGenerator.GraphGenerator(pointList, capacity, epsilon);
    MapDrawer.drawAndWrite(graph.getPointList(), "salamis2", false);
    System.out.println("---------------------------");
    System.out.println(graph.getPointList());
    System.out.println(graph.getPointList().size());
  }

  public static void runHop(String suffixPath) throws FileNotFoundException {
    List<Point> pointList = readPoints(suffixPath);
    System.out.println(pointList);
    MapDrawer.drawAndWrite(pointList, "salamis", true);

    double epsilon = 0.00001;
    double capacity = 1000 * epsilon;
    Graph graph = GraphGenerator.GraphGenerator(pointList, capacity, epsilon);
    MapDrawer.drawAndWrite(graph.getPointList(), "salamis2", true);
    System.out.println("---------------------------");
    System.out.println(graph.getPointList());
    System.out.println(graph.getPointList().size());
//    System.out.println(graph);

    List<List<Edge>> path = Hop.run(graph, capacity);
    System.out.printf("Path length: %d\n", path.size());
    for (List<Edge> p : path) {
      System.out.println("$$$$$$$$$$$$$$");
      System.out.println(p);
    }

    MapDrawer.drawAndWrite(graph.getPointList(), path, "salamis3", false);

  }

  public static void main(String[] args) throws Exception {
    String suffixPath = "salamis.txt";
//    readPoints(suffixPath);
//    generateAndDraw(suffixPath);
    runHop(suffixPath);
  }

}
