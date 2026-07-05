package graph;
import java.util.ArrayList;
import graph.*;
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
    private Line outLine;
    private boolean hasPartnership;
    private Node start;
    private Node end;
    private int numberOfPlayers;
    private Handle_PreGame handle_preGame;
    private Player currentPlayer;
    private ArrayList<Edge> edges = new ArrayList<>();
    private int edgeNumber;
    private Integer maxDistance = 0;
    private MediaPlayer errorSound;
    private Handle_TurningGame handle_TurningGame;
    private DrawBoard board;
    private DrawUndoButton undoButton;
    private boolean giveScore = false;
    public Edge(Node start, Node end, Handle_PreGame handle_PreGame, Handle_TurningGame handle_TurningGame, int edgeNumber, DrawBoard board, DrawUndoButton undoButton){
        this.handle_preGame = handle_PreGame;
        this.handle_TurningGame = handle_TurningGame;
        this.start = start;
        this.end = end;
        errorSound = start.getErrorSound();
        this.edgeNumber = edgeNumber;
        this.setStrokeWidth(6);
        this.setStroke(Color.WHITE);
        this.board = board;
        this.undoButton = undoButton;
        this.setOnMouseClicked(e -> drawPartnership());
    }
    public void drawPartnership(){
        if(board.getGameStoppage() || board.getDownSide().getMoveAuditor() || board.getDownSide().getOnTrade() == handle_TurningGame.getCurrentPlayer().getPlayerNumber())return;
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
                    else{
                        board.getTopSide().drawStatusPanel("player 1! this is your turn, roll dices");
                        undoButton.setNumberOfMoves(0);
                        undoButton.getObjects().clear();
                    }
                });
                undoButton.addStage(this, currentPlayer);
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }
        else if(handle_TurningGame.isTurning_Game() && board.getRightSide().getDicesRolled()){
            currentPlayer = handle_TurningGame.getCurrentPlayer();
            cards = currentPlayer.getMyCards();
            for(ResourceCard card: cards){
                if(card instanceof Capital )
                    capital = (Capital)card;
                else if(card instanceof Patent)
                    patent = (Patent)card;
                if(capital != null && patent != null){
                    break;
                }
            }
            if(capital != null && patent != null && validate()){
                this.setStroke(currentPlayer.getColor());
                this.setStrokeWidth(10);
                cards.remove(capital);
                cards.remove(patent);
                Platform.runLater(() -> board.getLeftSide().drawMyCards(currentPlayer)); 
                currentPlayer.setMyCards(cards);
                hasPartnership = true;
                undoButton.addStage(this, currentPlayer);
                if(new LongestPath(edges, this, maxDistance).bfs()){
                    currentPlayer.setScore(currentPlayer.getScore() + 2);
                    this.giveScore = true;
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
        if((start.HasMVP() && playerColor == (start.getNodeMVP().GetShape()).getFill()) || (end.HasMVP() && playerColor == (end.getNodeMVP().GetShape()).getFill())){
            if(handle_TurningGame.isTurning_Game()){
                start.addPartnership(playerColor);
                end.addPartnership(playerColor);
                return true;   
            }else if(handle_preGame.getTurn() == 2){
                start.addPartnership(playerColor);
                end.addPartnership(playerColor);
                return true;
            }
        }
        for(Color color: start.getLinkedPartnerships()){
            if(color == playerColor){
                if(handle_TurningGame.isTurning_Game())
                    return true;
                else if(handle_preGame.getTurn() == 2)
                    return true;
            }
        }
        for(Color color: end.getLinkedPartnerships()){
            if(color == playerColor){
                if(handle_TurningGame.isTurning_Game())
                    return true;
                else if(handle_preGame.getTurn() == 2)
                    return true;
            }
        }
        return false;
    }
    public void deletePartnership(Player player){
        this.setStrokeWidth(6);
        this.setStroke(Color.WHITE);
        this.hasPartnership = false;
        handle_preGame.setTurn(2);
        handle_preGame.NotifyBack();
        for(Color color: start.getLinkedPartnerships()){
            if(color == player.getColor()){
                start.getLinkedPartnerships().remove(color);
                break;
            }
        }
        for(Color color: end.getLinkedPartnerships()){
            if(color == player.getColor()){
                end.getLinkedPartnerships().remove(color);
                break;
            }
        }
        if(this.giveScore){
            player.setScore(player.getScore() - 2);
            this.giveScore = false;
        }
        if(handle_TurningGame.isTurning_Game()){
            ArrayList<ResourceCard> cards = currentPlayer.getMyCards();
            cards.add(new Capital());
            cards.add(new Patent());
        }
        Platform.runLater(() -> {
            if(handle_preGame.isPreGame())board.getTopSide().drawStatusPanel("player " + Integer.toString(player.getPlayerNumber()) + "! please put a Partnership");
            if(handle_TurningGame.isTurning_Game())board.getLeftSide().drawMyCards(currentPlayer);
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
