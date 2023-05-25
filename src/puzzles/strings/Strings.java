package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Main class for the strings puzzle.
 *
 * @author Ryan Webb
 */
public class Strings {
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            StringsConfig startingConfig = new StringsConfig(args[0], args[1]);
            Solution solution = Solver.solve(startingConfig);
            System.out.println("Start: "+args[0]+", End: "+args[1]);
            System.out.println("Total configs: "+solution.getNumConfigs());
            System.out.println("Unique configs: "+solution.getNumUniqueConfigs());

            if (solution.getPath().size() > 0) {
                List<Configuration> path = solution.getPath();
                for (int i=0; i<path.size(); i++) {
                    System.out.println("Step "+i+": "+path.get(i));
                }
                return;
            }
            System.out.println("No solution");
        }
    }
}
