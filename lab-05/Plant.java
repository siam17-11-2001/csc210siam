import java.util.ArrayList;
import java.util.List;

/** Abstract plant: booleans, string traits, and composed reproduction strategies. */
public abstract class Plant extends Creature {
    protected final boolean perennial;
    protected final boolean woodenStem;
    protected final boolean evergreen;
    protected final boolean makesFlowers;
    protected final boolean bearsFruit;
    protected final String growthHabit;
    private final List<ReproductionStrategy> reproductionStrategies = new ArrayList<>();
    private String flowerPhase = "dormant";

    protected Plant(String name, int row, int col, int energy,
                    boolean perennial, boolean woodenStem, boolean evergreen,
                    boolean makesFlowers, boolean bearsFruit, String growthHabit,
                    ReproductionStrategy... strategies) {
        super(name, row, col, energy);
        this.perennial = perennial;
        this.woodenStem = woodenStem;
        this.evergreen = evergreen;
        this.makesFlowers = makesFlowers;
        this.bearsFruit = bearsFruit;
        this.growthHabit = growthHabit;
        java.util.Collections.addAll(reproductionStrategies, strategies);
    }

    public boolean isPerennial() { return perennial; }
    public boolean hasWoodenStem() { return woodenStem; }
    public boolean isEvergreen() { return evergreen; }
    public boolean makesFlowers() { return makesFlowers; }
    public boolean bearsFruit() { return bearsFruit; }
    public String getGrowthHabit() { return growthHabit; }
    public String getFlowerPhase() { return flowerPhase; }

    public boolean hasLeaves(World world) {
        return evergreen || world.getTile(row, col).getTemperature() >= 15;
    }

    public final void addReproductionStrategy(ReproductionStrategy strategy) {
        if (strategy != null) reproductionStrategies.add(strategy);
    }

    /** Same name/traits as this parent, but at the new position. */
    protected abstract Plant createOffspring(int childRow, int childCol);

    /** Delegate reproduction to an internal strategy object. */
    protected void tryReproduce(World world) {
        if (energy < 20 || reproductionStrategies.isEmpty()) return;
        int choice = world.getRandom().nextInt(reproductionStrategies.size());
        if (reproductionStrategies.get(choice).reproduce(this, world)) addEnergy(-8);
    }

    @Override
    public void takeTurn(World world) {
        Tile tile = world.getTile(row, col);
        boolean comfortable = tile.getWater() >= 5 && tile.getNutrient() >= 3
                && tile.getTemperature() >= 10 && tile.getTemperature() <= 35;
        if (comfortable) {
            addEnergy(2);                   // photosynthesis
            tile.changeWater(-1);           // consumes water
            if (world.getTurn() % 4 == 0) tile.changeNutrient(-1);
        } else {
            addEnergy(perennial ? -1 : -2); // annuals are less tolerant
        }
        // Flowers progress from buds to blossom to fruit (if fruit-bearing).
        if (makesFlowers && comfortable) {
            int phase = world.getTurn() % 18;
            flowerPhase = phase < 6 ? "buds" : (phase < 12 ? "flowers"
                    : (bearsFruit ? "fruit" : "seed heads"));
        } else {
            flowerPhase = "dormant";
        }
        if (isAlive() && comfortable) grow(world);
    }

    /** Different subclasses choose their own growing/reproduction schedule. */
    protected abstract void grow(World world);

    @Override
    public String describe() {
        return getSpecies() + ": " + name + " at (" + row + "," + col
                + ") energy=" + energy + " growth=" + growthHabit
                + " phase=" + flowerPhase;
    }
}
