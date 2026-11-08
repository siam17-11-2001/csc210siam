package ecosystemgui;

import java.io.IOException;
import java.io.OutputStream;

/** Copies output to both the original console and the GUI log. */
public class TeeOutputStream extends OutputStream {
    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void write(int value) throws IOException {
        first.write(value);
        second.write(value);
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        first.write(bytes, offset, length);
        second.write(bytes, offset, length);
    }

    @Override
    public void flush() throws IOException {
        first.flush();
        second.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        // Do not close System.out, which is owned by the Java runtime.
    }
}
