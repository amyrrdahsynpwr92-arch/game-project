package graph;
import java.util.ArrayList;
import company.*;
import card.*;
import util.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class edge extends Pane {
    private Line edgeShape;
    private boolean hasPartnership;
    private Partnership nodePartnership;
    private node start;
    private node end;
    public edge(node start, node end){
        this.start = start;
        this.end = end;
        edgeShape = new Line();
        edgeShape.setStartX(start.getNodeShape().getTranslateX());
        edgeShape.setStartY(start.getNodeShape().getTranslateY());
        edgeShape.setEndX(end.getNodeShape().getTranslateX());
        edgeShape.setEndY(end.getNodeShape().getTranslateY());
        edgeShape.setStrokeWidth(3);
        edgeShape.setStroke(Color.WHITE);
        this.getChildren().add(edgeShape);
    }
    public void drawPartnership(Player player){
        Capital capital = null;
        Patent patent = null;
        ArrayList<ResourceCard> cards = player.getMyCards();
        for(ResourceCard card: cards){
            if(card instanceof Capital && capital == null)
                capital = (Capital)card;
            else if(card instanceof Patent && patent == null)
                patent = (Patent)card;
        }
        if(capital != null && patent != null && validate()){
            nodePartnership = new Partnership();
            this.getChildren().clear();
            this.getChildren().add(nodePartnership);
            cards.remove(capital);
            cards.remove(patent);
            player.setMyCards(cards);
            hasPartnership = true;
        }
    }
    private boolean validate(){
        if(start.getChildren().get(0) instanceof MVP || start.getChildren().get(0) instanceof Unicorn || end.getChildren().get(0) instanceof MVP || end.getChildren().get(0) instanceof Unicorn){
            if(start.getLinkedToPartnership())start.setLinkedToPartnership(true);
            if(end.getLinkedToPartnership())end.setLinkedToPartnership(true);
            return true;   
        }
        if(start.getLinkedToPartnership() || end.getLinkedToPartnership()){
            if(!start.getLinkedToPartnership())start.setLinkedToPartnership(true);
            if(!end.getLinkedToPartnership())end.setLinkedToPartnership(true);
            return true;
        } 
        return false;
    }
    public boolean getHasPartnership() {
        return hasPartnership;
    }
    public Line getEdgeShape() {
        return edgeShape;
    }
}
