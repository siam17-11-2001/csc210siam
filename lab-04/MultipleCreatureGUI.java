import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MultipleCreatureGUI extends JFrame {
    private final ArrayList<Creature> creatures = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> creatureList = new JList<>(listModel);
    private final JTextField nameField = new JTextField(12);
    private final JTextField sizeField = new JTextField(12);
    private final JTextField ageField = new JTextField(12);
    private final JTextArea detailsArea = new JTextArea(9, 18);

    public MultipleCreatureGUI() {
        setTitle("Multiple Creature GUI");
        setSize(750, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        creatures.add(new Creature("Leo", "Large", 5));
        creatures.add(new Creature("Milo", "Small", 2));
        creatures.add(new Creature("Luna", "Medium", 4));
        for (Creature creature : creatures) {
            listModel.addElement(creature.name);
        }

        JScrollPane listPane = new JScrollPane(creatureList);
        listPane.setPreferredSize(new Dimension(155, 240));
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
        detailsPane.setPreferredSize(new Dimension(210, 240));
        add(detailsPane, BorderLayout.EAST);

        JButton eatButton = new JButton("Eat");
        eatButton.addActionListener(event -> {
            int index = creatureList.getSelectedIndex();
            if (index >= 0) {
                creatures.get(index).eat();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> saveChanges());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(eatButton);
        buttons.add(saveButton);
        add(buttons, BorderLayout.SOUTH);

        creatureList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                showSelectedCreature();
            }
        });
        creatureList.setSelectedIndex(0);
    }

    private void showSelectedCreature() {
        int index = creatureList.getSelectedIndex();
        if (index < 0) {
            return;
        }
        Creature creature = creatures.get(index);
        nameField.setText(creature.name);
        sizeField.setText(creature.size);
        ageField.setText(String.valueOf(creature.age));
        detailsArea.setText("Name: " + creature.name
                + "\nSize: " + creature.size
                + "\nAge: " + creature.age);
    }

    private void saveChanges() {
        int index = creatureList.getSelectedIndex();
        if (index < 0) {
            return;
        }
        try {
            String name = nameField.getText().trim();
            String size = sizeField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            if (name.isEmpty() || size.isEmpty() || age < 0) {
                throw new IllegalArgumentException("Enter a name, size, and nonnegative age.");
            }
            Creature creature = creatures.get(index);
            creature.name = name;
            creature.size = size;
            creature.age = age;
            listModel.set(index, creature.name);
            showSelectedCreature();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MultipleCreatureGUI().setVisible(true));
    }
}
