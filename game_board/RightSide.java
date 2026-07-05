package game_board;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import card.Null;
import util.*;

public class RightSide extends VBox {
    private ArrayList<Player> players;
    private String sum = "";
    private DrawBoard board;
    private boolean dicesRolled = false;
    private boolean moveAuditor = false;
    ArrayList<Sector> sectors;
    double offsetX;
    double offsetY;
    double startX;
    double startY;
    public RightSide(DrawBoard board){
        this.board = board;
        players = board.getMap().getPlayers();
        this.sectors = board.getMap().getSectors();
        this.setMaxWidth(100);
        drawTotalCards();
        drawDices();
        drawSumOfDices();
        // drawAuditor();
    }
    public void drawTotalCards(){
        Optional<Integer> totalCards = players.stream().map(p -> p.getMyCards().size()).reduce((a, b) -> a + b);
        Text text1 = new Text("total cards:");
        Text text2 = new Text(Integer.toString(totalCards.get()));
        VBox vbox = new VBox(10);
        text1.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        text2.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        text1.setFill(Color.WHITE);
        text2.setFill(Color.WHITE);
        vbox.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5; -fx-text-fill: white");
        vbox.prefHeightProperty().bind(heightProperty().divide(6));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(text1, text2);
        if(this.getChildren().size() > 0)this.getChildren().remove(0);
        this.getChildren().add(0, vbox);
    }
    public void drawDices(){
        ImageView dice1 = new ImageView(new Image(getClass().getResourceAsStream("/images/details/dice.png")));
        ImageView dice2 = new ImageView(new Image(getClass().getResourceAsStream("/images/details/dice.png")));
        FadeTransition fadeOutForDice = new FadeTransition();
        FadeTransition fadeOutForText= new FadeTransition();
        FadeTransition fadeIn = new FadeTransition();
        VBox pane1 = new VBox(10);
        VBox pane2 = new VBox(10);
        fadeOutForDice.setDuration(Duration.millis(500));
        fadeOutForDice.setFromValue(1.0);
        fadeOutForDice.setToValue(0.0);
        fadeOutForText.setDuration(Duration.millis(500));
        fadeOutForText.setFromValue(1.0);
        fadeOutForText.setToValue(0.0);
        fadeIn.setDuration(Duration.millis(500));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        dice1.setFitHeight(70);
        dice1.setFitWidth(70);
        dice1.setPreserveRatio(true);
        dice1.setSmooth(true);
        dice2.setFitHeight(70);
        dice2.setFitWidth(70);
        dice2.setPreserveRatio(true);
        dice2.setSmooth(true);
        Text text1 = new Text("Dice 1");
        Text text2 = new Text("Dice 2");
        text1.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        text1.setFill(Color.WHITE);
        text2.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        text2.setFill(Color.WHITE);
        pane1.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        pane1.prefHeightProperty().bind(heightProperty().divide(3));
        pane1.setAlignment(Pos.CENTER);
        pane1.getChildren().addAll(dice1, text1);
        pane2.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        pane2.prefHeightProperty().bind(heightProperty().divide(3));
        pane2.setAlignment(Pos.CENTER);
        pane2.getChildren().addAll(dice2, text2);
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        this.getChildren().add(1, pane1);
        if(this.getChildren().size() > 2)this.getChildren().remove(2);
        this.getChildren().add(2, pane2);
        pane1.setOnMouseClicked(e -> {
            if(pane1.getChildren().size() == 2 && board.getMap().getHandle_TurningGame().isTurning_Game()){
                fadeOutForDice.setNode(dice1);
                fadeOutForDice.play();
                fadeOutForText.setNode(text1);
                fadeOutForText.play();
                fadeOutForDice.setOnFinished(event -> {
                    pane1.getChildren().clear();
                    int num = new Random().nextInt(6) + 1;
                    Text text = new Text(Integer.toString(num));
                    text.setFont(Font.font("Roboto", FontWeight.BOLD, 23));
                    text.setFill(Color.WHITE);
                    pane1.getChildren().add(text);
                    fadeIn.setNode(text);
                    fadeIn.play();
                });
            }
        });
        pane2.setOnMouseClicked(e -> {
            if(pane2.getChildren().size() == 2 && pane1.getChildren().size() == 1 && board.getMap().getHandle_TurningGame().isTurning_Game()){
                fadeOutForDice.setNode(dice2);
                fadeOutForDice.play();
                fadeOutForText.setNode(text2);
                fadeOutForText.play();
                fadeOutForDice.setOnFinished(event -> {
                    pane2.getChildren().clear();
                    int num = new Random().nextInt(6) + 1;
                    sum = Integer.toString(num + Integer.parseInt(((Text)pane1.getChildren().get(0)).getText()));
                    Text text = new Text(Integer.toString(num));
                    text.setFont(Font.font("Roboto", FontWeight.BOLD, 23));
                    text.setFill(Color.WHITE);
                    pane2.getChildren().add(text);
                    dicesRolled = true;
                    for(Sector sector: sectors){
                        if(!(sector.getResource() instanceof Null) && sector.getNumber() == Integer.parseInt(sum)){
                            sector.getMvpPlayers().stream().map(p -> p.getMyCards()).forEach(c -> c.add(sector.getResource()));
                            sector.getUnicornPlayers().stream().map(p -> p.getMyCards()).forEach(c -> {
                                c.add(sector.getResource());
                                c.add(sector.getResource());
                            });
                        }
                    }
                    Platform.runLater(() -> {
                        board.getLeftSide().drawMyCards(players.get(0));
                        if(Integer.parseInt(sum) == 7){
                            board.getDownSide().setMoveAuditor(true);
                            board.getTopSide().drawStatusPanel("sum equals to 7. move auditor piece to any possible sector");
                            board.getDownSide().ReDrawAuditor();
                        }else{
                            if(board.getMap().getHandle_TurningGame().getCurrentPlayer().getOnTradeRequest())
                                board.getTopSide().drawStatusPanel("Player " + board.getMap().getHandle_TurningGame().getCurrentPlayer().getPlayerNumber() + "! you have a Trade request. check trade panel");
                            else 
                                board.getTopSide().drawStatusPanel("All resources are obtained! Player " + board.getMap().getHandle_TurningGame().getCurrentPlayer().getPlayerNumber() + ", Do you turn");
                        }
                    });
                    drawSumOfDices();
                    fadeIn.setNode(text);
                    fadeIn.play();
                });
            }
        });
    }
    public void drawSumOfDices(){
        TextField text = new TextField("sum: " + sum);
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        text.prefHeightProperty().bind(heightProperty().divide(6));
        text.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5; -fx-text-fill: white;");
        text.setAlignment(Pos.CENTER);
        if(this.getChildren().size() > 3)this.getChildren().remove(3);
        this.getChildren().add(3, text);
    }

    public boolean getDicesRolled() {
        return dicesRolled;
    }

    public void setDicesRolled(boolean dicesRolled) {
        this.dicesRolled = dicesRolled;
    }
    public void setSum(String sum){
        this.sum = sum;
    }
}