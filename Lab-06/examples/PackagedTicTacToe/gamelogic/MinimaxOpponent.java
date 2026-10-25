package gamelogic;

import java.util.Objects;

/**
 * A deterministic perfect-play opponent using depth-aware minimax with
 * alpha-beta pruning. Search moves are applied to a private board snapshot,
 * and undone after recursion; the live game is never modified during search.
 */
public final class MinimaxOpponent implements Opponent {
    /** Creates the stateless opponent strategy. */
    public MinimaxOpponent() { }

    // Check the center, corners, then sides for reliable tie-breaking.
    private static final int[] MOVE_ORDER = {4, 0, 2, 6, 8, 1, 3, 5, 7};
    private static final int[][] WINNING_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    /**
     * Selects an optimal move for the player whose turn it is.
     *
     * @param game active game to inspect
     * @param me X or O, matching the current turn
     * @return board index from 0 through 8
     * @throws IllegalArgumentException for an invalid or out-of-turn mark
     * @throws IllegalStateException if the game has already finished
     */
    @Override
    public int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me) {
        Objects.requireNonNull(game, "game");
        Objects.requireNonNull(me, "me");
        if (game.isGameOver()) {
            throw new IllegalStateException("Cannot choose a move after the game ends");
        }
        if (me == TicTacToeGame.Mark.EMPTY || me != game.getCurrent()) {
            throw new IllegalArgumentException("Opponent mark must match the current player");
        }

        TicTacToeGame.Mark[] board = game.getBoard();
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;

        for (int index : MOVE_ORDER) {
            if (board[index] != TicTacToeGame.Mark.EMPTY) {
                continue;
            }
            board[index] = me;
            int score = scorePosition(board, other(me), me, 1, alpha, Integer.MAX_VALUE);
            board[index] = TicTacToeGame.Mark.EMPTY;

            if (score > bestScore) {
                bestScore = score;
                bestMove = index;
            }
            alpha = Math.max(alpha, bestScore);
        }
        return bestMove;
    }

    /**
     * Scores the position from the AI's perspective. Shorter wins are preferred
     * and longer losses are preferred to shorter losses.
     */
    private int scorePosition(TicTacToeGame.Mark[] board, TicTacToeGame.Mark turn,
                              TicTacToeGame.Mark me, int depth, int alpha, int beta) {
        TicTacToeGame.Mark winner = winnerOf(board);
        if (winner != TicTacToeGame.Mark.EMPTY) {
            return winner == me ? 10 - depth : depth - 10;
        }
        if (isFull(board)) {
            return 0;
        }

        boolean maximizing = turn == me;
        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int index : MOVE_ORDER) {
            if (board[index] != TicTacToeGame.Mark.EMPTY) {
                continue;
            }
            board[index] = turn;
            int value = scorePosition(board, other(turn), me, depth + 1, alpha, beta);
            board[index] = TicTacToeGame.Mark.EMPTY;

            if (maximizing) {
                best = Math.max(best, value);
                alpha = Math.max(alpha, best);
            } else {
                best = Math.min(best, value);
                beta = Math.min(beta, best);
            }
            if (alpha >= beta) {
                break;
            }
        }
        return best;
    }

    private static boolean isFull(TicTacToeGame.Mark[] board) {
        for (TicTacToeGame.Mark mark : board) {
            if (mark == TicTacToeGame.Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private static TicTacToeGame.Mark winnerOf(TicTacToeGame.Mark[] board) {
        for (int[] line : WINNING_LINES) {
            TicTacToeGame.Mark candidate = board[line[0]];
            if (candidate != TicTacToeGame.Mark.EMPTY
                    && candidate == board[line[1]] && candidate == board[line[2]]) {
                return candidate;
            }
        }
        return TicTacToeGame.Mark.EMPTY;
    }

    private static TicTacToeGame.Mark other(TicTacToeGame.Mark player) {
        return player == TicTacToeGame.Mark.X ? TicTacToeGame.Mark.O : TicTacToeGame.Mark.X;
    }
}
