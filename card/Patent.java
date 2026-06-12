package card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import type.ProductionResources;

public class Patent extends ResourceCard {
    public Patent(){
        this.type = ProductionResources.Patent;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/patent.png")));
    }
}
