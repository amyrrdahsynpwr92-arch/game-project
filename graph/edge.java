package graph;
import java.util.ArrayList;

import card.Capital;
import card.Patent;
import card.ResourceCard;
import cards.*;
import graph.*;
import game_board.DrawBoard;
import util.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import set_undo_and_redo.UndoAction;
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
    private int edgeNumber;
    private Integer maxDistance = 0;
    private MediaPlayer errorSound;
    private Handle_TurningGame handle_TurningGame;
    private DrawBoard board;
    private UndoAction undoAction;
    private boolean giveScore = false;

    public Edge(Node start, Node end, Handle_PreGame handle_PreGame, Handle_TurningGame handle_TurningGame, int edgeNumber, DrawBoard board, UndoAction undoAction){
        this.handle_preGame = handle_PreGame;
        this.handle_TurningGame = handle_TurningGame;
        this.start = start;
        this.end = end;
        errorSound = start.getErrorSound();
        this.edgeNumber = edgeNumber;
        this.setStrokeWidth(6);
        this.setStroke(Color.WHITE);
        this.board = board;
        this.undoAction = undoAction;
        this.setOnMouseClicked(e -> drawPartnership());
    }
    public void drawPartnership(){
        if(board.getGameStoppage() || board.getDownSide().getMoveAuditor() || handle_TurningGame.getCurrentPlayer().getOnTradeRequest() > 0)return;
        System.out.println(this);
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
                        undoAction.setNumberOfMoves(0);
                        undoAction.getActions().clear();
                    }
                });
                undoAction.addStage(this, currentPlayer);
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
                undoAction.addStage(this, currentPlayer);
                if(board.getMap().getLongestPath().bfs(this)){
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
        if(this.hasPartnership)return false;
        Color playerColor = currentPlayer.getColor();
        if((start.HasMVP() && playerColor == (start.getNodeMVP().GetShape()).getFill()) || (end.HasMVP() && playerColor == (end.getNodeMVP().GetShape()).getFill())){
            if(handle_TurningGame.isTurning_Game()){
                start.addPartnership(this);
                end.addPartnership(this);
                return true;   
            }else if(handle_preGame.getTurn() == 2){
                start.addPartnership(this);
                end.addPartnership(this);
                return true;
            }
        }
        for(Edge edge: start.getLinkedPartnerships()){
            if(edge.getStroke() == Color.RED)
                System.out.println("RED");
            else if(edge.getStroke() == Color.BLUE)
                System.out.println("BLUE");
            if(edge.getStroke() == Color.GREEN)
                System.out.println("GREEN");
            if(edge.getStroke() == Color.PURPLE)
                System.out.println("PURPLE");
            if(edge.getStroke() == playerColor){
                if(handle_TurningGame.isTurning_Game()){
                    start.addPartnership(this);
                    end.addPartnership(this);
                    return true;
                }
                else if(handle_preGame.getTurn() == 2){
                    start.addPartnership(this);
                    end.addPartnership(this);
                    return true;
                }
            }
        }
        for(Edge edge: end.getLinkedPartnerships()){
            if(edge.getStroke() == Color.RED)
                System.out.println("RED");
            else if(edge.getStroke() == Color.BLUE)
                System.out.println("BLUE");
            if(edge.getStroke() == Color.GREEN)
                System.out.println("GREEN");
            if(edge.getStroke() == Color.PURPLE)
                System.out.println("PURPLE");
            if(edge.getStroke() == playerColor){
                if(handle_TurningGame.isTurning_Game()){
                    start.addPartnership(this);
                    end.addPartnership(this);
                    return true;
                }
                else if(handle_preGame.getTurn() == 2){
                    start.addPartnership(this);
                    end.addPartnership(this);
                    return true;
                }
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
        for(Edge edge: start.getLinkedPartnerships()){
            if(edge == this){
                start.getLinkedPartnerships().remove(edge);
                break;
            }
        }
        for(Edge edge: end.getLinkedPartnerships()){
            if(edge == this){
                end.getLinkedPartnerships().remove(edge);
                break;
            }
        }
        if(this.giveScore){
            player.setScore(player.getScore() - 2);
            this.giveScore = false;
            board.getMap().getLongestPath().setMaxDistance(board.getMap().getLongestPath().getMaxDistance() - 1);
        }
        if(handle_TurningGame.isTurning_Game()){
            ArrayList<ResourceCard> cards = currentPlayer.getMyCards();
            new Thread(() -> {
                cards.add(new Capital());
                cards.add(new Patent());
                if(handle_TurningGame.isTurning_Game())
                    Platform.runLater(() -> board.getLeftSide().drawMyCards(currentPlayer));
            }).start();
        }
        Platform.runLater(() -> {
            if(handle_preGame.isPreGame())board.getTopSide().drawStatusPanel("player " + Integer.toString(player.getPlayerNumber()) + "! please put a Partnership");
            if(handle_TurningGame.isTurning_Game())board.getLeftSide().drawPlayersScore();
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
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public Handle_PreGame getHandle_preGame() {
        return handle_preGame;
    }
    @Override
    public String toString(){
        return "{" + Integer.toString(start.getRow()) + ", " + Integer.toString(start.getCol()) +"} and {" + Integer.toString(end.getRow()) + ", " + Integer.toString(end.getCol()) + "}";
    }
}
