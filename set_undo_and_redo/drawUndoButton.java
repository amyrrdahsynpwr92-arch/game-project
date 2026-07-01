package set_undo_and_redo;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import graph.*;
import util.*;

public class DrawUndoButton {
    Player player;
    int numberOfMoves = 0;
    ArrayList<Object> objects = new ArrayList<>();
    private static final Media soundAddress = new Media(Node.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    public void loadPrevoiusStage(){
        if(numberOfMoves <= 0 || player == null || objects.size() == 0){
            errorSound.stop();
            errorSound.play();
            return;
        }
        if(objects.get(objects.size() - 1) instanceof Node){
            if(((Node)objects.get(objects.size() - 1)).HasUnicorn()){
                ((Node)objects.get(objects.size() - 1)).deleteUnicorn(player);
            }else{
                ((Node)objects.get(objects.size() - 1)).deleteMVP(player);
            }
            objects.remove(objects.size() - 1);
            numberOfMoves--;
        }else if(objects.get(objects.size() - 1) instanceof Edge){
            ((Edge)objects.get(objects.size() - 1)).deletePartnership(player);
            objects.remove(objects.size() - 1);
            numberOfMoves--;
        }
    }
    public void addStage(Object O, Player player){
        if(this.player != player || this.player == null){
            this.player = player;
            objects.clear();
            numberOfMoves = 0;
        }
        objects.add(O);
        numberOfMoves++;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
}
