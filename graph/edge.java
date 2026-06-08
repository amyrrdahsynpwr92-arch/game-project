package graph;
import company.*;
import card.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class edge extends Line {
    private Line edgeShape;
    private boolean hasPartnership;
    private node start;
    private node end;
    public edge(node start, node end){
        this.start = start;
        this.end = end;
        this.setStrokeWidth(3);
        this.setStroke(Color.WHITE);
        this.setOnMouseClicked(e -> drawPartnership());
    }
    public void drawPartnership(){
        Capital capital = null;
        Patent patent = null;
        // ArrayList<ResourceCard> cards = player.getMyCards();
        // for(ResourceCard card: cards){
        //     if(card instanceof Capital && capital == null)
        //         capital = (Capital)card;
        //     else if(card instanceof Patent && patent == null)
        //         patent = (Patent)card;
        // }
        //if(capital != null && patent != null && validate()){
            // nodePartnership = new Partnership(start, end);
            // this.getChildren().clear();
            // this.getChildren().add(nodePartnership);
            this.setStroke(Color.PURPLE);
            this.setStrokeWidth(5);
            
            // cards.remove(capital);
            // cards.remove(patent);
            // player.setMyCards(cards);
            // hasPartnership = true;
        //}
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
