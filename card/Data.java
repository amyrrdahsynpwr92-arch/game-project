package card;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Data extends ResourceCard {
    public Data(){
        this.background_color = Color.GREEN;
        this.title_of_resource = new Text("Data");
        arrangeComponents();
    }
}
