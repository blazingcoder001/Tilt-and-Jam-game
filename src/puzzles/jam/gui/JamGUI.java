package puzzles.jam.gui;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
/**
 * Done by Jishnuraj Prakasan (jp4154)
 */

/**
 * Class that implements the User Interface of the project
 */
public class JamGUI extends Application implements Observer<JamModel, String>  {
    File sel_file;
    String msgcpy;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    JamModel j1;
    static int count=0;
    int r1=-1,c1=-1,r2=-1,c2=-1;
    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    String filename;
    static  String previous="some value";
    Button b1= new Button("Load");
    Button b2= new Button("Reset");
    Button b3= new Button("Hint");
    BorderPane bo=new BorderPane();
    GridPane gr= new GridPane();
    HBox h1= new HBox();
    HBox h2= new HBox();
    Text t1;
    Color[] colorarray;
    Color random;
    Stage st1;

    /**
     * This function reads the name of the file.
     * It creates new JamModel object
     * Add observer to the lists of observers and loads the file.
     * @throws FileNotFoundException
     */
    public void init() throws FileNotFoundException {
        filename = getParameters().getRaw().get(0);
        t1=new Text("Loaded "+ filename);
        this.j1=new JamModel();
        j1.addObserver(this);
        j1.loadfile(filename,1);
    }

    /**
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * Returns nothing.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
            this.st1= new Stage();
            st1=stage;

        j1.loadfile(filename,2);
    }

    /**
     * Helps to find the next hint in the GUI.
     * @throws FileNotFoundException
     */
    public void getHintGUI() throws FileNotFoundException {
        j1.getHint(2);

    }

    /**
     * Function that helps to pass the coordinates of the buttons clicked in the grid.
     * Returns nothing.
     * row is the x coordinate and col is the y coordinate
     * @param row
     * @param col
     * @throws FileNotFoundException
     */
    void pass(int row,int col) throws FileNotFoundException {
        if(count%2==0){
            r1=row;
            c1=col;
            count++;
        }
        else{
            count++;
            r2=row;
            c2=col;
            System.out.println(r1+" "+c1+" "+r2+" "+c2);
            JamConfig j2=j1.Move(r1, c1, r2, c2, j1.space[r1][c1], 2, j1.space, j1.rows, j1.cols);
        }
    }

    /**
     * Function that helps to reset the board for the new file to get loaded.
     * Returns nothing.
     */
    public void boardReset(){

        ObservableList<Node> children=gr.getChildren();
        Button x = new Button();
        for( Node n:children) {
            if(n instanceof Button) {
                x = (Button) n;
                x.setBackground(new Button().getBackground());
                x.setText(" ");
            }
        }
    }

