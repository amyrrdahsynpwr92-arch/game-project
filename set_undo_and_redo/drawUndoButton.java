package set_undo_and_redo;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;

public class drawUndoButton {
    private Pane pane;
    Button bt = new Button();
   // private ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/images/details/undo.png")));
    saveStages stages;
     BorderPane currentPane;
    public drawUndoButton(Pane pane, saveStages stages, BorderPane currentPane){
        this.pane = pane;
        this.stages = stages;
        this.currentPane = currentPane;
        //draw();
    }
    public void draw(){
        bt.setText("Undo");
       // bt.setGraphic(image);
        //bt.layoutXProperty().bind(pane.widthProperty().divide(2/5));
        bt.setLayoutX(200);
        bt.setLayoutY(50);
        pane.getChildren().add(bt);
        bt.setOnMouseClicked(e -> currentPane.setCenter(stages.getCurrentStage()));
    }
}
