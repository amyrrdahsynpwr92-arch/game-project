package company;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MVP extends ComanyStructure {
    private Rectangle shape = new Rectangle();
    public Rectangle GetShape() {
        return shape;
    }
    public MVP(){
        this.color = Color.RED;
        setLayoutX(-7.5);
        setLayoutY(-7.5);
        drawCompany();
    }
    @Override
    public void drawCompany(){
        shape.setWidth(15);
        shape.setHeight(15);
        shape.setArcWidth(10);
        shape.setArcHeight(10);
        shape.setFill(color);
        shape.setStroke(Color.BLACK);
        this.getChildren().add(shape);
    }
}
