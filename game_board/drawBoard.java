package game_board;
import javafx.animation.FadeTransition;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.util.Duration;
import util.Player;

public class DrawBoard {
    private LeftSide left;
    private TopSide top;
    private Map map;
    private FadeTransition fadeTransition = new FadeTransition();
    private BorderPane currentPane; 
    public DrawBoard(BorderPane currentPane, ArrayList<Player> players){
        this.currentPane = currentPane;
        left = new LeftSide(players);
        currentPane.setLeft(left);
        top = new TopSide(currentPane);
        currentPane.setTop(top);
        map = new Map(players.size(), players, this, 5);
        currentPane.setCenter(map);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setDuration(Duration.millis(6000));
        fadeTransition.setNode(currentPane);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }
    public LeftSide getLeftSide(){
        return left;
    }
    public TopSide getTopSide(){
        return top;
    }
    public Map getMap(){
        return map;
    }
    public BorderPane getCurrentPane() {
        return currentPane;
    }
}
