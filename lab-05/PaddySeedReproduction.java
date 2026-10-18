/** Rice establishes seedlings on nearby warm, moist, nutrient-rich tiles. */
public final class PaddySeedReproduction extends NearbyReproduction {
    public PaddySeedReproduction() {
        super(2);
    }

    @Override
    protected boolean suitable(Tile tile, int deltaRow, int deltaCol) {
        return tile.getWater() >= 15 && tile.getNutrient() >= 7
                && tile.getTemperature() >= 18 && tile.getTemperature() <= 35;
    }
}
