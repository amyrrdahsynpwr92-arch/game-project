package game_board;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import game_board.DrawBoard;
import set_undo_and_redo.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import cards.*;
import graph.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.scene.control.ContentDisplay;
import util.*;
import type.*;

public class TopSide extends HBox {
    private int index = 0;
    private Timeline motivateSign;
    private Timeline writingAnimation;
    private BorderPane currentPane;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private ImageView undoImage = new ImageView(new Image(getClass().getResourceAsStream("/images/details/undo.png")));
    private ImageView redoImage = new ImageView(new Image(getClass().getResourceAsStream("/images/details/redo.png")));
    ArrayList<Edge> edges;
    ArrayList<Sector> sectors;
    ArrayList<Player> players;
    int currentPlayer = 1;
    private DrawBoard board;
    private Market market = new Market();
    private TextField statusBox = new TextField("|");
    public Market getMarket(){
        return market;
    }
    public TopSide(BorderPane currentPane, DrawBoard board){
        this.currentPane = currentPane;
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.setMinHeight(50);
        undoAction = new UndoAction(board);
        redoAction = new RedoAction(board, undoAction);
        undoAction.setRedoAction(redoAction);
        if(board.getLoadClass() != null)
            market = board.getLoadClass().getMarket();
        if(board.getLoadClass() == null)
            drawPlayerBox(1);
        else{
            if(board.getLoadClass().getHandle_PreGame().isPreGame())
                drawPlayerBox(board.getLoadClass().getHandle_PreGame().getCurrentPlayer().getPlayerNumber());  
            else
                drawPlayerBox(board.getLoadClass().getHandle_TurningGame().getCurrentPlayer().getPlayerNumber()); 
        } 
        if(board.getLoadClass() == null)
            drawStatusPanel("player 1! please put a MVP");
        else
            drawStatusPanel(board.getLoadClass().getStatusText());
        drawUndoAction();
        drawRedoAction();
        drawPricesButton();
    }
    public void drawPlayerBox(int playerNumber){
        currentPlayer = playerNumber;
        Text playerBox = new Text("Player: ");
        Text playerNum = new Text(Integer.toString(currentPlayer));
        Rectangle rec = new Rectangle(18, 18);
        rec.setArcWidth(5);
        rec.setArcHeight(5);
        rec.setFill(board.getPlayers().get(playerNumber-1).getColor());
        HBox pane = new HBox(5);
        pane.prefWidthProperty().bind(widthProperty().divide(9));
        playerBox.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        playerBox.setFill(Color.WHITE);
        playerNum.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        playerNum.setFill(Color.WHITE);
        pane.setPrefHeight(40);
        pane.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5; -fx-text-fill: white");
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(playerBox, new StackPane(rec, playerNum));
        if(this.getChildren().size() > 0)this.getChildren().remove(0);
        this.getChildren().add(0, pane);
    }
    public void drawStatusPanel(String status){
        if(board.getGameStoppage())return;
        statusBox.setText("|");
        statusBox.setPrefHeight(50);
        statusBox.setEditable(false);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setStyle("-fx-background-color: rgb(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5; -fx-text-fill: white;");
        index = 0;
        if (writingAnimation != null) {
            writingAnimation.stop();
        }
        if (motivateSign != null) {
            motivateSign.stop();
        }
        motivateSign = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            if(statusBox.getText().charAt(statusBox.getText().length() - 1) == '|'){
                statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1) + " ");
            }
            else
                statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1) + "|");
        }));
        motivateSign.setCycleCount(Timeline.INDEFINITE);
        writingAnimation = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1));
            statusBox.appendText(Character.toString(status.charAt(index++)) + "|");
        }));
        statusBox.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        writingAnimation.setCycleCount(status.length());
        writingAnimation.setOnFinished(e -> motivateSign.play());
        writingAnimation.play();
        statusBox.prefWidthProperty().bind(widthProperty().divide(2));
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        this.getChildren().add(1, statusBox);
    }
    public void drawUndoAction(){
        Button btUndo = new Button("Undo", undoImage);
        undoImage.setFitHeight(20);
        undoImage.setFitWidth(20);
        undoImage.setPreserveRatio(true);
        btUndo.setContentDisplay(ContentDisplay.RIGHT);
        btUndo.setLayoutY(5);
        btUndo.setPrefHeight(45);
        btUndo.prefWidthProperty().bind(widthProperty().divide(9));
        btUndo.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btUndo.setTextFill(Color.WHITE);
        btUndo.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        if(this.getChildren().size() > 2)this.getChildren().remove(2);
        this.getChildren().add(2, btUndo);
        btUndo.setOnAction(e -> undoAction.loadPrevoiusStage());
    }
    public void drawRedoAction(){
        Button btRedo = new Button("Redo", redoImage);
        redoImage.setFitHeight(20);
        redoImage.setFitWidth(20);
        redoImage.setPreserveRatio(true);
        btRedo.setContentDisplay(ContentDisplay.RIGHT);
        btRedo.setLayoutY(5);
        btRedo.setPrefHeight(45);
        btRedo.prefWidthProperty().bind(widthProperty().divide(9));
        btRedo.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btRedo.setTextFill(Color.WHITE);
        btRedo.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        if(this.getChildren().size() > 3)this.getChildren().remove(3);
        this.getChildren().add(3, btRedo);
        btRedo.setOnAction(e -> redoAction.loadNextStage());
    }
    public void drawPricesButton(){
        Button btPrices = new Button("prices of sources");
        btPrices.setLayoutY(5);
        btPrices.setPrefHeight(45);
        btPrices.prefWidthProperty().bind(widthProperty().divide(6));
        btPrices.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btPrices.setTextFill(Color.WHITE);
        btPrices.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        if(this.getChildren().size() > 4)this.getChildren().remove(4);
        this.getChildren().add(4, btPrices);
        btPrices.setOnAction(e -> drawPricesOfSources());
    }
    private void drawPricesOfSources(){
        if(board.getGameStoppage() || (board.getLoadClass() != null && board.getLoadClass().getGameStoppage() != -1))return;
        Player player = board.getMap().getHandle_TurningGame().getCurrentPlayer();
        FadeTransition fadeIn = new FadeTransition();
        FadeTransition fadeOut = new FadeTransition();
        List<ResourceCard> resources = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
        ScrollPane paneForScroll = new ScrollPane();
        HBox hBox = new HBox(10);
        VBox vBox = new VBox(10);
        fadeOut.setNode(currentPane);
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeIn.setNode(currentPane);
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            currentPane.getChildren().clear();
            currentPane.setCenter(vBox);
            fadeIn.play();
        });
        for(ResourceCard resource: resources){
            VBox paneForResource = new VBox(8);
            ImageView image = resource.getSymbol();
            image.setFitWidth(400);
            image.setFitHeight(400);
            image.setSmooth(true);
            image.setPreserveRatio(true);
            Text title = new Text();
            switch(resource.getType()){
                case Capital:
                    if(player.getRole() == PlayerRole.The_Hacker_CEO)
                        title.setText(Integer.toString(market.getCards().get(0).getPrice() - 1) + " Capitals");
                    else
                        title.setText(Integer.toString(market.getCards().get(0).getPrice()) + " Capitals");
                    break;
                case Cloud:
                    if(player.getRole() == PlayerRole.The_Hacker_CEO)
                        title.setText(Integer.toString(market.getCards().get(1).getPrice() - 1) + " Capitals");
                    else
                        title.setText(Integer.toString(market.getCards().get(1).getPrice()) + " Capitals");
                    break;
                case Data:
                    if(player.getRole() == PlayerRole.The_Hacker_CEO)
                        title.setText(Integer.toString(market.getCards().get(2).getPrice() - 1) + " Capitals");
                    else
                        title.setText(Integer.toString(market.getCards().get(2).getPrice()) + " Capitals");
                    break;
                case Patent:
                    if(player.getRole() == PlayerRole.The_Hacker_CEO)
                        title.setText(Integer.toString(market.getCards().get(3).getPrice() - 1) + " Capitals");
                    else
                        title.setText(Integer.toString(market.getCards().get(3).getPrice()) + " Capitals");
                    break;
                case Talent:
                    if(player.getRole() == PlayerRole.The_Hacker_CEO)
                        title.setText(Integer.toString(market.getCards().get(4).getPrice() - 1) + " Capitals");
                    else
                        title.setText(Integer.toString(market.getCards().get(4).getPrice()) + " Capitals");
                    break;
                case Null:
                    break;
            }
            title.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
            paneForResource.getChildren().addAll(image, title);
            paneForResource.setAlignment(Pos.CENTER);
            paneForResource.setStyle("-fx-border-color: blue");
            hBox.getChildren().add(paneForResource);
        }
        Button btOK = new Button("Ok");
        btOK.setPrefSize(100, 50);
        btOK.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        btOK.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        paneForScroll.setContent(hBox);
        paneForScroll.setPrefSize(800, 500);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(paneForScroll, btOK);
        Node oldLeft = currentPane.getLeft();
        Node oldCenter = currentPane.getCenter();
        Node oldTop = currentPane.getTop();
        Node oldRight = currentPane.getRight();
        Node oldBottom = currentPane.getBottom();
        btOK.setOnAction(e -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                currentPane.setLeft(oldLeft);
                currentPane.setCenter(oldCenter);
                currentPane.setTop(oldTop);
                currentPane.setRight(oldRight);
                currentPane.setBottom(oldBottom);
                board.getCurrentPane().getChildren().add(board.getDownSide().getAuditor());
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
        });
    }

    public UndoAction getUndoAction() {
        return undoAction;
    }
    public TextField getStatusBox(){
        return statusBox;
    }
}
