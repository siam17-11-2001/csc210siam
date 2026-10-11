import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** Loads, edits, and saves Creature objects for Lab 04. */
public class ProcessCreatureFile {
    private final ArrayList<Creature> creatures = new ArrayList<>();

    public void loadFromFile(String filename) throws IOException {
        ArrayList<Creature> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                row++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = line.split(",", -1);
                if (fields.length != 3) {
                    throw new IOException("Invalid CSV row " + row + ": expected 3 columns.");
                }
                try {
                    String name = fields[0].trim();
                    String size = fields[1].trim();
                    int age = Integer.parseInt(fields[2].trim());
                    validate(name, size, age);
                    loaded.add(new Creature(name, size, age));
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Invalid CSV row " + row + ": " + ex.getMessage(), ex);
                }
            }
        }
        creatures.clear();
        creatures.addAll(loaded);
    }

    public ArrayList<Creature> getCreatures() {
        return new ArrayList<>(creatures);
    }

    public Creature getCreature(int index) {
        return creatures.get(index);
    }

    public void addCreature(String name, String size, int age) {
        validate(name, size, age);
        creatures.add(new Creature(name.trim(), size.trim(), age));
    }

    public void updateCreature(int index, String name, String size, int age) {
        validate(name, size, age);
        Creature creature = creatures.get(index);
        creature.name = name.trim();
        creature.size = size.trim();
        creature.age = age;
    }

    public void removeCreature(int index) {
        creatures.remove(index);
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Creature creature : creatures) {
                writer.println(creature.name + "," + creature.size + "," + creature.age);
            }
            if (writer.checkError()) {
                throw new IOException("Unable to write CSV file.");
            }
        }
    }

    private static void validate(String name, String size, int age) {
        if (name == null || name.trim().isEmpty() || size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("Name and size cannot be empty.");
        }
        if (name.contains(",") || name.contains("\n") || name.contains("\r")
                || size.contains(",") || size.contains("\n") || size.contains("\r")) {
            throw new IllegalArgumentException("Name and size cannot contain commas or line breaks.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
    }

    /** Keeps the file-processing demonstration from Lab 03 available. */
    public static void main(String[] args) {
        String filename = "creature-data.csv";
        ProcessCreatureFile processor = new ProcessCreatureFile();
        try {
            processor.loadFromFile(filename);
            System.out.println("Loaded " + processor.getCreatures().size() + " creatures.");
            processor.addCreature("Phoenix", "Medium", 1);
            if (processor.getCreatures().size() > 1) {
                processor.removeCreature(0);
            }
            if (!processor.getCreatures().isEmpty()) {
                Creature first = processor.getCreature(0);
                processor.updateCreature(0, first.name, "Large", first.age + 1);
            }
            processor.saveToFile(filename);
            System.out.println("Added, removed, and modified creatures; saved "
                    + processor.getCreatures().size() + " records.");
        } catch (IOException | IllegalArgumentException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
