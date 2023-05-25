package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Configuration class for the Strings puzzle.
 * Contains state and methods to calculate neighboring configs.
 *
 * @author Ryan Webb
 */
public class StringsConfig implements Configuration {
    private String goal;
    private String str;

    /**
     * Constructor to make the first configuration.
     * @param str The starting string
     * @param goal The string goal
     */
    public StringsConfig(String str, String goal) {
        this.str = str;
        this.goal = goal;
    }

    /**
     * Constructor used to construct neighbor objects.
     * @param oldConfig The old config to copy goal from
     * @param newStr The string of the new configuration
     */
    public StringsConfig(StringsConfig oldConfig, String newStr) {
        this.str = newStr;
        this.goal = oldConfig.goal;
    }

    /**
     * Returns whether this config is a solution to the puzzle.
     * @return whether this config is a solution.
     */
    public boolean isSolution() {
        return str.equals(goal);
    }

    /**
     * Returns a collection of neighbors of this config. Neighbors are defined by all strings with a single character
     * shifted either forward one character in the alphabet or backward one character, with edge wrapping.
     * @return
     */
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<Configuration>();
        for (int i = 0; i< str.length(); i++) {
            for (int k = -1; k < 2; k+=2) {
                char[] chars = str.toCharArray();
                int offset = chars[i] - 'A';
                int newOffset = Math.floorMod(offset + k, 26); // shift it forward a letter
                chars[i] = (char) ('A' + newOffset); // convert from offset back to correct alphabet character
                neighbors.add(new StringsConfig(this, new String(chars)));
            }
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StringsConfig otherConfig)) return false;
        return otherConfig.str.equals(this.str);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return str;
    }
}
