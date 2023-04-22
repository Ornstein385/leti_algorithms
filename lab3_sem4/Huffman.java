import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Huffman {
    public static void encode(String fileNameIn, String fileNameOut) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileNameIn));
        ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(fileNameOut));
        TreeMap<Character, Integer> frequencyMap = new TreeMap<>();
        char sym;
        int symCode;
        StringBuilder decodedText = new StringBuilder();
        StringBuilder encodedText = new StringBuilder();
        while ((symCode = reader.read()) >= 0) {
            sym = (char) symCode;
            decodedText.append(sym);
            if (frequencyMap.containsKey(sym)) {
                frequencyMap.put(sym, frequencyMap.get(sym) + 1);
            } else {
                frequencyMap.put(sym, 1);
            }
        }
        int nodeNum = 0;
        TreeSet<Node> nodes = new TreeSet<>(Comparator.comparingInt((Node o) -> o.freq).thenComparingInt(o -> o.num));
        TreeMap<Character, String> encodeMap = new TreeMap<>();
        TreeMap<String, Character> decodeMap = new TreeMap<>();
        for (Character c : frequencyMap.keySet()) {
            nodes.add(new Node(c, frequencyMap.get(c), true, nodeNum++));
        }
        frequencyMap.clear();
        switch (nodes.size()) {
            case 1:
                encodeMap.put(nodes.first().c, "0");
                decodeMap.put("0", nodes.first().c);
                break;
            case 2:
                encodeMap.put(nodes.first().c, "0");
                encodeMap.put(nodes.last().c, "1");
                decodeMap.put("0", nodes.first().c);
                decodeMap.put("1", nodes.last().c);
                break;
            default:
                Node root = null, left = null, right = null;
                while (!nodes.isEmpty()) {
                    left = nodes.pollFirst();
                    right = nodes.pollFirst();
                    root = new Node('\0', left.freq + right.freq, false, nodeNum++);
                    root.l = left;
                    root.r = right;
                    if (!nodes.isEmpty()) {
                        nodes.add(root);
                    }
                }
                root.code = "";
                setCode(root, encodeMap, decodeMap);
        }
        String text = decodedText.toString();
        for (int i = 0; i < text.length(); i++) {
            encodedText.append(encodeMap.get(text.charAt(i)));
        }
        writer.writeObject(decodeMap);
        encodeMap.clear();
        decodeMap.clear();
        nodes.clear();
        byte[] bytes = toEncodeBinary(encodedText.toString());
        writer.write(bytes);
        reader.close();
        writer.close();
    }

    public static void setCode(Node node, TreeMap<Character, String> encodeMap, TreeMap<String, Character> decodeMap) {
        if (node.l != null) {
            node.l.code = node.code + "0";
            setCode(node.l, encodeMap, decodeMap);
        }
        if (node.r != null) {
            node.r.code = node.code + "1";
            setCode(node.r, encodeMap, decodeMap);
        }
        if (node.isLeaf) {
            encodeMap.put(node.c, node.code);
            decodeMap.put(node.code, node.c);
        }
    }

    public static void decode(String fileNameIn, String fileNameOut) throws IOException, ClassNotFoundException {
        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(fileNameIn));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameOut));
        TreeMap<String, Character> codeMap = (TreeMap<String, Character>) reader.readObject();
        StringBuilder decodedText = new StringBuilder();
        byte[] bytes = reader.readAllBytes();
        String code = toDecodeBinary(bytes);
        for (int i = 0; i < code.length(); i++) {
            for (int j = 1; j < code.length() - i + 1; j++) {
                String substring = code.substring(i, i + j);
                if (codeMap.containsKey(substring)) {
                    decodedText.append(codeMap.get(substring));
                    i += j - 1;
                    break;
                }
            }
        }
        codeMap.clear();
        writer.write(decodedText.toString());
        reader.close();
        writer.close();
    }

    public static String toBinary(byte x) {
        StringBuilder s = new StringBuilder(Integer.toBinaryString(Byte.toUnsignedInt(x)));

        while (s.length() < 8) {
            s.insert(0, '0');
        }
        return s.toString();
    }

    public static byte toByte(String s) {
        if (s.length() > 8) {
            throw new IllegalArgumentException();
        }
        byte value = 0, mul = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            value += s.charAt(i) == '1' ? mul : 0;
            mul *= 2;
        }
        return value;
    }

    public static byte[] toEncodeBinary(String s) {
        ArrayList<Byte> list = new ArrayList<>();
        byte m = (byte) (s.length() % 8);
        list.add(m);
        for (int i = 0; i < s.length(); i += 8) {
            list.add(toByte(s.substring(i, Math.min(i + 8, s.length()))));
        }
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++){
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    public static String toDecodeBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int m = bytes[0];
        if (m == 0) {
            m = 8;
        }
        for (int i = 1; i < bytes.length; i++) {
            sb.append(toBinary(bytes[i]));
        }
        if (sb.length() > 8) {
            sb.delete(sb.length() - 8, sb.length() - m);
        } else {
            sb.delete(0, sb.length() - m);
        }
        return sb.toString();
    }

    public static char getChar(byte x1, byte x2) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(x1);
        bb.put(x2);
        return (char) bb.getShort(0);
    }

    static class Node {
        char c;
        int freq = 0, num = 0;
        boolean isLeaf;
        String code;
        Node l;
        Node r;

        public Node(char c, int freq, boolean isLeaf, int num) {
            this.c = c;
            this.freq = freq;
            this.isLeaf = isLeaf;
            this.num = num;
        }
    }
}
