package cards;
import cards.ResourceCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.ProductionResources;

public class Data extends ResourceCard {
    public Data(){
        this.type = ProductionResources.Data;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/data.png")));
        this.color = Color.GREEN;
    }
}
