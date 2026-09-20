public class Hex {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Error: Please provide one hexadecimal number.");
            System.exit(1);
        }

        String hex = args[0];
        long decimal = 0;

        for (int i = 0; i < hex.length(); i++) {
            char ch = hex.charAt(i);
            int value;

            if (ch >= '0' && ch <= '9') {
                value = ch - '0';
            } 
            else if (ch >= 'A' && ch <= 'F') {
                value = ch - 'A' + 10;
            } 
            else if (ch >= 'a' && ch <= 'f') {
                value = ch - 'a' + 10;
            } 
            else {
                System.err.println("Error: Invalid hexadecimal number.");
                System.exit(1);
                return;
            }

            decimal = decimal * 16 + value;
        }

        System.out.println(decimal);
    }
}