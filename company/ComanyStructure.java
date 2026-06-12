package company;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public abstract class ComanyStructure extends Pane{
    protected Color color; 
    public void setColor(Color color) {
        this.color = color;
    }
    public abstract void drawCompany();
}
