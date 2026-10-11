import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SingleCreatureGUI extends JFrame {
    private final Creature creature = new Creature("Leo", "Large", 5);
    private final JTextField nameField = new JTextField(creature.name, 12);
    private final JTextField sizeField = new JTextField(creature.size, 12);
    private final JTextField ageField = new JTextField(String.valueOf(creature.age), 12);
    private final JTextArea detailsArea = new JTextArea(8, 18);

    public SingleCreatureGUI() {
        setTitle("Single Creature GUI");
        setSize(540, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel editPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Size:"));
        editPanel.add(sizeField);
        editPanel.add(new JLabel("Age:"));
        editPanel.add(ageField);
        add(editPanel, BorderLayout.WEST);

        detailsArea.setEditable(false);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JButton eatButton = new JButton("Eat");
        eatButton.addActionListener(event -> creature.eat());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> saveChanges());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(eatButton);
        buttons.add(saveButton);
        add(buttons, BorderLayout.SOUTH);

        showCreature();
    }

    private void showCreature() {
        detailsArea.setText("Name: " + creature.name
                + "\nSize: " + creature.size
                + "\nAge: " + creature.age);
    }

    private void saveChanges() {
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if (age < 0 || nameField.getText().trim().isEmpty()
                    || sizeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Enter a name, size, and nonnegative age.");
            }
            creature.name = nameField.getText().trim();
            creature.size = sizeField.getText().trim();
            creature.age = age;
            showCreature();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SingleCreatureGUI().setVisible(true));
    }
}
