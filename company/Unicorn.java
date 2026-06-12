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
        setLayoutX(-17);
        setLayoutY(-17);
        drawCompany();
    }
    @Override
    public void drawCompany(){
        shape.setWidth(35);
        shape.setHeight(35);
        shape.setArcWidth(15);
        shape.setArcHeight(15);
        shape.setFill(color);
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(3);
        this.getChildren().add(shape);
    }
}
