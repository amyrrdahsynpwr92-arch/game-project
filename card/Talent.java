package card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import type.ProductionResources;

public class Talent extends ResourceCard {
    public Talent(){
        this.type = ProductionResources.Talent;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/talent.png")));
    }
}
