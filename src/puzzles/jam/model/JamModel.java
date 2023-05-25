package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solution;
import puzzles.common.solver.Solver;
import puzzles.jam.ptui.JamPTUI;
import puzzles.jam.solver.Jam;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
/**
 * Done by Jishnuraj Prakasan (jp4154)
 */

/**
 * Defines the model of the game and does all the background processes.
 */
public class JamModel {
    public int rows;
    public int cols;
    String filename;
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;
    public  char[][] space;
    public char[] vehicles;
    Scanner inp= new Scanner(System.in);

    /**
     * Helps to select the coordinates to which a vehicle should be moved. Also helps to call the move function to do
     * the movement.
     * filename is the name of the file from which the game is loaded.
     * inp is the scanner object that's passed.
     * returns nothing.
     * @param filename
     * @param inp
     * @throws FileNotFoundException
     */
    public void Select(String filename, Scanner inp) throws FileNotFoundException {
        //Scanner inp= new Scanner(System.in);
        this.filename=filename;
        this.inp=inp;
        int row= Integer.parseInt(inp.next());
        int col= Integer.parseInt(inp.next());
        if(space[row][col]!='.') {
            char c = space[row][col];
            System.out.println("Select cell at r,c to which the vehicle should be moved:");
            int row1 = Integer.parseInt(inp.next());
            int col1 = Integer.parseInt(inp.next());
            Move(row,col,row1,col1,c,0,space,rows,cols);
        }
        else{
            System.out.println("This move cannot be performed, there is no vehicle here. Going back...");
            this.printpuzzle(space,0);
        }

    }

