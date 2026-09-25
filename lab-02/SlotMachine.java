import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class SlotMachine {
    private char first;
    private char second;
    private char third;
    private double moneyPot;
    private final Random random = new Random();
    private static final char[] SYMBOLS = {'\u263A', '\u2665', '7'};

    public SlotMachine() { moneyPot = 1_000_000.00; }

    public SlotMachine(String filename) throws IOException {
        moneyPot = Double.parseDouble(Files.readString(Path.of(filename)).trim());
        if (!Double.isFinite(moneyPot) || moneyPot < 0) throw new IllegalArgumentException("Invalid money pot value");
    }

    public double pullLever(double amount) {
        if (!Double.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Invalid wager");
        first = SYMBOLS[random.nextInt(SYMBOLS.length)];
        second = SYMBOLS[random.nextInt(SYMBOLS.length)];
        third = SYMBOLS[random.nextInt(SYMBOLS.length)];
        moneyPot += amount;
        if (first == second && second == third) {
            double payout = Math.min(moneyPot, 10 * amount);
            moneyPot -= payout;
            return payout;
        }
        return 0;
    }

    @Override
    public String toString() { return "" + first + " " + second + " " + third; }

    public double getMoneyPot() { return moneyPot; }

    public void save(String filename) throws IOException {
        Files.writeString(Path.of(filename), Double.toString(moneyPot) + System.lineSeparator());
    }
}
