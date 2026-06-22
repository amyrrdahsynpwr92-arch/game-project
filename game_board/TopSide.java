package game_board;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TopSide extends HBox {
    int index = 0;
    BorderPane currentPane;
    public TopSide(BorderPane currentPane){
        this.currentPane = currentPane;
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.setMinHeight(50);
        drawPlayerBox(1);
        drawStatusPanel("player 1! please put a MVP");
        drawPricesButton();
    }
    public void drawPlayerBox(int playerNumber){
        TextField playerBox = new TextField("Player: " + Integer.toString(playerNumber));
        playerBox.prefWidthProperty().bind(widthProperty().divide(4));
        playerBox.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        playerBox.setLayoutY(5);
        //playerBox.setPrefWidth(200);
        playerBox.setPrefHeight(40);
        playerBox.setEditable(false);
        if(this.getChildren().size() > 0)this.getChildren().remove(0);
        this.getChildren().add(0, playerBox);
    }
    public void drawStatusPanel(String status){
        TextField statusBox = new TextField("|");
        statusBox.setLayoutY(5);
        //statusBox.setPrefWidth(450);
        statusBox.setPrefHeight(40);
        statusBox.setEditable(false);
        statusBox.setAlignment(Pos.CENTER);
        index = 0;
        Timeline motivateSign = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            if(statusBox.getText().charAt(statusBox.getText().length() - 1) == '|'){
                statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1) + " ");
            }
            else
                statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1) + "|");
        }));
        motivateSign.setCycleCount(Timeline.INDEFINITE);
        Timeline writingAnimation = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            statusBox.setText(statusBox.getText().substring(0, statusBox.getText().length() - 1));
            statusBox.appendText(Character.toString(status.charAt(index++)) + "|");
        }));
        statusBox.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        writingAnimation.setCycleCount(status.length());
        writingAnimation.setOnFinished(e -> motivateSign.play());
        writingAnimation.play();
        statusBox.prefWidthProperty().bind(widthProperty().divide(2));
        if(this.getChildren().size() > 1)this.getChildren().remove(1);
        this.getChildren().add(1, statusBox);
    }
    public void drawPricesButton(){
        Button btPrices = new Button("prices of sources");
        btPrices.setLayoutY(5);
        //btPrices.setPrefWidth(200);
        btPrices.setPrefHeight(40);
        btPrices.prefWidthProperty().bind(widthProperty().divide(4));
        if(this.getChildren().size() > 2)this.getChildren().remove(2);
        this.getChildren().add(2, btPrices);
    }
}
