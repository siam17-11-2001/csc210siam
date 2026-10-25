# JUnit 5 example (Section B)

This folder is based on the course's `Testing` example. Section B adds
`src/main/com/example/math/BooleanLogic.java` and its corresponding
`src/test/com/example/math/BooleanLogicTest.java` tests.

- `BooleanLogic` implements `And`, `Or`, `Not`, and `Xor` with the method names
  required by the assignment.
- The four required JUnit 5 tests verify complete truth tables.
- Two additional tests verify De Morgan's laws and double negation.
- The existing Calculator and Strings examples/tests remain available.

## Windows (Command Prompt or PowerShell)

Open the terminal in this **Testing** folder:

```cmd
.\compile.bat
.\run.bat
.\test.bat
```

`compile.bat` creates `lib/` and downloads the JUnit Console Standalone 1.11.3
JAR once using PowerShell. If your connection blocks the download, place
`junit-platform-console-standalone-1.11.3.jar` in `lib/` manually and retry.
The dependency JAR and generated `.class` files should not be committed to Git.

## Git Bash / macOS / Linux

```bash
bash compile.sh
bash run.sh
bash test.sh
```

`test.bat` uses the Windows classpath separator `;`; `test.sh` uses `:`.
For either operating system, tests run with `--fail-if-no-tests` so an empty
test discovery does not accidentally appear to pass.
