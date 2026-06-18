package game_board;
import javafx.animation.FadeTransition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import util.Player;

public class drawBoard {
    private leftSide left;
    private Map map;
    private FadeTransition fadeTransition = new FadeTransition();
    private BorderPane currentPane; 
    public drawBoard(BorderPane currentPane, ArrayList<Player> players){
        this.currentPane = currentPane;
        left = new leftSide(players);
        currentPane.setLeft(left);
        map = new Map(players.size(), players, this);
        currentPane.setCenter(map);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setDuration(Duration.millis(6000));
        fadeTransition.setNode(currentPane);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }
    public leftSide getLeftSide(){
        return left;
    }
    public Map getMap(){
        return map;
    }
    public BorderPane getCurrentPane() {
        return currentPane;
    }
}
