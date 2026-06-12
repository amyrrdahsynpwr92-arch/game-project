package graph;
import java.util.ArrayList;
import company.*;
import card.*;
import util.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

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
    private boolean linkedToPartnership;
    private int numberOfPlayers;
    private Handle_PreGame handle_preGame;
    private Node[][] nodes = new Node[5][5];
    private Handle_TurningGame handle_TurningGame;
    private static final Media soundAddress = new Media(Node.class.getResource("/voices/error.mp3").toExternalForm());
    private MediaPlayer errorSound = new MediaPlayer(soundAddress);
    private ArrayList<Sector> sectors = new ArrayList<>();
    private ArrayList<Color> linkedPartnerships = new ArrayList<>();
    public Node(int row, int col, Handle_PreGame handle_preGame, Handle_TurningGame handle_TurningGame, ArrayList<Sector> sectors){
        this.handle_preGame = handle_preGame;
        this.handle_TurningGame = handle_TurningGame;
        this.row = row;
        this.col = col;
        this.sectors = sectors;
        nodeShape = new Circle(5);
        nodeShape.setStyle("-fx-fill: black; -fx-stroke: white");
        nodeShape.setCenterX(0);
        nodeShape.setCenterY(0);
        this.getChildren().add(nodeShape);
        nodeShape.setOnMouseClicked(e -> drawMVP(row, col));
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void drawMVP(int row, int col){
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
                this.hasMVP = true;
                handle_preGame.setTurn(2);
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
                nodeMVP.GetShape().setFill(currentPlayer.getColor());
            }else{
                errorSound.stop(); // it may is playing already
                errorSound.play();
            }
        }
        for(Sector sector: sectors){
            if((sector.getRow() == this.row || sector.getRow() == this.row-1) && (sector.getCol() == this.col || sector.getCol() == this.col-1))
                cards.add(sector.getResource());
        }
        
    }
    public void drawUnicorn(){
        nodeUnicorn = new Unicorn();
        Cloud cloud1 = null;
        Cloud cloud2 = null;
        Data data1 = null;
        Data data2 = null;
        Data data3 = null;
        ArrayList<ResourceCard> cards = null;
        if(handle_TurningGame.isTurning_Game()){
            handle_TurningGame.Notify();
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
            if(cloud1 != null && cloud2 != null && data1 != null && data2 != null && data3 != null)
                nodeUnicorn.GetShape().setFill(currentPlayer.getColor());
            else{
                // errorSound.stop(); // it may is playing already
                // errorSound.play();
            }
        }
        if(cloud1 != null && cloud2 != null && data1 != null && data2 != null && data3 != null){
            nodeUnicorn = new Unicorn();
            this.getChildren().clear();
            this.getChildren().add(nodeUnicorn);
            if(cloud1 != null)cards.remove(cloud1);
            if(cloud2 != null)cards.remove(cloud2);
            if(data1 != null)cards.remove(data1);
            if(data2 != null)cards.remove(data2);
            if(data3 != null)cards.remove(data3);
            if(cards != null)currentPlayer.setMyCards(cards);
            currentPlayer.setScore(currentPlayer.getScore() + 2);
        }
    }
    public boolean validate(int row, int col){
        if((col>0 && nodes[col-1][row].hasMVP) || (col<4 && nodes[col+1][row].hasMVP) || (row>0 && nodes[col][row-1].hasMVP) || (row<4 && nodes[col][row+1].hasMVP))
            return false;
        if(handle_preGame.getTurn() == 2)
            return false;
        return true;
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
        if(this.row == node.row && this.col == node.col)
            return true;
        else 
            return false;
    }
    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public boolean getHasMVP() {
        return hasMVP;
    }
    public void addPartnership(Color c){
        linkedPartnerships.add(c);
    }
    public ArrayList<Color> getLinkedPartnerships(){
        return linkedPartnerships;
    }
}
