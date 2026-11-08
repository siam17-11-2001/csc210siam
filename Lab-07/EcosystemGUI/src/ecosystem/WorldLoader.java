package ecosystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads a World from the whitespace-separated world.config format used in Lab 05.
 * The format is intentionally kept compatible with the original Main class.
 */
public final class WorldLoader {
    private WorldLoader() {
    }

    public static World load(File configFile) throws IOException {
        if (configFile == null) {
            throw new IllegalArgumentException("A configuration file is required.");
        }
        if (!configFile.isFile()) {
            throw new IOException("Configuration file not found: " + configFile.getAbsolutePath());
        }

        registerSpecies();
        World world = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                Map<String, String> entry = parseLine(line);

                try {
                    if (world == null) {
                        String rowsValue = entry.get("rows");
                        String colsValue = entry.get("cols");

                        if (rowsValue == null || colsValue == null) {
                            throw new IllegalArgumentException(
                                    "The first configuration entry must contain rows and cols.");
                        }

                        int rows = Integer.parseInt(rowsValue);
                        int cols = Integer.parseInt(colsValue);
                        world = new World(rows, cols);
                        continue;
                    }

                    String species = required(entry, "species");
                    String name = entry.getOrDefault("name", species);
                    int col = Integer.parseInt(required(entry, "x"));
                    int row = Integer.parseInt(required(entry, "y"));

                    if (!world.inBounds(row, col)) {
                        throw new IllegalArgumentException(
                                "Position (" + row + "," + col + ") is outside the world.");
                    }

                    int count = Integer.parseInt(entry.getOrDefault("count", "1"));
                    if (count < 1) {
                        throw new IllegalArgumentException("count must be at least 1.");
                    }

                    for (int n = 0; n < count; n++) {
                        int[] position = n == 0
                                ? new int[]{row, col}
                                : randomEmptyPosition(world);

                        String creatureName = count == 1 ? name : name + "-" + (n + 1);
                        Creature creature = makeCreature(
                                species, creatureName, position[0], position[1]);

                        world.addCreature(creature, position[0], position[1]);
                    }
                } catch (RuntimeException exception) {
                    throw new IOException(
                            "Invalid configuration at line " + lineNumber + ": "
                                    + exception.getMessage(),
                            exception);
                }
            }
        }

        if (world == null) {
            throw new IOException("The configuration file does not define rows and cols.");
        }

        return world;
    }

    private static String required(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }
        return value;
    }

    private static Map<String, String> parseLine(String line) {
        Map<String, String> values = new HashMap<>();

        for (String token : line.split("\\s+")) {
            int equalsPosition = token.indexOf('=');
            if (equalsPosition <= 0 || equalsPosition == token.length() - 1) {
                continue;
            }

            String key = token.substring(0, equalsPosition);
            String value = token.substring(equalsPosition + 1);
            values.put(key, value);
        }

        return values;
    }

    private static void registerSpecies() {
        // Register only once. The registry is shared by all World instances.
        if (Creature.getSpeciesDefinition("Bird") != null) {
            return;
        }

        Creature.registerSpecies("Bird", "class=Bird");
        Creature.registerSpecies("Fish", "class=Fish");
        Creature.registerSpecies("Mammal", "class=Mammal");
        Creature.registerSpecies("MangoTree", "class=MangoTree");
        Creature.registerSpecies("RicePlant", "class=RicePlant");
        Creature.registerSpecies("WaterHyacinth", "class=WaterHyacinth");

        // Named varieties from the original Lab 05 configuration.
        Creature.registerSpecies("LangraMango", "class=MangoTree");
        Creature.registerSpecies("BRRI28Rice", "class=RicePlant");
        Creature.registerSpecies("Kochuripana", "class=WaterHyacinth");
    }

    private static Creature makeCreature(String species, String name, int row, int col) {
        String definition = Creature.getSpeciesDefinition(species);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown species: " + species);
        }

        String kind = definition.substring("class=".length());
        Creature creature;

        switch (kind) {
            case "Bird":
                creature = new Bird(name, row, col);
                break;
            case "Fish":
                creature = new Fish(name, row, col);
                break;
            case "Mammal":
                creature = new Mammal(name, row, col);
                break;
            case "MangoTree":
                creature = new MangoTree(name, row, col);
                break;
            case "RicePlant":
                creature = new RicePlant(name, row, col);
                break;
            case "WaterHyacinth":
                creature = new WaterHyacinth(name, row, col);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported class in species definition: " + kind);
        }

        creature.setSpecies(species);
        return creature;
    }

    private static int[] randomEmptyPosition(World world) {
        List<int[]> emptyPositions = new ArrayList<>();

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                if (world.getTile(row, col).isEmpty()) {
                    emptyPositions.add(new int[]{row, col});
                }
            }
        }

        if (emptyPositions.isEmpty()) {
            throw new IllegalStateException("No empty tiles are available for count=N.");
        }

        return emptyPositions.get(world.getRandom().nextInt(emptyPositions.size()));
    }
}
