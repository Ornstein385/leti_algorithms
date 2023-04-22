import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Arithmetic {

    public static void encode(String fileNameIn, String fileNameOut) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileNameIn));
        StringBuilder stringBuilder = new StringBuilder();
        String s;
        while ((s = reader.readLine()) != null) {
            stringBuilder.append(s).append('\n');
        }
        s = stringBuilder.toString();

        reader.close();
        HashMap<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (freq.containsKey(s.charAt(i))) {
                freq.put(s.charAt(i), freq.get(s.charAt(i)) + 1);
            } else {
                freq.put(s.charAt(i), 1);
            }
        }
        HashMap<Character, Integer> borders = new HashMap<>();
        int cumulative = 0;
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            borders.put(entry.getKey(), cumulative);
            cumulative += entry.getValue();
        }
        BigDecimal left = new BigDecimal(0), right = new BigDecimal(1);
        for (int i = 0; i < s.length(); i++) {
            BigDecimal dl = right.subtract(left).divide(new BigDecimal(s.length()), s.length() * 10, RoundingMode.HALF_UP);
            BigDecimal dr = dl.multiply(new BigDecimal(borders.get(s.charAt(i)) + freq.get(s.charAt(i))));
            dl = dl.multiply(new BigDecimal(borders.get(s.charAt(i))));
            right = left.add(dr);
            left = left.add(dl);
        }
        if (left.compareTo(BigDecimal.ZERO) < 0 || right.compareTo(BigDecimal.ZERO) < 0 || left.compareTo(BigDecimal.ONE) > 0 || right.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalStateException("вне диапазона 0...1");
        }
        if (left.compareTo(right) == 0) {
            throw new IllegalStateException("левая и правая границы совпадают");
        }
        if (left.compareTo(right) > 0) {
            throw new IllegalStateException("левая граница правее правой");
        }
        BigDecimal res = new BigDecimal(0), dx = new BigDecimal(1);
        StringBuilder binaryString = new StringBuilder();
        int i = 0;
        while (res.compareTo(left) <= 0) {
            if (i % 100 == 0) {
                System.out.println("Ari encode: " + i);
            }
            i++;
            if (res.add(dx).compareTo(right) < 0) {
                res = res.add(dx);
                binaryString.append('1');
            } else {
                binaryString.append('0');
            }
            dx = dx.divide(new BigDecimal(2), s.length() * 10, RoundingMode.HALF_UP);
        }
        while (binaryString.length() % 8 != 0) {
            binaryString.append('0');
        }
        FileOutputStream fileOut = new FileOutputStream(fileNameOut);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(freq);
        objectOut.writeObject(borders);
        objectOut.write(binaryStringToByteArray(binaryString.toString()));
        objectOut.close();
    }

    public static void decode(String fileNameIn, String fileNameOut) throws IOException, ClassNotFoundException {
        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(fileNameIn));
        HashMap<Character, Integer> freq = (HashMap<Character, Integer>) reader.readObject();
        HashMap<Integer, Character> bordersMap = invertHashMap((HashMap<Character, Integer>) reader.readObject());
        TreeSet<Integer> bordersSet = new TreeSet<>(bordersMap.keySet());
        String binaryString = byteArrayToBinaryString(reader.readAllBytes());
        reader.close();
        int sum = 0;
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            sum += entry.getValue();
        }
        bordersSet.add(sum);
        BigDecimal x = BigDecimal.ZERO, dx = BigDecimal.ONE;
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                x = x.add(dx);
            }
            dx = dx.divide(new BigDecimal(2));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sum; i++) {
            if (i % 100 == 0) {
                System.out.println("Ari decode: " + i + " / " + sum);
            }
            int ind = x.multiply(new BigDecimal(sum)).intValue();
            int floor = bordersSet.floor(ind), ceiling = bordersSet.ceiling(ind);
            if (floor == ceiling) {
                ceiling = floor + freq.get(bordersMap.get(ceiling));
            }
            BigDecimal l = new BigDecimal(floor).divide(new BigDecimal(sum), sum * 10, RoundingMode.HALF_UP);
            BigDecimal r = new BigDecimal(ceiling).divide(new BigDecimal(sum), sum * 10, RoundingMode.HALF_UP);
            BigDecimal a = r.subtract(l), b = x.subtract(l);
            x = b.divide(a, sum * 10, RoundingMode.HALF_UP);
            sb.append(bordersMap.get(floor));
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameOut));
        writer.write(sb.toString());
        writer.close();
    }

    public static byte[] binaryStringToByteArray(String binaryString) {
        int length = binaryString.length();
        byte[] byteArray = new byte[length / 8];
        for (int i = 0; i < length; i += 8) {
            byteArray[i / 8] = (byte) Integer.parseInt(binaryString.substring(i, i + 8), 2);
        }
        return byteArray;
    }

    public static String byteArrayToBinaryString(byte[] byteArray) {
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            String binaryString = Integer.toBinaryString(b & 0xFF);
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            binaryStringBuilder.append(binaryString);
        }
        return binaryStringBuilder.toString();
    }

    public static <K, V> HashMap<V, K> invertHashMap(HashMap<K, V> map) {
        HashMap<V, K> invertedMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            invertedMap.put(entry.getValue(), entry.getKey());
        }
        return invertedMap;
    }
}