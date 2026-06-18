package card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.ProductionResources;

public class Cloud extends ResourceCard {
    public Cloud(){
        this.type = ProductionResources.Cloud;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/cloud.png")));
        this.color = Color.BLUE;
    }
}
