import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bird extends Creature {
    // Both overloaded constructors directly invoke the base constructor.
    public Bird(String name, int row, int col) {
        super(name, row, col, 20);
    }

    public Bird(String name, int row, int col, int startingEnergy) {
        super(name, row, col, startingEnergy);
    }

    @Override
    public void takeTurn(World world) {
        Tile home = world.getTile(row, col);
        addEnergy(-1); // metabolism
        if (home.getNutrient() >= 10 && home.getWater() >= 10) addEnergy(2);
        if (home.getTemperature() < 14 || home.getTemperature() > 32) addEnergy(-1);
        for (Creature neighbor : home.getCreatures()) {
            if (neighbor instanceof Mammal) addEnergy(-1); // avoid predators
        }
        if (!isAlive()) return;

        List<int[]> directions = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr != 0 || dc != 0) directions.add(new int[]{dr, dc});
            }
        }
        Collections.shuffle(directions, world.getRandom());
        int[] bestDestination = null;
        int bestHabitatScore = Integer.MIN_VALUE;
        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            if (!world.inBounds(nextRow, nextCol)) continue;
            Tile target = world.getTile(nextRow, nextCol);
            if (!target.isEmpty()) continue;
            int habitatScore = target.getNutrient() + target.getWater() / 3
                    - Math.abs(target.getTemperature() - 24) / 2;
            if (habitatScore > bestHabitatScore) {
                bestHabitatScore = habitatScore;
                bestDestination = new int[]{nextRow, nextCol};
            }
        }
        if (bestDestination != null) {
            world.moveCreature(this, bestDestination[0], bestDestination[1]);
            addEnergy(-1); // flying costs energy
        }
    }

    @Override
    public String describe() {
        return "Bird: " + name + " at (" + row + "," + col + ") energy=" + energy;
    }

    @Override
    public char symbol() { return 'B'; }
}
