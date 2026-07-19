package game_board;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Map;
import cards.ResourceCard;
import java.util.HashMap;
import util.Player;

public class LeftSide extends VBox {
    private ArrayList<Player> players;
    private Player player;
    private BorderPane currentPane;
    private DrawBoard board;
    int playerWon = -1;
    public LeftSide(ArrayList<Player> players, DrawBoard board){
        this.players = players;
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.color(0.0, 0.0, 0.0, 0.92),CornerRadii.EMPTY,Insets.EMPTY)));
        this.currentPane = board.getCurrentPane();
        drawPlayersScore();
        drawMyCards(players.get(0));
    }
    public void drawPlayersScore() {
        if(board.getGameStoppage())return;
        VBox pane = new VBox(20);
        Text text = new Text("scores:");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        pane.getChildren().add(text);
        for(Player player: players){
            Rectangle back = new Rectangle();
            back.setWidth(80);
            back.setHeight(30);
            back.setArcWidth(13);
            back.setArcHeight(13);
            back.setFill(player.getColor());
            Text score = new Text("player " + player.getPlayerNumber() + ": " + player.getScore());
            if(board.getMap().getHandle_PreGame().isPreGame()){
                if(player == board.getMap().getHandle_PreGame().getCurrentPlayer()){
                    FillTransition ft = new FillTransition(Duration.millis(400), back, player.getColor(), Color.TRANSPARENT);
                    ft.setAutoReverse(true);
                    ft.setCycleCount(2);
                    ft.play();
                }
            }else{
                if(player == board.getMap().getHandle_TurningGame().getCurrentPlayer()){
                    FillTransition ft = new FillTransition(Duration.millis(400), back, player.getColor(), Color.TRANSPARENT);
                    ft.setAutoReverse(true);
                    ft.setCycleCount(2);
                    ft.play();
                }
            }
            if(player.getScore() >= 10 && playerWon == -1){
                if(board.getLoadClass() == null || board.getLoadClass().getGameStoppage() == -1)
                    playerWon = player.getPlayerNumber();
            }
            score.setFill(Color.WHITE);
            score.setFont(Font.font("Roboto", FontWeight.NORMAL, 15));
            pane.getChildren().add(new StackPane(back, score));
        }
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-border-color: turquoise; -fx-border-width: 3");
        pane.setPrefHeight(325);
        pane.setPrefWidth(100);
        if(this.getChildren().size() > 0)this.getChildren().remove(0);
        this.getChildren().add(0, pane);
        if(playerWon != -1){
            try{
                Thread.sleep(500);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
            Platform.runLater(() -> new WinReport(currentPane, playerWon));
            board.setGameStoppage(true);
        }
    }
    public void drawMyCards(Player player){
        if(board.getGameStoppage())return;
        this.player = player;
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        VBox pane = new VBox(10);
        Text text = new Text("resource cards:");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        pane.getChildren().add(text);
        String[] playerTitles = new String[players.size()];
        for(int i=0; i<players.size(); i++){
            playerTitles[i] = "Player " + (i+1);
        }
        ObservableList<String> items = FXCollections.observableArrayList(playerTitles);
        ComboBox<String> cmb = new ComboBox<>();
        cmb.setItems(items);
        cmb.setValue(playerTitles[player.getPlayerNumber() - 1]);
        pane.getChildren().add(cmb);
        Map<String, Integer> numbers = new HashMap<>();
        Map<String, Boolean> isWrote = new HashMap<>();
        for(ResourceCard card: player.getMyCards()){
            if(!numbers.keySet().contains(card.getType().name())){
                numbers.put(card.getType().name(), 1);
                isWrote.put(card.getType().name(), false);
            }
            else
                numbers.put(card.getType().name(), numbers.get(card.getType().name()) + 1);
        }
        for(ResourceCard card: player.getMyCards()){
            if(!isWrote.get(card.getType().name())){
                Rectangle back = new Rectangle();
                back.setWidth(80);
                back.setHeight(30);
                back.setArcWidth(13);
                back.setArcHeight(13);
                back.setFill(card.getColor());
                Text resource = new Text(card.getType().name() + ": " + Integer.toString(numbers.get(card.getType().name())));
                resource.setFill(Color.WHITE);
                resource.setFont(Font.font("Roboto", FontWeight.NORMAL, 15));
                pane.getChildren().add(new StackPane(back, resource));
                isWrote.replace(card.getType().name(), true);
            }
        }
        cmb.setOnAction(e -> {
            this.player = players.get(items.indexOf(cmb.getValue()));
            pane.getChildren().remove(2, pane.getChildren().size());
            numbers.clear();
            isWrote.clear();
            for(ResourceCard card: this.player.getMyCards()){
                if(!numbers.keySet().contains(card.getType().name())){
                    numbers.put(card.getType().name(), 1);
                    isWrote.put(card.getType().name(), false);
                }
                else
                    numbers.put(card.getType().name(), numbers.get(card.getType().name()) + 1);
            }
            for(ResourceCard card: this.player.getMyCards()){
                if(!isWrote.get(card.getType().name())){
                    Rectangle back = new Rectangle();
                    back.setWidth(80);
                    back.setHeight(30);
                    back.setArcWidth(13);
                    back.setArcHeight(13);
                    back.setFill(card.getColor());
                    Text resource = new Text(card.getType().name() + ": " + Integer.toString(numbers.get(card.getType().name())));
                    resource.setFill(Color.WHITE);
                    resource.setFont(Font.font("Roboto", FontWeight.NORMAL, 15));
                    pane.getChildren().add(new StackPane(back, resource));
                    isWrote.replace(card.getType().name(), true);
                }
            }
        });
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-border-color: turquoise; -fx-border-width: 3");
        pane.setPrefHeight(325);
        pane.setPrefWidth(100);
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        this.getChildren().add(1, pane);
    }
    public int getPlayerWon(){
        return playerWon;
    }
}
