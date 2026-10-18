/** Water hyacinth makes daughter plants by runners, only on adjacent wet tiles. */
public final class WaterRunnerReproduction extends NearbyReproduction {
    public WaterRunnerReproduction() {
        super(1);
    }

    @Override
    protected boolean suitable(Tile tile, int deltaRow, int deltaCol) {
        // Eight immediate neighboring cells represent nearby floating runners.
        return tile.getWater() >= 18 && tile.getNutrient() >= 5
                && tile.getTemperature() >= 18 && tile.getTemperature() <= 35;
    }
}
