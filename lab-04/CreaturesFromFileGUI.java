import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CreaturesFromFileGUI extends JFrame {
    private final ProcessCreatureFile processor = new ProcessCreatureFile();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> creatureList = new JList<>(listModel);
    private final JTextField nameField = new JTextField(12);
    private final JTextField sizeField = new JTextField(12);
    private final JTextField ageField = new JTextField(12);
    private final JTextArea detailsArea = new JTextArea(9, 18);
    private File currentFile = new File("creature-data.csv");

    public CreaturesFromFileGUI() {
        setTitle("Creatures From File GUI");
        setSize(790, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JScrollPane listPane = new JScrollPane(creatureList);
        listPane.setPreferredSize(new Dimension(155, 250));
        add(listPane, BorderLayout.WEST);

        JPanel editPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Size:"));
        editPanel.add(sizeField);
        editPanel.add(new JLabel("Age:"));
        editPanel.add(ageField);
        add(editPanel, BorderLayout.CENTER);

        detailsArea.setEditable(false);
        JScrollPane detailsPane = new JScrollPane(detailsArea);
        detailsPane.setPreferredSize(new Dimension(205, 250));
        add(detailsPane, BorderLayout.EAST);

        JButton openButton = new JButton("Open File");
        openButton.addActionListener(event -> chooseFile());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(event -> addCreature());
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(event -> removeCreature());
        JButton eatButton = new JButton("Eat");
        eatButton.addActionListener(event -> {
            int index = creatureList.getSelectedIndex();
            if (index >= 0) {
                processor.getCreature(index).eat();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> updateCreature());
        JButton saveFileButton = new JButton("Save to File");
        saveFileButton.addActionListener(event -> saveToFile());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(openButton);
        buttons.add(addButton);
        buttons.add(removeButton);
        buttons.add(eatButton);
        buttons.add(saveButton);
        buttons.add(saveFileButton);
        add(buttons, BorderLayout.SOUTH);

        creatureList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                showSelectedCreature();
            }
        });
        loadFile(currentFile);
    }

    private void loadFile(File file) {
        try {
            processor.loadFromFile(file.getPath());
            currentFile = file;
            refreshList(0);
            setTitle("Creatures From File GUI - " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not open file: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadFile(chooser.getSelectedFile());
        }
    }

    private void refreshList(int selectedIndex) {
        listModel.clear();
        for (Creature creature : processor.getCreatures()) {
            listModel.addElement(creature.name);
        }
        if (listModel.isEmpty()) {
            clearFields();
        } else {
            creatureList.setSelectedIndex(Math.min(Math.max(selectedIndex, 0), listModel.size() - 1));
        }
    }

    private void showSelectedCreature() {
        int index = creatureList.getSelectedIndex();
        if (index < 0) {
            clearFields();
            return;
        }
        Creature creature = processor.getCreature(index);
        nameField.setText(creature.name);
        sizeField.setText(creature.size);
        ageField.setText(String.valueOf(creature.age));
        detailsArea.setText("Name: " + creature.name
                + "\nSize: " + creature.size
                + "\nAge: " + creature.age);
    }

    private void clearFields() {
        nameField.setText("");
        sizeField.setText("");
        ageField.setText("");
        detailsArea.setText("");
    }

    private int readAge() {
        return Integer.parseInt(ageField.getText().trim());
    }

    private void addCreature() {
        try {
            processor.addCreature(nameField.getText(), sizeField.getText(), readAge());
            refreshList(processor.getCreatures().size() - 1);
        } catch (IllegalArgumentException ex) {
            showInputError(ex);
        }
    }

    private void updateCreature() {
        int index = creatureList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Select a creature first.");
            return;
        }
        try {
            processor.updateCreature(index, nameField.getText(), sizeField.getText(), readAge());
            refreshList(index);
        } catch (IllegalArgumentException ex) {
            showInputError(ex);
        }
    }

    private void removeCreature() {
        int index = creatureList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Select a creature first.");
            return;
        }
        processor.removeCreature(index);
        refreshList(index);
    }

    private void saveToFile() {
        try {
            processor.saveToFile(currentFile.getPath());
            JOptionPane.showMessageDialog(this, "Saved to " + currentFile.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save file: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInputError(IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreaturesFromFileGUI().setVisible(true));
    }
}
