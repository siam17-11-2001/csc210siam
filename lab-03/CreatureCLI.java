import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Program 3: command-line CRUD using CreatureRegistry. */
public class CreatureCLI {
    public static void main(String[] args) {
        // Validate command shape before opening the file, so misuse always shows help.
        if (args.length == 0 || !validCommand(args)) {
            help();
            System.exit(1);
            return;
        }
        try {
            int index = -1;
            Creature creature = null;
            if (!args[0].equals("create")) {
                int row = Integer.parseInt(args[1]);
                if (row < 1) throw new IndexOutOfBoundsException("Rows start at 1.");
                index = row - 1;
            }
            if (args[0].equals("create")) creature = parseCreature(args[1]);
            if (args[0].equals("update")) creature = parseCreature(args[2]);
            CreatureRegistry registry = new CreatureRegistry("creature-data.csv");
            switch (args[0]) {
                case "create":
                    registry.addCreature(creature);
                    registry.save();
                    System.out.println("Created creature at row " + registry.getCount());
                    break;
                case "read":
                    registry.getCreature(index).showInformation();
                    break;
                case "update":
                    registry.modifyCreature(index, creature);
                    registry.save();
                    System.out.println("Updated row " + (index + 1));
                    break;
                case "delete":
                    registry.deleteCreature(index);
                    registry.save();
                    System.out.println("Deleted row " + (index + 1));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command");
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            // Preserve the exception type in stderr; the process reports failure.
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            help();
            System.exit(1);
        }
    }

    private static boolean validCommand(String[] args) {
        switch (args[0]) {
            case "create": case "read": case "delete": return args.length == 2;
            case "update": return args.length == 3;
            default: return false;
        }
    }

    private static Creature parseCreature(String text) {
        Map<String, String> fields = new HashMap<>();
        for (String token : text.trim().split("\\s+")) {
            String[] pair = token.split(":", 2);
            if (pair.length != 2 || pair[1].isEmpty()
                    || (!pair[0].equals("name") && !pair[0].equals("size") && !pair[0].equals("age"))
                    || fields.put(pair[0], pair[1]) != null) {
                throw new IllegalArgumentException("Use name:<name> size:<size> age:<integer> exactly once each.");
            }
        }
        if (fields.size() != 3) throw new IllegalArgumentException("Name, size, and age are required.");
        return new Creature(fields.get("name"), fields.get("size"), Integer.parseInt(fields.get("age")));
    }

    private static void help() {
        System.out.println("Usage: run from lab-03 with creature-data.csv present.");
        System.out.println("  java CreatureCLI create 'name:dragon size:Large age:8'");
        System.out.println("  java CreatureCLI read 1");
        System.out.println("  java CreatureCLI update 2 'name:phoenix size:Medium age:12'");
        System.out.println("  java CreatureCLI delete 3");
        System.out.println("Rows are numbered from 1. Create/update replace all three fields.");
        System.out.println("Keep the field string quoted; values cannot contain spaces or commas.");
        System.out.println("Windows Command Prompt: use double quotes instead of single quotes.");
    }
}
