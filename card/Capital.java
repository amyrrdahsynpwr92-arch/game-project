package card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.w3c.dom.css.RGBColor;

import cards.ResourceCard;
import type.*;

public class Capital extends ResourceCard {
    public Capital(){
        this.type = ProductionResources.Capital;
        this.symbol = new ImageView(new Image(getClass().getResourceAsStream("/images/cards/capital.png")));
        this.color = Color.rgb(228, 187, 3, 0.92);
    }
}
