import java.io.FileWriter;
import java.io.IOException;

public class Lab2FileGenerator {
    public static void main(String[] args) throws IOException {
        int num = 0;
        for (int i = 100; i <= 10000000; i*=3.16228, i = i % 2 != 0 ? i+1 : i) {
            FileWriter fileWriter = new FileWriter("file_"+num++ +".txt");
            fileWriter.write(i + "\n");
            for (int j = 0; j < i; j++){
                fileWriter.write(Double.toString(Math.random()*2000-1000)+" "+Double.toString(Math.random()*2000-1000)+"\n");
            }
            fileWriter.close();
        }
    }
}