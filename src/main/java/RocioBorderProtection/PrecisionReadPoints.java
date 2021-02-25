package RocioBorderProtection;

import RocioBorderProtection.MyFile.Address;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrecisionReadPoints {

  Scanner scanner;
  int n;

  public PrecisionReadPoints(Scanner scanner) {
    this.scanner = scanner;
    n = scanner.nextInt();
  }

  public static Points inexactRead(String suffixPath) throws FileNotFoundException {
    File myFile = MyFile.getInstance(suffixPath, Address.RESOURCE);
    Scanner scanner = new Scanner(myFile);
    PrecisionReadPoints precisionReadPoints = new PrecisionReadPoints(scanner);
    return precisionReadPoints.inexactRead();
  }

  public static Points exactRead(String suffixPath) throws FileNotFoundException {
    File myFile = MyFile.getInstance(suffixPath, Address.RESOURCE);
    Scanner scanner = new Scanner(myFile);
    PrecisionReadPoints precisionReadPoints = new PrecisionReadPoints(scanner);
    return precisionReadPoints.exactRead();
  }

  private double valueOf(String str) {
    return Double.parseDouble(str);
  }

  public Points exactRead() {
    List<Point> pointList = new ArrayList<>();
    scanner.nextLine();
//    int k = 8;
    for (int i = 0; i < n; i++) {
      String str = scanner.nextLine();
      String[] strs = str.split("\\s+");
      assert strs.length == 2;
      double x = valueOf(strs[0]);
      double y = valueOf(strs[1]);
      pointList.add(Point.getInstance(x, y));
    }
    return Points.getInstance(pointList);
  }

  public Points inexactRead() {
    List<Point> pointList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      double x = scanner.nextDouble();
      double y = scanner.nextDouble();
      pointList.add(Point.getInstance(x, y));
    }
    return Points.getInstance(pointList);
  }

}
