package util;
import type.SectorType;
import type.ProductionResources;
import java.util.Random;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Sector extends Pane{
    private SectorType type;
    private ProductionResources ResourceType;
    private int number;
    private GamePlan gamePlan;  
    private Color color; 
    
    public Sector(SectorType type, GamePlan gamePlan){
        this.type = type;
        this.number = new Random().nextInt(11) + 2;
        this.gamePlan = gamePlan;
        SetSectorType();
        SetColor();
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
                ResourceType = null;
                break;
        }
    }
    private void SetColor(){
        switch (ResourceType) {
            case Talent:
                this.color = Color.BLUEVIOLET;
                break;
            case Capital:
                this.color = Color.GOLD;
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
            default:
                this.color = Color.WHITESMOKE;
                break;
        }
    }
    private void arrangeComponents(){
        setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        setPrefSize(100, 100);
        Text num = new Text(Integer.toString(number));
        num.setFill(Color.WHITE);
        num.setFont(Font.font(35));
        Text title = new Text(type.name());
        title.setFill(Color.WHITE);
        title.setFont(Font.font(20));
        VBox paneForComponents = new VBox(5);
        paneForComponents.getChildren().addAll(title, num);
        paneForComponents.setAlignment(Pos.CENTER);
        paneForComponents.prefWidthProperty().bind(widthProperty());
        paneForComponents.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(paneForComponents);
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
