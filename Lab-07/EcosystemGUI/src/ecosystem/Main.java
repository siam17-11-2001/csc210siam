package ecosystem;

import java.io.File;

/** Console entry point retained for the original Lab 05 simulation. */
public class Main {
    public static void main(String[] args) throws Exception {
        File configFile = args.length > 0
                ? new File(args[0])
                : new File("data/initial-ecosystem.txt");

        if (!configFile.isFile() && args.length == 0) {
            configFile = new File("world.config");
        }

        World world = WorldLoader.load(configFile);

        for (int i = 0; i < 100; i++) {
            world.takeTurn();
            world.print();
            System.out.println();
        }
    }
}
