import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Loads world.config, constructs Creatures, and prints 100 simulation turns. */
public class Main {
    // The registry allows named species with different properties, without new classes.
    private static void registerSpecies() {
        if (Creature.getSpeciesDefinition("Bird") != null) return;
        Creature.registerSpecies("Bird", "class=Bird");
        Creature.registerSpecies("Fish", "class=Fish");
        Creature.registerSpecies("Mammal", "class=Mammal");
        Creature.registerSpecies("MangoTree", "class=MangoTree");
        Creature.registerSpecies("RicePlant", "class=RicePlant");
        Creature.registerSpecies("WaterHyacinth", "class=WaterHyacinth");
        // Optional named varieties reuse the same Java classes.
        Creature.registerSpecies("LangraMango", "class=MangoTree");
        Creature.registerSpecies("BRRI28Rice", "class=RicePlant");
        Creature.registerSpecies("Kochuripana", "class=WaterHyacinth");
    }

    /** Split whitespace-separated tokens, then split each key=value token. */
    private static Map<String, String> parseLine(String line) {
        Map<String, String> values = new HashMap<>();
        for (String token : line.trim().split(" ")) {
            if (token.isEmpty()) continue;
            String[] pair = token.split("=");
            if (pair.length == 2) values.put(pair[0], pair[1]);
        }
        return values;
    }

    /** Parse the semicolon-separated string stored in the species registry. */
    private static Map<String, String> parseDefinition(String definition) {
        Map<String, String> result = new HashMap<>();
        for (String token : definition.split(";")) {
            String[] pair = token.split("=");
            if (pair.length == 2) result.put(pair[0], pair[1]);
        }
        return result;
    }

    private static Creature makeCreature(String species, String name, int row, int col) {
        String definition = Creature.getSpeciesDefinition(species);
        if (definition == null) throw new IllegalArgumentException("Unknown species: " + species);
        Map<String, String> traits = parseDefinition(definition);
        String kind = traits.get("class");
        Creature creature;
        if ("Bird".equals(kind)) {
            creature = new Bird(name, row, col);
        } else if ("Fish".equals(kind)) {
            creature = new Fish(name, row, col);
        } else if ("Mammal".equals(kind)) {
            creature = new Mammal(name, row, col);
        } else if ("MangoTree".equals(kind)) {
            creature = new MangoTree(name, row, col);
        } else if ("RicePlant".equals(kind)) {
            creature = new RicePlant(name, row, col);
        } else if ("WaterHyacinth".equals(kind)) {
            creature = new WaterHyacinth(name, row, col);
        } else {
            throw new IllegalArgumentException("Unsupported class in species definition: " + kind);
        }
        creature.setSpecies(species);
        return creature;
    }

    /** Pick an empty tile for additional creatures created by count=N. */
    private static int[] randomEmptyPosition(World world) {
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                if (world.getTile(r, c).isEmpty()) empty.add(new int[]{r, c});
            }
        }
        if (empty.isEmpty()) throw new IllegalStateException("No empty tiles for count=N");
        return empty.get(world.getRandom().nextInt(empty.size()));
    }

    public static void main(String[] args) throws IOException {
        registerSpecies();
        String configFile = args.length > 0 ? args[0] : "world.config";
        World world = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                Map<String, String> entry = parseLine(line);

                if (world == null) {
                    int rows = Integer.parseInt(entry.get("rows"));
                    int cols = Integer.parseInt(entry.get("cols"));
                    world = new World(rows, cols);
                    continue;
                }

                String species = entry.get("species");
                int col = Integer.parseInt(entry.get("x")); // x = column
                int row = Integer.parseInt(entry.get("y")); // y = row
                if (!world.inBounds(row, col)) {
                    throw new IllegalArgumentException("Out-of-bounds config position for " + species);
                }
                int count = Integer.parseInt(entry.getOrDefault("count", "1"));
                if (count < 1) throw new IllegalArgumentException("count must be at least 1");
                String baseName = entry.getOrDefault("name", species);
                for (int n = 0; n < count; n++) {
                    int[] position = n == 0 ? new int[]{row, col} : randomEmptyPosition(world);
                    String name = count == 1 ? baseName : baseName + "-" + (n + 1);
                    world.addCreature(makeCreature(species, name, position[0], position[1]),
                            position[0], position[1]);
                }
            }
        }

        if (world == null) throw new IllegalArgumentException("Missing dimensions in " + configFile);
        for (int i = 0; i < 100; i++) {
            world.takeTurn();
            world.print();
            System.out.println();
        }
    }
}
