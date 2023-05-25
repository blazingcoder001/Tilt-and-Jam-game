package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TiltConfig implements Configuration {
    public static final char BLOCKER = '*';
    public static final char GOAL = 'O';
    public static final char GREEN = 'G';
    public static final char BLUE = 'B';
    public static final char EMPTY = '.';

    private static final String BLOCKER_STR = "⬛";
    private static final String GOAL_STR = "🟡";
    private static final String GREEN_STR = "🟩";
    private static final String BLUE_STR = "🟦";
    private static final String EMPTY_STR = "⬜";

    // Declare the static character to emoji map
    private static final Map<Character, String> charMap;
    // Instantiate the static map
     static
    {
        charMap = new HashMap<>();
        charMap.put(BLOCKER, BLOCKER_STR);
        charMap.put(GOAL, GOAL_STR);
        charMap.put(GREEN, GREEN_STR);
        charMap.put(BLUE, BLUE_STR);
        charMap.put(EMPTY, EMPTY_STR);
    }

    private int BOARD_SIZE;
    private int NUM_STARTING_BLUES;
    private char[][] board;

    /**
     * Constructor for initial TiltConfig. Takes a board of characters, and sets all private state from the board.
     */
    public TiltConfig(File file) throws FileNotFoundException{
        try (Scanner in = new Scanner(file)) {
            int bluesCount = 0;
            this.BOARD_SIZE = Integer.parseInt(in.nextLine());
            this.board = new char[BOARD_SIZE][BOARD_SIZE];

            while (in.hasNextLine()) {
                int i = 0;
                while (in.hasNextLine()) {
                    int j = 0;
                    String line = in.nextLine();
                    for (String c : line.stripTrailing().split(" ")) {
                        this.board[i][j] = c.charAt(0);
                        if (c.charAt(0) == BLUE) bluesCount++;
                        j++;
                    }
                    i++;
                }
            }
            this.NUM_STARTING_BLUES = bluesCount;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File could not be loaded.");
        }
    }

    /**
     * Constructor for neighbor TiltConfig.
     */
    public TiltConfig(TiltConfig oldConfig, char[][] newBoard) {
        this.BOARD_SIZE = oldConfig.BOARD_SIZE;
        this.NUM_STARTING_BLUES = oldConfig.NUM_STARTING_BLUES;
        this.board = newBoard;
    }

    /**
     * Getter for the board.
     */
    public char[][] getBoard() { return board; }

    /**
     * A configuration is considered a solution if all the green sliders have fallen into the goal
     * and none of the blue sliders have shared the same fate.
     * @return
     */

    @Override
    public boolean isSolution() {
        int greenCount = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == GREEN) greenCount++;
            }
        }

        return greenCount == 0 && isValid();
    }

    /**
     * Whether this configuration is valid. A configuration is valid if
     * no blue sliders have fallen into the goal.
     */
    public boolean isValid() {
        int blueCount = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == BLUE) blueCount++;
            }
        }

        return blueCount == NUM_STARTING_BLUES;
    }

    /**
     * Returns true if the contents of the specified cell is a slider.
     * @param board The board to consider
     * @param row The row of the cell in question
     * @param col The column of the cell in question
     * @return whether the contents of the given cell on the given board is a slider.
     */
    private boolean isSlider(char[][] board, int row, int col) {
        return board[row][col] == BLUE || board[row][col] == GREEN;
    }

    /**
     * Returns true if the contents of the specified cell is empty and can be slid into.
     * @param board The board to consider
     * @param row The row of the cell in question
     * @param col The column of the cell in question
     * @return
     */
    private boolean isEmptyCell(char[][] board, int row, int col) {
        return board[row][col] == EMPTY || board[row][col] == GOAL;
    }

    /**
     * Returns a clone of the current board.
     * @return A clone of the current board.
     */
    private char[][] cloneBoard() {
        char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            newBoard[i] = this.board[i].clone();
        }
        return newBoard;
    }

    /**
     * Slides the slider at the specified cell upwards as far as it can go.
     * This method is used by getTiltUp() to generate the tilt up neighbor config.
     * @param newBoard The board clone to slide on
     * @param row The row of the slider to slide
     * @param col The column of the slider to slide
     */
    private void slideSliderUp(char[][] newBoard, int row, int col) {
        while (row > 0 && isEmptyCell(newBoard, row - 1, col)) {
            // if the next cell is the goal, the slider falls in and is removed
            if (newBoard[row-1][col] == GOAL) {
                newBoard[row][col] = EMPTY;
                break;
            }
            // move the slider one step upwards
            char slider = newBoard[row][col];
            newBoard[row][col] = EMPTY;
            newBoard[--row][col] = slider; // decrement row first
        }
    }

    /**
     * Generates a new neighbor config where the board has been tilted up,
     * sliding all sliders which are free to slide in that direction.
     * @return The neighbor state where the board has been tilted upwards.
     */
    public Configuration getTiltUp() {
        // copy the board
        char[][] newBoard = cloneBoard();

        // iterate through rows starting with the second from the top
        for (int row = 1; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // if a cell is a slider, slide it upwards
                if (isSlider(newBoard, row, col)) {
                    slideSliderUp(newBoard, row, col);
                }
            }
        }

        return new TiltConfig(this, newBoard);
    }

    /**
     * Slides the slider at the specified cell downwards as far as it can go.
     * This method is used by getTiltDown() to generate the tilt down neighbor config.
     * @param newBoard The board clone to slide on
     * @param row The row of the slider to slide
     * @param col The column of the slider to slide
     */
    private void slideSliderDown(char[][] newBoard, int row, int col) {
        while (row < BOARD_SIZE-1 && isEmptyCell(newBoard, row + 1, col)) {
            // if the next cell is the goal, the slider falls in and is removed
            if (newBoard[row+1][col] == GOAL) {
                newBoard[row][col] = EMPTY;
                break;
            }
            // move the slider one step down
            char slider = newBoard[row][col];
            newBoard[row][col] = EMPTY;
            newBoard[++row][col] = slider; // increment row first
        }
    }

    /**
     * Generates a new neighbor config where the board has been tilted down,
     * sliding all sliders which are free to slide in that direction.
     * @return The neighbor state where the board has been tilted downwards.
     */
    public Configuration getTiltDown() {
        // copy the board
        char[][] newBoard = cloneBoard();

        Configuration newConfig;
        // iterate through rows starting with the second from the bottom
        for (int row = BOARD_SIZE-2; row >= 0; row--) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // if a cell is a slider, slide it downwards
                if (isSlider(newBoard, row, col)) {
                    slideSliderDown(newBoard, row, col);
                }
            }
        }

        return new TiltConfig(this, newBoard);
    }

    /**
     * Slides the slider at the specified cell left as far as it can go.
     * This method is used by getTiltLeft() to generate the tilt left neighbor config.
     * @param newBoard The board clone to slide on
     * @param row The row of the slider to slide
     * @param col The column of the slider to slide
     */
    private void slideSliderLeft(char[][] newBoard, int row, int col) {
        while (col > 0 && isEmptyCell(newBoard, row, col - 1)) {
            // if the next cell is the goal, the slider falls in and is removed
            if (newBoard[row][col-1] == GOAL) {
                newBoard[row][col] = EMPTY;
                break;
            }
            char slider = newBoard[row][col];
            newBoard[row][col] = EMPTY;
            newBoard[row][--col] = slider;
        }
    }

    /**
     * Generates a new neighbor config where the board has been tilted left,
     * sliding all sliders which are free to slide in that direction.
     * @return The neighbor state where the board has been tilted left.
     */
    public Configuration getTiltLeft() {
        // copy the board
        char[][] newBoard = cloneBoard();

        Configuration newConfig;
        // iterate through columns starting with the second from the left
        for (int col = 1; col < BOARD_SIZE; col++) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                // if a cell is a slider, slide it left
                if (isSlider(newBoard, row, col)) {
                    slideSliderLeft(newBoard, row, col);
                }
            }
        }

        return new TiltConfig(this, newBoard);
    }

    /**
     * Slides the slider at the specified cell right as far as it can go.
     * This method is used by getTiltRight() to generate the tilt right neighbor config.
     * @param newBoard The board clone to slide on
     * @param row The row of the slider to slide
     * @param col The column of the slider to slide
     */
    private void slideSliderRight(char[][] newBoard, int row, int col) {
        while (col < BOARD_SIZE-1 && isEmptyCell(newBoard, row, col + 1)) {
            // if the next cell is the goal, the slider falls in and is removed
            if (newBoard[row][col+1] == GOAL) {
                newBoard[row][col] = EMPTY;
                break;
            }
            // slide one cell right
            char slider = newBoard[row][col];
            newBoard[row][col] = EMPTY;
            newBoard[row][++col] = slider; // increment column before use
        }
    }

    /**
     * Generates a new neighbor config where the board has been tilted right,
     * sliding all sliders which are free to slide in that direction.
     * @return The neighbor state where the board has been tilted right.
     */
    public Configuration getTiltRight() {
        // copy the board
        char[][] newBoard = cloneBoard();

        Configuration newConfig;
        // iterate through columns starting with the second from the right
        for (int col = BOARD_SIZE-2; col >= 0; col--) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                // if a cell is a slider, slide it right
                if (isSlider(newBoard, row, col)) {
                    slideSliderRight(newBoard, row, col);
                }
            }
        }

        return new TiltConfig(this, newBoard);
    }

    /**
     * Returns a list of neighbors where the board has been slid up, down, left, and right.
     * @return The list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();
        neighbors.add(getTiltUp());
        neighbors.add(getTiltDown());
        neighbors.add(getTiltLeft());
        neighbors.add(getTiltRight());

        return neighbors;
    }

    /**
     * Two boards are equal if they're boards are the same, and if they both have the same
     * number of starting blue sliders.
     */
    @Override
    public boolean equals(Object other) {
        // return false early for special cases
        if (!(other instanceof TiltConfig otherConfig)) return false;
        if (this.BOARD_SIZE != otherConfig.BOARD_SIZE) return false;
        if (this.NUM_STARTING_BLUES != otherConfig.NUM_STARTING_BLUES) return false;

        boolean result = true;
        for (int i = 0; i < board.length; i++) {
            if (!Arrays.equals(board[i], otherConfig.board[i])) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Hash code is generated from a string representation of the board and the number of
     * starting blue sliders.
     */
    @Override
    public int hashCode() {
        return (toString()+NUM_STARTING_BLUES).hashCode();
    }

    /**
     * The string representation uses color emojis instead of ASCII characters.
     */
    @Override
    public String toString() {
        String result = "";
        for (var row : board) {
            for (char c : row)
                result += charMap.get(c);
            result += System.lineSeparator();
        }
        return result;
    }
}
