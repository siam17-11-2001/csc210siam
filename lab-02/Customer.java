import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Customer {
    private double wallet;

    public Customer() { wallet = 500.00; }

    public Customer(String filename) throws IOException {
        wallet = Double.parseDouble(Files.readString(Path.of(filename)).trim());
        if (!Double.isFinite(wallet) || wallet < 0) throw new IllegalArgumentException("Invalid wallet value");
    }

    public double spend(double amount) {
        if (!Double.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Invalid spending amount");
        double spent = Math.min(wallet, amount);
        wallet -= spent;
        return spent;
    }

    public void receive(double amount) {
        if (!Double.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Invalid received amount");
        wallet += amount;
    }

    public double checkWallet() { return wallet; }

    public void save(String filename) throws IOException {
        Files.writeString(Path.of(filename), Double.toString(wallet) + System.lineSeparator());
    }
}
