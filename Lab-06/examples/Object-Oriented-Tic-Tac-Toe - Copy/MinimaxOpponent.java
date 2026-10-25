import java.util.List;

/**
 * Computer opponent that searches available moves using minimax with alpha-beta pruning.
 */
public class MinimaxOpponent implements Opponent {
    /** Creates a MinimaxOpponent instance. */
    public MinimaxOpponent() { }
    /**
     * Selects a legal computer move, using the center-opening shortcut and minimax.
     *
     * @param game game model being played or inspected
     * @param me mark controlled by this opponent
     * @return selected zero-based board index
     */
    @Override
    public int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me) {
        TicTacToeGame.Mark opp = (me == TicTacToeGame.Mark.X ? TicTacToeGame.Mark.O : TicTacToeGame.Mark.X);

        // Opening shortcut: on our first move, taking the center is always sound
        // and skips the most expensive search. After that, run full minimax.
        List<Integer> moves = game.legalMoves();
        if (moves.size() >= 8 && moves.contains(4)) return 4;
        return bestByMinimax(game, me, opp);
    }

    /**
     * Evaluates candidate moves and returns the best one.
     *
     * @param game game model being played or inspected
     * @param me mark controlled by this opponent
     * @param opp mark controlled by the opposing player
     * @return highest-scoring legal move index
     */
    private int bestByMinimax(TicTacToeGame game, TicTacToeGame.Mark me, TicTacToeGame.Mark opp) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int move : game.legalMoves()) {
            // simulate
            Sim s = new Sim(game);
            s.play(move);
            int score = minimax(s, false, me, opp, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) { bestScore = score; bestMove = move; }
        }
        return bestMove;
    }

    /**
     * Scores a simulated position with alpha-beta minimax.
     *
     * @param s simulated board state
     * @param maximizing whether the current recursion maximizes the score
     * @param me mark controlled by this opponent
     * @param opp mark controlled by the opposing player
     * @param alpha lower alpha-beta search bound
     * @param beta upper alpha-beta search bound
     * @return minimax evaluation score for the simulated state
     */
    private int minimax(Sim s, boolean maximizing, TicTacToeGame.Mark me, TicTacToeGame.Mark opp, int alpha, int beta) {
        if (s.isOver()) {
            TicTacToeGame.Mark w = s.winner();
            if (w == me) return +10;
            if (w == opp) return -10;
            return 0;
        }

        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (int m : s.legalMoves()) {
                Sim nxt = s.copy(); nxt.play(m);
                int val = minimax(nxt, false, me, opp, alpha, beta);
                best = Math.max(best, val);
                alpha = Math.max(alpha, val);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int m : s.legalMoves()) {
                Sim nxt = s.copy(); nxt.play(m);
                int val = minimax(nxt, true, me, opp, alpha, beta);
                best = Math.min(best, val);
                beta = Math.min(beta, val);
                if (beta <= alpha) break;
            }
            return best;
        }
    }

    /**
     * Internal board state used to search hypothetical Tic-Tac-Toe moves.
     */
    private static class Sim {
        private final TicTacToeGame.Mark[] b = new TicTacToeGame.Mark[9];
        private TicTacToeGame.Mark turn;
        private boolean over = false;
        private TicTacToeGame.Mark win = TicTacToeGame.Mark.EMPTY;

        /**
         * Creates an isolated board representation for search.
         *
         * @param g game model used to initialize a simulation
         */
        Sim(TicTacToeGame g) {
            TicTacToeGame.Mark[] src = g.getBoard();
            System.arraycopy(src, 0, b, 0, 9);
            turn = g.getCurrent();
            over = g.isGameOver();
            win  = g.getWinner();
        }

        /**
         * Creates an isolated board representation for search.
         *
         * @param b board or array of board marks
         * @param turn mark whose move comes next
         * @param over whether the simulated game has ended
         * @param win simulated winning mark
         */
        Sim(TicTacToeGame.Mark[] b, TicTacToeGame.Mark turn, boolean over, TicTacToeGame.Mark win) {
            System.arraycopy(b, 0, this.b, 0, 9);
            this.turn = turn; this.over = over; this.win = win;
        }

        /**
         * Copies this search position without sharing mutable board storage.
         * @return independent copy of the simulated position
         */
        Sim copy() { return new Sim(b, turn, over, win); }

        /**
         * Reports whether the simulated game has ended.
         * @return true if play has finished
         */
        boolean isOver() { return over; }
        /**
         * Reports the winner in the simulated position.
         * @return winning mark, or EMPTY if there is no winner
         */
        TicTacToeGame.Mark winner() { return win; }

        /**
         * Lists unoccupied squares in the simulated game.
         * @return list of legal square indexes
         */
        List<Integer> legalMoves() {
            java.util.ArrayList<Integer> ms = new java.util.ArrayList<>();
            if (over) return ms;
            for (int i = 0; i < 9; i++) if (b[i] == TicTacToeGame.Mark.EMPTY) ms.add(i);
            return ms;
        }

        /**
         * Applies a simulated move if the square is available.
         *
         * @param idx zero-based board cell index
         * @return true if the move was accepted
         */
        boolean play(int idx) {
            if (over || idx < 0 || idx > 8 || b[idx] != TicTacToeGame.Mark.EMPTY) return false;
            b[idx] = turn;
            // check winner
            int[][] L = {
                    {0,1,2},{3,4,5},{6,7,8},
                    {0,3,6},{1,4,7},{2,5,8},
                    {0,4,8},{2,4,6}
            };
            for (int[] line : L) {
                TicTacToeGame.Mark a = b[line[0]];
                TicTacToeGame.Mark c = b[line[1]];
                TicTacToeGame.Mark d = b[line[2]];

                if (a != TicTacToeGame.Mark.EMPTY && a == c && c == d) {
                    win = a; over = true; return true;
                }
            }
            boolean full = true;
            for (TicTacToeGame.Mark m : b) if (m == TicTacToeGame.Mark.EMPTY) { full = false; break; }
            if (full) { over = true; win = TicTacToeGame.Mark.EMPTY; return true; }
            turn = (turn == TicTacToeGame.Mark.X ? TicTacToeGame.Mark.O : TicTacToeGame.Mark.X);
            return true;
        }
    }
}

