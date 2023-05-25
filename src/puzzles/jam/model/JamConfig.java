package puzzles.jam.model;



import puzzles.common.solver.Configuration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
/**
 * Done by Jishnuraj Prakasan (jp4154)
 */

/**
 * JamConfiguration class
 * It defines the configuration of the Board.
 */
public class JamConfig implements Configuration {
    public JamModel m1 = new JamModel();
    char cpy;
    int flag = -1;
    int d;
    char c;
    public char[][] space;
    int flagalph = -1;
    int rows, cols, i, j;

    int vehiclei, vehiclej;

    /**
     * JamConfig constructor that sets the values of rows,cols and space matrix.
     * rows represent the number of rows of the puzzle.
     * cols represent the number of columns in the puzzle.
     * space represent the orientation of vehicles in the puzzle.
     * @param rows
     * @param cols
     * @param space
     */
    public JamConfig(int rows, int cols, char[][] space) {
        this.rows = rows;
        this.cols = cols;
        this.space = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.space[i][j] = space[i][j];
            }
        }
    }

    /**
     * Function to check whether a configuration is solution or not.
     * returns boolean value.
     * @return
     */
    @Override
    public boolean isSolution() {

        for (i = 0; i < rows; i++) {
            if (space[i][cols - 1] == 'X') {
                return true;
            }
        }
        return false;
    }

    /**
     * Function to get the neighbours of the current configuration.
     * Returns a collection of neighbours.
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public Collection<Configuration> getNeighbors() throws FileNotFoundException {
        Collection<Configuration> neigh = new ArrayList<>();


        for (int i = 0; i < rows; i++) {

            flag = -1;
            for (d = 0; d < cols; d++) {
                if (space[i][d] != '.') {
                    cpy = space[i][d];
                    break;
                }
            }

            for (int j = d; j < cols; j++) {

                vehiclei = i;
                vehiclej = j;
                if (space[vehiclei][vehiclej] != '.' && (cpy != space[vehiclei][vehiclej] || flag == -1)) {
                    cpy = space[vehiclei][vehiclej];
                    for (int k = vehiclej; k < cols; k++) {

                        if (k != vehiclej && space[vehiclei][k] == '.') {
                            for(int y=vehiclej;y<=k;y++){
                                if(space[vehiclei][y]!=cpy){
                                    break;
                                }
                            }
                            c = space[vehiclei][vehiclej];
                            if(((vehiclei-1>=0 && space[vehiclei-1][vehiclej]!=c)|| vehiclei-1<0) &&((vehiclei+1<rows && space[vehiclei+1][vehiclej]!=c) || vehiclei+1>=rows) ) {


                                            JamConfig x = m1.Move(vehiclei, vehiclej, vehiclei, k, c, 1, space, rows, cols);
                                            if (x != null) {
                                                neigh.add(x);
                                            }



                            }
                            break;
                        }
                    }
                    for (int k = vehiclej; k >= 0; k--) {
                        //System.out.println(k);
                        if (k != vehiclej && space[vehiclei][k] == '.') {
                            for(int y=vehiclej;y<=k;y++){
                                if(space[vehiclei][y]!=cpy){
                                    break;
                                }
                            }
                            c = space[vehiclei][vehiclej];
                            if(((vehiclei-1>=0 && space[vehiclei-1][vehiclej]!=c)|| vehiclei-1<0) &&((vehiclei+1<rows && space[vehiclei+1][vehiclej]!=c) || vehiclei+1>=rows) ) {

                                            JamConfig x = m1.Move(vehiclei, vehiclej, vehiclei, k, c, 1, space, rows, cols);
                                            if (x != null) {
                                                neigh.add(x);
                                            }



                            }
                            break;
                        }
                    }
                    flag = 0;
                }

            }

        }
        for (int i = 0; i < cols; i++) {
            flag = -1;
            for (d = 0; d < rows; d++) {
                if (space[d][i] != '.') {
                    cpy = space[d][i];
                    break;
                }
            }
            for (int j = d; j < rows; j++) {
                vehiclei = j;
                vehiclej = i;
                if (space[vehiclei][vehiclej] != '.' && (cpy != space[vehiclei][vehiclej] || flag == -1)) {
                    cpy = space[vehiclei][vehiclej];
                    for (int k = vehiclei; k < rows; k++) {
                        if (k != vehiclei && space[k][vehiclej] == '.') {
                            for(int y=vehiclei;y<=k;y++){
                                if(space[y][vehiclej]!=cpy){
                                    break;
                                }
                            }
                            c = space[vehiclei][vehiclej];
                            if(((vehiclej-1>=0 && space[vehiclei][vehiclej - 1] != c) || vehiclej-1<0 )&& ((vehiclej+1<cols && space[vehiclei][vehiclej + 1] != c) ||vehiclej+1>=cols)) {

                                            JamConfig x = m1.Move(vehiclei, vehiclej, k, vehiclej, c, 1, space, rows, cols);
                                            if (x != null) {
                                                neigh.add(x);
                                            }



                            }
                            break;

                        }
                    }
                    for (int k = vehiclei; k >= 0; k--) {
                        if (k != vehiclei && space[k][vehiclej] == '.') {
                            for(int y=vehiclei;y<=k;y++){
                                if(space[y][vehiclej]!=cpy){
                                    break;
                                }
                            }
                            c = space[vehiclei][vehiclej];
                            if(((vehiclej-1>=0 && space[vehiclei][vehiclej - 1] != c) || vehiclej-1<0 )&& ((vehiclej+1<cols && space[vehiclei][vehiclej + 1] != c) ||vehiclej+1>=cols)) {

                                            JamConfig x = m1.Move(vehiclei, vehiclej, k, vehiclej, c, 1, space, rows, cols);
                                            if (x != null) {
                                                neigh.add(x);
                                            }



                            }
                            break;
                        }
                    }
                    flag = 0;
                }
            }
        }
        return neigh;
    }

    /**
     * Overridden equals function
     * returns boolean value
     * O is of Object type
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        int k=1;
        if (o == null || getClass() != o.getClass()) return false;
        JamConfig jamConfig = (JamConfig) o;
        for(int i=0;i<rows;i++){
            for(j=0;j<cols;j++){
                if(space[i][j]!=jamConfig.space[i][j]){
                    k=0;
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Overridden hashcode function
     * return an integer
     * @return
     */


    @Override
    public int hashCode() {

        return  this.toString().hashCode();
    }

    /**
     * Overridden toString function
     * returns a String consisting of the orientation of the vehicles in the current JamConfig Object.
     * @return
     */
    @Override
    public String toString() {
        String str = new String();
        str = "";
        str = str + "   ";
        for (i = 0; i < cols; i++)
            str = str + String.valueOf(i) + " ";
        str = str + "\n";
        str = str + "   ";
        for (i = 0; i < cols; i++)
            str = str + "--";
        str += "\n";
        for (i = 0; i < rows; i++) {
            str = str + String.valueOf(i) + "|" + " ";
            for (j = 0; j < cols; j++) {
                str = str + space[i][j] + " ";
            }
            str += "\n";

        }
        return str;
    }
}
