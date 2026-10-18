import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** One cell: environment and any number of living creatures. */
public class Tile implements TurnTaker {
    private final ArrayList<Creature> creatures = new ArrayList<>();
    private int water;
    private int temperature;
    private int nutrient;

    public Tile() { this(12, 22, 8); }

    public Tile(int water, int temperature, int nutrient) {
        this.water = Math.max(0, water);
        this.temperature = temperature;
        this.nutrient = Math.max(0, nutrient);
    }

    public boolean isEmpty() { return creatures.isEmpty(); }
    public List<Creature> getCreatures() { return Collections.unmodifiableList(creatures); }
    public Creature getOccupant() { return isEmpty() ? null : creatures.get(0); }
    public void addCreature(Creature creature) {
        if (creature != null && !creatures.contains(creature)) creatures.add(creature);
    }
    public void removeCreature(Creature creature) { creatures.remove(creature); }
    // Compatibility with the starter's single-occupant API.
    public void setOccupant(Creature creature) {
        clear();
        addCreature(creature);
    }
    public void clear() { creatures.clear(); }

    public int getWater() { return water; }
    public int getTemperature() { return temperature; }
    public int getNutrient() { return nutrient; }
    public void changeWater(int amount) { water = Math.max(0, water + amount); }
    public void changeNutrient(int amount) { nutrient = Math.max(0, nutrient + amount); }

    @Override
    public void takeTurn(World world) {
        // Snapshot prevents ConcurrentModificationException if creatures move or reproduce.
        for (Creature creature : new ArrayList<>(creatures)) {
            // The World ensures every original creature acts at most once per turn.
            if (world.claimTurn(creature)) creature.takeTurn(world);
        }
    }

    /** Each day brings rain and slower replenishment of soil nutrients. */
    public void replenishResources(int turn) {
        changeWater(1);
        if (water > 25) water = 25;
        if (turn % 3 == 0) changeNutrient(1);
        if (nutrient > 16) nutrient = 16;
    }

    public char symbol() { return isEmpty() ? '.' : creatures.get(0).symbol(); }
}
