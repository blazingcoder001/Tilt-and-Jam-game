package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;
import puzzles.water.WaterConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Tilt {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
            return;
        }
        String filename = args[0];
        File file = new File(filename);
        TiltConfig startingConfig = new TiltConfig(file);

        Solution solution = Solver.solve(startingConfig);
        System.out.println("Start:\n"+startingConfig);
        System.out.println("Total configs: "+solution.getNumConfigs());
        System.out.println("Unique configs: "+solution.getNumUniqueConfigs());

        if (solution.getPath().size() > 0) {
            List<Configuration> path = solution.getPath();
            for (int i=0; i<path.size(); i++) {
                System.out.println("Step "+i+": \n"+path.get(i));
            }
            return;
        }

        System.out.println("No solution");
    }
}
