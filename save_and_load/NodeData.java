package save_and_load;
import graph.*;
import java.io.Serializable;
import java.util.*;
import javafx.scene.paint.Color;

public class NodeData implements Serializable {
    boolean hasMVP;
    boolean hasUnicorn;
    ArrayList<Integer> linkedPartnerships = new ArrayList<>();
    String colorName = "";

    public NodeData(Node node){
        hasMVP = node.HasMVP();
        hasUnicorn = node.HasUnicorn();
        for(Edge edge: node.getLinkedPartnerships()){
            linkedPartnerships.add(edge.getEdgeNumber());
        }
        if(hasMVP){
            if((Color)node.getNodeMVP().GetShape().getFill() == Color.RED)
                colorName = "red";
            else if((Color)node.getNodeMVP().GetShape().getFill() == Color.BLUE)
                colorName = "blue";
            else if((Color)node.getNodeMVP().GetShape().getFill() == Color.PURPLE)
                colorName = "purple";
            else
                colorName = "green";
        }else if(hasUnicorn){
            if((Color)node.getNodeUnicorn().GetShape().getFill() == Color.RED)
                colorName = "red";
            else if((Color)node.getNodeUnicorn().GetShape().getFill() == Color.BLUE)
                colorName = "blue";
            else if((Color)node.getNodeUnicorn().GetShape().getFill() == Color.PURPLE)
                colorName = "purple";
            else
                colorName = "green";
        }
    }
    public boolean HasMVP(){
        return hasMVP;
    }
    public boolean HasUnicorn(){
        return hasUnicorn;
    }
    public ArrayList<Integer> getLinkedPartnerships(){
        return linkedPartnerships;
    }
    public Color getColor(){
        switch(colorName){
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "purple":
                return Color.PURPLE;
            case "green":
                return Color.GREEN;
        }
        return Color.RED;
    }
}
