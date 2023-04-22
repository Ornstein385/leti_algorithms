import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MTF {

    private static final int ALPHABET_SIZE = 65536;

    public static String encode(String input) {
        ArrayList<Integer> alphabet = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        int index;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            alphabet.add(i);
        }
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            int value = b & 0xFF;
            index = alphabet.indexOf(value);
            output.append(index).append(' ');
            alphabet.remove(index);
            alphabet.add(0, value);
        }
        return output.toString();
    }

    public static String decode(String input) {
        if (input.length() == 0) {
            return input;
        }
        ArrayList<Integer> alphabet = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        int index = -1;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            alphabet.add(i);
        }
        String[] tokens = input.split(" ");
        for (String token : tokens) {
            try {
                index = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                continue;
            }
            int value = alphabet.get(index);
            output.append((char) value);
            alphabet.remove(index);
            alphabet.add(0, value);
        }
        return output.toString();
    }
}
