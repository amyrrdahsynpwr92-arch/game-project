package util;
import type.SectorType;
import type.ProductionResources;
import java.util.Random;

import cards.ResourceCard;
import cards.*;
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
import java.util.ArrayList;

public class Sector extends Pane{
    private Rectangle sectorShape;
    private SectorType type;
    private ProductionResources ResourceType;
    private int number;
    private String name;
    private Color color;
    private static final Image image_address = new Image(Sector.class.getResourceAsStream("/images/backgrounds/effect.png")); 
    private ImageView effect = new ImageView(image_address);
    private int row;
    private int col;
    private ResourceCard resource;
    private ArrayList<Player> mvpPlayers = new ArrayList<>();
    private ArrayList<Player> unicornPlayers = new ArrayList<>();
    private boolean hasAuditor;

    public Sector(ResourceCard resource, int row, int col){
        this.resource = resource;
        this.ResourceType = resource.getType();
        this.number = new Random().nextInt(11) + 2;
        SetSectorType();
        SetColor();
        setSectorName();
        this.row = row;
        this.col = col;
        sectorShape = new Rectangle(50, 50);
        arrangeComponents();
    }
    
    
    private void SetSectorType(){
        switch (ResourceType) {
            case Talent:
                type = SectorType.AI_Hub;
                break;
            case Capital:
                type = SectorType.Fintech_District;
                break;
                case Cloud:
                type = SectorType.Cloud_Campus;
                break;
            case Patent:
                type = SectorType.IP_Quarter;
                break;       
            case Data:
                type = SectorType.Data_Valley;
                break;
            case Null:
                type = SectorType.Regulatory_Zone;
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
    public ResourceCard getResource(){
        return resource;
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
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public Rectangle getSectorShape() {
        return sectorShape;
    }

    public ArrayList<Player> getMvpPlayers() {
        return mvpPlayers;
    }
    public ArrayList<Player> getUnicornPlayers() {
        return unicornPlayers;
    }
    public int getNumber() {
        return number;
    }
    public void setHasAuditor(boolean hasAuditor){
        this.hasAuditor = hasAuditor;
    }
    public boolean HasAuditor(){
        return hasAuditor;
    }
}

