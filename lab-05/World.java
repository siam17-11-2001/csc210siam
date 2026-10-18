import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** 2-D grid of Tiles plus the simulation's single turn loop. */
public class World implements TurnTaker {
    private final int rows;
    private final int cols;
    private final Tile[][] grid;
    private final List<Creature> creatures = new ArrayList<>();
    private final Random random = new Random(42);
    private Set<Creature> scheduled = Collections.emptySet();
    private Set<Creature> alreadyActed = Collections.emptySet();
    private int turn;

    public World(int rows, int cols) {
        if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("World dimensions must be positive");
        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Small deterministic differences in each cell's habitat.
                grid[r][c] = new Tile(9 + (r * 7 + c * 3) % 14,
                        12 + (r * 2 + c) % 19,
                        5 + (r * 3 + c * 5) % 11);
            }
        }
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
    public Tile getTile(int r, int c) {
        if (!inBounds(r, c)) throw new IndexOutOfBoundsException("Tile outside world");
        return grid[r][c];
    }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getTurn() { return turn; }
    public Random getRandom() { return random; }
    public List<Creature> getCreatures() { return Collections.unmodifiableList(creatures); }

    public void addCreature(Creature creature, int row, int col) {
        if (creature == null || !inBounds(row, col) || creatures.contains(creature)) {
            throw new IllegalArgumentException("Cannot add creature at (" + row + "," + col + ")");
        }
        creature.setRow(row);
        creature.setCol(col);
        grid[row][col].addCreature(creature);
        creatures.add(creature);
    }

    public void removeCreature(Creature creature) {
        if (creature != null && creatures.remove(creature) && inBounds(creature.getRow(), creature.getCol())) {
            grid[creature.getRow()][creature.getCol()].removeCreature(creature);
        }
    }

    public void moveCreature(Creature creature, int newRow, int newCol) {
        if (!inBounds(newRow, newCol) || !creatures.contains(creature)) return;
        grid[creature.getRow()][creature.getCol()].removeCreature(creature);
        creature.setRow(newRow);
        creature.setCol(newCol);
        grid[newRow][newCol].addCreature(creature);
    }

    /** Called from Tile; prevents a moving creature from receiving two turns. */
    public boolean claimTurn(Creature creature) {
        return scheduled.contains(creature) && creatures.contains(creature)
                && creature.isAlive() && alreadyActed.add(creature);
    }

    public void takeTurn() { takeTurn(this); }

    @Override
    public void takeTurn(World world) {
        if (world != this) throw new IllegalArgumentException("World must advance itself");
        turn++;
        scheduled = new HashSet<>(creatures);
        alreadyActed = new HashSet<>();
        // A plant born mid-turn is not in 'scheduled' and acts next turn.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].takeTurn(this);
                grid[r][c].replenishResources(turn);
            }
        }
        for (Creature creature : new ArrayList<>(creatures)) {
            if (!creature.isAlive()) removeCreature(creature);
        }
        scheduled = Collections.emptySet();
        alreadyActed = Collections.emptySet();
    }

    public void print() {
        System.out.println("Turn " + turn + "  Creatures: " + creatures.size());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) System.out.print(grid[r][c].symbol());
            System.out.println();
        }
    }
}
