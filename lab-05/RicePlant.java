/** Dhan (rice): seasonal, soft-stemmed crop requiring wet, fertile soil. */
public final class RicePlant extends Plant {
    private int ageInTurns;

    public RicePlant(String name, int row, int col) {
        super(name, row, col, 18, false, false, false,
                true, false, "short", new PaddySeedReproduction());
    }

    public RicePlant(String name, int row, int col, int initialEnergy) {
        super(name, row, col, initialEnergy, false, false, false,
                true, false, "short", new PaddySeedReproduction());
    }

    @Override
    public void takeTurn(World world) {
        ageInTurns++;
        Tile paddy = world.getTile(row, col);
        // Water is a toy moisture index; the crop tolerates wet habitats best.
        if (paddy.getWater() < 15 || paddy.getNutrient() < 5) {
            addEnergy(-3);
        }
        super.takeTurn(world);
        // Annual crop: plants naturally senesce late in their life cycle.
        if (ageInTurns > 44) addEnergy(-6);
    }

    @Override
    protected Plant createOffspring(int newRow, int newCol) {
        RicePlant seedling = new RicePlant(name + "-seedling", newRow, newCol, 12);
        seedling.setSpecies(getSpecies());
        return seedling;
    }

    @Override
    protected void grow(World world) {
        Tile field = world.getTile(row, col);
        // The superclass has already consumed one unit of tile water this turn.
        if (ageInTurns >= 12 && getFlowerPhase().equals("seed heads")
                && world.getTurn() % 18 == 15 && field.getWater() >= 14
                && field.getNutrient() >= 7) {
            tryReproduce(world);
        }
    }

    @Override
    public char symbol() {
        return 'R'; // Rice
    }
}
