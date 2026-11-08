package ecosystemgui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;

/** Displays text written to System.out during the simulation. */
public class StdoutLogPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextArea logArea;

    public StdoutLogPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Simulation Output (System.out)"));

        logArea = new JTextArea(8, 80);
        logArea.setEditable(false);
        logArea.setLineWrap(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Log");
        clearButton.addActionListener(event -> logArea.setText(""));
        add(clearButton, BorderLayout.EAST);
    }

    public JTextArea getLogArea() {
        return logArea;
    }
}
