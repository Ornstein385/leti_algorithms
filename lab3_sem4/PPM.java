import java.util.HashMap;
import java.util.Map;

public class PPM {
    private static final int ORDER = 3;
    private static final int SYMBOL_LIMIT = 256;

    public static String encode(String input) {
        StringBuilder output = new StringBuilder();
        Map<String, Integer>[] maps = new Map[ORDER + 1];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new HashMap<>();
        }
        for (int i = 0; i < input.length(); i++) {
            String context;
            char symbol = input.charAt(i);

            int count = 1;
            if (maps[ORDER].containsKey(String.valueOf(symbol))) {
                count += maps[ORDER].get(String.valueOf(symbol));
            }
            maps[ORDER].put(String.valueOf(symbol), count);

            for (int j = ORDER - 1; j >= 0; j--) {
                context = input.substring(Math.max(0, i - j - 1), i);
                if (!maps[j].containsKey(context)) {
                    maps[j].put(context, SYMBOL_LIMIT);
                }

                int total = 0;
                for (int k = 0; k < SYMBOL_LIMIT; k++) {
                    String key = context + (char) k;
                    int value = maps[j + 1].getOrDefault(key, 0);
                    total += value;
                }
                int index = (int) (Math.random() * total);
                int sum = 0;
                char predictedSymbol = 0;
                for (int k = 0; k < SYMBOL_LIMIT; k++) {
                    String key = context + (char) k;
                    int value = maps[j + 1].getOrDefault(key, 0);
                    sum += value;
                    if (sum > index) {
                        predictedSymbol = (char) k;
                        break;
                    }
                }
                int count2 = 1;
                if (maps[j].containsKey(context + predictedSymbol)) {
                    count2 += maps[j].get(context + predictedSymbol);
                }
                maps[j].put(context + predictedSymbol, count2);
            }
            output.append((char) symbol);
        }
        return output.toString();
    }

    public static String decode(String input) {
        StringBuilder output = new StringBuilder();
        Map<String, Integer>[] maps = new Map[ORDER + 1];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new HashMap<>();
        }
        String context = "";
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);

            int count = 1;
            if (maps[ORDER].containsKey(String.valueOf(symbol))) {
                count += maps[ORDER].get(String.valueOf(symbol));
            }
            maps[ORDER].put(String.valueOf(symbol), count);

            output.append(symbol);

            for (int j = ORDER - 1; j >= 0; j--) {
                if (context.length() < j) {
                    context = input.substring(0, i + 1);
                } else {
                    context = context.substring(1) + symbol;
                }

                if (!maps[j].containsKey(context)) {
                    maps[j].put(context, SYMBOL_LIMIT);
                }

                int total = 0;
                for (int k = 0; k < SYMBOL_LIMIT; k++) {
                    String key = context.substring(context.length() - j + 1) + (char) k;
                    int value = maps[j + 1].getOrDefault(key, 0);
                    total += value;
                }

                int index = (int) (Math.random() * total);
                int sum = 0;
                char predictedSymbol = 0;
                for (int k = 0; k < SYMBOL_LIMIT; k++) {
                    String key = context.substring(context.length() - j + 1) + (char) k;
                    int value = maps[j + 1].getOrDefault(key, 0);
                    sum += value;
                    if (sum > index) {
                        predictedSymbol = (char) k;
                        break;
                    }
                }
                int count2 = 1;
                if (maps[j].containsKey(context.substring(context.length() - j + 1) + predictedSymbol)) {
                    count2 += maps[j].get(context.substring(context.length() - j + 1) + predictedSymbol);
                }
                maps[j].put(context.substring(context.length() - j + 1) + predictedSymbol, count2);
            }
        }
        return output.toString();
    }
}
