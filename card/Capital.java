package card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import type.*;

public class Capital extends ResourceCard {
    public Capital(){
        this.type = ProductionResources.Capital;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/capital.png")));
        this.color = Color.YELLOW;
    }
}
