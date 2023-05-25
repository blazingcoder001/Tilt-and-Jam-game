package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;
    private boolean gameInProgress = true;
    private boolean shouldLoop = true;
    private Scanner in;

    private static final String HELP = "h(int)              -- hint next move\n" +
                                       "l(oad) filename     -- load new puzzle file\n" +
                                       "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                                       "q(uit)              -- quit the game\n" +
                                       "r(eset)             -- reset the current game";

    public TiltPTUI(String filename) throws FileNotFoundException {
        model = new TiltModel(filename);
        model.addObserver(this);
        in = new Scanner(System.in);

        System.out.println(TiltModel.LOADED_PREFIX+filename);
        System.out.println(model.toString());
        System.out.println(HELP);
    }

    @Override
    public void update(TiltModel model, String message) {
        if (message.startsWith(TiltModel.LOADED_PREFIX)) {
            System.out.println(message);
            System.out.println(model.toString());
//            gameLoaded = true;
        }
        else if (message.equals(TiltModel.LOAD_FAILED)) {
            System.out.println(message);
        }
        else if (message.equals(TiltModel.PUZZLE_RESET)) {
            System.out.println(message);
            System.out.println(model.toString());
        }
        else if (message.equals(TiltModel.HINT)) {
            System.out.println(message);
            System.out.println(model.toString());
        }
        else if (message.equals(TiltModel.ALREADY_SOLVED)) {
            System.out.println(message);
        }
        else if (message.equals(TiltModel.ILLEGAL_MOVE)) {
            System.out.println(message);
            System.out.println(model.toString());
        }
        else if (message.equals(TiltModel.CONGRATULATIONS)) {
            System.out.println(message);
            System.out.println(model.toString());
        }
        else if (message.equals(TiltModel.NO_SOLUTION)) {
            System.out.println(message);
            System.out.println(model.toString());
        }
        else {
            System.out.println(model.toString());
        }
    }

    /**
     * The main program loop. Keeps getting user input until
     */
    public void run() throws FileNotFoundException {
        while (shouldLoop) {
            gameLoop(); // gameplay
        }

    }

    /**
     * Handles gameplay.
     */
    private void gameLoop() throws FileNotFoundException {
        String command = in.nextLine().strip();
        if (gameInProgress)
        {
            if (command.toLowerCase().startsWith("h")) {
                model.getHint();
                return;
            } else if (command.toLowerCase().startsWith("t")) {
                String dir = command.split(" ")[1];
                model.tilt(dir);
                return;
            } else if (command.toLowerCase().startsWith("r")) {
                model.resetBoard();
                return;
            }
        }

        if (command.toLowerCase().startsWith("l")) {
            String filename = command.split(" ")[1];
            model.loadGameFromFile("data\\tilt\\"+filename);
        }
        else if (command.toLowerCase().startsWith("q")) {
            shouldLoop = false;
        }
        else {
            System.out.println("Invalid command. Available commands:");
            System.out.println(HELP);
            System.out.println(model.toString());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        String filename = args[0];

        TiltPTUI tiltPTUI = new TiltPTUI(filename);
        tiltPTUI.run();
    }
}
