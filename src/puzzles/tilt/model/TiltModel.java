package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class TiltModel {
    // Message constants for Views
    /**
     * Message prefix to the message sent when a board has successfully loaded.
     * The name of the file should be appended to it.
     */
    public static String LOADED_PREFIX = "Loaded: ";
    /**
     * Message sent when a board has failed to load.
     */
    public static String LOAD_FAILED = "Load failed.";
    /**
     * The message sent when a hint is given.
     */
    public static String HINT = "Next step!";
    /**
     * The message sent when no solution could be found.
     */
    public static String NO_SOLUTION = "Hint not available."; // a bit evil >:D
    /**
     * The message sent when a hint is selected on a solved board.
     */
    public static String ALREADY_SOLVED = "Already solved!";
    /**
     * The message sent when the board is reset.
     */
    public static String PUZZLE_RESET = "Puzzle reset!";
    /**
     * The message sent when the selected move is illegal.
     */
    public static String ILLEGAL_MOVE = "Illegal move. A blue slider will fall through the hole!";
    /**
     * The message sent when the selected move solves the board.
     */
    public static String CONGRATULATIONS = "Congratulations!";

    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    private TiltConfig initialConfig;

    public TiltModel(String filename) throws FileNotFoundException {
        loadGameFromFile(filename);
    }

    /**
     * Getter for the board.
     */
    public char[][] getBoard() { return currentConfig.getBoard(); }

    /**
     * Load a game board using a filename.
     */
    public boolean loadGameFromFile(String filename) throws FileNotFoundException {
        return loadGameFromFile(new File(filename));
    }

    /**
     * Loads a game board from a file
     */
    public boolean loadGameFromFile(File file) throws FileNotFoundException {
        try {
            currentConfig = new TiltConfig(file);
            initialConfig = currentConfig;
            alertObservers(LOADED_PREFIX+file.getName());
            return true;
        } catch (FileNotFoundException e) {
            alertObservers(LOAD_FAILED);
            return false;
        }
    }

    /**
     * Helper method for the tilt method to handle common operations for each tilt case.
     * @param config The tilted config to check
     * @return true iff the tilt action was successful.
     */
    private boolean handleTiltedConfig(TiltConfig config) throws FileNotFoundException {
        if (!config.isValid()) {
            alertObservers(ILLEGAL_MOVE);
            return false;
        }
        currentConfig = config;
        if (config.isSolution())
            alertObservers(CONGRATULATIONS);
        else alertObservers("");
        return true;
    }

    /**
     * Performs a tilt action administered by the controller.
     *
     * @return true iff the tilt action was successful.
     */
    public boolean tilt(String direction) throws FileNotFoundException {
        TiltConfig newConfig;
        switch (direction) {
            case "N", "n" -> {
                newConfig = (TiltConfig) currentConfig.getTiltUp();
                return handleTiltedConfig(newConfig);
            }
            case "S", "s" -> {
                newConfig = (TiltConfig) currentConfig.getTiltDown();
                return handleTiltedConfig(newConfig);
            }
            case "E", "e" -> {
                newConfig = (TiltConfig) currentConfig.getTiltRight();
                return handleTiltedConfig(newConfig);
            }
            case "W", "w" -> {
                newConfig = (TiltConfig) currentConfig.getTiltLeft();
                return handleTiltedConfig(newConfig);
            }
        }

        return false;
    }

    /**
     * Reset the game to the initial state.
     */
    public void resetBoard() throws FileNotFoundException {
        currentConfig = initialConfig;
        alertObservers(PUZZLE_RESET);
    }

    /**
     * Gives a hint to the user. This requires solving the entire puzzle, it may take a long time or even run out of memory for sufficiently complex puzzles ran on weak baby machines.
     * @return true iff a hint could be generated. false if already in win-state or no solution could be found.
     */
    public boolean getHint() throws FileNotFoundException {
        if (currentConfig.isSolution()) {
            alertObservers(ALREADY_SOLVED);
            return false;
        }
        Solution solution = Solver.solve(currentConfig);
        List<Configuration> path = solution.getPath();

        if (path.size() >= 2) {
            currentConfig = (TiltConfig) path.get(1);
            alertObservers(HINT);
            return true;
        } else {
            alertObservers(NO_SOLUTION);
            return false;
        }

    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) throws FileNotFoundException {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    @Override
    public String toString() {
        return currentConfig.toString();
    }
}
