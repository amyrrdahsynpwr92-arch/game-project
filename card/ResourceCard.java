package card;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.*;

public class ResourceCard {
    protected int price = 1;
    protected int unused_turns;
    protected ProductionResources type;
    protected ImageView symbol;
    protected Color color;

    public ProductionResources getType(){
        return type;
    }
    public Color getColor(){
        return color;
    }
    public ImageView getSymbol(){
        return symbol;
    }
    public int getPrice(){
        return price;
    }
}
