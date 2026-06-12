package util;
import java.util.List;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Handle_PreGame {
    private boolean isPreGame = true;
    private Color currentColor = null;
    private  int currentPlayer = 0;
    private enum Mode{Increment, Decrement, Constant};
    private Mode mode = Mode.Increment;
    private enum Turn{Node, Partnership};
    private Turn turn = Turn.Node;
    private int numberOfPlayers;
    private List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.PURPLE, Color.GREEN);
    private Handle_TurningGame handle_TurningGame;
    private ArrayList<Player> players;

    public Handle_PreGame(ArrayList<Player> players, Handle_TurningGame handle_TurningGame){
        this.players = players;
        this.numberOfPlayers = players.size();
        this.handle_TurningGame = handle_TurningGame;
    }
    public void NotifyMVP() {
        currentColor = colors.get(currentPlayer);
    }
    public void NotifyPartnership() {
        currentColor = colors.get(currentPlayer);
        if(currentPlayer == numberOfPlayers-1 && mode != Mode.Decrement)mode = Mode.Constant;
        if(mode == Mode.Increment){
            currentPlayer++;
        }else if(mode == Mode.Constant){
            mode = Mode.Decrement; // for next round
        }else{
            currentPlayer--;
            if(currentPlayer == -1){
                isPreGame = false;
                handle_TurningGame.setTurning_Game(true);
            }
        }
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
    public Player getCurrentPlayer(){
        return players.get(currentPlayer);
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
