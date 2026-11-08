package ecosystemgui;

import ecosystem.Creature;
import ecosystem.Mammal;
import ecosystem.Plant;
import ecosystem.Fish;
import ecosystem.Bird;
import ecosystem.World;
import ecosystem.Tile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;

/** Displays the real Tile objects held by the Lab 05 World. */
public class GridPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton[][] cellButtons;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GridPanel() {
        setBackground(Color.WHITE);
    }

    public void setWorld(World world, BiConsumer<Integer, Integer> selectionHandler) {
        removeAll();

        int rows = world.getRows();
        int cols = world.getCols();
        cellButtons = new JButton[rows][cols];
        setLayout(new GridLayout(rows, cols, 1, 1));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int selectedR = row;
                final int selectedC = col;

                JButton button = new JButton();
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
                button.setFocusable(false);
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.addActionListener(event ->
                        selectionHandler.accept(selectedR, selectedC));

                cellButtons[row][col] = button;
                add(button);
            }
        }

        setPreferredSize(new Dimension(Math.max(500, cols * 25),
                Math.max(300, rows * 25)));
        revalidate();
        repaint();
    }

    public void refresh(World world, int selectedRow, int selectedCol) {
        if (cellButtons == null
                || cellButtons.length != world.getRows()
                || cellButtons[0].length != world.getCols()) {
            return;
        }

        this.selectedRow = selectedRow;
        this.selectedCol = selectedCol;

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                Tile tile = world.getTile(row, col);
                JButton button = cellButtons[row][col];

                button.setText(getCellText(tile));
                button.setBackground(getCellColor(tile));
                button.setForeground(new Color(35, 45, 55));
                button.setBorder(row == selectedRow && col == selectedCol
                        ? BorderFactory.createLineBorder(new Color(220, 75, 55), 3)
                        : BorderFactory.createLineBorder(new Color(190, 198, 205), 1));
                button.setToolTipText(createTooltip(tile, row, col));
            }
        }

        repaint();
    }

    private String getCellText(Tile tile) {
        if (tile.isEmpty()) {
            return "";
        }

        String text = String.valueOf(tile.symbol());
        if (tile.getCreatures().size() > 1) {
            text += "+";
        }
        return text;
    }

    private Color getCellColor(Tile tile) {
        if (tile.isEmpty()) {
            int water = tile.getWater();
            if (water >= 18) return new Color(218, 237, 246);
            if (water >= 10) return new Color(229, 240, 222);
            return new Color(244, 232, 211);
        }

        Creature creature = tile.getOccupant();
        if (creature instanceof Fish) return new Color(135, 190, 230);
        if (creature instanceof Bird) return new Color(190, 218, 242);
        if (creature instanceof Mammal) return new Color(232, 202, 158);
        if (creature instanceof Plant) return new Color(157, 205, 151);
        return new Color(210, 210, 210);
    }

    private String createTooltip(Tile tile, int row, int col) {
        StringBuilder tip = new StringBuilder("<html><b>Tile (")
                .append(row).append(", ").append(col).append(")</b><br>")
                .append("Water: ").append(tile.getWater())
                .append(" | Temperature: ").append(tile.getTemperature())
                .append(" | Nutrient: ").append(tile.getNutrient())
                .append("<br>");

        if (tile.isEmpty()) {
            tip.append("No creatures");
        } else {
            for (Creature creature : tile.getCreatures()) {
                tip.append(creature.describe()).append("<br>");
            }
        }

        return tip.append("</html>").toString();
    }
}
