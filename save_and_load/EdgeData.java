package save_and_load;
import java.io.Serializable;
import graph.*;
import javafx.scene.paint.Color;

public class EdgeData implements Serializable {
    boolean hasPartnership;
    String colorName = "";

    public EdgeData(Edge edge){
        this.hasPartnership = edge.getHasPartnership();
        if(hasPartnership){
            if((Color)edge.getStroke() == Color.RED)
                colorName = "red";
            else if((Color)edge.getStroke() == Color.BLUE)
                colorName = "blue";
            else if((Color)edge.getStroke() == Color.PURPLE)
                colorName = "purple";
            else
                colorName = "green";
        }
    }
    public boolean HasPartnership(){
        return hasPartnership;
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
