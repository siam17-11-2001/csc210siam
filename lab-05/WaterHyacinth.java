/** Kochuripana (water hyacinth): floating aquatic plant reproducing by runners. */
public final class WaterHyacinth extends Plant {
    private int turnsFloating;

    public WaterHyacinth(String name, int row, int col) {
        super(name, row, col, 22, true, false, true,
                true, false, "floating", new WaterRunnerReproduction());
    }

    public WaterHyacinth(String name, int row, int col, int initialEnergy) {
        super(name, row, col, initialEnergy, true, false, true,
                true, false, "floating", new WaterRunnerReproduction());
    }

    @Override
    public void takeTurn(World world) {
        Tile pond = world.getTile(row, col);
        if (pond.getWater() < 18) {
            addEnergy(-5); // floating plants fare poorly when their tile dries out
        } else {
            turnsFloating++;
        }
        super.takeTurn(world);
    }

    @Override
    protected Plant createOffspring(int newRow, int newCol) {
        WaterHyacinth daughter = new WaterHyacinth(name + "-runner", newRow, newCol, 15);
        daughter.setSpecies(getSpecies());
        return daughter;
    }

    @Override
    protected void grow(World world) {
        Tile pond = world.getTile(row, col);
        if (turnsFloating >= 8 && world.getTurn() % 12 == 0
                && pond.getWater() >= 18 && pond.getNutrient() >= 5) {
            tryReproduce(world);
        }
    }

    @Override
    public char symbol() {
        return 'W';
    }
}
