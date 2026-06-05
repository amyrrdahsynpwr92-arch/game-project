package card;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class ResourceCard extends Pane {
    protected Color background_color;
    protected ImageView symbol_of_resource;
    protected Image background;
    protected Text title_of_resource;
    protected int price;
    protected int unused_turns;

    protected void arrangeComponents(){
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(getWidth(), getHeight(), true, true, false, true));
        ArrayList<BackgroundFill> fills = new ArrayList<>();
        fills.add(backgroundFill);
        ArrayList<BackgroundImage> images = new ArrayList<>();
        images.add(backgroundImage);
        this.setBackground(new Background(fills, images));
        VBox paneForComponents = new VBox(5);
        title_of_resource.setFill(Color.WHITE);
        title_of_resource.setFont(Font.font(100));
        paneForComponents.getChildren().addAll(symbol_of_resource, title_of_resource);
        paneForComponents.prefWidthProperty().bind(widthProperty());
        paneForComponents.prefHeightProperty().bind(heightProperty());
        this.getChildren().addAll(paneForComponents);
    }
}
