package util;
import java.util.List;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;

public class Handle_PreGame implements Serializable {
    private boolean isPreGame = true;
    private transient Color currentColor = null;
    private int currentPlayer = 1;
    private enum Mode implements Serializable {Increment, Decrement, Constant};
    private Mode mode = Mode.Increment;
    private enum Turn implements Serializable {Node, Partnership};
    private Turn turn = Turn.Node;
    private int numberOfPlayers;
    private transient List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.PURPLE, Color.GREEN);
    private Handle_TurningGame handle_TurningGame;
    private ArrayList<Player> players;

    public Handle_PreGame(ArrayList<Player> players, Handle_TurningGame handle_TurningGame){
        this.players = players;
        this.numberOfPlayers = players.size();
        this.handle_TurningGame = handle_TurningGame;
    }
    public void NotifyMVP() {
        if(colors == null)
            colors = Arrays.asList(Color.RED, Color.BLUE, Color.PURPLE, Color.GREEN);
        currentColor = colors.get(currentPlayer-1);
    }
    public void NotifyPartnership() {
        if(colors == null)
            colors = Arrays.asList(Color.RED, Color.BLUE, Color.PURPLE, Color.GREEN);
        currentColor = colors.get(currentPlayer-1);
        if(currentPlayer == numberOfPlayers && mode != Mode.Decrement)mode = Mode.Constant;
        if(mode == Mode.Increment){
            currentPlayer++;
        }else if(mode == Mode.Constant){
            mode = Mode.Decrement; // for next round
        }else{
            currentPlayer--;
            if(currentPlayer == 0){
                isPreGame = false;
                handle_TurningGame.setTurning_Game(true);
            }
        }
    }
    public void NotifyBack() {
        if(mode == Mode.Increment){
            currentPlayer--;
        }else if(mode == Mode.Decrement){
            if(currentPlayer == players.size()){
                mode = Mode.Constant;
            }
            else{
                currentPlayer++;
            }
        }
        currentColor = colors.get(currentPlayer-1);
    }
    public Color isCurrentColor() {
        return currentColor;
    }
    public boolean isPreGame() {
        return isPreGame;
    }
    public Color getCurrentColor() {
        return currentColor;
    }
    public void setCurrentColor() {
        switch(currentPlayer){
            case 1:
                currentColor = Color.RED;
                break;
            case 2:
                currentColor = Color.BLUE;
                break;
            case 3:
                currentColor = Color.PURPLE;
                break;
            case 4:
                currentColor = Color.GREEN;
                break;
        }
    }
    public Player getCurrentPlayer(){
        if(currentPlayer == 0)return players.get(0);
        else return players.get(currentPlayer-1);
    }
    public void setTurn(int n){
        if(n == 1)turn = Turn.Node;
        else turn = Turn.Partnership;
    }
    public int getTurn(){
        if(turn == Turn.Node)return 1;
        else return 2;
    }
}
