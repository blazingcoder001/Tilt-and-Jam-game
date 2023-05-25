package puzzles.common.solver;

import java.util.List;

/**
 * Encapsulates solution statistics gathered by the solver during the breadth-first search.
 *
 * @author Ryan Webb
 */
public class Solution {
    private final int numConfigs;
    private final int numUniqueConfigs;
    List<Configuration> path;

    public int getNumConfigs() { return numConfigs; }
    public int getNumUniqueConfigs() { return numUniqueConfigs; }
    public List<Configuration> getPath() { return path; }

    public Solution(int numConfigs, int numUniqueConfigs, List<Configuration> path) {
        this.numConfigs = numConfigs;
        this.numUniqueConfigs = numUniqueConfigs;
        this.path = path;
    }
}
