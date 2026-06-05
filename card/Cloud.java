package card;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Cloud extends ResourceCard {
    public Cloud(){
        this.background_color = Color.BLUE;
        this.title_of_resource = new Text("Cloud");
        arrangeComponents();
    }
}
