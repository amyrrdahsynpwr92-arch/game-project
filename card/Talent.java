package card;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Talent extends ResourceCard {
    public Talent(){
        this.background_color = Color.BLUEVIOLET;
        this.title_of_resource = new Text("Talent");
        arrangeComponents();
    }
}
