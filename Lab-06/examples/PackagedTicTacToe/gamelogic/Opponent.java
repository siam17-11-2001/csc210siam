package gamelogic;

/**
 * Strategy interface for choosing a computer-controlled move.
 */
public interface Opponent {
    /**
     * Chooses the next legal board index for the given player.
     *
     * @param game game model being played or inspected
     * @param me mark controlled by this opponent
     * @return selected zero-based board index
     */
    int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me);
}

