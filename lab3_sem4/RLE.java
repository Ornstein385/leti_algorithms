public class RLE {
    public static String encode(String input) {
        if (input.length() == 0){
            return "";
        }
        StringBuilder output = new StringBuilder();
        int count = 1;
        char current = input.charAt(0);

        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == current && count < 255) {
                count++;
            } else {
                output.append((char) count).append(current);
                count = 1;
                current = input.charAt(i);
            }
        }

        output.append((char) count).append(current);
        return output.toString();
    }
    public static String decode(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length()-1; i += 2) {
            int count = input.charAt(i);
            for (int j = 0; j < count; j++) {
                output.append(input.charAt(i + 1));
            }
        }
        return output.toString();
    }
}
