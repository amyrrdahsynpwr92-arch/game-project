package card;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import type.*;

public class ResourceCard {
    protected int price = 4;
    protected int unused_turns;
    protected ProductionResources type;
    protected ImageView symbol;

    public ProductionResources getType(){
        return type;
    }
    // protected void arrangeComponents(){
    //     BackgroundFill backgroundFill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
    //     BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(getWidth(), getHeight(), true, true, false, true));
    //     ArrayList<BackgroundFill> fills = new ArrayList<>();
    //     fills.add(backgroundFill);
    //     ArrayList<BackgroundImage> images = new ArrayList<>();
    //     images.add(backgroundImage);
    //     this.setBackground(new Background(fills, images));
    //     VBox paneForComponents = new VBox(5);
    //     title_of_resource.setFill(Color.WHITE);
    //     title_of_resource.setFont(Font.font(100));
    //     paneForComponents.getChildren().addAll(symbol_of_resource, title_of_resource);
    //     paneForComponents.prefWidthProperty().bind(widthProperty());
    //     paneForComponents.prefHeightProperty().bind(heightProperty());
    //     this.getChildren().addAll(paneForComponents);
    // }
}
