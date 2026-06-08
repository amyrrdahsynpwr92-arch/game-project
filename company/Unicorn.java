package company;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Unicorn extends ComanyStructure {
    private Rectangle shape = new Rectangle();
    public Rectangle GetShape() {
        return shape;
    }
    public Unicorn(){
        this.color = Color.GREEN;
        setLayoutX(-10);
        setLayoutY(-10);
        drawCompany();
    }
    @Override
    public void drawCompany(){
        shape.setWidth(20);
        shape.setHeight(20);
        shape.setArcWidth(10);
        shape.setArcHeight(10);
        shape.setFill(color);
        shape.setStroke(Color.BLACK);
        this.getChildren().add(shape);
    }
}
