import java.io.*;
import java.util.ArrayList;

/** Program 1: demonstrate file processing with an ArrayList. */
public class ProcessCreatureFile {
    public static void main(String[] args) {
        String filename = "creature-data.csv";
        ArrayList<Creature> creatures = new ArrayList<>();
        try {
            // Each record uses the Lab 01 properties: name, size, age.
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                int row = 0;
                while ((line = reader.readLine()) != null) {
                    row++;
                    if (line.trim().isEmpty()) continue;
                    String[] fields = line.split(",", -1);
                    if (fields.length != 3) throw new IOException("Invalid CSV row " + row);
                    creatures.add(new Creature(fields[0].trim(), fields[1].trim(),
                            Integer.parseInt(fields[2].trim())));
                }
            }
            System.out.println("Loaded " + creatures.size() + " creatures.");
            creatures.add(new Creature("Phoenix", "Medium", 1));
            if (creatures.size() > 1) creatures.remove(0);
            Creature first = creatures.get(0);
            first.size = "Large";
            first.age++;
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (Creature creature : creatures) {
                    writer.println(creature.name + "," + creature.size + "," + creature.age);
                }
                if (writer.checkError()) throw new IOException("Unable to write CSV.");
            }
            System.out.println("Added, removed, and modified creatures; saved "
                    + creatures.size() + " records.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
