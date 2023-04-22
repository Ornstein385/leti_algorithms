import java.util.Arrays;

public class BWT {

    public static String encode(String input) {
        int length = input.length();
        String[] rotations = new String[length];
        for (int i = 0; i < length; i++) {
            if (i % 100 == 0) {
                System.out.println("BWT encode: " + i + " / " + length);
            }
            rotations[i] = input.substring(i, length) + input.substring(0, i);
        }
        Arrays.sort(rotations);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rotations[i].charAt(length - 1));
        }
        int index = Arrays.asList(rotations).indexOf(input);
        return sb + "\0" + index;
    }

    public static String decode(String input) {
        String[] tmp = input.split("\0");
        if (tmp.length != 2){
            System.out.println("---");
        }
        input = tmp[0];
        int index = Integer.parseInt(tmp[1]);
        int length = input.length();
        String[] rotations = new String[length];
        for (int i = 0; i < length; i++) {
            if (i % 100 == 0) {
                System.out.println("BWT decode: " + i + " / " + length);
            }
            for (int j = 0; j < length; j++) {
                if (rotations[j] == null) {
                    rotations[j] = "";
                }
                rotations[j] = input.charAt(j) + rotations[j];
            }
            Arrays.sort(rotations);
        }
        return rotations[index];
    }
}