    /**
     * Helps to actually move the vehicle in the puzzle if the given coordinates are valid. Acts as the backbone of
     * movement in this project
     * row stands for the x-coordinate of the current location of the vehicle.
     * col stands for the y-coordinate of the current location of the vehicle.
     * row1 stands for the x-coordinate of the location to which the vehicle should be moved.
     * col1 stands for the y-coordinate of the location to which the vehicle should be moved.
     * c stands for the vehicle in the current location.
     * flag is just a variable passed to know aht to do in this function.
     * space represents the orientation of the vehicles in a matrix.
     * rows is the number of rows of the puzzle.
     * cols is the number of columns in the puzzle.
     * Returns a JamConfig object.
     * @param row
     * @param col
     * @param row1
     * @param col1
     * @param c
     * @param flag
     * @param space
     * @param rows
     * @param cols
     * @return
     * @throws FileNotFoundException
     */
    public JamConfig Move(int row, int col, int row1, int col1, char c, int flag, char[][] space,int rows,int cols) throws FileNotFoundException {
        this.rows=rows;
        this.cols=cols;
        this.space=new char[rows][cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                this.space[i][j]=space[i][j];
            }
        }
        char cpy;int j=0;int k;
        int diff1,diff2;diff1=diff2=0;
        int cour=0;int couc=0;int cf,cl,rf,rl;cf=cl=rf=rl=0;
        if(this.space[row1][col1]=='.') {
            for (int i = 0; i < cols; i++) {
                if (this.space[row][i] == c) {
                    if (!((row - 1 >= 0 && this.space[row - 1][i] == c) || (row + 1 < rows && this.space[row + 1][i] == c))) {
                        cour++;
                        rf = i;
                        break;
                    }
                }
                if (this.space[i][col] == c) {
                    if (!((col - 1 >= 0 && this.space[i][col - 1] == c) || (col + 1 < cols && this.space[i][col + 1] == c))) {
                        couc++;
                        cf = i;
                        break;
                    }
                }
            }
            if (cour != 0) {
                if (row1 == row) {
                    for (int i = cols - 1; i >= 0; i--) {
                        if (this.space[row][i] == c) {
                            rl = i;
                            break;
                        }

                    }
                    if (col1 > rl) {
                        diff1 = col1 - rl;
                    } else {
                        diff2 = rf - col1;
                    }
                    if (diff1 != 0) {
                        for(int i=rl+1;i<=col1;i++){
                            if(this.space[row][i]!='.'){
                                if(flag==0){
                                    System.out.println("Cannot move. Going back...");
                                    this.printpuzzle(this.space, 0);
                                }
                               else if(flag==2){
                                    alertObservers("Cannot move. Going back...");
                                }
                                else{
                                    return null;
                                }
                            }
                        }
                        k = rl - j;
                        for (int i = 0; i < diff1; i++) {

                            while (k >= rf) {
                                cpy = this.space[row][k];
                                this.space[row][k] = '.';
                                this.space[row][k + 1] = cpy;
                                j++;
                                k = rl - j;
                            }
                            rf = rf + 1;
                            rl = rl + 1;
                            j = 0;
                            k = rl - j;

                        }
                    } else if (diff2 != 0) {
                        for(int i=col1;i<rf;i++){
                            if(this.space[row][i]!='.'){
                                if(flag==0){
                                    System.out.println("Cannot move. Going back...");
                                    this.printpuzzle(this.space, 0);
                                }
                                else if(flag==2){
                                    alertObservers("Cannot move. Going back...");
                                }
                                else{
                                    return null;
                                }
                            }
                        }
                        k = rf + j;

                        for (int i = 0; i < diff2; i++) {
                            while (k <= rl) {
                                cpy = this.space[row][k];
                                this.space[row][k] = '.';
                                this.space[row][k - 1] = cpy;
                                j++;
                                k = rf + j;
                            }
                            rf = rf - 1;
                            rl = rl - 1;
                            j = 0;
                            k = rf + j;
                        }
                    }
                    if(flag==0)
                    this.printpuzzle(this.space,0);
                    else if(flag==2){
                        alertObservers("Done");
                    }
                    else{
                        JamConfig j1= new JamConfig(rows,cols,this.space);
                        return j1;
                    }
                } else {
                    if(flag==0) {
                        System.out.println("Cannot move. Going back...");
                        this.printpuzzle(this.space, 0);
                    }
                    else if(flag==2){
                        alertObservers("Cannot move. Going back...");
                    }
                }
            } else if (couc != 0) {
                if (col1 == col) {
                    for (int i = rows - 1; i >= 0; i--) {
                        if (this.space[i][col] == c) {
                            cl = i;
                            break;
                        }
                    }
                    if (row1 > cl) {
                        diff1 = row1 - cl;
                    } else {
                        diff2 = cf - row1;
                    }
                    if (diff1 != 0) {
                        for(int i=cl+1;i<=row1;i++){
                            if(this.space[i][col]!='.'){
                                if(flag==0) {
                                    System.out.println("Cannot move. Going back...");
                                    this.printpuzzle(this.space, 0);
                                }
                                else if(flag==2){
                                    alertObservers("Cannot move. Going back...");
                                }

                                else{
                                    return null;
                                }
                            }
                        }
                        k = cl - j;
                        for (int i = 0; i < diff1; i++) {

                            while (k >= cf) {
                                cpy = this.space[k][col];
                                this.space[k][col] = '.';
                                this.space[k + 1][col] = cpy;
                                j++;
                                k = cl - j;
                            }
                            cf = cf + 1;
                            cl = cl + 1;
                            j = 0;
                            k = cl - j;

                        }
                    } else if (diff2 != 0) {
                        for(int i=row1;i<cf;i++){
                            if(this.space[i][col]!='.'){
                                if(flag==0) {
                                    //System.out.println(cf);
                                    System.out.println("Cannot move. Going back...");
                                    this.printpuzzle(this.space, 0);
                                }
                                else if(flag==2){
                                    alertObservers("Cannot move. Going back...");
                                }
                                else{
                                    return null;
                                }
                            }
                        }
                        k = cf + j;
                        for (int i = 0; i < diff2; i++) {
                            while (k <= cl) {
                                cpy = this.space[k][col];
                                this.space[k][col] = '.';
                                this.space[k - 1][col] = cpy;
                                j++;
                                k = cf + j;
                            }
                            cf = cf - 1;
                            cl = cl - 1;
                            j = 0;
                            k = cf + j;
                        }
                    }
                    if(flag==0)
                    this.printpuzzle(this.space,0);
                    else if(flag==2){
                        alertObservers("Done");
                    }
                    else{

                        JamConfig j1= new JamConfig(rows,cols,this.space);
                        return j1;
                    }
                }
                else {
                    if(flag==0) {
                        System.out.println("Cannot move. Going back...");
                        this.printpuzzle(this.space, 0);
                    }
                    else if(flag==2){
                        alertObservers("Cannot move. Going back...");
                    }
                }
            }
        }
        else{

            if(flag==0) {
                System.out.println("This move cannot be performed as the slot is not empty. Going back...");
                this.printpuzzle(this.space, 0);
            }
            else if(flag==2){
                alertObservers("Cannot move. Going back...");
            }
        }

