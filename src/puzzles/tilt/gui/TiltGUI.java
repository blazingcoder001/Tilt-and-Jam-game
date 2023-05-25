package puzzles.tilt.gui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // images
    private final Image blue = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "blue.png")));
    private final Image green = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "green.png")));
    private final Image block = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "block.png")));
    private final Image hole = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "hole.png")));

    private TiltModel model;

    private Stage stage;
    private GridPane boardGrid;
    private Label messageLabel;

    private final int gridWidth = 250;

    /**
     * Creates the GridPane node which organizes the game's buttons into a grid.
     * @return The grid representing the game board
     */
    private GridPane createBoardGridPane(char[][] board) {
        int dim = board[0].length;

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true); // bad, I know...

        for (int row = 0 ; row < dim ; row++ ){
            RowConstraints rc = new RowConstraints((float) gridWidth/dim);
            rc.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(rc);
        }
        for (int col = 0 ; col < dim; col++ ) {
            ColumnConstraints cc = new ColumnConstraints((float) gridWidth/dim);
            cc.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(cc);
        }

        for (int i = 0 ; i < dim*dim ; i++) {
            int x = i % dim;
            int y = i / dim;

            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setFitWidth((float) gridWidth/dim*.95);

            if (board[y][x] == TiltConfig.BLUE)
                imageView.setImage(blue);
            else if (board[y][x] == TiltConfig.GREEN)
                imageView.setImage(green);
            else if (board[y][x] == TiltConfig.BLOCKER)
                imageView.setImage(block);
            else if (board[y][x] == TiltConfig.GOAL)
                imageView.setImage(hole);

            grid.add(imageView, x, y);
        }

        return grid;
    }

    /**
     * Updates the GridPane for the board with new state from the model.
     */
    private void updateBoardGridPane() {
        char[][] board = model.getBoard();
        int dim = board[0].length;

        // Update the board tiles from the model
        for (int col = 0; col < dim; col++) {
            for (int row = 0; row < dim; row++) {
                char cell = model.getBoard()[row][col];
                ImageView imageView = (ImageView) this.boardGrid.getChildren().get(row*dim+col+1);

                if (cell == TiltConfig.BLUE)
                    imageView.setImage(blue);
                else if (cell == TiltConfig.GREEN)
                    imageView.setImage(green);
                else if (cell == TiltConfig.BLOCKER)
                    imageView.setImage(block);
                else if (cell == TiltConfig.GOAL)
                    imageView.setImage(hole);
                else {
                    imageView.setImage(null);
                }
            }
        }
    }

    /**
     * Creates the VBox containing the load, reset, and hint buttons.
     * @return the VBox
     */
    private VBox createRightButtonsVBox() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        // setup button size
        int buttonWidth = 50;
        int buttonHeight = 20;

        // Initialize load button
        Button loadButton = new Button("Load");
        loadButton.setPrefSize(buttonWidth, buttonHeight);

        // Initialize reset button
        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(buttonWidth, buttonHeight);

        // Initialize hint button
        Button hintButton = new Button("Hint");
        hintButton.setPrefSize(buttonWidth, buttonHeight);

        // Add the buttons to the vbox
        vBox.getChildren().add(loadButton);
        vBox.getChildren().add(resetButton);
        vBox.getChildren().add(hintButton);

        // *** set up the click action for each button ***
        // load game event action
        loadButton.setOnAction(e -> {
            //create a new FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            //set the directory to the boards folder in the current working directory
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
            // limit the file chooser to only show .lob file extensions
            fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            //open up a window for the user to interact with.
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    model.loadGameFromFile(selectedFile);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                constructStage(stage);
//                this.messageLabel.setText("Message: board loaded.");
            }
//            else this.messageLabel.setText("Message: Failed to load file.");
        });

        // reset game event action
        resetButton.setOnAction(e -> {
            try {
                model.resetBoard();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // hint event action
        hintButton.setOnAction(e -> {
            try {
                model.getHint();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        return vBox;
    }

    /**
     * Creates the HBox consisting of the tilt west button, the board grid, and tilt east button.
     * @return the HBox
     */
    private HBox createBoardHBox() {
        HBox centerHBox = new HBox();
        centerHBox.setSpacing(10);
        centerHBox.setAlignment(Pos.CENTER);
        // setup button size
        int EWTiltButtonWidth = 40;
        int EWTiltButtonHeight = 250;

        // initialize and setup tilt west button
        Button tiltWestButton = new Button("<");
        tiltWestButton.setStyle("-fx-font: 24 arial;");
        tiltWestButton.setPrefSize(EWTiltButtonWidth, EWTiltButtonHeight);
        tiltWestButton.setOnAction(e -> {
            try {
                model.tilt("W");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // initialize and setup tilt east button
        Button tiltEastButton = new Button(">");
        tiltEastButton.setStyle("-fx-font: 24 arial;");
        tiltEastButton.setPrefSize(EWTiltButtonWidth, EWTiltButtonHeight);
        tiltEastButton.setOnAction(e -> {
            try {
                model.tilt("E");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        centerHBox.getChildren().add(tiltWestButton);
        centerHBox.getChildren().add(this.boardGrid);
        centerHBox.getChildren().add(tiltEastButton);

        return centerHBox;
    }

    /**
     * Creates the VBox consisting of the tilt north button, the board HBox, and tilt south button.
     * @return the VBox
     */
    private VBox createBoardVBox() {
        VBox boardVBox = new VBox();
        boardVBox.setSpacing(10);
        boardVBox.setAlignment(Pos.CENTER);
        // button size
        int NSTiltButtonWidth = 250;
        int NSTiltButtonHeight = 40;

        // Initialize and setup tilt buttons
        Button tiltNorthButton = new Button("^");
        tiltNorthButton.setStyle("-fx-font: 24 arial;");
        tiltNorthButton.setPrefSize(NSTiltButtonWidth, NSTiltButtonHeight);
        tiltNorthButton.setOnAction(e -> {
            try {
                model.tilt("N");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button tiltSouthButton = new Button("v");
        tiltSouthButton.setStyle("-fx-font: 24 arial;");
        tiltSouthButton.setPrefSize(NSTiltButtonWidth, NSTiltButtonHeight);
        tiltSouthButton.setOnAction(e -> {
            try {
                model.tilt("S");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // add buttons to the board Vbox
        boardVBox.getChildren().add(tiltNorthButton);
        boardVBox.getChildren().add(createBoardHBox());
        boardVBox.getChildren().add(tiltSouthButton);

        return boardVBox;
    }

    /**
     * The application initialization method. This method is called immediately after
     * the Application class is loaded and constructed. An application may override
     * this method to perform initialization prior to the actual starting of the application.
     */
    public void init() throws FileNotFoundException {
        String filename = getParameters().getRaw().get(0);
        model = new TiltModel(filename);
        this.messageLabel = new Label(TiltModel.LOADED_PREFIX+filename); // don't see a way around doing this first one manually
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setMinWidth(450);
        stage.setMinHeight(430);
        stage.setResizable(false);
        constructStage(stage);
    }

    /**
     * Constructs the stage using the model. This is called each time a board is loaded.
     * @param stage the stage.
     */
    private void constructStage(Stage stage) {
//        stage.hide();
        this.boardGrid = createBoardGridPane(model.getBoard());

        VBox outerVBox = new VBox();
        outerVBox.setAlignment(Pos.CENTER);
        outerVBox.setSpacing(10);

        outerVBox.getChildren().add(this.messageLabel);

        HBox innerHBox = new HBox();
        innerHBox.setAlignment(Pos.CENTER);
        innerHBox.setSpacing(10);

        VBox boardVBox = createBoardVBox();
        innerHBox.getChildren().add(boardVBox);
        innerHBox.getChildren().add(createRightButtonsVBox());

        outerVBox.getChildren().add(innerHBox);

        Scene scene = new Scene(outerVBox);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
        // process each of the possible update types by message
        if (message.startsWith(TiltModel.LOADED_PREFIX)) {
            messageLabel.setText(message);
            this.boardGrid = createBoardGridPane(model.getBoard());
        }
        else if (message.equals(TiltModel.LOAD_FAILED)) {
            messageLabel.setText(message);
        }
        else if (message.equals(TiltModel.PUZZLE_RESET)) {
            performUpdate(message);
        }
        else if (message.equals(TiltModel.HINT)) {
            performUpdate(message);
        }
        else if (message.equals(TiltModel.ALREADY_SOLVED)) {
            messageLabel.setText(message);
        }
        else if (message.equals(TiltModel.ILLEGAL_MOVE)) {
            performUpdate(message);
        }
        else if (message.equals(TiltModel.CONGRATULATIONS)) {
            performUpdate(message);
        }
        else if (message.equals(TiltModel.NO_SOLUTION)) {
            performUpdate(message);
        }
        else {
            updateBoardGridPane();
        }
    }

    /**
     * Helper method that updates the message label and the board with new model state.
     * @param message Teh message from the model.
     */
    private void performUpdate(String message) {
        messageLabel.setText(message);
        updateBoardGridPane();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
