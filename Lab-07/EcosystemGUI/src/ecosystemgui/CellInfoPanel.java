package ecosystemgui;

import ecosystem.Creature;
import ecosystem.MangoTree;
import ecosystem.Plant;
import ecosystem.Tile;
import ecosystem.World;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;

/** Shows the selected tile's habitat values and all creatures on that tile. */
public class CellInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextArea information;

    public CellInfoPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Selected Cell Information"));

        information = new JTextArea();
        information.setEditable(false);
        information.setLineWrap(true);
        information.setWrapStyleWord(true);
        information.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        information.setText("Click a grid cell to inspect its information.");

        add(new JScrollPane(information), BorderLayout.CENTER);
    }

    public void showTile(World world, int row, int col) {
        Tile tile = world.getTile(row, col);
        StringBuilder text = new StringBuilder();

        text.append("POSITION\n");
        text.append("Row: ").append(row).append('\n');
        text.append("Column: ").append(col).append("\n\n");

        text.append("ENVIRONMENT\n");
        text.append("Water: ").append(tile.getWater()).append('\n');
        text.append("Temperature: ").append(tile.getTemperature()).append('\n');
        text.append("Nutrient: ").append(tile.getNutrient()).append('\n');
        text.append("Empty: ").append(tile.isEmpty() ? "Yes" : "No").append("\n\n");

        text.append("CREATURES (").append(tile.getCreatures().size()).append(")\n");

        if (tile.isEmpty()) {
            text.append("No creatures are currently in this cell.\n");
        } else {
            int number = 1;
            for (Creature creature : tile.getCreatures()) {
                text.append("\n").append(number++).append(". ")
                        .append(creature.getName()).append('\n');
                text.append("Type: ").append(creature.getClass().getSimpleName()).append('\n');
                text.append("Species: ").append(creature.getSpecies()).append('\n');
                text.append("Energy: ").append(creature.getEnergy()).append('\n');
                text.append("Alive: ").append(creature.isAlive() ? "Yes" : "No").append('\n');
                text.append("Description: ").append(creature.describe()).append('\n');

                if (creature instanceof Plant) {
                    appendPlantDetails(text, (Plant) creature, world);
                }
            }
        }

        information.setText(text.toString());
        information.setCaretPosition(0);
    }

    public void clearInformation() {
        information.setText("Click a grid cell to inspect its information.");
    }

    private void appendPlantDetails(StringBuilder text, Plant plant, World world) {
        text.append("Plant attributes:\n");
        text.append("  Perennial: ").append(plant.isPerennial()).append('\n');
        text.append("  Wooden stem: ").append(plant.hasWoodenStem()).append('\n');
        text.append("  Evergreen: ").append(plant.isEvergreen()).append('\n');
        text.append("  Makes flowers: ").append(plant.makesFlowers()).append('\n');
        text.append("  Bears fruit: ").append(plant.bearsFruit()).append('\n');
        text.append("  Growth habit: ").append(plant.getGrowthHabit()).append('\n');
        text.append("  Flower phase: ").append(plant.getFlowerPhase()).append('\n');
        text.append("  Has leaves: ").append(plant.hasLeaves(world)).append('\n');

        if (plant instanceof MangoTree) {
            text.append("  Fruit harvests: ")
                    .append(((MangoTree) plant).getFruitHarvests()).append('\n');
        }
    }
}
