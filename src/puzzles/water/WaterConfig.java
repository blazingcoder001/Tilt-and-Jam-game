package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Configuration class for the Water puzzle.
 * Contains state and methods to calculate neighboring configs.
 *
 * @author Ryan Webb
 */
public class WaterConfig implements Configuration {
    private Integer[] current;
    private int goal;
    private Integer[] bucketSizes;

    /**
     * Constructor for initial configuration.
     * @param goal The desired ending configuration.
     * @param bucketSizes The sizes of the buckets in use.
     */
    public WaterConfig(int goal, Integer[] bucketSizes) {
        this.current = new Integer[bucketSizes.length];
        Arrays.fill(current, 0);
        this.goal = goal;
        this.bucketSizes = bucketSizes;
    }

    /**
     * Constructor for neighbor configurations.
     * @param oldConfig The predecessor configuration
     * @param newCurrent The new current configuration of the neighbor
     */
    public WaterConfig(WaterConfig oldConfig, Integer[] newCurrent) {
        this.current = newCurrent;
        this.goal = oldConfig.goal;
        this.bucketSizes = oldConfig.bucketSizes;
    }

    /**
     * Is this configuration a solution to the puzzle?
     * @return Whether this configuration is a solution.
     */
    public boolean isSolution() {
        for (int i : current) {
            if (i == goal) return true;
        }

        return false;
    }

    /**
     * Returns a collection of this configuration's neighbors, including:
     * configurations with a single bucket emptied,
     * configurations with a single bucket filled, and
     * configurations with a single bucket poured into another bucket.
     * Neighbors identical to the current configuration are discarded.
     * @return a collection of this configuration's neighbors.
     */
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();
        for (int i = 0; i< current.length; i++) {
            // add config resulting from emptying bucket i
            Integer[] emptied = current.clone();
            emptied[i] = 0;
            if (!Arrays.equals(emptied, current)) { // ignore if result is identical to current
                neighbors.add(new WaterConfig(this, emptied));
            }

            // add config resulting from filling bucket i
            Integer[] filled = current.clone();
            filled[i] = bucketSizes[i];
            if (!Arrays.equals(filled, current)) { // ignore if result is identical to current
                neighbors.add(new WaterConfig(this, filled));
            }

            // add configs resulting from pouring bucket i into another bucket (j)
            for (int j = 0; j < current.length; j++) {
                if (j == i) continue;
                Integer[] poured = current.clone();
                // subtract water from bucket i
                int amount = poured[i];
                poured[i] = Math.max(0, poured[i] - (bucketSizes[j] - poured[j]));
                poured[j] = Math.min(bucketSizes[j], poured[j] + amount);
                if (!Arrays.equals(poured, current)) {
                    neighbors.add(new WaterConfig(this, poured));
                }
            }
        }
        return neighbors;
    }

    /**
     * Two configurations are equal if they're current and bucketSizes properties are the same.
     * @param other The other object to compare with
     * @return Whether this and other are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WaterConfig otherConfig)) return false;
        return Arrays.equals(otherConfig.current, this.current) &&
                Arrays.equals(otherConfig.bucketSizes, this.bucketSizes);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return Arrays.toString(current);
    }
}
