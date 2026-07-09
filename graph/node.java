package graph;
import java.util.ArrayList;

import card.Capital;
import card.Cloud;
import card.Data;
import card.Null;
import card.ResourceCard;
import card.Talent;
import cards.*;
import company.*;
import util.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.shape.Circle;
import type.PlayerRole;
import set_undo_and_redo.UndoAction;
import javafx.application.Platform;
import game_board.DrawBoard;

public class Node extends Pane{
    private Player currentPlayer;
    private Circle nodeShape;
    private int row;
    private int col;
    private MVP nodeMVP;
    public MVP getNodeMVP() {
        return nodeMVP;
    }
    private Unicorn nodeUnicorn;
    private boolean hasMVP;
    private boolean hasUnicorn;
    private boolean linkedToPartnership;
    private Handle_PreGame handle_preGame;
    private Node[][] nodes = new Node[5][5];
    private Handle_TurningGame handle_TurningGame;
    private static final Media soundAddress = new Media(Node.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    private ArrayList<Sector> sectors = new ArrayList<>();
    private ArrayList<Edge> linkedPartnerships = new ArrayList<>();
    private DrawBoard board;
    private UndoAction undoAction; 
    private int n;
    public Node(int row, int col, Handle_PreGame handle_preGame, Handle_TurningGame handle_TurningGame, ArrayList<Sector> sectors, DrawBoard board, UndoAction undoAction){
        this.handle_preGame = handle_preGame;
        this.handle_TurningGame = handle_TurningGame;
        this.row = row;
        this.col = col;
        this.sectors = sectors;
        this.n = board.getN();
        nodeShape = new Circle(5);
        nodeShape.setStyle("-fx-fill: black; -fx-stroke: white");
        nodeShape.setCenterX(0);
        nodeShape.setCenterY(0);
        this.board = board;
        this.getChildren().add(nodeShape);
        this.undoAction = undoAction;
        nodeShape.setOnMouseClicked(e -> drawMVP());
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void drawMVP(){
        if(board.getGameStoppage() || board.getDownSide().getMoveAuditor() || handle_TurningGame.getCurrentPlayer().getOnTradeRequest() > 0){
            errorSound.stop();
            errorSound.play();
            return;
        }
        nodeMVP = new MVP();
        Capital capital = null;
        Talent talent = null;
        Cloud cloud = null;
        Data data = null;
        ArrayList<ResourceCard> cards = new ArrayList<>();
        if(handle_preGame.isPreGame()){
            handle_preGame.NotifyMVP();
            currentPlayer = handle_preGame.getCurrentPlayer();
            if(validate(row, col)){
                cards = currentPlayer.getMyCards();
                nodeMVP.GetShape().setFill(handle_preGame.getCurrentColor());
                this.getChildren().clear();
                this.getChildren().add(nodeMVP);
                nodeMVP.setOnMouseClicked(e -> drawUnicorn());
                currentPlayer.setScore(currentPlayer.getScore() + 1);
                Platform.runLater(() -> { // UI update 
                    board.getLeftSide().drawPlayersScore();
                    board.getTopSide().drawPlayerBox(currentPlayer.getPlayerNumber());
                    board.getTopSide().drawStatusPanel("player " + Integer.toString(currentPlayer.getPlayerNumber()) + "! please put a Partnership");
                }); 
                this.hasMVP = true;
                handle_preGame.setTurn(2);
                undoAction.addStage(this, currentPlayer);
                for(Sector sector: sectors){
                    if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                        if(!(sector.getResource() instanceof Null || sector.HasAuditor())){
                            cards.add(sector.getResource());
                            sector.getMvpPlayers().add(currentPlayer);
                        }
                }
                Platform.runLater(() -> {
                    board.getRightSide().drawTotalCards();
                    board.getLeftSide().drawMyCards(currentPlayer);
                });
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }
        else if(handle_TurningGame.isTurning_Game() && board.getRightSide().getDicesRolled()){
            currentPlayer = handle_TurningGame.getCurrentPlayer();
            cards = currentPlayer.getMyCards();
            for(ResourceCard card: cards){
                if(card instanceof Capital)
                    capital = (Capital)card;
                else if(card instanceof Talent)
                    talent = (Talent)card;
                else if(card instanceof Cloud)
                    cloud = (Cloud)card;
                else if(card instanceof Data)
                    data = (Data)card;
                if(capital != null && talent != null && cloud != null && data != null)
                    break;
            }
            if((capital != null && talent != null && cloud != null && data != null) && validate(row, col)){
                this.getChildren().clear();
                this.getChildren().add(nodeMVP);
                nodeMVP.setOnMouseClicked(e -> drawUnicorn());
                this.hasMVP = true;  
                cards.remove(capital);
                cards.remove(talent);
                cards.remove(cloud);
                cards.remove(data);
                currentPlayer.setScore(currentPlayer.getScore() + 1);
                Platform.runLater(() -> board.getLeftSide().drawPlayersScore()); // UI update
                nodeMVP.GetShape().setFill(currentPlayer.getColor());
                undoAction.addStage(this, currentPlayer);
                for(Sector sector: sectors){
                    if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                        if(!(sector.getResource() instanceof Null)){
                            sector.getMvpPlayers().add(currentPlayer);
                        }
                }
                Platform.runLater(() -> {
                    board.getRightSide().drawTotalCards();
                    board.getLeftSide().drawMyCards(currentPlayer);
                });
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }else{
            errorSound.stop(); // it may is playing already
            errorSound.play();
        }
    }
    public void drawUnicorn(){
        if(board.getGameStoppage() || board.getDownSide().getMoveAuditor() || handle_TurningGame.getCurrentPlayer().getOnTradeRequest() > 0 || this.nodeMVP.GetShape().getFill() != handle_TurningGame.getCurrentPlayer().getColor()){
            errorSound.stop();
            errorSound.play();
            return;
        }
        nodeUnicorn = new Unicorn();
        Cloud cloud1 = null;
        Cloud cloud2 = null;
        Data data1 = null;
        Data data2 = null;
        Data data3 = null;
        ArrayList<ResourceCard> cards = null;
        if(handle_TurningGame.isTurning_Game()){
            currentPlayer = handle_TurningGame.getCurrentPlayer();
            cards = currentPlayer.getMyCards();
            for(ResourceCard card: cards){
                if(card instanceof Cloud){
                    if(cloud1 == null)
                        cloud1 = (Cloud)card;
                    else if(cloud2 == null)
                        cloud2 = (Cloud)card;
                }
                else if(card instanceof Data){
                    if(data1 == null)
                        data1 = (Data)card;
                    else if(data2 == null)
                        data2 = (Data)card;
                    else if(data3 == null)
                        data3 = (Data)card;
                }
            }
            if(currentPlayer.getRole() == PlayerRole.The_Teck_GURU){
                if(cloud1 != null && data1 != null && data2 != null && data3 != null){
                    nodeUnicorn = new Unicorn();
                    this.getChildren().clear();
                    this.getChildren().add(nodeUnicorn);
                    nodeUnicorn.GetShape().setFill(currentPlayer.getColor());
                    this.hasUnicorn = true;
                    undoAction.addStage(this, currentPlayer);
                    cards.remove(cloud1);
                    cards.remove(data1);
                    cards.remove(data2);
                    cards.remove(data3);
                    currentPlayer.setMyCards(cards);
                    currentPlayer.setScore(currentPlayer.getScore() + 2);
                    for(Sector sector: sectors){
                        if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                            if(!(sector.getResource() instanceof Null)){
                                sector.getUnicornPlayers().add(currentPlayer);
                            }
                    }
                    Platform.runLater(() -> {
                        board.getLeftSide().drawPlayersScore();
                        board.getLeftSide().drawMyCards(currentPlayer);
                    }); // UI update
                }else{
                    errorSound.stop(); // it may is playing already
                    errorSound.play();
                }
            }else{
                if(cloud1 != null && cloud2 != null && data1 != null && data2 != null && data3 != null){
                    nodeUnicorn = new Unicorn();
                    this.getChildren().clear();
                    this.getChildren().add(nodeUnicorn);
                    nodeUnicorn.GetShape().setFill(currentPlayer.getColor());
                    this.hasUnicorn = true;
                    undoAction.addStage(this, currentPlayer);
                    cards.remove(cloud1);
                    cards.remove(cloud2);
                    cards.remove(data1);
                    cards.remove(data2);
                    cards.remove(data3);
                    currentPlayer.setMyCards(cards);
                    currentPlayer.setScore(currentPlayer.getScore() + 2);
                    for(Sector sector: sectors){
                        if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                            if(!(sector.getResource() instanceof Null)){
                                sector.getUnicornPlayers().add(currentPlayer);
                            }
                    }
                    Platform.runLater(() -> {
                        board.getLeftSide().drawPlayersScore();
                        board.getLeftSide().drawMyCards(currentPlayer);
                    });
                }else{
                    errorSound.stop(); // it may is playing already
                    errorSound.play();
                }
            }
        }
    }
    public boolean validate(int row, int col){
        if((col>0 && nodes[col-1][row].hasMVP) || (col<n-1 && nodes[col+1][row].hasMVP) || (row>0 && nodes[col][row-1].hasMVP) || (row<n-1 && nodes[col][row+1].hasMVP))
            return false;
        if(handle_preGame.isPreGame() && handle_preGame.getTurn() == 2)
            return false;
        return true;
    }
    public void deleteMVP(Player player){
        this.getChildren().clear();
        this.getChildren().add(nodeShape);
        player.setScore(player.getScore() - 1);
        ArrayList<ResourceCard> cards = player.getMyCards();
        if(handle_preGame.isPreGame()){
            for(Sector sector: sectors){
                if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                    if(!(sector.getResource() instanceof Null)){
                        currentPlayer.getMyCards().remove(sector.getResource());
                    }
            }    
            new Thread(() -> {
                Platform.runLater(() -> board.getLeftSide().drawMyCards(player));
            }).start();
        }
        for(Sector sector: sectors){
            if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                if(!(sector.getResource() instanceof Null))sector.getMvpPlayers().remove(currentPlayer);
        }
        if(handle_TurningGame.isTurning_Game()){
            new Thread(() -> {
                cards.add(new Capital());
                cards.add(new Talent());
                cards.add(new Cloud());
                cards.add(new Data());
                Platform.runLater(() -> board.getLeftSide().drawMyCards(player));
            }).start();
        }
        this.hasMVP = false;
        handle_preGame.setTurn(1);
        Platform.runLater(() -> {
            board.getLeftSide().drawPlayersScore();
            if(handle_preGame.isPreGame())board.getTopSide().drawStatusPanel("player " + Integer.toString(player.getPlayerNumber()) + "! please put a MVP");
            board.getRightSide().drawTotalCards();
        });
    }
    public void deleteUnicorn(Player player){
        this.getChildren().clear();
        this.getChildren().add(nodeMVP);
        player.setScore(player.getScore() - 2);
        ArrayList<ResourceCard> cards = player.getMyCards();
        for(Sector sector: sectors){
            if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                if(!(sector.getResource() instanceof Null)){
                    sector.getUnicornPlayers().remove(currentPlayer);
                }
        }
        if(handle_TurningGame.isTurning_Game()){
            if(currentPlayer.getRole() == PlayerRole.The_Teck_GURU){
                new Thread(() -> {
                    cards.add(new Cloud());
                    cards.add(new Data());
                    cards.add(new Data());
                    cards.add(new Data());
                    Platform.runLater(() -> board.getLeftSide().drawMyCards(player));
                }).start();
            }else{
                new Thread(() -> {
                    cards.add(new Cloud());
                    cards.add(new Cloud());
                    cards.add(new Data());
                    cards.add(new Data());
                    cards.add(new Data());
                    Platform.runLater(() -> board.getLeftSide().drawMyCards(player));
                }).start();
            }
        }
        this.hasUnicorn = false;
        Platform.runLater(() -> {
            board.getLeftSide().drawPlayersScore();
            board.getRightSide().drawTotalCards();
        });
    }
    public Circle getNodeShape() {
        return nodeShape;
    }
    public boolean getLinkedToPartnership() {
        return linkedToPartnership;
    }
    public void setLinkedToPartnership(boolean linkedToPartnership) {
        this.linkedToPartnership = linkedToPartnership;
    }
    @Override
    public boolean equals(Object o){
        Node node = (Node)o;
        return this.row == node.row && this.col == node.col;
    }
    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public boolean HasMVP() {
        return hasMVP;
    }
    public void addPartnership(Edge e){
        linkedPartnerships.add(e);
    }
    public ArrayList<Edge> getLinkedPartnerships(){
        return linkedPartnerships;
    }
    public boolean HasUnicorn() {
        return hasUnicorn;
    }

    public MediaPlayer getErrorSound() {
        return errorSound;
    }
}

