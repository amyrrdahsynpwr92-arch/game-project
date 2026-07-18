package cards;
import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.*;

public class ResourceCard implements Serializable{
    protected int price = 4;
    protected int unused_turns;
    protected ProductionResources type;
    protected transient ImageView symbol;
    protected transient Color color;

    public ProductionResources getType(){
        return type;
    }
    public Color getColor(){
        return color;
    }
    public ImageView getSymbol(){
        return symbol;
    }
    public void setColor(){
        switch(type){
            case Patent:
                color = Color.RED;
                break;
            case Capital:
                color = Color.rgb(228, 187, 3, 0.92);
                break;
            case Talent:
                color = Color.PURPLE;
                break;
            case Data:
                color = Color.GREEN;
                break;
            case Cloud:
                color = Color.BLUE;
                break;
        }
    }
    public void setImage(){
        switch(type){
            case Capital:
                symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/capital.png")));
                break;
            case Cloud:
                symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/cloud.png")));
                break;
            case Data:
                symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/data.png")));
                break;
            case Patent:
                symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/patent.png")));
                break;
            case Talent:
                symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/talent.png")));
                break;
        }
    }
    public int getPrice(){
        return price;
    }
}
