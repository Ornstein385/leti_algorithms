import java.io.FileWriter;
import java.io.IOException;

public class Tester {
    public static void main(String[] args) {
        //lowConTest();
        //midConTest();
        //highConTest();
    }

    public static void lowConTest() {
        try {
            FileWriter fw = new FileWriter("low.csv");
            for (int i = 10; i <= 3000; i+= 10) {
                fw.write(timeChecker(generateLowGraphConnectivityMatrix(i))/1000000 + ";");
                System.out.println(i);
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("ошибка записи в файл");
        }
    }

    public static void midConTest() {
        try {
            FileWriter fw = new FileWriter("mid.csv");
            for (int i = 10; i <= 3000; i+= 10) {
                fw.write(timeChecker(generateMediumGraphConnectivityMatrix(i))/1000000 + ";");
                System.out.println(i);
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("ошибка записи в файл");
        }
    }

    public static void highConTest() {
        try {
            FileWriter fw = new FileWriter("high.csv");
            for (int i = 10; i <= 3000; i+= 10) {
                fw.write(timeChecker(generateHighGraphConnectivityMatrix(i))/1000000 + ";");
                System.out.println(i);
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("ошибка записи в файл");
        }
    }

    public static long timeChecker(int[][] matrix) {
        long startTime = System.nanoTime();
        DijkstraAlgorithm.dijkstra(matrix, (int) (Math.random() * matrix.length));
        return System.nanoTime() - startTime;
    }

    public static int[][] generateHighGraphConnectivityMatrix(int n) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int x = (int) (Math.random() * 100);
                a[i][j] = x;
                a[j][i] = x;
            }
        }
        return a;
    }

    public static int[][] generateMediumGraphConnectivityMatrix(int n) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.random() < 0.5) {
                    int x = (int) (Math.random() * 100);
                    a[i][j] = x;
                    a[j][i] = x;
                }
            }
        }
        return a;
    }

    public static int[][] generateLowGraphConnectivityMatrix(int n) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.random() < 0.25) {
                    int x = (int) (Math.random() * 100);
                    a[i][j] = x;
                    a[j][i] = x;
                }
            }
        }
        return a;
    }
}
