package ecosystemgui;

import ecosystem.World;
import ecosystem.WorldLoader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Lab 07 graphical interface.
 * The GUI displays and controls the existing Lab 05 World; it does not
 * implement a second set of ecosystem rules.
 */
public class EcosystemGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private World world;
    private File currentConfigFile;

    private final GridPanel gridPanel;
    private final CellInfoPanel cellInfoPanel;
    private final StdoutLogPanel logPanel;

    private final JLabel turnLabel;
    private final JLabel creatureLabel;
    private final JLabel fileLabel;
    private final JLabel statusLabel;

    private final JTextField speedInput;
    private final JButton playButton;

    private Timer timer;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private PrintStream originalOut;

    public EcosystemGUI(File initialConfigFile) throws Exception {
        super("EcosystemGUI - Lab 07");

        currentConfigFile = initialConfigFile.getAbsoluteFile();
        world = WorldLoader.load(currentConfigFile);

        gridPanel = new GridPanel();
        cellInfoPanel = new CellInfoPanel();
        logPanel = new StdoutLogPanel();

        turnLabel = new JLabel();
        creatureLabel = new JLabel();
        fileLabel = new JLabel();
        statusLabel = new JLabel("Ready");

        speedInput = new JTextField("500", 5);
        playButton = new JButton("Play");

        buildInterface();
        installStdoutCapture();
        refreshInterface();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1450, 900);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                stopSimulation();
                if (originalOut != null) {
                    System.setOut(originalOut);
                }
            }
        });

        System.out.println("EcosystemGUI started.");
        System.out.println("Loaded initial state from: " + currentConfigFile.getPath());
        System.out.println("Grid: " + world.getRows() + " rows x "
                + world.getCols() + " columns");
        System.out.println("Initial creatures: " + world.getCreatures().size());
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        root.add(buildTopSection(), BorderLayout.NORTH);

        gridPanel.setWorld(world, this::selectCell);
        JScrollPane gridScrollPane = new JScrollPane(gridPanel);
        gridScrollPane.setBorder(BorderFactory.createTitledBorder("Ecosystem Grid"));

        JPanel sidebar = new JPanel(new BorderLayout(6, 6));
        sidebar.setPreferredSize(new Dimension(360, 500));
        sidebar.add(cellInfoPanel, BorderLayout.CENTER);
        sidebar.add(buildLegend(), BorderLayout.SOUTH);

        JSplitPane centerSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, gridScrollPane, sidebar);
        centerSplit.setResizeWeight(0.72);
        centerSplit.setContinuousLayout(true);
        root.add(centerSplit, BorderLayout.CENTER);

        logPanel.setPreferredSize(new Dimension(1000, 190));
        root.add(logPanel, BorderLayout.SOUTH);
    }

    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout(8, 8));

        JPanel heading = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Ecosystem Simulation");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        heading.add(title, BorderLayout.WEST);

        JPanel statistics = new JPanel();
        statistics.add(turnLabel);
        statistics.add(new JLabel("     "));
        statistics.add(creatureLabel);
        heading.add(statistics, BorderLayout.EAST);

        top.add(heading, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        controls.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(event -> advanceOneTurn());

        playButton.addActionListener(event -> togglePlay());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(event -> resetWorld());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(event -> chooseConfigurationFile());

        controls.add(stepButton);
        controls.add(playButton);
        controls.add(new JLabel("Speed (ms):"));
        controls.add(speedInput);
        controls.add(resetButton);
        controls.add(loadButton);

        fileLabel.setHorizontalAlignment(SwingConstants.LEFT);
        controls.add(fileLabel);

        top.add(controls, BorderLayout.CENTER);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 3, 2));
        top.add(statusLabel, BorderLayout.SOUTH);

        return top;
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel();
        legend.setBorder(BorderFactory.createTitledBorder("Grid Legend"));
        legend.add(new JLabel("B = Bird"));
        legend.add(new JLabel("F = Fish"));
        legend.add(new JLabel("M = Mammal"));
        legend.add(new JLabel("A/R/W = Plant"));
        return legend;
    }

    private void installStdoutCapture() {
        originalOut = System.out;
        try {
            PrintStream guiStream = new PrintStream(
                    new TeeOutputStream(
                            originalOut,
                            new TextAreaOutputStream(logPanel.getLogArea())),
                    true,
                    "UTF-8");
            System.setOut(guiStream);
        } catch (UnsupportedEncodingException exception) {
            // UTF-8 is required by every standard Java implementation.
            throw new IllegalStateException("UTF-8 output is not available.", exception);
        }
    }

    private void selectCell(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        cellInfoPanel.showTile(world, row, col);
        gridPanel.refresh(world, selectedRow, selectedCol);
        statusLabel.setText("Selected tile: row " + row + ", column " + col);
    }

    private void advanceOneTurn() {
        try {
            world.takeTurn();

            System.out.println();
            System.out.println("========== TURN " + world.getTurn() + " ==========");
            System.out.println("Turn completed. Living creatures: "
                    + world.getCreatures().size());
            world.print();

            refreshInterface();
            statusLabel.setText("Completed turn " + world.getTurn());
        } catch (RuntimeException exception) {
            stopSimulation();
            showError("The simulation could not advance.", exception);
        }
    }

    private void togglePlay() {
        if (timer != null && timer.isRunning()) {
            stopSimulation();
            statusLabel.setText("Simulation paused at turn " + world.getTurn());
            return;
        }

        int delay;
        try {
            delay = Integer.parseInt(speedInput.getText().trim());
            if (delay < 50 || delay > 10000) {
                throw new NumberFormatException("Speed must be between 50 and 10000.");
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a whole-number speed between 50 and 10000 milliseconds.",
                    "Invalid speed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        timer = new Timer(delay, event -> advanceOneTurn());
        timer.setCoalesce(true);
        timer.start();
        playButton.setText("Stop");
        statusLabel.setText("Simulation running...");
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
        }
        playButton.setText("Play");
    }

    private void resetWorld() {
        stopSimulation();

        try {
            world = WorldLoader.load(currentConfigFile);
            selectedRow = -1;
            selectedCol = -1;
            cellInfoPanel.clearInformation();
            gridPanel.setWorld(world, this::selectCell);
            refreshInterface();

            System.out.println();
            System.out.println("World reset from: " + currentConfigFile.getPath());
            System.out.println("Turn reset to " + world.getTurn());
            statusLabel.setText("World reset to its initial state.");
        } catch (Exception exception) {
            showError("Could not reset the ecosystem.", exception);
        }
    }

    private void chooseConfigurationFile() {
        JFileChooser chooser = new JFileChooser(
                currentConfigFile == null ? new File(".") : currentConfigFile.getParentFile());
        chooser.setDialogTitle("Load an ecosystem configuration");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Ecosystem configuration (*.config, *.txt)", "config", "txt"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                World loadedWorld = WorldLoader.load(selectedFile);
                stopSimulation();

                world = loadedWorld;
                currentConfigFile = selectedFile.getAbsoluteFile();
                selectedRow = -1;
                selectedCol = -1;

                cellInfoPanel.clearInformation();
                gridPanel.setWorld(world, this::selectCell);
                refreshInterface();

                System.out.println();
                System.out.println("Loaded ecosystem configuration: "
                        + currentConfigFile.getPath());
                System.out.println("Grid: " + world.getRows() + " rows x "
                        + world.getCols() + " columns");
                System.out.println("Creatures loaded: " + world.getCreatures().size());

                statusLabel.setText("Loaded " + currentConfigFile.getName());
            } catch (Exception exception) {
                showError("Could not load the selected configuration file.", exception);
            }
        }
    }

    private void refreshInterface() {
        turnLabel.setText("Turn: " + world.getTurn());
        creatureLabel.setText("Creatures: " + world.getCreatures().size());
        fileLabel.setText("File: " + currentConfigFile.getName());

        gridPanel.refresh(world, selectedRow, selectedCol);

        if (selectedRow >= 0 && selectedCol >= 0
                && world.inBounds(selectedRow, selectedCol)) {
            cellInfoPanel.showTile(world, selectedRow, selectedCol);
        }
    }

    private void showError(String message, Exception exception) {
        String detail = exception.getMessage() == null
                ? exception.getClass().getSimpleName()
                : exception.getMessage();

        System.err.println(message + " " + detail);
        JOptionPane.showMessageDialog(
                this,
                message + "\n" + detail,
                "EcosystemGUI Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        File configFile = args.length > 0
                ? new File(args[0])
                : new File("data/initial-ecosystem.txt");

        if (!configFile.isFile()) {
            File originalConfig = new File("world.config");
            if (originalConfig.isFile()) {
                configFile = originalConfig;
            }
        }

        final File startupFile = configFile;

        SwingUtilities.invokeLater(() -> {
            try {
                EcosystemGUI window = new EcosystemGUI(startupFile);
                window.setVisible(true);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        null,
                        "The ecosystem could not be started.\n" + exception.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });
    }
}
