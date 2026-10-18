import java.util.ArrayList;

/** Shared placement algorithm. A strategy can customize what habitat is suitable. */
public abstract class NearbyReproduction implements ReproductionStrategy {
    private final int radius;

    protected NearbyReproduction(int radius) {
        this.radius = radius;
    }

    /** Override to limit offspring to a particular habitat or travel direction. */
    protected boolean suitable(Tile tile, int deltaRow, int deltaCol) {
        return tile.getWater() >= 5 && tile.getNutrient() >= 3;
    }

    @Override
    public boolean reproduce(Plant parent, World world) {
        ArrayList<int[]> locations = new ArrayList<>();
        for (int dr = -radius; dr <= radius; dr++) {
            for (int dc = -radius; dc <= radius; dc++) {
                if (dr == 0 && dc == 0) continue;
                int newRow = parent.getRow() + dr;
                int newCol = parent.getCol() + dc;
                if (!world.inBounds(newRow, newCol)) continue;
                Tile tile = world.getTile(newRow, newCol);
                if (tile.isEmpty() && suitable(tile, dr, dc)) {
                    locations.add(new int[]{newRow, newCol});
                }
            }
        }
        if (locations.isEmpty()) return false;
        int[] location = locations.get(world.getRandom().nextInt(locations.size()));
        world.addCreature(parent.createOffspring(location[0], location[1]),
                location[0], location[1]);
        return true;
    }
}
