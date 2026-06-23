package graph;
import java.util.ArrayList;
import card.*;
import game_board.DrawBoard;
import util.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import set_undo_and_redo.DrawUndoButton;
import javafx.application.Platform;

public class Edge extends Line {
    private Line edgeShape;
    private boolean hasPartnership;
    private Node start;
    private Node end;
    private int numberOfPlayers;
    private Handle_PreGame handle_preGame;
    private Player currentPlayer;
    private ArrayList<Edge> edges = new ArrayList<>();
    private int edgeNumber;
    private Integer maxDistance = 0;
    private static final Media soundAddress = new Media(Edge.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    private Handle_TurningGame handle_TurningGame;
    private DrawBoard board;
    private DrawUndoButton undoButton;
    public Edge(Node start, Node end, Handle_PreGame handle_PreGame, Handle_TurningGame handle_TurningGame, int edgeNumber, DrawBoard board, DrawUndoButton undoButton){
        this.handle_preGame = handle_PreGame;
        this.handle_TurningGame = handle_TurningGame;
        this.start = start;
        this.end = end;
        this.edgeNumber = edgeNumber;
        this.setStrokeWidth(6);
        this.setStroke(Color.WHITE);
        this.board = board;
        this.undoButton = undoButton;
        this.setOnMouseClicked(e -> drawPartnership());
    }
    public void drawPartnership(){
        Capital capital = null;
        Patent patent = null;
        ArrayList<ResourceCard> cards = new ArrayList<>();
        if(handle_preGame.isPreGame()){
            currentPlayer = handle_preGame.getCurrentPlayer();
            if(validate()){
                handle_preGame.NotifyPartnership();
                this.setStroke(handle_preGame.getCurrentColor());
                this.setStrokeWidth(10);
                hasPartnership = true;
                handle_preGame.setTurn(1);
                Platform.runLater(() -> {
                    if(handle_preGame.isPreGame())
                        board.getTopSide().drawStatusPanel("player " + Integer.toString(handle_preGame.getCurrentPlayer().getPlayerNumber()) + "! please put a MVP");
                    else
                        board.getTopSide().drawStatusPanel("player 1! this is your turn");
                });
                undoButton.addStage(this, currentPlayer);
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }
        else if(handle_TurningGame.isTurning_Game()){
            handle_TurningGame.Notify();
            currentPlayer = handle_TurningGame.getCurrentPlayer();
            cards = currentPlayer.getMyCards();
            for(ResourceCard card: cards){
                if(card instanceof Capital)
                    capital = (Capital)card;
                else if(card instanceof Patent)
                    patent = (Patent)card;
                if(capital != null && patent != null)
                    break;
            }
            this.setStroke(currentPlayer.getColor());
            if((capital != null && patent != null)){
                this.setStroke(Color.RED);
                this.setStrokeWidth(10);
                if(capital != null)cards.remove(capital);
                cards.remove(patent);
                currentPlayer.setMyCards(cards);
                hasPartnership = true;
                if(new LongestPath(edges, this, maxDistance).bfs()){
                    currentPlayer.setScore(currentPlayer.getScore() + 2);
                    Platform.runLater(() -> board.getLeftSide().drawPlayersScore()); // UI update
                }
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }
    }
    private boolean validate(){
        Color playerColor = currentPlayer.getColor();
        if((start.getHasMVP() && playerColor == (start.getNodeMVP().GetShape()).getFill()) || (end.getHasMVP() && playerColor == (end.getNodeMVP().GetShape()).getFill()) && handle_preGame.getTurn() == 2){
            start.addPartnership(playerColor);
            end.addPartnership(playerColor);
            return true;   
        }
        for(Color color: start.getLinkedPartnerships()){
            if(color == playerColor && handle_preGame.getTurn() == 2)
                return true;
        }
        for(Color color: end.getLinkedPartnerships()){
            if(color == playerColor && handle_preGame.getTurn() == 2)
                return true;
        }
        return false;
    }
    public void deletePartnership(Player player){
        this.setStrokeWidth(6);
        this.setStroke(Color.WHITE);
        this.hasPartnership = false;
        handle_preGame.setTurn(2);
        handle_preGame.NotifyBack();
        Platform.runLater(() -> {
            board.getTopSide().drawStatusPanel("player " + Integer.toString(player.getPlayerNumber()) + "! please put a Partnership");
        });
    }
    public boolean getHasPartnership() {
        return hasPartnership;
    }
    public Node getStart() {
        return start;
    }
    public Node getEnd() {
        return end;
    }
    public Line getEdgeShape() {
        return edgeShape;
    }
    public int getEdgeNumber() {
        return edgeNumber;
    }
    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public Handle_PreGame getHandle_preGame() {
        return handle_preGame;
    }
    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
