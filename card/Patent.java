package card;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Patent extends ResourceCard {
    public Patent(){
        this.background_color = Color.RED;
        this.title_of_resource = new Text("Patent");
        arrangeComponents();
    }
}
