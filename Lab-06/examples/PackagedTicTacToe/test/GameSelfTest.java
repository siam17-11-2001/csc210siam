package test;

import gamelogic.GameListener;
import gamelogic.MinimaxOpponent;
import gamelogic.TicTacToeGame;
import gamelogic.TicTacToeGame.Mark;

import java.util.ArrayList;
import java.util.List;

/** Dependency-free regression tests for the packaged game and its AI. */
public final class GameSelfTest {
    private static final MinimaxOpponent OPPONENT = new MinimaxOpponent();
    private static int checks;
    private static int finishedAiGames;

    private GameSelfTest() { }

    public static void main(String[] args) {
        testInvalidMoves();
        testWinAndDraw();
        testListenerState();
        testBoardEncapsulation();
        testAiInputValidation();
        verifyNeverLoses(Mark.X, new ArrayList<>());
        verifyNeverLoses(Mark.O, new ArrayList<>());
        System.out.printf("PASS: %d checks; %d completed adversarial AI games.%n",
                checks, finishedAiGames);
    }

    private static void testInvalidMoves() {
        TicTacToeGame game = new TicTacToeGame();
        check(!game.play(-1), "negative cell must be rejected");
        check(!game.play(9), "cell after board must be rejected");
        check(game.play(0), "first move should be accepted");
        check(!game.play(0), "occupied cell must be rejected");
        check(game.getCurrent() == Mark.O, "failed moves must not change turns");
        game.reset();
        check(game.getCurrent() == Mark.X && game.legalMoves().size() == 9,
                "reset should restore the opening position");
    }

    private static void testWinAndDraw() {
        TicTacToeGame xWins = playSequence(0, 3, 1, 4, 2);
        check(xWins.isGameOver() && xWins.getWinner() == Mark.X, "top row is an X win");
        check(!xWins.play(8) && xWins.legalMoves().isEmpty(), "finished game is closed");

        TicTacToeGame oWins = playSequence(0, 3, 1, 4, 8, 5);
        check(oWins.isGameOver() && oWins.getWinner() == Mark.O, "middle row is an O win");

        TicTacToeGame drawn = playSequence(0, 1, 2, 4, 3, 5, 7, 6, 8);
        check(drawn.isGameOver() && drawn.getWinner() == Mark.EMPTY, "full board is a draw");
    }

    private static void testListenerState() {
        TicTacToeGame game = new TicTacToeGame();
        final int[] resetCount = {0};
        final int[] moveCount = {0};
        final int[] gameOverCount = {0};

        game.addListener(new GameListener() {
            @Override public void onReset(Mark starting) {
                resetCount[0]++;
                check(starting == Mark.X, "reset event starts with X");
            }
            @Override public void onMove(int index, Mark who) {
                moveCount[0]++;
                check(game.getBoard()[index] == who, "listener sees committed board");
                if (moveCount[0] == 1) {
                    check(game.getCurrent() == Mark.O, "listener sees next turn, not old turn");
                }
            }
            @Override public void onGameOver(Mark winner) {
                gameOverCount[0]++;
                check(game.isGameOver(), "listener sees finished state");
            }
        });
        for (int move : new int[] {0, 3, 1, 4, 2}) {
            check(game.play(move), "expected valid sequence move");
        }
        check(moveCount[0] == 5 && gameOverCount[0] == 1, "event counts match win");
        game.reset();
        check(resetCount[0] == 1, "registered listener receives reset event");
    }

    private static void testBoardEncapsulation() {
        TicTacToeGame game = new TicTacToeGame();
        Mark[] board = game.getBoard();
        board[4] = Mark.X;
        check(game.getBoard()[4] == Mark.EMPTY, "board getter must return copy");
    }

    private static void testAiInputValidation() {
        TicTacToeGame game = new TicTacToeGame();
        check(throwsException(() -> OPPONENT.chooseMove(game, Mark.EMPTY)),
                "empty mark is invalid");
        check(throwsException(() -> OPPONENT.chooseMove(game, Mark.O)),
                "cannot move for the wrong player");
        TicTacToeGame completed = playSequence(0, 3, 1, 4, 2);
        check(throwsException(() -> OPPONENT.chooseMove(completed, Mark.X)),
                "cannot choose move in completed game");
    }

    /** Enumerate all possible human replies while AI selects every own move. */
    private static void verifyNeverLoses(Mark aiAs, List<Integer> history) {
        TicTacToeGame game = new TicTacToeGame();
        for (int index : history) {
            check(game.play(index), "history should always be legal");
        }
        if (game.isGameOver()) {
            check(game.getWinner() == aiAs || game.getWinner() == Mark.EMPTY,
                    "AI must never lose under any human response");
            finishedAiGames++;
            return;
        }
        if (game.getCurrent() == aiAs) {
            int selected = OPPONENT.chooseMove(game, aiAs);
            check(game.legalMoves().contains(selected), "AI move must be legal");
            List<Integer> next = new ArrayList<>(history);
            next.add(selected);
            verifyNeverLoses(aiAs, next);
        } else {
            for (int humanMove : game.legalMoves()) {
                List<Integer> next = new ArrayList<>(history);
                next.add(humanMove);
                verifyNeverLoses(aiAs, next);
            }
        }
    }

    private static TicTacToeGame playSequence(int... moves) {
        TicTacToeGame game = new TicTacToeGame();
        for (int move : moves) {
            check(game.play(move), "fixture move must be legal");
        }
        return game;
    }

    private static boolean throwsException(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException | IllegalStateException expected) {
            return true;
        }
    }

    private static void check(boolean condition, String description) {
        checks++;
        if (!condition) {
            throw new AssertionError(description);
        }
    }
}
