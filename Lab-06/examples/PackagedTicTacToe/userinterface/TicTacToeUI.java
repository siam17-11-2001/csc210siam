package userinterface;

import gamelogic.*;

/**
 * Common contract for graphical and command-line game interfaces.
 */
public interface TicTacToeUI extends GameListener {
    /**
     * Begins a user-interface session for the supplied game.
     *
     * @param game game model being played or inspected
     * @param vsAI whether a computer opponent is enabled
     * @param ai computer opponent strategy
     * @param aiPlaysAs mark assigned to the computer opponent
     */
    void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs);
}

