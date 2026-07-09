package set_undo_and_redo;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import graph.*;
import util.*;
import game_board.*;

public class RedoAction {
    Player player;
    int numberOfMoves = 0;
    ArrayList<Object> actions = new ArrayList<>();
    DrawBoard board;
    UndoAction undoAction; 
    private static final Media soundAddress = new Media(Node.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    public RedoAction(DrawBoard board, UndoAction undoAction){
        this.board = board;
        this.undoAction = undoAction;
    }
    public void loadNextStage(){
        if(board.getGameStoppage())return;
        if(numberOfMoves <= 0 || player == null || actions.size() == 0){
            errorSound.stop();
            errorSound.play();
            return;
        }
        if(actions.get(actions.size() - 1) instanceof Node){
            if(((Node)actions.get(actions.size() - 1)).HasMVP()){
                ((Node)actions.get(actions.size() - 1)).drawUnicorn();
            }else{
                ((Node)actions.get(actions.size() - 1)).drawMVP();
            }
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }else if(actions.get(actions.size() - 1) instanceof Edge){
            ((Edge)actions.get(actions.size() - 1)).drawPartnership();
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }else if(actions.get(actions.size() - 1) instanceof Sector){
            board.getDownSide().RePlaceAuditor((Sector)actions.get(actions.size() - 1));
            actions.remove(actions.size() - 1);
            numberOfMoves--;
        }
        // else if(actions.get(actions.size() - 1) instanceof Player){
        //     ((Player)actions.get(actions.size() - 1)).backToOldCards();
        //     actions.remove(actions.size() - 1);
        //     numberOfMoves--;
        // }
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
}
