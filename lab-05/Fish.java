import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fish extends Creature {
    public Fish(String name, int row, int col) {
        super(name, row, col, 15);
    }

    @Override
    public void takeTurn(World world) {
        Tile home = world.getTile(row, col);
        if (home.getWater() >= 14 && home.getTemperature() <= 29) addEnergy(1);
        else addEnergy(-2); // dry or hot water harms fish
        if (!isAlive()) return;

        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-1, 0});
        directions.add(new int[]{1, 0});
        directions.add(new int[]{0, -1});
        directions.add(new int[]{0, 1});
        Collections.shuffle(directions, world.getRandom());
        int[] bestDestination = null;
        int highestWater = Integer.MIN_VALUE;
        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            if (!world.inBounds(nextRow, nextCol)) continue;
            Tile target = world.getTile(nextRow, nextCol);
            if (!target.isEmpty() || target.getWater() < 12) continue;
            if (target.getWater() > highestWater) {
                highestWater = target.getWater();
                bestDestination = new int[]{nextRow, nextCol};
            }
        }
        if (bestDestination != null) {
            world.moveCreature(this, bestDestination[0], bestDestination[1]);
            addEnergy(-1); // swimming costs energy
        }
    }

    @Override
    public String describe() {
        return "Fish: " + name + " at (" + row + "," + col + ") energy=" + energy;
    }

    @Override
    public char symbol() { return 'F'; }
}
