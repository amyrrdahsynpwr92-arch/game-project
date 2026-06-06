package util;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class ExecutionClass extends Application {
    @Override
    public void start(Stage primaryStage){
        Pane pane = new drawMap();
        Scene scene = new Scene(pane, 350, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
