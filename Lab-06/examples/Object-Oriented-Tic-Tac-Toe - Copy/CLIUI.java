import java.util.Scanner;

/**
 * Command-line interface for playing Tic-Tac-Toe with another player or a minimax opponent.
 */
public class CLIUI implements TicTacToeUI {
    /** Creates a CLIUI instance. */
    public CLIUI() { }
    private TicTacToeGame game;
    private boolean vsAI;
    private Opponent ai;
    private TicTacToeGame.Mark aiAs;
    private final Scanner sc = new Scanner(System.in);

    /**
     * Runs the command-line game, accepting human moves and optional AI turns.
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

        game.addListener(this);
        draw(game.getBoard());

        while (!game.isGameOver()) {
            if (vsAI && game.getCurrent() == aiAs) {
                int move = ai.chooseMove(game, aiAs);
                game.play(move);
            } else {
                int move = promptMove();
                if (!game.play(move)) {
                    System.out.println("Illegal move. Try again.");
                }
            }
        }
        // gameOver message handled by onGameOver
    }

    /**
     * Prompts until the user supplies a board position from 1 through 9.
     * @return selected zero-based board cell index
     */
    private int promptMove() {
        while (true) {
            System.out.print("Player " + game.getCurrent() + " move (1-9): ");
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s) - 1;
                if (v >= 0 && v <= 8) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Enter a number 1..9.");
        }
    }

    /**
     * Prints a three-by-three view of the current board.
     *
     * @param b board or array of board marks
     */
    private void draw(TicTacToeGame.Mark[] b) {
        System.out.println();
        for (int r = 0; r < 3; r++) {
            int i = r * 3;
            System.out.printf(" %s | %s | %s %n", sym(b[i]), sym(b[i+1]), sym(b[i+2]));
            if (r < 2) System.out.println("---+---+---");
        }
        System.out.println();
    }

    /**
     * Converts a board mark to its printable symbol.
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
     * Redraws the board after a valid move.
     *
     * @param index zero-based board cell index
     * @param who mark placed by the player
     */
    @Override public void onMove(int index, TicTacToeGame.Mark who) { draw(game.getBoard()); }
    /**
     * Prints the game outcome.
     *
     * @param winner winning mark, or EMPTY for a draw
     */
    @Override public void onGameOver(TicTacToeGame.Mark winner) {
        if (winner == TicTacToeGame.Mark.EMPTY) System.out.println("Draw!");
        else System.out.println("Winner: " + winner);
    }
    /**
     * Announces the player who begins a reset game.
     *
     * @param starting mark of the player who starts
     */
    @Override public void onReset(TicTacToeGame.Mark starting) {
        System.out.println("=== New Game. " + starting + " starts. ===");
    }
}

