# EcosystemGUI — Lab 07

This project adapts the Lab 05 ecosystem simulation into a Java Swing graphical user interface.

## Project structure

```text
EcosystemGUI/
├── data/
│   └── initial-ecosystem.txt
├── src/
│   ├── ecosystem/       # Lab 05 simulation classes and WorldLoader
│   └── ecosystemgui/    # Lab 07 Swing interface
└── README.md
```

## Requirements

- JDK 8 or later
- IntelliJ IDEA, Eclipse, or Visual Studio Code with Java support

No external libraries are required.

## Run in an IDE

1. Open the `EcosystemGUI` folder as a Java project.
2. Make sure `src` is configured as the source folder.
3. Open `src/ecosystemgui/EcosystemGUI.java`.
4. Run its `main` method.
5. The default configuration is `data/initial-ecosystem.txt`.

If you use a different configuration file, pass its path as a program argument.

## Compile and run from a terminal

Run these commands from the `EcosystemGUI` project folder.

### Windows PowerShell / Command Prompt

```powershell
New-Item -ItemType Directory -Force out
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
java -cp out ecosystemgui.EcosystemGUI
```

### macOS / Linux

```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out ecosystemgui.EcosystemGUI
```

## Controls

- **Step**: advances the existing `World` by one turn.
- **Play**: repeatedly advances the world using a Swing Timer. The button changes to **Stop** while running.
- **Speed (ms)**: sets the delay between turns (50–10000 milliseconds).
- **Reset**: reloads the currently selected configuration file, restoring the initial state.
- **Load**: chooses another `.config` or `.txt` ecosystem configuration file.
- **Grid cell click**: displays the selected tile's environmental attributes and all creatures on it in the sidebar.
- **Simulation Output**: displays text written to `System.out` and also keeps it in the terminal.

## Design notes

- The existing Lab 05 ecosystem rules remain in the `ecosystem` package.
- The GUI is in the separate `ecosystemgui` package.
- The GUI uses `World.takeTurn()` for both Step and Play. It does not duplicate the simulation rules.
- `Tile` is retained as the existing Lab 05 grid-cell class.
- The initial configuration format remains compatible with the original `world.config`.
- The optional save-state stretch feature is not included in this core implementation.
