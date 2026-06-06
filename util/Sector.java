package util;
import type.SectorType;
import type.ProductionResources;
import java.util.Random;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

public class Sector extends Pane{
    private Rectangle sectorShape;
    public Rectangle getSectorShape() {
        return sectorShape;
    }

    private SectorType type;
    private ProductionResources ResourceType;
    private int number;
    private String name;
    private GamePlan gamePlan;  
    private Color color; 
    private ImageView effect = new ImageView(new Image(getClass().getResourceAsStream("/images/effect.png")));
    
    public Sector(SectorType type, double x, double y){
        this.type = type;
        this.number = new Random().nextInt(11) + 2;
       // this.gamePlan = gamePlan;
        SetSectorType();
        SetColor();
        setSectorName();
        this.setPrefSize(50, 50);
        this.setLayoutX(x);
        this.setLayoutY(y);
        sectorShape = new Rectangle(50, 50);
        arrangeComponents();
    }
    
    
    private void SetSectorType(){
        switch (type) {
            case AI_Hub:
                ResourceType = ProductionResources.Talent;
                break;
            case Fintech_District:
                ResourceType = ProductionResources.Capital;
                break;
                case Cloud_Campus:
                ResourceType = ProductionResources.Cloud;
                break;
            case IP_Quarter:
                ResourceType = ProductionResources.Patent;
                break;       
            case Data_Valley:
                ResourceType = ProductionResources.Data;
                break;
            case Regulatory_Zone:
                ResourceType = ProductionResources.Null;
                break;
        }
    }
    private void SetColor(){
        switch (ResourceType) {
            case Talent:
                this.color = Color.BLUEVIOLET;
                break;
            case Capital:
                this.color = Color.GOLDENROD;
                break;
            case Cloud:
                this.color = Color.BLUE;
                break;
            case Patent:
                this.color = Color.RED;
                break;       
            case Data:
                this.color = Color.GREEN;
                break;
            case Null:
                this.color = Color.GRAY;
                break;
        }
    }
    private void arrangeComponents(){
        sectorShape.setFill(color);
        sectorShape.setArcWidth(10);
        sectorShape.setArcHeight(10);
        Text num = new Text(Integer.toString(number));
        num.setFill(Color.WHITE);
        num.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        Text title = new Text(name);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        widthProperty().addListener(ov -> {
            num.setFont(Font.font("Roboto", FontWeight.BOLD, getWidth()/4));
            title.setFont(Font.font("Roboto", FontWeight.BOLD, getWidth()/5));
        });
        heightProperty().addListener(ov -> {
            num.setFont(Font.font("Roboto", FontWeight.BOLD, getHeight()/4));
            title.setFont(Font.font("Roboto", FontWeight.BOLD, getHeight()/5));
        });
        VBox paneForComponents = new VBox(5);
        paneForComponents.getChildren().addAll(title, num);
        paneForComponents.setAlignment(Pos.CENTER);
        paneForComponents.prefWidthProperty().bind(widthProperty());
        paneForComponents.prefHeightProperty().bind(heightProperty());
        effect.fitWidthProperty().bind(widthProperty());
        effect.fitHeightProperty().bind(heightProperty());
        effect.setSmooth(true);
        this.getChildren().addAll(sectorShape, effect, paneForComponents);
    }
    public void setSectorName(){
        switch (type) {
            case AI_Hub:
                this.name = "AI";
                break;
            case Fintech_District:
                this.name = "Fintech";
                break;
                case Cloud_Campus:
                this.name = "Cloud";
                break;
            case IP_Quarter:
                this.name = "IP";
                break;       
            case Data_Valley:
                this.name = "Data";
                break;
            case Regulatory_Zone:
                this.name = "Regulatary";
                break;
        }
    }
    public void updateNumber(int n1, int n2){
        this.number = n1 + n2;
    }

    public SectorType getType() {
        return type;
    }

    public ProductionResources getResourceType() {
        return ResourceType;
    }
}
