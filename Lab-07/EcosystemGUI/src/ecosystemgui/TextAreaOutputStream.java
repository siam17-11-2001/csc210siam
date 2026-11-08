package ecosystemgui;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** Sends System.out text to a Swing text area without blocking the simulation thread. */
public class TextAreaOutputStream extends OutputStream {
    private static final int MAX_CHARACTERS = 120000;

    private final JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int value) {
        append(new String(new byte[]{(byte) value}, StandardCharsets.UTF_8));
    }

    @Override
    public void write(byte[] bytes, int offset, int length) {
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }
        if (offset < 0 || length < 0 || length > bytes.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return;
        }
        append(new String(bytes, offset, length, StandardCharsets.UTF_8));
    }

    private void append(String value) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(value);

            int excess = textArea.getDocument().getLength() - MAX_CHARACTERS;
            if (excess > 0) {
                try {
                    textArea.getDocument().remove(0, excess);
                } catch (BadLocationException exception) {
                    // Keeping the log visible is more important than trimming one update.
                }
            }

            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