        if(this.space[row][col]=='.'){
            if(flag==0) {
                System.out.println("No vehicle found at the location. Going back...");
                this.printpuzzle(this.space, 0);
            }
            else if(flag==2){
                alertObservers("Cannot move. Going back...");
            }
    }

        return null;
    }

    /**
     * Helps to load a file in the PTUI.
     * name stands for the filename to be loaded.
     * flag is just a variable to select what to do in this function.
     * returns nothing.
     * @param name
     * @param flag
     * @throws FileNotFoundException
     */
    public  void loadfile(String name, int flag) throws FileNotFoundException {
        this.filename=name;
        int i,j;
        File f= new File(name);
        Scanner s=new Scanner(f);
        String line=s.nextLine();
        rows= Character.getNumericValue((line.charAt(0)));
        cols= Character.getNumericValue((line.charAt(2)));
        space=new char[rows][cols];
        for(i=0;i<rows;i++){
            for(j=0;j<cols;j++){
                space[i][j]='.';
            }
        }
        int carno=Integer.parseInt(s.nextLine());
        vehicles= new char[carno];
        char c1;
        int n2,n3,n4,n5;
        for(i=0;i<carno;i++){
            line=s.nextLine();
            c1=line.charAt(0);
            vehicles[i]=c1;
            n2=Character.getNumericValue(line.charAt(2));
            n3=Character.getNumericValue(line.charAt(4));
            n4=Character.getNumericValue(line.charAt(6));
            n5=Character.getNumericValue(line.charAt(8));
            for( j=n2;j<=n4;j++) {
                for (int k = n3; k <= n5; k++) {
                    space[j][k]=c1;
                }
            }
        }
        if(flag==0)
        printpuzzle(space,0);
        else if(flag==2){
            alertObservers("Loaded");
        }

    }

    /**
     * Helps to print the puzzle in text format.
     * space represents the matrix that holds the orientation of the vehicles.
     * flag is just a variable to select what to do in this function.
     * returns nothing.
     * @param space
     * @param flag
     * @throws FileNotFoundException
     */
    public  void printpuzzle(char[][] space,int flag) throws FileNotFoundException {
        int i,j;
        for(i=0;i<rows;i++){
            for(j=0;j<cols;j++){
                this.space[i][j]=space[i][j];
            }
        }
        System.out.print("   ");
        for(i=0;i<cols;i++)
            System.out.print(i+" ");
        System.out.println();
        System.out.print("   ");
        for(i=0;i<cols;i++)
            System.out.print("--");
        System.out.println();
        for(i=0;i<rows;i++) {
            System.out.print(i+"|"+" ");
            for (j = 0; j < cols; j++) {
                System.out.print(space[i][j]+" ");
            }
            System.out.println();
        }
        if(flag==0) {
            for(  i=0;i<rows;i++){

                if(space[i][cols-1]=='X'){

                    System.out.println("Congratulations! You won! Resetting the game.");
                    loadfile(filename,0);
                }

            }
            JamPTUI ptui = new JamPTUI();
            ptui.run();
        }
    }

    /**
     * Helps to get the next move in the puzzle.
     * flag is just a variable to select what to do in this function.
     * @param flag
     * @throws FileNotFoundException
     * @throws IndexOutOfBoundsException
     */
    public void getHint(int flag) throws FileNotFoundException,IndexOutOfBoundsException {
        String x;
        try {
            Solver solve1 = new Solver();
            JamConfig jconfig = new JamConfig(this.rows, this.cols, this.space);
            Solution solu1 = solve1.solve(jconfig);
            List<Configuration> path1;
            path1 = solu1.getPath();
            if (solu1.getPath().size() == 0) {
                System.out.println("No solution");
                x = "No solution";
            } else {
                x = "Done";
                path1 = solu1.getPath();


                System.out.println(path1.get(1));
                if (path1.get(1) instanceof JamConfig) {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            this.space[i][j] = ((JamConfig) path1.get(1)).space[i][j];
                        }
                    }
                    if (path1.get(1).isSolution()) {
                        System.out.println("Congratulations! You won! Please reset the puzzle or load a puzzle.");
                    }
                }

            }
            if (flag == 2) {
                alertObservers(x);
            }
        }
        catch (Exception ex){

        }

    }
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
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
}
