package card;
import cards.ResourceCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.ProductionResources;

public class Patent extends ResourceCard {
    public Patent(){
        this.type = ProductionResources.Patent;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/patent.png")));
        this.color = Color.RED;
    }
}
