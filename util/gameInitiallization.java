package util;
import java.util.ArrayList;

import game_board.drawMap;
import type.PlayerRole;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import game_board.drawBoard;
import card.*;

public class gameInitiallization{
    private BorderPane currentPane;
    private ArrayList<Player> players = new ArrayList<>();
    private int numberOfPlayers;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/bg2.png")));
    private ImageView cover = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/cover.png")));
    private ImageView image1 = new ImageView(new Image(getClass().getResourceAsStream("/images/roles/the_hacker_ceo.png")));
    private ImageView image2 = new ImageView(new Image(getClass().getResourceAsStream("/images/roles/the_tech_guru_cto.png")));
    private ImageView image3 = new ImageView(new Image(getClass().getResourceAsStream("/images/roles/the_vc_funded.png")));
    private ImageView image4 = new ImageView(new Image(getClass().getResourceAsStream("/images/roles/null.jpg")));
    private MediaPlayer clickSound = new MediaPlayer(new Media(getClass().getResource("/voices/click.mp3").toExternalForm()));
    private int playerIndex = 0;
    
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public gameInitiallization(BorderPane currentPane){
        this.currentPane = currentPane;
        background.fitWidthProperty().bind(currentPane.widthProperty());
        background.fitHeightProperty().bind(currentPane.heightProperty());
        new setCover();
    }
    class setCover {
        public setCover() {
            cover.fitWidthProperty().bind(currentPane.widthProperty());
            cover.fitHeightProperty().bind(currentPane.heightProperty());
            FadeTransition fade1 = new FadeTransition(Duration.seconds(1), currentPane);
            FadeTransition fade2 = new FadeTransition(Duration.seconds(1), currentPane);
            PauseTransition pause1 = new PauseTransition(Duration.seconds(2));
            PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
            currentPane.setCenter(cover);
            fade1.setFromValue(0.0);
            fade1.setToValue(1.0);
            currentPane.setOpacity(0);
            pause1.play();
            pause1.setOnFinished(e -> {
                fade1.play();
            });
            fade1.setOnFinished(e -> {
                pause2.play();
            });
            pause2.setOnFinished(e -> {
                fade2.setFromValue(1.0);
                fade2.setToValue(0.0);
                fade2.play();
            });
            fade2.setOnFinished(e -> {
                currentPane.getChildren().clear();
                currentPane.getChildren().add(background);
                new loadGame();
            });
        }
    }
    class loadGame extends VBox{
        private FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), currentPane);
        private Text text = new Text("play the previous game?");
        private Rectangle backText = new Rectangle();
        private StackPane paneForText = new StackPane();
        HBox paneForButtons = new HBox(15);
        private Button btYes = new Button("Yes");
        private Button btNo = new Button("No");
        public loadGame(){
            this.setSpacing(60);
            text.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
            backText.setFill(Color.RED);
            backText.setArcWidth(30);
            backText.setArcHeight(30);
            backText.setWidth(400);
            backText.setHeight(60);
            btYes.setPrefSize(150, 50);
            btNo.setPrefSize(150, 50);
            btYes.setFont(Font.font(50));
            btNo.setFont(Font.font(50));
            paneForText.getChildren().addAll(backText, text);
            paneForButtons.getChildren().addAll(btYes, btNo);
            paneForButtons.setAlignment(Pos.CENTER);
            fadeTransition.setCycleCount(1);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            this.setAlignment(Pos.CENTER);
            this.setPadding(new Insets(40, 0, 40, 0));
            this.getChildren().addAll(paneForText, paneForButtons);
            
            currentPane.setCenter(this);
            fadeTransition.play();
            btYes.setOnAction(e -> {
                clickSound.play();
            });
            btNo.setOnAction(e -> {
                clickSound.play();
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    clickSound.stop();
                    currentPane.getChildren().clear();
                    currentPane.getChildren().add(background);
                    currentPane.setCenter(new SetPlayersNumber());
                });
            });
        }
    }
    class SetPlayersNumber extends VBox{
        private FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), currentPane);
        private Text text = new Text("Specify the number of players:");
        private Rectangle backText = new Rectangle();
        private StackPane paneForText = new StackPane();
        HBox paneForButtons = new HBox(15);
        private Button bt1 = new Button("2");
        private Button bt2 = new Button("3");
        private Button bt3 = new Button("4");
        public SetPlayersNumber(){
            this.setSpacing(60);
            text.setFont(Font.font("Roboto", FontWeight.BOLD, 27));
            backText.setFill(Color.TURQUOISE);
            backText.setArcWidth(30);
            backText.setArcHeight(30);
            backText.setWidth(400);
            backText.setHeight(60);
            bt1.setPrefSize(100, 50);
            bt2.setPrefSize(100, 50);
            bt3.setPrefSize(100, 50);
            bt1.setFont(Font.font(50));
            bt2.setFont(Font.font(50));
            bt3.setFont(Font.font(50));
            paneForText.getChildren().addAll(backText, text);
            paneForButtons.getChildren().addAll(bt1, bt2, bt3);
            paneForButtons.setAlignment(Pos.CENTER);
            fadeTransition.setAutoReverse(false);
            fadeTransition.setCycleCount(1);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            bt1.setDisable(true);
            bt2.setDisable(true);
            bt3.setDisable(true);
            fadeTransition.setOnFinished(e -> {
                bt1.setDisable(false);
                bt2.setDisable(false);
                bt3.setDisable(false);
            });
            fadeTransition.play();
            this.setAlignment(Pos.CENTER);
            this.setPadding(new Insets(40, 0, 40, 0));
            this.getChildren().addAll(paneForText, paneForButtons);
            bt1.setOnAction(e -> {
                clickSound.play();
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    clickSound.stop();
                    numberOfPlayers = Integer.parseInt(bt1.getText());
                    for(int i=0; i<numberOfPlayers; i++){
                        players.add(new Player(i+1));
                    }
                    currentPane.getChildren().clear();
                    currentPane.setCenter(new setRoles());
                });
            });
            bt2.setOnAction(e -> {
                clickSound.play();
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    clickSound.stop();
                    numberOfPlayers = Integer.parseInt(bt2.getText());
                    for(int i=0; i<numberOfPlayers; i++){
                        players.add(new Player(i+1));
                    }
                    currentPane.getChildren().clear();
                    currentPane.setCenter(new setRoles());
                });
            });
            bt3.setOnAction(e -> {
                clickSound.play();
                bt3.setDisable(true);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    clickSound.stop();
                    numberOfPlayers = Integer.parseInt(bt3.getText());
                    for(int i=0; i<numberOfPlayers; i++){
                        players.add(new Player(i+1));
                    }
                    currentPane.getChildren().clear();
                    currentPane.setCenter(new setRoles());
                });
            });
        }
    }
    class setRoles extends VBox{
        private FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), currentPane);
        private Text text = new Text("The role of player 1:");
        private Rectangle backText = new Rectangle();
        private ScrollPane paneForScroll = new ScrollPane();
        private StackPane paneForText = new StackPane();
        private HBox paneForImages = new HBox(10);
        public setRoles(){
            this.setSpacing(60);
            text.setFont(Font.font("Roboto", FontWeight.BOLD, 35));
            backText.setFill(Color.TURQUOISE);
            backText.setArcWidth(30);
            backText.setArcHeight(30);
            backText.setWidth(500);
            backText.setHeight(60);
            paneForText.getChildren().addAll(backText, text);
            image1.setFitWidth(400);
            image1.setFitHeight(400);
            image1.setPreserveRatio(true);
            image2.setFitWidth(400);
            image2.setFitHeight(400);
            image2.setPreserveRatio(true);
            image3.setFitWidth(400);
            image3.setFitHeight(400);
            image3.setPreserveRatio(true);
            image4.setFitWidth(400);
            image4.setFitHeight(400);
            image4.setPreserveRatio(true);
            fadeTransition.setCycleCount(1);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            image1.setDisable(true);
            image2.setDisable(true);
            image3.setDisable(true);
            image4.setDisable(true);
            fadeTransition.setOnFinished(e -> {
                image1.setDisable(false);
                image2.setDisable(false);
                image3.setDisable(false);
                image4.setDisable(false);
            });
            fadeTransition.play();
            paneForImages.getChildren().addAll(image1, image2, image3, image4);
            paneForScroll.setContent(paneForImages);
            paneForScroll.setFitToWidth(true);
            paneForScroll.setFitToHeight(true);
            this.getChildren().addAll(paneForText, paneForScroll);
            image1.setOnMouseClicked(e -> {
                if(clickSound.getStatus() == MediaPlayer.Status.PLAYING)clickSound.stop();
                clickSound.play();
                paneForImages.getChildren().remove(image1);
                if(numberOfPlayers > 1){
                    players.get(playerIndex).setRole(PlayerRole.The_Hacker_CEO);
                    players.get(playerIndex).getMyCards().add(new Capital());
                    players.get(playerIndex).getMyCards().add(new Capital());
                    playerIndex++;
                    numberOfPlayers--;
                    text.setText("The role of player " + Integer.toString(playerIndex+1) + ":");
                }else if(numberOfPlayers == 1){
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setDuration(Duration.millis(1000));
                    fadeTransition.setOnFinished(event -> {
                        clickSound.stop();
                        currentPane.getChildren().clear();
                        new drawBoard(currentPane, players);
                        numberOfPlayers = players.size();
                    });
                    fadeTransition.play();
                }
            });
            image2.setOnMouseClicked(e -> {
                if(clickSound.getStatus() == MediaPlayer.Status.PLAYING)clickSound.stop();
                clickSound.play();
                paneForImages.getChildren().remove(image2);
                if(numberOfPlayers > 1){
                    players.get(playerIndex).setRole(PlayerRole.The_Teck_GURU);
                    players.get(playerIndex).getMyCards().add(new Capital());
                    players.get(playerIndex).getMyCards().add(new Capital());
                    playerIndex++;
                    numberOfPlayers--;
                    text.setText("The role of player " + Integer.toString(playerIndex+1) + ":");
                }else if(numberOfPlayers == 1){
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setDuration(Duration.millis(1000));
                    fadeTransition.setOnFinished(event -> {
                        clickSound.stop();
                        currentPane.getChildren().clear();
                        new drawBoard(currentPane, players);
                        numberOfPlayers = players.size();
                    });
                    fadeTransition.play();
                }
            });
            image3.setOnMouseClicked(e -> {
                if(clickSound.getStatus() == MediaPlayer.Status.PLAYING)clickSound.stop();
                clickSound.play();
                paneForImages.getChildren().remove(image3);
                if(numberOfPlayers > 1){
                    players.get(playerIndex).setRole(PlayerRole.The_VC_Funded);
                    players.get(playerIndex).getMyCards().add(new Capital());
                    players.get(playerIndex).getMyCards().add(new Capital());
                    playerIndex++;
                    numberOfPlayers--;
                    text.setText("The role of player " + Integer.toString(playerIndex+1) + ":");
                }else if(numberOfPlayers == 1){
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setDuration(Duration.millis(1000));
                    fadeTransition.setOnFinished(event -> {
                        clickSound.stop();
                        currentPane.getChildren().clear();
                        new drawBoard(currentPane, players);
                        numberOfPlayers = players.size();
                    });
                    fadeTransition.play();
                }
            });
            image4.setOnMouseClicked(e -> {
                if(clickSound.getStatus() == MediaPlayer.Status.PLAYING)clickSound.stop();
                clickSound.play();
                paneForImages.getChildren().remove(image4);
                if(numberOfPlayers > 1){
                    players.get(playerIndex).setRole(PlayerRole.Null);
                    players.get(playerIndex).getMyCards().add(new Capital());
                    players.get(playerIndex).getMyCards().add(new Capital());
                    playerIndex++;
                    numberOfPlayers--;
                    text.setText("The role of player " + Integer.toString(playerIndex+1) + ":");
                }else if(numberOfPlayers == 1){
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setDuration(Duration.millis(1000));
                    fadeTransition.setOnFinished(event -> {
                        clickSound.stop();
                        currentPane.getChildren().clear();
                        new drawBoard(currentPane, players);
                    });
                    fadeTransition.play();
                }
            });
        }
    }
}
