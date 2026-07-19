package set_undo_and_redo;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import graph.*;
import util.*;
import game_board.*;

public class UndoAction {
    Player player;
    int numberOfMoves = 0;
    ArrayList<Object> actions = new ArrayList<>();
    DrawBoard board;
    RedoAction redoAction;
    private static final Media soundAddress = new Media(Node.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    
    public UndoAction(DrawBoard board){
        this.board = board;
    }
    public void loadPrevoiusStage(){
        if(board.getGameStoppage() || (board.getLoadClass() != null && board.getLoadClass().getGameStoppage() != -1))return;
        if(numberOfMoves <= 0 || player == null || actions.size() == 0){
            errorSound.stop();
            errorSound.play();
            return;
        }
        if(actions.get(actions.size() - 1) instanceof Node){
            if(((Node)actions.get(actions.size() - 1)).HasUnicorn()){
                ((Node)actions.get(actions.size() - 1)).deleteUnicorn(player);
                redoAction.addStage((Node)actions.get(actions.size() - 1), player);
            }else{
                ((Node)actions.get(actions.size() - 1)).deleteMVP(player);
                redoAction.addStage((Node)actions.get(actions.size() - 1), player);
            }
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }else if(actions.get(actions.size() - 1) instanceof Edge){
            ((Edge)actions.get(actions.size() - 1)).deletePartnership(player);
            redoAction.addStage((Edge)actions.get(actions.size() - 1), player);
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }else if(actions.get(actions.size() - 1) instanceof Sector){
            redoAction.addStage((Sector)actions.get(actions.size() - 1), player);
            board.getDownSide().deleteAuditor((Sector)actions.get(actions.size() - 1));
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }
    }
    public void addStage(Object O, Player player){
        if(this.player != player || this.player == null){
            this.player = player;
            actions.clear();
            numberOfMoves = 0;
        }
        actions.add(O);
        numberOfMoves++;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }
    public ArrayList<Object> getActions(){
        return actions;
    }
    public void setRedoAction(RedoAction redoAction){
        this.redoAction = redoAction;  
    }
}
