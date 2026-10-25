# Lab 06 — Java project management
## Core sections

| Section | Folder | Action |
| --- | --- | --- |
| A — Javadoc | `examples/SimpleJavadocDemo`, `examples/Documentation`, `examples/Object-Oriented-Tic-Tac-Toe` | Compile and generate HTML API docs |
| B — JUnit 5 | `examples/Testing` | Compile and run truth-table/unit tests |
| C — Multiple source files | `examples/AquariumSim` | Build 11 separate Java classes/enums |
| D — Packages | `examples/PackagedTicTacToe` | Run packaged CLI/Swing game and regression checks |
| E — Maven | `examples/Maven/simple-maven-project` | Run basic Maven example |

### A — Javadoc (Windows commands)

From `examples/SimpleJavadocDemo`:

```cmd
javac -d out src\com\example\docdemo\*.java
java -cp out com.example.docdemo.Main
javadoc -d docs -sourcepath src com.example.docdemo
```

From `examples/Documentation`:

```cmd
javac -d out src\com\example\app\*.java src\com\example\geometry\*.java src\com\example\util\*.java
java -cp out com.example.app.Main
javadoc -d docs -sourcepath src com.example.app com.example.geometry com.example.util
```

From `examples/Object-Oriented-Tic-Tac-Toe`:

```cmd
javac -d out *.java
java -cp out Main --cli
javadoc -private -d docs *.java
```

The generated API home page is `docs/index.html` in each project folder.

### B — JUnit 5

From `examples/Testing` run `compile.bat`, `run.bat`, and `test.bat` on
Windows; use the corresponding `.sh` files with Bash. Read its README for
JAR download details and full command examples.

### C — AquariumSim

From `examples/AquariumSim`:

```cmd
javac -d out *.java
java -cp out AquariumSim
```

Requires a graphical desktop. Keep the original single-file
`examples/AquariumSim.java` **out of the same compilation command**.

### D — Packaged Tic-Tac-Toe

From `examples/PackagedTicTacToe`:

```cmd
verify.bat
java -cp out main.Main --cli --ai
```

For Bash use `bash verify.sh`. See its README for run modes, GUI support, and
what is checked.

### E — Simple Maven project

The example under `examples/Maven/simple-maven-project` is retained as provided.
From that directory with Maven installed:

```cmd
mvn compile
mvn exec:java -Dexec.mainClass=com.example.App
mvn package
java -cp target\simple-maven-project-1.0-SNAPSHOT.jar com.example.App
```

The other `examples/Maven/` subprojects are retained as optional course
examples; they were not modified for this version.

## GitHub submission

Extract the archive, then copy the `lab-06` folder into your Git repository.
From the **repository root**:

```bash
git add lab-06
git commit -m "Improve Lab 06 tests and packaged game"
git push
```