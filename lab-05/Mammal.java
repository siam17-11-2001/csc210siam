import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mammal extends Creature {
    public Mammal(String name, int row, int col) {
        super(name, row, col, 25);
    }

    @Override
    public void takeTurn(World world) {
        Tile current = world.getTile(row, col);
        addEnergy(-1); // metabolism
        if (current.getWater() < 8 || current.getTemperature() < 14) addEnergy(-1);
        if (!isAlive()) return;

        List<int[]> neighbors = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr != 0 || dc != 0) neighbors.add(new int[]{dr, dc});
            }
        }
        Collections.shuffle(neighbors, world.getRandom());

        // Eat neighboring plants, then move into the vacated tile.
        for (int[] d : neighbors) {
            int r = row + d[0];
            int c = col + d[1];
            if (!world.inBounds(r, c)) continue;
            for (Creature prey : new ArrayList<>(world.getTile(r, c).getCreatures())) {
                if (prey instanceof Plant) {
                    world.removeCreature(prey);
                    addEnergy(10);
                    world.moveCreature(this, r, c);
                    addEnergy(-1);
                    return;
                }
            }
        }
        // No food nearby: walk into a random empty neighbor.
        for (int[] d : neighbors) {
            int r = row + d[0];
            int c = col + d[1];
            if (world.inBounds(r, c) && world.getTile(r, c).isEmpty()) {
                world.moveCreature(this, r, c);
                addEnergy(-1);
                return;
            }
        }
    }

    @Override
    public String describe() {
        return "Mammal: " + name + " at (" + row + "," + col + ") energy=" + energy;
    }

    @Override
    public char symbol() { return 'M'; }
}
