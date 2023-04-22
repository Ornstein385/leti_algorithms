import java.util.HashMap;
import java.util.Map;

public class LZ78 {

    public static String encode(String input) {
        Map<String, Integer> dictionary = new HashMap<>();
        dictionary.put("", 0);
        int nextCode = 1;
        StringBuilder output = new StringBuilder();
        String current = "";
        for (char c : input.toCharArray()) {
            String newString = current + c;
            if (dictionary.containsKey(newString)) {
                current = newString;
            } else {
                output.append(dictionary.get(current)).append('\0').append(c).append('\0');
                dictionary.put(newString, nextCode++);
                current = "";
            }
        }
        if (!current.equals("")) {
            output.append(dictionary.get(current));
        }
        return output.toString();
    }

    public static String decode(String input) {
        String[] array = input.split("\0");
        StringBuilder output = new StringBuilder();
        Map<Integer, String> dictionary = new HashMap<>();
        dictionary.put(0, "");
        int nextCode = 1;
        for (int i = 0; i < array.length - 1; i += 2) {
            int x = Integer.parseInt(array[i]);
            String current = "";
            if (dictionary.containsKey(x)) {
                current = dictionary.get(x) + array[i + 1];
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
            output.append(current);
            dictionary.put(nextCode++, current);
        }
        if (array.length % 2 > 0 && array.length > 1) {
            int x = Integer.parseInt(array[array.length - 1]);
            if (dictionary.containsKey(x)) {
                output.append(dictionary.get(x));
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
        }
        return output.toString();
    }
}