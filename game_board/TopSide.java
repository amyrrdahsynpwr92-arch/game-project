package game_board;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.scene.control.ContentDisplay;
import card.*;
import util.*;
import graph.*;
import set_undo_and_redo.*;

public class TopSide extends HBox {
    int index = 0;
    private Timeline motivateSign;
    private Timeline writingAnimation;
    private BorderPane currentPane;
    private DrawUndoButton undoButton;
    private ImageView undoImage = new ImageView(new Image(getClass().getResourceAsStream("/images/details/undo.png")));
    ArrayList<Edge> edges;
    ArrayList<Sector> sectors;
    ArrayList<Player> players;
    int currentPlayer = 1;
    private DrawBoard board;
    private Market market = new Market();
    public Market getMarket(){
        return market;
    }
    public TopSide(BorderPane currentPane, DrawBoard board){
        this.currentPane = currentPane;
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.setMinHeight(50);
        undoButton = new DrawUndoButton();
        //this.nodes = board.getMap().getNodes();
        // this.edges = board.getMap().getEdges();
        // this.sectors = board.getMap().getSectors();
        // this.players = board.getMap().getPlayers();
        drawPlayerBox(1);
        drawUndoButton();
        drawStatusPanel("player 1! please put a MVP");
        drawPricesButton();
    }
    public void drawPlayerBox(int playerNumber){
        currentPlayer = playerNumber;
        TextField playerBox = new TextField("Player: " + Integer.toString(playerNumber));
        playerBox.prefWidthProperty().bind(widthProperty().divide(6));
        playerBox.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        playerBox.setPrefHeight(40);
        playerBox.setEditable(false);
        playerBox.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5; -fx-text-fill: white");
        if(this.getChildren().size() > 0)this.getChildren().remove(0);
        this.getChildren().add(0, playerBox);
    }
    public void drawUndoButton(){
        Button btUndo = new Button("Undo", undoImage);
        undoImage.setFitHeight(20);
        undoImage.setFitWidth(20);
        undoImage.setPreserveRatio(true);
        btUndo.setContentDisplay(ContentDisplay.RIGHT);
        btUndo.setLayoutY(5);
        btUndo.setPrefHeight(40);
        btUndo.prefWidthProperty().bind(widthProperty().divide(6));
        btUndo.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btUndo.setTextFill(Color.WHITE);
        btUndo.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        this.getChildren().add(1, btUndo);
        btUndo.setOnAction(e -> undoButton.loadPrevoiusStage());
    }
    public void drawStatusPanel(String status){
        if(board.getGameStoppage())return;
        TextField statusBox = new TextField("|");
        statusBox.setPrefHeight(40);
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
        statusBox.prefWidthProperty().bind(widthProperty().divide(3));
        if(this.getChildren().size() > 2)this.getChildren().remove(2);
        this.getChildren().add(2, statusBox);
    }
    public void drawPricesButton(){
        Button btPrices = new Button("prices of sources");
        btPrices.setLayoutY(5);
        btPrices.setPrefHeight(40);
        btPrices.prefWidthProperty().bind(widthProperty().divide(3));
        btPrices.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btPrices.setTextFill(Color.WHITE);
        btPrices.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        if(this.getChildren().size() > 3)this.getChildren().remove(3);
        this.getChildren().add(3, btPrices);
        btPrices.setOnAction(e -> drawPricesOfSources());
    }
    private void drawPricesOfSources(){
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
                    title.setText(Integer.toString(market.getCards().get(0).getPrice()) + " Capitals");
                    break;
                case Cloud:
                    title.setText(Integer.toString(market.getCards().get(1).getPrice()) + " Capitals");
                    break;
                case Data:
                    title.setText(Integer.toString(market.getCards().get(2).getPrice()) + " Capitals");
                    break;
                case Patent:
                    title.setText(Integer.toString(market.getCards().get(3).getPrice()) + " Capitals");
                    break;
                case Talent:
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
        btOK.setOnAction(e -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                currentPane.setLeft(oldLeft);
                currentPane.setCenter(oldCenter);
                currentPane.setTop(oldTop);
                currentPane.setRight(oldRight);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
        });
    }

    public DrawUndoButton getUndoButton() {
        return undoButton;
    }
}
