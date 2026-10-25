# AquariumSim — split into separate class files

The behavior from `../AquariumSim.java` is unchanged; its eleven top-level classes/enums now live in eleven files.

From **this** `AquariumSim` folder on Windows:

```cmd
javac -d out *.java
java -cp out AquariumSim
```

A graphical desktop is necessary to display the Swing simulation. The original one-file example remains alongside this folder for comparison. Do **not** compile the original single-file `AquariumSim.java` and these files in the same invocation, as they contain the same class names.
