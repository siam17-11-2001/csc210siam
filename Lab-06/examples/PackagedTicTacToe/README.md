# Packaged Tic-Tac-Toe (Section D)

This project is the **course Tic-Tac-Toe example reorganized into packages**:
`gamelogic` (board and opponent), `userinterface` (CLI and Swing), and `main`
(application entry point). The project also includes documented behavior fixes,
revised AI search, and a small standalone regression suite.

## Windows / VS Code terminal

From **this folder** (the folder containing `gamelogic`, `main`, and `userinterface`):

```cmd
javac -d out gamelogic\*.java userinterface\*.java main\*.java
java -cp out main.Main --cli
java -cp out main.Main --cli --ai
java -cp out main.Main --cli --ai --ai-first
```

For Swing use `java -cp out main.Main --swing --ai` on a graphical desktop.
In CLI mode, type a square number `1`–`9`, or `q` to exit. The AI searches a
private board copy and makes a legal optimal move on its own turn.

## Automated checks (no JUnit dependency)

Run `verify.bat` on Windows, or `bash verify.sh` on macOS/Linux/Git Bash.
The tests cover illegal moves, win/draw detection, events, board encapsulation,
AI input validation, and exhaustive human-response paths with AI as X and O.

The tests and build output are local project tools, not additional submission
requirements. `out/` is ignored by Git.
