import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class HelloGUI extends JFrame {
    private static final String[] GREETINGS = {
        "Hello!", "Hi there!", "Good morning!", "Welcome!", "How are you?"
    };
    private final Random random = new Random();

    public HelloGUI() {
        setTitle("Hello GUI");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel greetingLabel = new JLabel("", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JButton greetButton = new JButton("Greet");
        greetButton.addActionListener(event ->
                greetingLabel.setText(GREETINGS[random.nextInt(GREETINGS.length)]));

        add(greetingLabel, BorderLayout.CENTER);
        add(greetButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HelloGUI().setVisible(true));
    }
}
