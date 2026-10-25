package gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Game model that enforces board rules, turn changes, and win or draw detection.
 */
public class TicTacToeGame {
    /**
     * Possible Tic-Tac-Toe marks, including an unoccupied square.
     */
    public enum Mark {
        /** First player's mark. */ X,
        /** Second player's mark. */ O,
        /** An unoccupied square or no winner. */ EMPTY
    }

    private final Mark[] board = new Mark[9];
    private Mark current = Mark.X;
    private boolean gameOver = false;
    private Mark winner = Mark.EMPTY;
    private final List<GameListener> listeners = new ArrayList<>();

    /**
     * Constructs a new game with an empty board.
     */
    public TicTacToeGame() { reset(); }

    /**
     * Clears the board and restores X as the starting player.
     */
    public void reset() {
        Arrays.fill(board, Mark.EMPTY);
        current = Mark.X;
        winner = Mark.EMPTY;
        gameOver = false;
        fireReset(current);
    }

    /**
     * Returns a defensive copy of the current board.
     * @return defensive copy of the board
     */
    public Mark[] getBoard() { return board.clone(); }

    /**
     * Returns the player whose turn it is.
     * @return mark of the player whose turn is next
     */
    public Mark getCurrent() { return current; }

    /**
     * Reports whether the game is complete.
     * @return true if play has finished
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Returns the winner, or EMPTY if there is no winner.
     * @return winner, or EMPTY when there is no winner
     */
    public Mark getWinner() { return winner; } // EMPTY means no winner or draw during play; on gameOver & EMPTY = draw

    /**
     * Attempts to apply a move, notifying listeners after success.
     *
     * @param index zero-based board cell index
     * @return true if the move was accepted
     */
    public boolean play(int index) {
        if (gameOver || index < 0 || index >= board.length || board[index] != Mark.EMPTY) {
            return false;
        }

        Mark playedBy = current;
        board[index] = playedBy;
        winner = computeWinner();

        if (winner != Mark.EMPTY || isFull()) {
            gameOver = true;
        } else {
            current = playedBy == Mark.X ? Mark.O : Mark.X;
        }

        // Publish the fully updated game state, not a partially processed turn.
        fireMove(index, playedBy);
        if (gameOver) {
            fireGameOver(winner); // EMPTY indicates a draw.
        }
        return true;
    }

    /**
     * Lists unoccupied square indexes while the game is active.
     * @return list of legal square indexes
     */
    public List<Integer> legalMoves() {
        List<Integer> moves = new ArrayList<>();
        if (gameOver) return moves;
        for (int i = 0; i < 9; i++) if (board[i] == Mark.EMPTY) moves.add(i);
        return moves;
    }

    /**
     * Registers a listener for game-state changes.
     *
     * @param l listener receiving game notifications
     */
    public void addListener(GameListener l) { listeners.add(Objects.requireNonNull(l)); }
    /**
     * Unregisters a previously registered listener.
     *
     * @param l listener receiving game notifications
     */
    public void removeListener(GameListener l) { listeners.remove(l); }

    /**
     * Notifies listeners about an accepted move.
     *
     * @param idx zero-based board cell index
     * @param who mark placed by the player
     */
    private void fireMove(int idx, Mark who) {
        for (GameListener listener : new ArrayList<>(listeners)) {
            listener.onMove(idx, who);
        }
    }
    /**
     * Notifies listeners that the game ended.
     *
     * @param winner winning mark, or EMPTY for a draw
     */
    private void fireGameOver(Mark winner) {
        for (GameListener listener : new ArrayList<>(listeners)) {
            listener.onGameOver(winner);
        }
    }
    /**
     * Notifies listeners that a game was reset.
     *
     * @param starting mark of the player who starts
     */
    private void fireReset(Mark starting) {
        for (GameListener listener : new ArrayList<>(listeners)) {
            listener.onReset(starting);
        }
    }

    /**
     * Checks whether all nine squares have been occupied.
     * @return true when no board squares are empty
     */
    private boolean isFull() {
        for (Mark mark : board) {
            if (mark == Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private static final int[][] LINES = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
    };

    /**
     * Checks all winning lines and returns the resulting mark.
     * @return mark with three in a row or EMPTY
     */
    private Mark computeWinner() {
        for (int[] line : LINES) {
            Mark a = board[line[0]], b = board[line[1]], c = board[line[2]];
            if (a != Mark.EMPTY && a == b && b == c) return a;
        }
        return Mark.EMPTY;
    }
}

