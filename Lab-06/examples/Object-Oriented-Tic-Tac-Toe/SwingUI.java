import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Swing user interface for the Tic-Tac-Toe game.
 */
public class SwingUI implements TicTacToeUI {
    /** Creates a SwingUI instance. */
    public SwingUI() { }
    private TicTacToeGame game;
    private boolean vsAI;
    private Opponent ai;
    private TicTacToeGame.Mark aiAs;

    private final JButton[] cells = new JButton[9];
    private final JLabel status = new JLabel(" ");
    private final JFrame frame = new JFrame("Tic-Tac-Toe");

    /**
     * Starts the Swing interface on the Swing event dispatch thread.
     *
     * @param game game model being played or inspected
     * @param vsAI whether a computer opponent is enabled
     * @param ai computer opponent strategy
     * @param aiPlaysAs mark assigned to the computer opponent
     */
    @Override
    public void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs) {
        this.game = game;
        this.vsAI = vsAI;
        this.ai = ai;
        this.aiAs = aiPlaysAs;

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Creates and displays the interface.
             */
            @Override public void run() {
                initUi();
                game.addListener(SwingUI.this);
                updateFromBoard();
                frame.setVisible(true);
                maybeAIMove(); // in case AI starts
            }
        });
    }

    /**
     * Builds the window, board controls, and reset button.
     */
    private void initUi() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8,8));

        JPanel grid = new JPanel(new GridLayout(3,3,6,6));
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 42);

        for (int i = 0; i < 9; i++) {
            final int idx = i;
            JButton btn = new JButton(" ");
            btn.setFont(f);
            btn.setFocusPainted(false);
            btn.addActionListener(new ActionListener() {
                /**
                 * Handles an interface button or timer event.
                 *
                 * @param e Swing action event
                 */
                @Override public void actionPerformed(ActionEvent e) { onCell(idx); }
            });
            cells[i] = btn;
            grid.add(btn);
        }

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        status.setFont(status.getFont().deriveFont(Font.BOLD));
        top.add(status);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton reset = new JButton(new AbstractAction("Reset") {
            /**
             * Handles an interface button or timer event.
             *
             * @param e Swing action event
             */
            @Override public void actionPerformed(ActionEvent e) { game.reset(); maybeAIMove(); }
        });
        bottom.add(reset);

        frame.add(top, BorderLayout.NORTH);
        frame.add(grid, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setSize(360, 420);
        frame.setLocationByPlatform(true);
    }

    /**
     * Handles a human click on a board square.
     *
     * @param idx zero-based board cell index
     */
    private void onCell(int idx) {
        if (game.isGameOver()) return;
        if (vsAI && game.getCurrent() == aiAs) return; // wait for AI
        if (game.play(idx)) { maybeAIMove(); }
    }

    /**
     * Schedules the AI move when it is the AI player's turn.
     */
    private void maybeAIMove() {
        if (!vsAI || game.isGameOver()) return;
        if (game.getCurrent() != aiAs) return;
        // small delay for UX
        Timer t = new Timer(200, new ActionListener() {
            /**
             * Handles an interface button or timer event.
             *
             * @param e Swing action event
             */
            @Override public void actionPerformed(ActionEvent e) {
                int move = ai.chooseMove(game, aiAs);
                game.play(move);
            }
        });
        t.setRepeats(false);
        t.start();
    }

    /**
     * Refreshes the board buttons and status label from the model.
     */
    private void updateFromBoard() {
        TicTacToeGame.Mark[] b = game.getBoard();
        for (int i = 0; i < 9; i++) {
            cells[i].setText(sym(b[i]));
            cells[i].setEnabled(!game.isGameOver() && b[i] == TicTacToeGame.Mark.EMPTY);
        }
        if (game.isGameOver()) {
            if (game.getWinner() == TicTacToeGame.Mark.EMPTY) status.setText("Draw. Click Reset.");
            else status.setText("Winner: " + game.getWinner() + ". Click Reset.");
        } else {
            status.setText("Turn: " + game.getCurrent() + (vsAI ? (", AI as " + aiAs) : ""));
        }
    }

    /**
     * Converts a board mark into its displayed text.
     *
     * @param m board mark to format
     * @return printable symbol for the supplied mark
     */
    private String sym(TicTacToeGame.Mark m) {
        if (m == TicTacToeGame.Mark.X) return "X";
        if (m == TicTacToeGame.Mark.O) return "O";
        return " ";
    }

    // GameListener
    /**
     * Updates the interface after a move.
     *
     * @param index zero-based board cell index
     * @param who mark placed by the player
     */
    @Override public void onMove(int index, TicTacToeGame.Mark who) { updateFromBoard(); }
    /**
     * Updates the interface when the game finishes.
     *
     * @param winner winning mark, or EMPTY for a draw
     */
    @Override public void onGameOver(TicTacToeGame.Mark winner) { updateFromBoard(); }
    /**
     * Updates the interface after a reset.
     *
     * @param starting mark of the player who starts
     */
    @Override public void onReset(TicTacToeGame.Mark starting) { updateFromBoard(); }
}

