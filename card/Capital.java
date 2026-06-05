package card;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Capital extends ResourceCard {
    public Capital(){
        this.background_color = Color.GOLD;
        this.title_of_resource = new Text("Capital");
        arrangeComponents();
    }
}
