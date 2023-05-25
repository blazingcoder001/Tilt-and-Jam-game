package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Ryan Webb
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            int amount = Integer.parseInt(args[0]);
            Integer[] buckets = new Integer[args.length-1];
            for (int i = 1; i < args.length; i++) {
                buckets[i-1] = Integer.parseInt(args[i]);
            }

            WaterConfig startingConfig = new WaterConfig(amount, buckets);
            Solution solution = Solver.solve(startingConfig);
            System.out.println("Amount: "+amount+", Buckets: "+Arrays.toString(buckets));
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