    /**
     * Helps to create a new grid for every file to be loaded.
     * returns nothing
     */
    public void creategrid(){
        Button b1= new Button("Load");
        Button b2= new Button("Reset");
        Button b3= new Button("Hint");
        BorderPane bo=new BorderPane();
        GridPane gr= new GridPane();
        HBox h1= new HBox();
        HBox h2= new HBox();
        Text t1;
        t1=new Text("Loaded "+ filename);
        this.b1=b1;
        this.b2=b2;
        this.b3=b3;
        this.bo=bo;
        this.gr=gr;
        this.h1=h1;
        this.h2=h2;
        this.t1=t1;
        b1.setOnAction(event -> {
            boardReset();
            try {
                loadfileGUI(null);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        b2.setOnAction(event -> {
            boardReset();
            try {
                loadfileGUI(filename);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        b3.setOnAction(event -> {
            try {
                getHintGUI();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        bo.setTop(h1);
        bo.setCenter(gr);
        bo. setBottom(h2);
        h1.getChildren().addAll(t1);
        h1.setAlignment(Pos.BASELINE_CENTER);
        h2.getChildren().addAll(b1,b2,b3);
        h2.setAlignment(Pos.BASELINE_CENTER);
        int i,j;
        for(i=0;i< j1.rows;i++){
            for(j=0;j<j1.cols;j++){
                Button b= new Button();
                b.setMinSize(100,100);
                GridPane.setRowIndex(b,i);
                GridPane.setColumnIndex(b,j);
                gr.getChildren().addAll(b);
                int row=i;
                int col=j;

                b.setOnAction(event -> {
                    try {

                        pass(row,col);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        gr.setGridLinesVisible(true);
        st1.setScene(new Scene(bo));
        st1.setTitle("Rush Hour");
        st1.show();
    }

    /**
     *
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     * @throws FileNotFoundException
     */
    @Override
    public void update(JamModel jamModel, String message) throws FileNotFoundException {

            if(message.equals("Loaded")){
                if(!previous.equals(filename) ) {
                    setColor();
                    previous=filename;
                }
                creategrid();
                msgcpy=message+" "+filename;
                boardDisplay(message+" "+filename);
            }
            else if(message.equals("Cannot move. Going back...")){
               //boardReset();
                if(!previous.equals(filename) ) {
                    setColor();
                    previous=filename;
                }

                creategrid();
                msgcpy=message;
                boardDisplay(message);
            }
            else if(message.equals("Done")){
               //boardReset();
                if(!previous.equals(filename) ) {
                    setColor();
                    previous=filename;
                }
               if( checkSolu()==false) {
                   msgcpy="Moved";
                   creategrid();
                   boardDisplay("Moved!");
               }


            }
            else if(message.equals("No solution")){
                //boardReset();
                creategrid();
                boardDisplay(message);
            }
    }

    /**
     * Helps to load a new file with file chooser as well as filename passed.
     * @param filename
     * @throws FileNotFoundException
     */
    public void loadfileGUI(String filename) throws FileNotFoundException {
        if (filename == null) {
            try {
                char arr[]= new char[10];
                int c=0;
                sel_file = new File("random path");
                FileChooser fi = new FileChooser();
                fi.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                fi.setTitle("Load a game board.");
                sel_file = fi.showOpenDialog(new Stage());
                filename = String.valueOf(sel_file);
                for(int i=filename.length()-1;i>=0;i--){
                    if (filename.charAt(i) != 'j') {
                        arr[c]=filename.charAt(i);
                        c++;
                    }
                    else{
                        arr[c]=filename.charAt(i);
                        break;
                    }
                }
                filename="";
                for(int i=c;i>=0;i--){
                        filename=filename+arr[i];
                }
                if(!filename.contains("null")) {
                    this.filename = "data/jam/" + filename;
                    System.out.println(filename);
                    j1.loadfile(this.filename, 2);
                }
                else{
                    boardDisplay(msgcpy);
                }
            }
            catch (Exception ex){

            }
        }
        else{
            j1.loadfile(this.filename,2);
        }
    }

    /**
     * Sets the color for each vehicle in the grid. It is unique for each vehicle.
     * Returns nothing
     */

    public void setColor(){
        Random r1= new Random();
        colorarray= new Color[j1.vehicles.length];
        for(int i=0;i<j1.vehicles.length;i++){
            float red=r1.nextFloat();
            float green=r1.nextFloat();
            float blue=r1.nextFloat();
            random=new Color(red,green,blue,1);
            colorarray[i]=random;
        }
    }

    /**
     * Helps to check whether the current configuration is a solution in GUI.
     * Returns boolean value.
     * @return
     */
    public boolean checkSolu(){
        for(int i=0;i<j1.rows;i++){

                if(j1.space[i][j1.cols-1]=='X'){
                    boardDisplay("Congratulations. You won! Please click reset or load a new game!" );
                    return true;
                }

        }
        return false;
    }

    /**
     * Helps to display the updated board in the GUI.
     * msg  stands for the message that is to be printed in the application window in the text field on the top Hbox.
     * Returns nothing.
     * @param msg
     */
    public void boardDisplay(String msg){

        for(int i=0;i< j1.rows;i++){
            for(int j=0;j<j1.cols;j++){
                for(int k=0;k<j1.vehicles.length;k++){
                    if(j1.space[i][j]==j1.vehicles[k]){
                        ObservableList<Node> children=gr.getChildren();
                        Button x = new Button();
                        for( Node n:children) {
                            if(n instanceof Button)
                                if (GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == j) {
                                    x = (Button) n;
                                    x.setBackground(Background.fill(colorarray[k]));
                                    x.setText(String.valueOf(j1.vehicles[k]));
                                }
                        }
                    }
                }
            }
        }
        ObservableList b1=h1.getChildren();
        for (Object n:b1) {
            if (n instanceof Text) {
                ((Text) n).setText(msg);
            }
        }
    }

    /**
     * Launches the aplication.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
