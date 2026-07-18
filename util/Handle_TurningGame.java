package util;
import java.io.Serializable;
import java.util.ArrayList;

public class Handle_TurningGame implements Serializable {
    private int numberOfPlayers;
    private int currentPlayer = 1;
    private ArrayList<Player> players;
    private boolean isTurning_Game = false;

    public Handle_TurningGame(ArrayList<Player> players){
        this.numberOfPlayers = players.size();
        this.players = players;
    }
    public void Notify(){
        currentPlayer = (currentPlayer % numberOfPlayers) + 1;
    }
    public Player getCurrentPlayer() {
        return players.get(currentPlayer-1);
    }
    public void setTurning_Game(boolean isTurning_Game) {
        this.isTurning_Game = isTurning_Game;
    }
    public boolean isTurning_Game() {
        return isTurning_Game;
    }
}
