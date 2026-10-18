import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Abstract base class for all living things in the ecosystem. */
public abstract class Creature implements TurnTaker {
    protected String name;
    protected int row;
    protected int col;
    protected int energy;
    protected String species;

    // Stretch: species definitions can refer to existing classes with new traits.
    private static final Map<String, String> SPECIES_REGISTRY = new LinkedHashMap<>();

    public Creature(String name, int row, int col, int energy) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A creature needs a name");
        }
        this.name = name;
        this.row = row;
        this.col = col;
        this.energy = energy;
        this.species = getClass().getSimpleName();
    }

    public static void registerSpecies(String species, String definition) {
        if (species == null || species.trim().isEmpty()
                || definition == null || definition.trim().isEmpty()) {
            throw new IllegalArgumentException("Species and definition must not be blank");
        }
        if (SPECIES_REGISTRY.containsKey(species)) {
            throw new IllegalArgumentException("Species already registered: " + species);
        }
        SPECIES_REGISTRY.put(species, definition);
    }

    public static String getSpeciesDefinition(String species) {
        return SPECIES_REGISTRY.get(species);
    }

    public static Map<String, String> getSpeciesRegistry() {
        return Collections.unmodifiableMap(SPECIES_REGISTRY);
    }

    public String getName() { return name; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getEnergy() { return energy; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public void setRow(int r) { row = r; }
    public void setCol(int c) { col = c; }
    public void addEnergy(int amount) { energy += amount; }

    public boolean isAlive() { return energy > 0; }

    public abstract String describe();
    public abstract char symbol();
}
