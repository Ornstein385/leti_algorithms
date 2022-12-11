import java.io.*;

public class TestAlgoKurs {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        for (int i = 0; i < 1000; i++) {
            int maxTry = 5, cntTry = 0;
            for (int j = 0; j < maxTry; j++) {
                if (test(i)) {
                    cntTry++;
                }
            }
            if (!(cntTry == maxTry)) {
                System.out.println("!!!");
            }
        }
    }

    static boolean test(int i) throws IOException, ClassNotFoundException {
        String fileName1 = "testfile_" + i + "_изначальный.txt", fileName2 = "testfile_" + i + "_закодированный.txt",
                fileName3 = "testfile_" + i + "_раскодированный.txt";
        generateFile(fileName1, (int) (Math.random() * 500 + 500));
        AlgoKurs.encode(fileName1, fileName2);
        AlgoKurs.decode(fileName2, fileName3);
        boolean b = equalsFiles(fileName1, fileName3);
        //System.out.println(i + " " + (b ? "+" : "-"));
        return b;
    }


    static boolean equalsFiles(String fileName1, String fileName2) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(fileName1));
        BufferedReader reader2 = new BufferedReader(new FileReader(fileName2));
        while (true) {
            int x1 = reader1.read();
            int x2 = reader2.read();
            if (x1 != x2) {
                reader1.close();
                reader2.close();
                return false;
            }
            if (x1 == -1 && x1 == -1) {
                reader1.close();
                reader2.close();
                return true;
            }
        }
    }

    static void generateFile(String fileName, int len) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = '\0';
            while (!(Character.isLetter(c) || Character.isDigit(c))) {
                c = (char) (Math.random() * Short.MAX_VALUE * 2 + 1);
            }
            sb.append(c);
        }
        writer.write(sb.toString());
        writer.close();
    }
}