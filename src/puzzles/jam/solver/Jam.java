package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.FileNotFoundException;
import java.util.List;
/**
 * Done by Jishnuraj Prakasan (jp4154)
 */
/**
 * This class helps to connect to the common solver.
 */
public class Jam {
    static JamModel k= new JamModel();

    /**
     * Helps to make solver object and pass the current configuration to solve it.
     * It calls the function to display the solution to the current configuration.
     * It also prints the number of total and unique configurations derived from the current configuration.
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else{
            Solver solve1= new Solver();
            JamModel model1= new JamModel();
            model1.loadfile(args[0],1);
            JamConfig jconfig= new JamConfig(model1.rows,model1.cols,model1.space);
            Solution solu1= solve1.solve(jconfig);
            System.out.println("Total configs: "+solu1.getNumConfigs());
            System.out.println("Unique configs: "+solu1.getNumUniqueConfigs());
            if(solu1.getPath().size()==0)
                System.out.println("No solution");
            else{
                List<Configuration> path1 = solu1.getPath();
                for (int i=0; i<path1.size(); i++) {
                    System.out.println("Step "+i+": \n"+path1.get(i));

                }
            }
        }
    }
}