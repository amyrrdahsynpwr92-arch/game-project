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
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import util.Player;
import card.*;

public class leftSide extends VBox {
    private ArrayList<Player> players;
    private VBox myCards;
    public leftSide(ArrayList<Player> players){
        this.players = players;
        this.setBackground(new Background(new BackgroundFill(Color.color(0.0, 0.0, 0.0, 0.92),CornerRadii.EMPTY,Insets.EMPTY)));
        drawPlayersScore();
        drawMyCards(players.get(0));
    }
    public void drawPlayersScore(){
        if(!this.getChildren().isEmpty())this.getChildren().clear();
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
            score.setFill(Color.WHITE);
            score.setFont(Font.font("Roboto", FontWeight.NORMAL, 15));
            pane.getChildren().add(new StackPane(back, score));
        }
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-border-color: turquoise; -fx-border-width: 3");
        pane.setPrefHeight(325);
        pane.setPrefWidth(100);
        this.getChildren().add(pane);
        if(myCards != null)this.getChildren().add(myCards);
    }
    public void drawMyCards(Player player){
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        VBox pane = new VBox(10);
        Text text = new Text("my cards:");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        pane.getChildren().add(text);
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
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-border-color: turquoise; -fx-border-width: 3");
        pane.setPrefHeight(325);
        pane.setPrefWidth(100);
        myCards = pane;
        this.getChildren().add(pane);
    }
}
