package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Done by Jishnuraj Prakasan (jp4154)
 */

/**
 * JamPTUI defines the TUI of the project Rush Hour.
 */
public class JamPTUI implements Observer<JamModel, String> {
    static int count=0;

    private static JamModel model=new JamModel();
    static private char[][] space;
    private Scanner inp= new Scanner(System.in);
    private static int rows,cols;
    public static String filename;

    /**
     *
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel jamModel, String message) {
    }

    /**
     *  This function checks whether the user wants to quit the game or not and returns boolean value according to that.
     *  Returns a boolean value.
     * @return
     * @throws FileNotFoundException
     */
    public boolean gameQuit() throws FileNotFoundException {
    int flag=-1;
    boolean finish=false;
    System.out.println("\nh(int)\t\t\t\t-- hint next move\nl(oad) filename\t\t-- load new puzzle file" +
            "\ns(elect) r c\t\t-- select cell at r,c\nq(uit)\t\t\t\t-- quit the game\nr(eset)\t\t\t\t-- reset the current game");
    String choice=inp.next();
    switch (choice){
        case "h":
            case "H": JamConfig j1= new JamConfig(rows,cols,space);
                      model.getHint(0);

                      break;
        case "l":
            case "L":
                      String name= inp.next();
                      String reg=".txt$";
                      Pattern pa= Pattern.compile(reg);
                      Matcher ma= pa.matcher(name);
                      if(ma.find()) {
                          filename=name;
                          model.loadfile(filename,0);

                      }
                      else{
                          System.out.println("The given file cannot be loaded as it is not readable. Going back...");
                          model.printpuzzle(space,0);
                      }
                      break;
        case "s":
            case"S":
                     model.Select(filename,inp);

                     break;
        case "q":
            case"Q": System.out.println("Exiting now...");
                     finish=true;
                     return finish;
        case "r":
            case "R":System.out.println("Resetting the game...");
                     model.loadfile(filename,0);
                     break;
        default:     System.out.println("Enter the right input!!");
                     break;
    }
    return false;
    }
    public void run() throws FileNotFoundException {
       while(true) {
           if (gameQuit() == true) {
               System.exit(0);
           }

       }

    }

    /**
     * Reads the filename and loads the file.
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");

        }
        else{
            filename=args[0];
            model.loadfile(filename,0);
        }
    }
}
