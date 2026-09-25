import java.io.IOException;
import java.util.Scanner;

public class GoodCasino {
    public static double play(Customer customer, SlotMachine slotMachine, double amount) {
        return slotMachine.pullLever(customer.spend(amount));
    }

    public static void main(String[] args) {
        Customer customer;
        SlotMachine machine;
        try {
            customer = new Customer("customer.txt");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not load customer.txt; starting with $500.00.");
            customer = new Customer();
        }
        try {
            machine = new SlotMachine("slot-machine.txt");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not load slot-machine.txt; starting with $1,000,000.00.");
            machine = new SlotMachine();
        }
        Scanner input = new Scanner(System.in);
        while (customer.checkWallet() > 0 && machine.getMoneyPot() > 0) {
            System.out.printf("Wallet: $%.2f | Machine: $%.2f%n", customer.checkWallet(), machine.getMoneyPot());
            System.out.print("Enter your wager or quit: ");
            if (!input.hasNextLine()) break;
            String line = input.nextLine().trim();
            if (line.equalsIgnoreCase("quit")) break;
            double wager;
            try {
                wager = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number or quit.");
                continue;
            }
            if (!Double.isFinite(wager) || wager <= 0 || wager > customer.checkWallet()) {
                System.out.println("Enter a positive wager no greater than your wallet.");
                continue;
            }
            double winnings = play(customer, machine, wager);
            System.out.printf("%s | Won: $%.2f%n", machine, winnings);
            customer.receive(winnings);
        }
        try {
            customer.save("customer.txt");
            machine.save("slot-machine.txt");
            System.out.println("Game saved.");
        } catch (IOException e) {
            System.err.println("Could not save game: " + e.getMessage());
        }
    }
}
