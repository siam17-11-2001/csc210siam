/** Aam gach (mango): evergreen woody fruit tree that spreads by seed. */
public final class MangoTree extends Plant {
    private int fruitHarvests;

    public MangoTree(String name, int row, int col) {
        super(name, row, col, 30, true, true, true,
                true, true, "tall", new SeedReproduction());
    }

    // Overloaded constructor explicitly delegates common initialization to Plant.
    public MangoTree(String name, int row, int col, int initialEnergy) {
        super(name, row, col, initialEnergy, true, true, true,
                true, true, "tall", new SeedReproduction());
    }

    @Override
    protected Plant createOffspring(int newRow, int newCol) {
        MangoTree sapling = new MangoTree(name + "-sapling", newRow, newCol, 17);
        sapling.setSpecies(getSpecies());
        return sapling;
    }

    @Override
    protected void grow(World world) {
        Tile soil = world.getTile(row, col);
        // Fruit appears late in an 18-turn illustrative seasonal cycle.
        if (getFlowerPhase().equals("fruit") && world.getTurn() % 18 == 15
                && soil.getWater() >= 8 && soil.getNutrient() >= 6) {
            fruitHarvests++;
            tryReproduce(world);
        }
    }

    public int getFruitHarvests() {
        return fruitHarvests;
    }

    @Override
    public char symbol() {
        return 'A'; // Aam
    }
}
