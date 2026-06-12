package company;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MVP extends ComanyStructure {
    private Circle shape = new Circle();
    public Circle GetShape() {
        return shape;
    }
    public MVP(){
        this.color = Color.RED;
        setLayoutX(-2);
        setLayoutY(-2);
        drawCompany();
    }
    @Override
    public void drawCompany(){
        shape.setRadius(13);
        shape.setFill(color);
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(3);
        this.getChildren().add(shape);
    }
}
