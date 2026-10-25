/**
 * Listener contract for updates to the game board and game lifecycle.
 */
public interface GameListener {
    /**
     * Receives a notification when a move is accepted.
     *
     * @param index zero-based board cell index
     * @param who mark placed by the player
     */
    void onMove(int index, TicTacToeGame.Mark who);

    /**
     * Receives a game completion notification.
     *
     * @param winner winning mark, or EMPTY for a draw
     */
    void onGameOver(TicTacToeGame.Mark winner);

    /**
     * Receives a notification when a game is reset.
     *
     * @param starting mark of the player who starts
     */
    void onReset(TicTacToeGame.Mark starting);
}

