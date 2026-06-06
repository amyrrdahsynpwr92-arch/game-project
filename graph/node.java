package graph;
import company.*;
import card.*;
import util.Player;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import type.PlayerRole;

import java.util.ArrayList;

public class node extends Pane{
    private Circle nodeShape;
    private int row;
    private int col;
    private MVP nodeMVP;
    private Unicorn nodeUnicorn;
    private boolean linkedToPartnership;
    public node(int row, int col, double width, double height){
        this.row = row;
        this.col = col;
        nodeShape = new Circle(5);
        nodeShape.setStyle("-fx-fill: black; -fx-stroke: white");
        nodeShape.setCenterX(0);
        nodeShape.setCenterY(0);
        this.getChildren().add(nodeShape);
        nodeShape.setOnMouseClicked(e -> drawMVP());
        //setStyle("-fx-border-color:red;");
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void drawMVP(){
        Capital capital = null;
        Talent talent = null;
        Cloud cloud = null;
        Data data = null;
        // ArrayList<ResourceCard> cards = player.getMyCards();
        // for(ResourceCard card: cards){
        //     if(card instanceof Capital && capital == null)
        //         capital = (Capital)card;
        //     else if(card instanceof Talent && talent == null)
        //         talent = (Talent)card;
        //     else if(card instanceof Cloud && cloud == null)
        //         cloud = (Cloud)card;
        //     else if(card instanceof Data && data == null)
        //         data = (Data)card;
        // }
        //if(capital != null && talent != null && cloud != null && data != null){
            nodeMVP = new MVP();
            this.getChildren().clear();
            this.getChildren().add(nodeMVP);
            nodeMVP.setOnMouseClicked(e -> drawUnicorn());
            // nodeMVP.GetShape().widthProperty().bind(widthProperty());
            // nodeMVP.GetShape().heightProperty().bind(heightProperty());
            // cards.remove(capital);
            // cards.remove(talent);
            // cards.remove(cloud);
            // cards.remove(data);
            // player.setMyCards(cards);
            // player.setScore(player.getScore() + 1);
        //}
    }
    public void drawUnicorn(){
        Cloud cloud1 = null;
        Cloud cloud2 = null;
        Data data1 = null;
        Data data2 = null;
        Data data3 = null;
        // ArrayList<ResourceCard> cards = player.getMyCards();
        // for(ResourceCard card: cards){
        //     if(card instanceof Cloud){
        //         if(cloud1 == null)
        //             cloud1 = (Cloud)card;
        //         else if(cloud2 == null)
        //             cloud2 = (Cloud)card;
        //     }
        //     else if(card instanceof Data){
        //         if(data1 == null)
        //             data1 = (Data)card;
        //         else if(cloud2 == null)
        //             data2 = (Data)card;
        //         else if(data3 == null)
        //             data3 = (Data)card;
        //     }
        // }
        //if(cloud1 != null && cloud2 != null && data1 != null && data2 != null && data3 != null){
            nodeUnicorn = new Unicorn();
            this.getChildren().clear();
            this.getChildren().add(nodeUnicorn);
        //     cards.remove(cloud1);
        //     cards.remove(cloud2);
        //     cards.remove(data1);
        //     cards.remove(data2);
        //     cards.remove(data3);
        //     player.setMyCards(cards);
        //     player.setScore(player.getScore() + 1);
        // }
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
}
