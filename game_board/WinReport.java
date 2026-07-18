package game_board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.animation.ScaleTransition;

public class WinReport {
    private BorderPane currentPane;
    private ImageView box = new ImageView(new Image(getClass().getResourceAsStream("/images/details/winBox.png")));
    public WinReport(BorderPane currentPane, int player){
        this.currentPane = currentPane;
        drawWinBox(player);
    }
    public void drawWinBox(int player){
        StackPane pane = new StackPane();
        Text text = new Text("Player " + Integer.toString(player) + " won!");
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        text.setFill(Color.WHITE);
        pane.getChildren().addAll(box, text);
        pane.layoutXProperty().bind(currentPane.widthProperty().divide(2));
        pane.layoutYProperty().bind(currentPane.heightProperty().divide(2));
        box.setFitWidth(600);
        box.setFitHeight(600);
        currentPane.getChildren().add(pane);
        pane.setScaleX(0);
        pane.setScaleY(0);

        ScaleTransition st = new ScaleTransition(Duration.seconds(1), pane);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();
        currentPane.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY)));
        currentPane.getTop().setOpacity(0.4);
        currentPane.getBottom().setOpacity(0.4);
        currentPane.getCenter().setOpacity(0.4);
        currentPane.getRight().setOpacity(0.4);
        currentPane.getLeft().setOpacity(0.4);
    }
}
