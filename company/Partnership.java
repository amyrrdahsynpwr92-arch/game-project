package company;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import graph.*;

public class Partnership extends ComanyStructure {
    private Line shape = new Line();
    public Partnership(Node start, Node end){
        shape.setStartX(start.getLayoutX());
        shape.setStartY(start.getLayoutY());
        shape.setEndX(end.getLayoutX());
        shape.setEndY(end.getLayoutY());
        this.color = Color.PURPLE;
        drawCompany();
    }
    @Override
    public void drawCompany(){
        shape.setStroke(color);
        shape.setStrokeWidth(5);
        this.getChildren().add(shape);
    }
}
