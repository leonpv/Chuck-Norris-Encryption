import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        while (true) {
            System.out.println("\nPlease input operation (encode/decode/exit):");
            String inputCommand = scanner.nextLine();
            switch (inputCommand) {
                case "encode" -> encodeCase();
                case "decode" -> decodeCase();
                case "exit" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("There is no '" + inputCommand + "' operation");
            }
        }
    }

    private static void encodeCase() {
        System.out.println("Input string:");
        String str = CNEncode(scanner.nextLine());
        System.out.println("Encoded string:\n" + str);
    }

    private static void decodeCase() {
        System.out.println("Input encoded string:");
        String str = scanner.nextLine();
        if (isTestEncodedStrFailed(str)) return;
        try {
            System.out.println("Decoded string:\n" + CNDecode(str));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isTestEncodedStrFailed(String str) {
        StringBuilder CNEncoded = new StringBuilder(str);
        var index = CNEncoded.indexOf(" ");

        if (str.matches("(.*)[^0 ](.*)")) {
            System.out.println("Encoded string is not valid.1");
            return true;
        }

        while (index >= 0) {
            if (index > 2 || index < 1) {
                System.out.println("Encoded string is not valid.2");
                return true;
            }
            if (index == 2) {
                // 00
                CNEncoded.delete(0, index + 1);
                index = CNEncoded.indexOf(" ");
                if (index == -1) index = CNEncoded.length();
                CNEncoded.delete(0, index + 1);
            }
            if (index == 1) {
                // 0
                CNEncoded.delete(0, index + 1);
                index = CNEncoded.indexOf(" ");
                if (index == -1) index = CNEncoded.length();
                CNEncoded.delete(0, index + 1);
            }
            index = CNEncoded.indexOf(" ");
        }

        CNEncoded = new StringBuilder(str);
        index = CNEncoded.indexOf(" ");
        short count = 0;
        if (index == -1) {
            System.out.println("Encoded string is not valid.3");
            return true;
        }

        while (index >= 0) {
            CNEncoded.delete(index, index + 1);
            index = CNEncoded.indexOf(" ");
            count++;
        }
        if (count % 2 == 0) {
            System.out.println("Encoded string is not valid.4");
            return true;
        }

        return false;
    }

    private static String CNEncode(String str) {
        StringBuilder CNBinary = new StringBuilder();
        StringBuilder CNEncoded = new StringBuilder();

        str.chars().forEach(value ->
                CNBinary.append(String.format("%7s", Integer.toBinaryString((char) value)).replace(" ", "0")));

        for (int i = 0; i < CNBinary.length(); i++) {
            if (i == 0) {
                switch (CNBinary.charAt(i)) {
                    case '1' -> CNEncoded.append("0 0");
                    case '0' -> CNEncoded.append("00 0");
                }
                continue;
            }
            if (CNBinary.charAt(i) == CNBinary.charAt(i - 1)) {
                CNEncoded.append("0");
            } else {
                switch (CNBinary.charAt(i)) {
                    case '1' -> CNEncoded.append(" 0 0");
                    case '0' -> CNEncoded.append(" 00 0");
                }
            }
        }

        return CNEncoded.toString();
    }

    private static String CNDecode(String encodedStr) {
        StringBuilder CNEncoded = new StringBuilder(encodedStr);
        var index = CNEncoded.indexOf(" ");
        StringBuilder CNBinary = new StringBuilder();
        StringBuilder CNDecoded = new StringBuilder();

        while (index >= 0) {
            if (index == 2) {
                // 0
                CNEncoded.delete(0, index + 1);
                index = CNEncoded.indexOf(" ");
                if (index == -1) index = CNEncoded.length();
                CNEncoded.delete(0, index + 1);
                while (index-- != 0) {
                    CNBinary.append("0");
                }
            }
            if (index == 1) {
                // 1
                CNEncoded.delete(0, index + 1);
                index = CNEncoded.indexOf(" ");
                if (index == -1) index = CNEncoded.length();
                CNEncoded.delete(0, index + 1);
                while (index-- != 0) {
                    CNBinary.append("1");
                }
            }
            index = CNEncoded.indexOf(" ");
        }

        if (CNBinary.length() % 7 != 0) {
            throw new NumberFormatException("Encoded string is not valid.5");
        }

        while (CNBinary.length() != 0) {
            CNDecoded.append((char) Integer.parseInt(CNBinary.substring(0, 7), 2));
            CNBinary.delete(0, 7);
        }

        return CNDecoded.toString();
    }
}