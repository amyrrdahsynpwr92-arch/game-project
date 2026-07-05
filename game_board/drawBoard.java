package game_board;
import javafx.animation.FadeTransition;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.util.Duration;
import game_board.*;
import util.Player;

public class DrawBoard {
    private LeftSide left;
    private TopSide top;
    private Map map;
    private RightSide right;
    private DownSide down;
    private FadeTransition fadeTransition = new FadeTransition();
    private BorderPane currentPane; 
    private boolean gameStoppage = false;
    private int n;
    public DrawBoard(BorderPane currentPane, ArrayList<Player> players, int n){
        this.currentPane = currentPane;
        this.n = n;
        left = new LeftSide(players, this);
        currentPane.setLeft(left);
        top = new TopSide(currentPane, this);
        currentPane.setTop(top);
        map = new Map(players.size(), players, this, n);
        currentPane.setCenter(map);
        right = new RightSide(this);
        currentPane.setRight(right);
        down = new DownSide(map.getHandle_TurningGame(), this);
        currentPane.setBottom(down);
        this.currentPane.getChildren().add(down.getAuditor());
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
    public RightSide getRightSide(){
        return right;
    }
    public DownSide getDownSide(){
        return down;
    }
    public BorderPane getCurrentPane() {
        return currentPane;
    }
    public void setGameStoppage(boolean gameStoppage){
        this.gameStoppage = gameStoppage;
    }
    public boolean getGameStoppage(){
        return gameStoppage;
    }

    public int getN() {
        return n;
    }
}
