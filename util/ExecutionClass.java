package util;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
public class ExecutionClass extends Application {
    private BorderPane currentPane = new BorderPane();
    private gameInitiallization gameInitiallization = new gameInitiallization(currentPane);
    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(currentPane, 850, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
