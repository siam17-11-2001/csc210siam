package main;

import gamelogic.*;
import userinterface.*;

/**
 * Command-line entry point that configures and starts the Tic-Tac-Toe application.
 */
public class Main {
    /** Creates a Main instance. */
    public Main() { }
    /**
     * Starts Tic-Tac-Toe using the selected interface and opponent flags.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Config (you can change these or make them flags)
        boolean useSwing = argsContain(args, "--swing");
        boolean useCLI   = argsContain(args, "--cli") || !useSwing;
        boolean vsAI     = argsContain(args, "--ai");          // human vs AI if present
        boolean aiFirst  = argsContain(args, "--ai-first");     // AI plays X if present

        TicTacToeGame game = new TicTacToeGame();
        Opponent ai = new MinimaxOpponent();
        TicTacToeGame.Mark aiAs = aiFirst ? TicTacToeGame.Mark.X : TicTacToeGame.Mark.O;

        if (useSwing) {
            TicTacToeUI ui = new SwingUI();
            ui.start(game, vsAI, ai, aiAs);
        } else if (useCLI) {
            TicTacToeUI ui = new CLIUI();
            ui.start(game, vsAI, ai, aiAs);
        }
    }

    /**
     * Checks whether a command-line flag is present, ignoring case.
     *
     * @param args command-line arguments
     * @param flag flag to search for
     * @return true when the given flag is present in the arguments
     */
    private static boolean argsContain(String[] args, String flag) {
        if (args == null) return false;
        for (String a : args) if (flag.equalsIgnoreCase(a)) return true;
        return false;
    }
}

