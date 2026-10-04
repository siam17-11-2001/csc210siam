import java.io.*;
import java.util.ArrayList;

/** Program 2: owns a creature collection and its backing CSV file. */
public class CreatureRegistry {
    private final ArrayList<Creature> creatures = new ArrayList<>();
    private final String filename;

    public CreatureRegistry(String filename) throws IOException {
        this.filename = filename;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                row++;
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(",", -1);
                if (fields.length != 3) throw new IOException("Invalid CSV row " + row);
                try {
                    addCreature(new Creature(fields[0].trim(), fields[1].trim(),
                            Integer.parseInt(fields[2].trim())));
                } catch (IllegalArgumentException e) {
                    throw new IOException("Invalid CSV row " + row + ": " + e.getMessage(), e);
                }
            }
        }
    }

    public int getCount() { return creatures.size(); }

    // Registry methods use normal Java zero-based indexes.
    public Creature getCreature(int index) {
        checkIndex(index);
        return copy(creatures.get(index));
    }

    public void modifyCreature(int index, Creature creature) {
        checkIndex(index);
        validate(creature);
        creatures.set(index, copy(creature));
    }

    public void deleteCreature(int index) {
        checkIndex(index);
        creatures.remove(index);
    }

    public void addCreature(Creature creature) {
        validate(creature);
        creatures.add(copy(creature));
    }

    public void save() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Creature creature : creatures) {
                writer.println(creature.name + "," + creature.size + "," + creature.age);
            }
            if (writer.checkError()) throw new IOException("Unable to write " + filename);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= creatures.size()) {
            throw new IndexOutOfBoundsException("Creature index does not exist: " + index);
        }
    }

    private static Creature copy(Creature creature) {
        return new Creature(creature.name, creature.size, creature.age);
    }

    private static void validate(Creature creature) {
        if (creature == null || !validText(creature.name) || !validText(creature.size)
                || creature.age < 0) {
            throw new IllegalArgumentException("Name and size must be nonempty without commas or newlines; age must be nonnegative.");
        }
    }

    private static boolean validText(String value) {
        return value != null && !value.trim().isEmpty() && !value.contains(",")
                && !value.contains("\n") && !value.contains("\r");
    }

    /** Tests every operation and restores the original records before saving. */
    public static void main(String[] args) {
        try {
            CreatureRegistry registry = new CreatureRegistry("creature-data.csv");
            int count = registry.getCount();
            registry.addCreature(new Creature("RegistryTest", "Small", 1));
            if (registry.getCount() != count + 1) throw new IllegalStateException("Add failed");
            Creature detached = registry.getCreature(count);
            detached.name = "ChangedCopy";
            if (!registry.getCreature(count).name.equals("RegistryTest")) {
                throw new IllegalStateException("Copy is not independent");
            }
            registry.modifyCreature(count, new Creature("UpdatedTest", "Large", 2));
            if (!registry.getCreature(count).name.equals("UpdatedTest")) {
                throw new IllegalStateException("Modify failed");
            }
            registry.save();
            CreatureRegistry reloaded = new CreatureRegistry("creature-data.csv");
            if (reloaded.getCount() != count + 1
                    || !reloaded.getCreature(count).name.equals("UpdatedTest")) {
                throw new IllegalStateException("Save/reload failed");
            }
            registry.deleteCreature(count);
            registry.save();
            if (registry.getCount() != count) throw new IllegalStateException("Delete failed");
            System.out.println("Registry tests passed: count, copy, add, modify, delete, save, reload.");
        } catch (IOException | RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
