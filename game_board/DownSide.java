package game_board;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import game_board.DrawBoard;
import set_undo_and_redo.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import graph.*;
import javafx.scene.image.Image;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import card.*;
import util.*;

public class DownSide extends HBox {
    private Handle_TurningGame handle_TurningGame;
    private DrawBoard board;
    private ImageView auditor = new ImageView(new Image(getClass().getResourceAsStream("/images/details/auditor.png")));
    private boolean moveAuditor = false;
    private double offsetX;
    private double offsetY;
    private double startX;
    private double startY;
    private MediaPlayer errorSound;
    private Market market;
    private ArrayList<Sector> sectors;
    int price = 0;
    int index = 0;
    private Timeline motivateSign;
    private Timeline writingAnimation;
    String status = "";
    TextField resourceTitle = new TextField();
    int onTrade = -1;
    int wantedCapitalCount = 0;
    int wantedCloudCount = 0;
    int wantedDataCount = 0;
    int wantedPatentCount = 0;
    int wantedTalentCount = 0;
    int lostCapitalCount = 0;
    int lostCloudCount = 0;
    int lostDataCount = 0;
    int lostPatentCount = 0;
    int lostTalentCount = 0;
    private ArrayList<Integer> wantedResources = new ArrayList<>();
    private ArrayList<Integer> lostResources = new ArrayList<>();
    private ArrayList<Integer> onTaxResources = new ArrayList<>();
    private Player fromRequest = null;
    private Player onRequest = null;
    private int onTax = 0;
    private Rectangle auditorBack;
    private Text taxText = new Text("");
    private ArrayList<Integer> lastResourcesCount = new ArrayList<>();

    public DownSide(Handle_TurningGame handle_TurningGame, DrawBoard board){
        this.handle_TurningGame = handle_TurningGame;
        this.board = board;
        market = board.getTopSide().getMarket();
        this.sectors = board.getMap().getSectors();
        errorSound = board.getMap().getNodes()[0][0].getErrorSound();
        drawBuyButton();
        drawTradeButton();
        drawEndOfButton();
        drawAuditor();
    }
    public void drawBuyButton(){
        Button btBuy = new Button("Buy");
        btBuy.prefWidthProperty().bind(widthProperty().divide(4));
        btBuy.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btBuy.setTextFill(Color.WHITE);
        btBuy.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        btBuy.setOnAction(e -> buy());
        btBuy.setPrefHeight(60);
        this.getChildren().add(0, btBuy);
    }
    public void drawTradeButton(){
        Button btTrade = new Button("Trade");
        btTrade.prefWidthProperty().bind(widthProperty().divide(4));
        btTrade.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btTrade.setTextFill(Color.WHITE);
        btTrade.setPrefHeight(60);
        btTrade.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        btTrade.setOnAction(e -> {
            if(handle_TurningGame.getCurrentPlayer().getOnTradeRequest())
                tradeRequest();
            else 
                trade();
        });
        this.getChildren().add(1, btTrade);
    }
    public void drawEndOfButton(){
        Button btEnd = new Button("End of my turn");
        btEnd.prefWidthProperty().bind(widthProperty().divide(4));
        btEnd.setStyle("-fx-background-color: rgba(20, 20, 20); -fx-border-color: turquoise; -fx-border-width: 5;");
        btEnd.setTextFill(Color.WHITE);
        btEnd.setPrefHeight(60);
        btEnd.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        btEnd.setOnAction(e -> endOfTurn());
        this.getChildren().add(2, btEnd);
    }
    public void drawAuditor(){
        auditorBack = new Rectangle();
        auditor.setFitWidth(40);
        auditor.setFitHeight(40);
        auditor.setSmooth(true);
        auditor.setPreserveRatio(true);
        auditor.setOpacity(0.5);
        auditorBack.setFill(Color.rgb(20, 20, 20));
        auditorBack.setStroke(Color.TURQUOISE);
        auditorBack.setStrokeWidth(5);  
        auditorBack.widthProperty().bind(widthProperty().divide(4));      
        auditorBack.setHeight(55);
        taxText.setFill(Color.WHITE);
        taxText.setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        Bounds imageBounds = auditor.getBoundsInParent();
        Platform.runLater(() -> {
            Bounds b = auditorBack.localToScene(auditorBack.getBoundsInLocal());
            Point2D p = board.getCurrentPane().sceneToLocal(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight() / 2);
            auditor.relocate(p.getX() - auditor.getFitWidth() / 2, p.getY() - auditor.getFitHeight() / 2);
        });
        board.getCurrentPane().widthProperty().addListener(ov -> {
            Platform.runLater(() -> {
                Bounds b = auditorBack.localToScene(auditorBack.getBoundsInLocal());
                Point2D p = board.getCurrentPane().sceneToLocal(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight() / 2);
                auditor.relocate(p.getX() - auditor.getFitWidth() / 2, p.getY() - auditor.getFitHeight() / 2);
            });
        });
        board.getCurrentPane().heightProperty().addListener(ov -> {
            Platform.runLater(() -> {
                Bounds b = auditorBack.localToScene(auditorBack.getBoundsInLocal());
                Point2D p = board.getCurrentPane().sceneToLocal(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight() / 2);
                auditor.relocate(p.getX() - auditor.getFitWidth() / 2, p.getY() - auditor.getFitHeight() / 2);
            });
        });
        auditor.setOnMousePressed(e -> {
            startX = auditor.getLayoutX();
            startY = auditor.getLayoutY();
            offsetX = e.getX();
            offsetY = e.getY();
        });

        auditor.setOnMouseDragged(e -> {
            if(auditor.getOpacity() == 1.0){
                Point2D p = board.getCurrentPane().sceneToLocal(e.getSceneX(), e.getSceneY());
                auditor.relocate(p.getX() - offsetX, p.getY() - offsetY);
            }
        });
        auditor.setOnMouseReleased(e -> {
            if(auditor.getOpacity() == 1.0){
                Bounds auditorBounds = auditor.localToScene(auditor.getBoundsInLocal());
                for (Sector sector: sectors) {
                    Bounds sectorBounds = sector.localToScene(sector.getBoundsInLocal());
                    if (auditorBounds.intersects(sectorBounds)) {
                        if(!sector.getMvpPlayers().isEmpty() || !sector.getUnicornPlayers().isEmpty()){
                            Point2D p = board.getCurrentPane().sceneToLocal(sectorBounds.getMinX() + sectorBounds.getWidth()/2, sectorBounds.getMinY() + sectorBounds.getHeight()/2);
                            auditor.relocate(p.getX() - imageBounds.getWidth()/2, p.getY() - imageBounds.getHeight()/2);
                            auditor.setOpacity(0.5);
                            Platform.runLater(() -> board.getTopSide().drawStatusPanel("the auditor is moved to correct place! go to Tax panel"));
                            taxText.setText("Tax");
                            board.getTopSide().getUndoButton().addStage(sector, handle_TurningGame.getCurrentPlayer());
                            auditorBack.setOnMouseClicked(event -> loseCards());
                        }else{
                            auditor.relocate(startX, startY);
                        }
                        break;
                    }else{
                        auditor.relocate(startX, startY);
                    }
                }
            }
        });
        if(this.getChildren().size() > 3)this.getChildren().remove(3);
        this.getChildren().add(3, new StackPane(auditorBack, taxText));
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public void endOfTurn(){
        if(!handle_TurningGame.isTurning_Game() || !board.getRightSide().getDicesRolled() || moveAuditor || onTrade == handle_TurningGame.getCurrentPlayer().getPlayerNumber()){
            errorSound.stop();
            errorSound.play();
            return;
        }
        handle_TurningGame.Notify();
        board.getRightSide().setDicesRolled(false);
        market.setPrices();
        Platform.runLater(() -> {
            board.getTopSide().drawPlayerBox(handle_TurningGame.getCurrentPlayer().getPlayerNumber());
            board.getTopSide().drawStatusPanel("Player " + handle_TurningGame.getCurrentPlayer().getPlayerNumber() + "!  this is your turn. roll dices");
            board.getRightSide().drawDices();
            board.getRightSide().setSum("");
            board.getRightSide().drawSumOfDices();
            board.getLeftSide().drawMyCards(handle_TurningGame.getCurrentPlayer());
        });
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    private void buy(){
        if(!handle_TurningGame.isTurning_Game() || !board.getRightSide().getDicesRolled() || moveAuditor || onTrade == handle_TurningGame.getCurrentPlayer().getPlayerNumber()){
            errorSound.stop();
            errorSound.play();
            return;
        }
        Player player = handle_TurningGame.getCurrentPlayer();
        FadeTransition fadeIn = new FadeTransition();
        FadeTransition fadeOut = new FadeTransition();
        List<ResourceCard> resources = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
        ScrollPane paneForScroll = new ScrollPane();
        HBox hBox = new HBox(10);
        VBox vBox = new VBox(10);
        TextField textField = new TextField("|");
        textField.setAlignment(Pos.CENTER);
        textField.setEditable(false);
        fadeOut.setNode(board.getCurrentPane());
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeIn.setNode(board.getCurrentPane());
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            board.getCurrentPane().getChildren().clear();
            board.getCurrentPane().setCenter(vBox);
            fadeIn.play();
        });
        for(ResourceCard resource: resources){
            VBox paneForResource = new VBox(8);
            ImageView image = resource.getSymbol();
            image.setFitWidth(400);
            image.setFitHeight(400);
            image.setSmooth(true);
            image.setPreserveRatio(true);
            Text title = new Text();
            Button btBuy = new Button("buy");
            switch(resource.getType()){
                case Capital:
                    price = market.getCards().get(0).getPrice();
                    title.setText(Integer.toString(price) + " Capitals");
                    break;
                case Cloud:
                    price = market.getCards().get(1).getPrice();
                    title.setText(Integer.toString(price) + " Capitals");
                    break;
                case Data:
                    price = market.getCards().get(2).getPrice();
                    title.setText(Integer.toString(price) + " Capitals");
                    break;
                case Patent:
                    price = market.getCards().get(3).getPrice();
                    title.setText(Integer.toString(price) + " Capitals");
                    break;
                case Talent:
                    price = market.getCards().get(4).getPrice();
                    title.setText(Integer.toString(price) + " Capitals");
                    break;
                case Null:
                    break;
            }
            btBuy.setOnAction(e -> {
                if(writingAnimation != null) {
                    writingAnimation.stop();
                }
                if(motivateSign != null) {
                    motivateSign.stop();
                }
                motivateSign = new Timeline(new KeyFrame(Duration.millis(500), event -> {
                    if(textField.getText().charAt(textField.getText().length() - 1) == '|'){
                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1) + " ");
                    }
                    else
                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1) + "|");
                }));
                motivateSign.setCycleCount(Timeline.INDEFINITE);
                writingAnimation = new Timeline(new KeyFrame(Duration.millis(30), event -> {
                    textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                    textField.appendText(Character.toString(status.charAt(index++)) + "|");
                }));
                textField.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
                writingAnimation.setOnFinished(event -> motivateSign.play());
                if(player.getCapitals() >= price){
                    textField.setText("|");
                    status = "Resource '" + resource.getType().name() + "' is bought by you!";
                    writingAnimation.setCycleCount(status.length());
                    writingAnimation.play();
                    player.deleteCapitals(price);
                    player.getMyCards().add(resource);
                    vBox.getChildren().remove(1);
                    vBox.getChildren().add(1, drawResources(resources, player));
                    market.IncreasePrice(resource.getType());
                    Platform.runLater(() -> board.getLeftSide().drawMyCards(handle_TurningGame.getCurrentPlayer()));
                }else{
                    errorSound.stop();
                    errorSound.play();
                }
            });
            title.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
            paneForResource.getChildren().addAll(image, title, btBuy);
            paneForResource.setAlignment(Pos.CENTER);
            paneForResource.setStyle("-fx-border-color: blue");
            hBox.getChildren().add(paneForResource);
        }
        Button btOK = new Button("Ok");
        btOK.setPrefSize(100, 50);
        btOK.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        btOK.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        paneForScroll.setContent(hBox);
        paneForScroll.setPrefSize(800, 500);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField, drawResources(resources, player), paneForScroll, btOK);
        Node oldLeft = board.getCurrentPane().getLeft();
        Node oldCenter = board.getCurrentPane().getCenter();
        Node oldTop = board.getCurrentPane().getTop();
        Node oldRight = board.getCurrentPane().getRight();
        Node oldBottom = board.getCurrentPane().getBottom();
        btOK.setOnAction(e -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                board.getCurrentPane().getChildren().add(auditor);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
        });
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    private void trade(){
        if(!handle_TurningGame.isTurning_Game() || !board.getRightSide().getDicesRolled() || moveAuditor){
            errorSound.stop();
            errorSound.play();
            return;
        }
        onTrade = -1;
        wantedCapitalCount = 0;
        wantedCloudCount = 0;
        wantedDataCount = 0;
        wantedPatentCount = 0;
        wantedTalentCount = 0;
        lostCapitalCount = 0;
        lostCloudCount = 0;
        lostDataCount = 0;
        lostPatentCount = 0;
        lostTalentCount = 0;
        Player player = handle_TurningGame.getCurrentPlayer();
        Button btOk = new Button("OK");
        Button btCancel = new Button("Cancel");
        FadeTransition fadeIn = new FadeTransition();
        FadeTransition fadeOut = new FadeTransition();
        List<ResourceCard> resources = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
        VBox vBox = new VBox(70);
        vBox.setAlignment(Pos.CENTER);
        fadeOut.setNode(board.getCurrentPane());
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeIn.setNode(board.getCurrentPane());
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            board.getCurrentPane().getChildren().clear();
            board.getCurrentPane().setCenter(vBox);
            fadeIn.play();
        });
        TextField textField1 = new TextField();
        textField1.setEditable(false);
        textField1.setAlignment(Pos.CENTER);
        textField1.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        TextField textField2 = new TextField();
        textField2.setEditable(false);
        textField2.setAlignment(Pos.CENTER);
        textField2.setText("Which player do you want to trade with? choose him via clicking on his name");
        textField2.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        HBox paneForPlayers = new HBox(20);
        paneForPlayers.setAlignment(Pos.CENTER);
        VBox playerPane = new VBox(10);
        Rectangle backPlayer = new Rectangle(150, 40);
        backPlayer.setArcWidth(7);
        backPlayer.setArcHeight(7);
        backPlayer.setFill(player.getColor()); 
        backPlayer.setStyle("-fx-stroke: black; -fx-stroke-width: 3");
        Text playerName = new Text("You");
        playerName.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        playerName.setFill(Color.WHITE);
        Rectangle playerBackResources = new Rectangle(150, 250);
        playerBackResources.setArcWidth(7);
        playerBackResources.setArcHeight(7);
        playerBackResources.setFill(player.getColor()); 
        playerBackResources.setStyle("-fx-stroke: black; -fx-stroke-width: 3");
        VBox playerPaneForTextes = new VBox(15);
        playerPaneForTextes.setAlignment(Pos.CENTER);
        for(ResourceCard resource: resources){
            HBox myHBox = new HBox(5);
            myHBox.setAlignment(Pos.CENTER);
            TextField minus = new TextField("-");
            minus.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
            minus.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
            minus.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            minus.setMinWidth(35);
            minus.setPrefWidth(35);
            minus.setMaxWidth(35);
            minus.setMinHeight(35);
            minus.setPrefHeight(35);
            minus.setMaxHeight(35);
            minus.setEditable(false);
            TextField resourceTitle = new TextField();
            resourceTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            resourceTitle.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
            resourceTitle.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            resourceTitle.setMinWidth(85);
            resourceTitle.setPrefWidth(85);
            resourceTitle.setMaxWidth(85);
            resourceTitle.setMinHeight(35);
            resourceTitle.setPrefHeight(35);
            resourceTitle.setMaxHeight(35);
            resourceTitle.setEditable(false);
            minus.setOnMouseClicked(e -> {
                if(onTrade != -1){
                        switch(resource.getType()){
                            case Capital:
                                if(player.getCapitalCount() - lostCapitalCount > 0){
                                    lostCapitalCount++;
                                    resourceTitle.setText("Capital: " + Long.toString(player.getCapitalCount() - lostCapitalCount));
                                }
                                break;
                            case Cloud:
                                if(player.getCloudCount() - lostCloudCount > 0){
                                    lostCloudCount++;
                                    resourceTitle.setText("Cloud: " + Long.toString(player.getCloudCount() - lostCloudCount));
                                }
                                break;
                            case Data:
                                if(player.getDataCount() - lostDataCount > 0){
                                    lostDataCount++;
                                    resourceTitle.setText("Data: " + Long.toString(player.getDataCount() - lostDataCount));
                                }
                                break;
                            case Patent:
                                if(player.getPatentCount() - lostPatentCount > 0){
                                    lostPatentCount++;                                    
                                    resourceTitle.setText("Patent: " + Long.toString(player.getPatentCount() - lostPatentCount));
                                }
                                break;
                            case Talent:
                                if(player.getTalentCount() - lostTalentCount > 0){
                                    lostTalentCount++;
                                    resourceTitle.setText("Talent: " + Long.toString(player.getTalentCount() - lostTalentCount));
                                }
                                break;
                            case Null:
                                break;
                        }
                        textField1.setText("Your resources you want to hand over: ");
                        for(ResourceCard resourceCard: resources){
                            switch(resourceCard.getType()){
                                case Capital:
                                    if(lostCapitalCount > 0 && player.getCapitalCount() - lostCapitalCount >= 0)
                                        textField1.appendText("Capital: " + lostCapitalCount + ", ");
                                    break;
                                case Cloud:
                                    if(lostCloudCount > 0 && player.getCloudCount() - lostCloudCount >= 0)
                                        textField1.appendText("Cloud: " + lostCloudCount + ", ");
                                    break;
                                case Data:
                                    if(lostDataCount > 0 && player.getDataCount() - lostDataCount >= 0)
                                        textField1.appendText("Data: " + lostDataCount + ", ");
                                    break;
                                case Patent:
                                    if(lostPatentCount > 0 && player.getPatentCount() - lostPatentCount >= 0)
                                        textField1.appendText("Patent: " + lostPatentCount + ", ");
                                    break;
                                case Talent:
                                    if(lostTalentCount > 0 && player.getTalentCount() - lostTalentCount >= 0)
                                        textField1.appendText("Talent: " + lostTalentCount + ", ");
                                    break;
                                case Null:
                                    break;
                            }
                        }
                    }
            });
            myHBox.getChildren().addAll(minus, resourceTitle);
            playerPaneForTextes.getChildren().add(myHBox);
            switch(resource.getType()){
                case Capital:
                    resourceTitle.setText("Capital: " + player.getCapitalCount());
                    break;
                case Cloud:
                    resourceTitle.setText("Cloud: " + player.getCloudCount());
                    break;
                case Data:
                    resourceTitle.setText("Data: " + player.getDataCount());
                    break;
                case Patent:
                    resourceTitle.setText("Patent: " + player.getPatentCount());
                    break;
                case Talent:
                    resourceTitle.setText("Talent: " + player.getTalentCount());
                    break;
                case Null:
                    break;
            }
        }
        playerPane.getChildren().addAll(new StackPane(backPlayer, playerName), new StackPane(playerBackResources, playerPaneForTextes));
        paneForPlayers.getChildren().add(playerPane);
        for(Player p: board.getMap().getPlayers()){
            if(p == player)continue;
            wantedCapitalCount = 0;
            wantedCloudCount = 0;
            wantedDataCount = 0;
            wantedPatentCount = 0;
            wantedTalentCount = 0;
            VBox pane = new VBox(10);
            Text pName = new Text("Player " + p.getPlayerNumber());
            pName.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
            pName.setFill(Color.WHITE);
            Rectangle backP = new Rectangle(150, 40);
            backP.setArcWidth(7);
            backP.setArcHeight(7);
            backP.setFill(p.getColor()); 
            backP.setStyle("-fx-stroke: black; -fx-stroke-width: 3");
            backP.setOnMouseClicked(event -> {
                if(onTrade == -1){
                    backP.setStyle("-fx-stroke: rgb(231, 112, 0); -fx-stroke-width: 4");
                    textField1.setText("your resources you want to hand over: ");
                    textField2.setText("player " + p.getPlayerNumber() + "'s resources you want to obtain: ");
                    onTrade = p.getPlayerNumber();
                    btOk.setDisable(false);
                    fromRequest = player;
                    onRequest = p;
                }
            });
            VBox paneForTextes = new VBox(15);
            paneForTextes.setAlignment(Pos.CENTER);
            Rectangle backResources = new Rectangle(150, 250);
            backResources.setArcWidth(7);
            backResources.setArcHeight(7);
            backResources.setFill(p.getColor()); 
            backResources.setStyle("-fx-stroke: black; -fx-stroke-width: 3");
            for(ResourceCard resource: resources){
                HBox myHBox = new HBox(5);
                myHBox.setAlignment(Pos.CENTER);
                TextField plus = new TextField("+");
                plus.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
                plus.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
                plus.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                plus.setEditable(false);
                plus.setMinWidth(35);
                plus.setPrefWidth(35);
                plus.setMaxWidth(35);
                plus.setMinHeight(35);
                plus.setPrefHeight(35);
                plus.setMaxHeight(35);
                TextField resourceTitle = new TextField();
                resourceTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
                resourceTitle.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
                resourceTitle.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                resourceTitle.setMinWidth(85);
                resourceTitle.setPrefWidth(85);
                resourceTitle.setMaxWidth(85);
                resourceTitle.setMinHeight(35);
                resourceTitle.setPrefHeight(35);
                resourceTitle.setMaxHeight(35);
                resourceTitle.setEditable(false);
                plus.setOnMouseClicked(event -> {
                    if(onTrade == p.getPlayerNumber()){
                        switch(resource.getType()){
                            case Capital:
                                if(p.getCapitalCount() - wantedCapitalCount > 0){
                                    wantedCapitalCount++;
                                    resourceTitle.setText("Capital: " + Long.toString(p.getCapitalCount() - wantedCapitalCount));
                                }
                                break;
                            case Cloud:
                                if(p.getCloudCount() - wantedCloudCount > 0){
                                    wantedCloudCount++;
                                    resourceTitle.setText("Cloud: " + Long.toString(p.getCloudCount() - wantedCloudCount));
                                }
                                break;
                            case Data:
                                if(p.getDataCount() - wantedDataCount > 0){
                                    wantedDataCount++;
                                    resourceTitle.setText("Data: " + Long.toString(p.getDataCount() - wantedDataCount));
                                }
                                break;
                            case Patent:
                                if(p.getPatentCount() - wantedPatentCount > 0){
                                    wantedPatentCount++;                                    
                                    resourceTitle.setText("Patent: " + Long.toString(p.getPatentCount() - wantedPatentCount));
                                }
                                break;
                            case Talent:
                                if(p.getTalentCount() - wantedTalentCount > 0){
                                    wantedTalentCount++;
                                    resourceTitle.setText("Talent: " + Long.toString(p.getTalentCount() - wantedTalentCount));
                                }
                                break;
                            case Null:
                                break;
                        }
                        textField2.setText("player " + p.getPlayerNumber() + "'s resources you want to obtain: ");
                        for(ResourceCard resourceCard: resources){
                            switch(resourceCard.getType()){
                                case Capital:
                                    if(wantedCapitalCount > 0 && p.getCapitalCount() - wantedCapitalCount >= 0)
                                        textField2.appendText("Capital: " + wantedCapitalCount + ", ");
                                    break;
                                case Cloud:
                                    if(wantedCloudCount > 0 && p.getCloudCount() - wantedCloudCount >= 0)
                                        textField2.appendText("Cloud: " + wantedCloudCount + ", ");
                                    break;
                                case Data:
                                    if(wantedDataCount > 0 && p.getDataCount() - wantedDataCount >= 0)
                                        textField2.appendText("Data: " + wantedDataCount + ", ");
                                    break;
                                case Patent:
                                    if(wantedPatentCount > 0 && p.getPatentCount() - wantedPatentCount >= 0)
                                        textField2.appendText("Patent: " + wantedPatentCount + ", ");
                                    break;
                                case Talent:
                                    if(wantedTalentCount > 0 && p.getTalentCount() - wantedTalentCount >= 0)
                                        textField2.appendText("Talent: " + wantedTalentCount + ", ");
                                    break;
                                case Null:
                                    break;
                            }
                        }
                    }
                });
                myHBox.getChildren().addAll(plus, resourceTitle);
                paneForTextes.getChildren().add(myHBox);
                switch(resource.getType()){
                    case Capital:
                        resourceTitle.setText("Capital: " + p.getCapitalCount());
                        break;
                    case Cloud:
                        resourceTitle.setText("Cloud: " + p.getCloudCount());
                        break;
                    case Data:
                        resourceTitle.setText("Data: " + p.getDataCount());
                        break;
                    case Patent:
                        resourceTitle.setText("Patent: " + p.getPatentCount());
                        break;
                    case Talent:
                        resourceTitle.setText("Talent: " + p.getTalentCount());
                        break;
                    case Null:
                        break;
                }
            }
            pane.getChildren().addAll(new StackPane(backP, pName), new StackPane(backResources, paneForTextes));
            paneForPlayers.getChildren().add(pane);
        }
        HBox paneForButtons = new HBox(10);
        paneForButtons.setAlignment(Pos.CENTER);
        btOk.setPrefSize(100, 50);
        btOk.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        btOk.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        btOk.setDisable(true);
        btCancel.setPrefSize(100, 50);
        btCancel.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        btCancel.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        paneForButtons.getChildren().addAll(btCancel, btOk);
        vBox.getChildren().addAll(textField1, textField2, paneForPlayers, paneForButtons);
        Node oldLeft = board.getCurrentPane().getLeft();
        Node oldCenter = board.getCurrentPane().getCenter();
        Node oldTop = board.getCurrentPane().getTop();
        Node oldRight = board.getCurrentPane().getRight();
        Node oldBottom = board.getCurrentPane().getBottom();
        btOk.setOnAction(e -> {
            wantedResources.add(wantedCapitalCount);
            wantedResources.add(wantedCloudCount);
            wantedResources.add(wantedDataCount);
            wantedResources.add(wantedPatentCount);
            wantedResources.add(wantedTalentCount);
            lostResources.add(lostCapitalCount);
            lostResources.add(lostCloudCount);
            lostResources.add(lostDataCount);
            lostResources.add(lostPatentCount);
            lostResources.add(lostTalentCount);
            onRequest.setOnTradeRequest(true);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                board.getCurrentPane().getChildren().add(auditor);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
        });
        btCancel.setOnAction(e -> {
            fromRequest = null;
            onRequest = null;
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                board.getCurrentPane().getChildren().add(auditor);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
        });
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public void tradeRequest(){
        if(!handle_TurningGame.isTurning_Game() || !board.getRightSide().getDicesRolled() || moveAuditor){
            errorSound.stop();
            errorSound.play();
            return;
        }
        Player p = handle_TurningGame.getCurrentPlayer();
        FadeTransition fadeIn = new FadeTransition();
        FadeTransition fadeOut = new FadeTransition();
        VBox vBox = new VBox(120);
        vBox.setAlignment(Pos.CENTER);
        fadeOut.setNode(board.getCurrentPane());
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeIn.setNode(board.getCurrentPane());
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            board.getCurrentPane().getChildren().clear();
            board.getCurrentPane().setCenter(vBox);
            fadeIn.play();
        });
        TextField textField1 = new TextField("Player " + Integer.toString(fromRequest.getPlayerNumber()) +"'s resources he wants to hand over: ");
        TextField textField2 = new TextField("Your resources he wants you to hand over: ");
        TextField textField3 = new TextField("Do you accept this trade?");
        textField1.setEditable(false);
        textField1.setAlignment(Pos.CENTER);
        textField1.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        textField2.setEditable(false);
        textField2.setAlignment(Pos.CENTER);
        textField2.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        textField3.setEditable(false);
        textField3.setAlignment(Pos.CENTER);
        textField3.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
        Button btYes = new Button("Yes");
        Button btNo = new Button("No");
        btYes.setPrefSize(100, 40);
        btYes.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        btNo.setPrefSize(100, 40);
        btNo.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        if(lostResources.get(0) > 0)textField1.appendText("Capital: " + lostResources.get(0) + ", ");
        if(lostResources.get(1) > 0)textField1.appendText("Cloud: " + lostResources.get(1) + ", ");
        if(lostResources.get(2) > 0)textField1.appendText("Data: " + lostResources.get(2) + ", ");
        if(lostResources.get(3) > 0)textField1.appendText("Patent: " + lostResources.get(3) + ", ");
        if(lostResources.get(4) > 0)textField1.appendText("Talent: " + lostResources.get(4) + ", ");
        if(wantedResources.get(0) > 0)textField2.appendText("Capital: " + wantedResources.get(0) + ", ");
        if(wantedResources.get(1) > 0)textField2.appendText("Cloud: " + wantedResources.get(1) + ", ");
        if(wantedResources.get(2) > 0)textField2.appendText("Data: " + wantedResources.get(2) + ", ");
        if(wantedResources.get(3) > 0)textField2.appendText("Patent: " + wantedResources.get(3) + ", ");
        if(wantedResources.get(4) > 0)textField2.appendText("Talent: " + wantedResources.get(4) + ", ");
        wantedCapitalCount = 0;
        wantedCloudCount = 0;
        wantedDataCount = 0;
        wantedPatentCount = 0;
        wantedTalentCount = 0;
        lostCapitalCount = 0;
        lostCloudCount = 0;
        lostDataCount = 0;
        lostPatentCount = 0;
        lostTalentCount = 0;
        p.setOnTradeRequest(false);
        textField3.setMinWidth(210);
        textField3.setPrefWidth(210);
        textField3.setMaxWidth(210);
        textField3.setMinHeight(50);
        textField3.setPrefHeight(50);
        textField3.setMaxHeight(50);
        HBox paneForButtons = new HBox(10);
        paneForButtons.getChildren().addAll(btYes, btNo);
        paneForButtons.setAlignment(Pos.CENTER);
        VBox pane = new VBox(15);
        pane.getChildren().addAll(textField3, paneForButtons);
        pane.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField1, textField2, pane);
        Node oldLeft = board.getCurrentPane().getLeft();
        Node oldCenter = board.getCurrentPane().getCenter();
        Node oldTop = board.getCurrentPane().getTop();
        Node oldRight = board.getCurrentPane().getRight();
        Node oldBottom = board.getCurrentPane().getBottom();
        btYes.setOnAction(e -> {
            for(int i=0; i<wantedResources.get(0); i++){
                fromRequest.getMyCards().add(new Capital());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Capital){
                        p.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<wantedResources.get(1); i++){
                fromRequest.getMyCards().add(new Cloud());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Cloud){
                        p.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<wantedResources.get(2); i++){
                fromRequest.getMyCards().add(new Data());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Data){
                        p.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<wantedResources.get(3); i++){
                fromRequest.getMyCards().add(new Patent());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Patent){
                        p.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<wantedResources.get(4); i++){
                fromRequest.getMyCards().add(new Talent());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Talent){
                        p.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<lostResources.get(0); i++){
                p.getMyCards().add(new Capital());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Capital){
                        fromRequest.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<lostResources.get(1); i++){
                p.getMyCards().add(new Cloud());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Cloud){
                        fromRequest.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<lostResources.get(2); i++){
                p.getMyCards().add(new Data());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Data){
                        fromRequest.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<lostResources.get(3); i++){
                p.getMyCards().add(new Patent());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Patent){
                        fromRequest.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            for(int i=0; i<lostResources.get(4); i++){
                p.getMyCards().add(new Talent());
                for(ResourceCard resource: p.getMyCards()){
                    if(resource instanceof Talent){
                        fromRequest.getMyCards().remove(resource);
                        break;
                    }
                }
            }
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
                lostResources.clear();
                wantedResources.clear();
                onRequest = null;
                onTrade = -1;
                Platform.runLater(() -> {
                    board.getTopSide().drawStatusPanel("Player " + p.getPlayerNumber() + "! Do your turn");
                    board.getLeftSide().drawMyCards(p);
                    board.getCurrentPane().getChildren().add(auditor);
                });
            });
        });
        btNo.setOnAction(e -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
                lostResources.clear();
                wantedResources.clear();
                onRequest = null;
                onTrade = -1;
                Platform.runLater(() -> {
                    board.getTopSide().drawStatusPanel("Player " + p.getPlayerNumber() + "! Do your turn");
                    board.getCurrentPane().getChildren().add(auditor);
                });
            });
        });
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public HBox drawResources(List<ResourceCard> resources, Player player){
        HBox myResources = new HBox(10);
        myResources.setAlignment(Pos.CENTER);
        Rectangle backText = new Rectangle(128, 30);
        backText.setArcWidth(7);
        backText.setArcHeight(7);
        Text text = new Text("your resources:");
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        text.setFill(Color.WHITE);
        backText.setStyle("-fx-fill: rgba(77, 77, 77);");
        myResources.getChildren().add(new StackPane(backText, text));
        for(ResourceCard resource: resources){
            backText = new Rectangle(80, 30);
            backText.setArcWidth(7);
            backText.setArcHeight(7);
            text = new Text("");
            text.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
            text.setFill(Color.WHITE);
            switch(resource.getType()){
                case Capital:
                    backText.setFill(resource.getColor());
                    text.setText("Capital: " + Long.toString(player.getCapitalCount()));
                    break;
                case Cloud:
                    backText.setFill(resource.getColor());
                    text.setText("Cloud: " + Long.toString(player.getCloudCount()));
                    break;
                case Data:
                    backText.setFill(resource.getColor());
                    text.setText("Data: " + Long.toString(player.getDataCount()));
                    break;
                case Patent:
                    backText.setFill(resource.getColor());
                    text.setText("Patent: " + Long.toString(player.getPatentCount()));
                    break;
                case Talent:
                    backText.setFill(resource.getColor());
                    text.setText("Talent: " + Long.toString(player.getTalentCount()));
                    break;
            }
            myResources.getChildren().add(new StackPane(backText, text));
        }
        return myResources;
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public void ReDrawAuditor(){
        Platform.runLater(() -> {
            Bounds b = auditorBack.localToScene(auditorBack.getBoundsInLocal());
            Point2D p = board.getCurrentPane().sceneToLocal(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight() / 2);
            auditor.relocate(p.getX() - auditor.getFitWidth() / 2, p.getY() - auditor.getFitHeight() / 2);
        });
        auditorBack.setOnMouseClicked(e -> {});
        auditor.setOpacity(1.0);
    }
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public void loseCards(){
        FadeTransition fadeIn = new FadeTransition();
        FadeTransition fadeOut = new FadeTransition();
        VBox vBox = new VBox(120);
        vBox.setAlignment(Pos.CENTER);
        fadeOut.setNode(board.getCurrentPane());
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeIn.setNode(board.getCurrentPane());
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            board.getCurrentPane().getChildren().clear();
            board.getCurrentPane().setCenter(vBox);
            fadeIn.play();
        });
        List<ResourceCard> resources = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
        TextField textField = new TextField("Please hand over half of your resources");
        textField.setEditable(false);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font("Roboto", FontWeight.BOLD, 17));
        Button btDone = new Button("Done!");
        btDone.setDisable(true);
        btDone.setPrefSize(120, 70);
        btDone.setStyle("fx-border-color: turquoise; -fx-border-width: 5;");
        HBox paneForPlayers = new HBox(20);
        paneForPlayers.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField, paneForPlayers, btDone);
        onTaxResources.add(0);
        onTaxResources.add(0);
        onTaxResources.add(0);
        onTaxResources.add(0);
        for(int i=0; i<board.getMap().getPlayers().size(); i++)
            lastResourcesCount.add(board.getMap().getPlayers().get(i).getMyCards().size());
        for(Player player: board.getMap().getPlayers()){
            if(player.getMyCards().size() <= 7)continue;
            else onTax++;
            VBox paneForTitles = new VBox(10);
            TextField number = new TextField("Player " + player.getPlayerNumber() + ": 0");
            number.setAlignment(Pos.CENTER);
            paneForTitles.getChildren().add(number);
            for(ResourceCard resource: resources){
                HBox myHBox = new HBox(5);
                myHBox.setAlignment(Pos.CENTER);
                TextField minus = new TextField("-");
                minus.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
                minus.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
                minus.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                minus.setEditable(false);
                minus.setMinWidth(35);
                minus.setPrefWidth(35);
                minus.setMaxWidth(35);
                minus.setMinHeight(35);
                minus.setPrefHeight(35);
                minus.setMaxHeight(35);
                TextField resourceTitle = new TextField();
                resourceTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
                resourceTitle.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-text-fill: white;");
                resourceTitle.setBackground(new Background(new BackgroundFill(resource.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                resourceTitle.setMinWidth(85);
                resourceTitle.setPrefWidth(85);
                resourceTitle.setMaxWidth(85);
                resourceTitle.setMinHeight(35);
                resourceTitle.setPrefHeight(35);
                resourceTitle.setMaxHeight(35);
                resourceTitle.setEditable(false);
                switch(resource.getType()){
                    case Capital:
                        resourceTitle.setText("Capital: " + player.getCapitalCount());
                        break;
                    case Cloud:
                        resourceTitle.setText("Cloud: " + player.getCloudCount());
                        break;
                    case Data:
                        resourceTitle.setText("Data: " + player.getDataCount());
                        break;
                    case Patent:
                        resourceTitle.setText("Patent: " + player.getPatentCount());
                        break;
                    case Talent:
                        resourceTitle.setText("Talent: " + player.getTalentCount());
                        break;
                    case Null:
                        break;
                }
                minus.setOnMouseClicked(event -> {
                    if(!number.getText().equals("It's done!")){
                        switch(resource.getType()){
                            case Capital:
                                if(player.getCapitalCount() > 0){
                                    for(ResourceCard resourceCard: player.getMyCards()){
                                        if(resourceCard instanceof Capital){
                                            player.getMyCards().remove(resourceCard);
                                            onTaxResources.set(player.getPlayerNumber() - 1, onTaxResources.get(player.getPlayerNumber() - 1) + 1);
                                            if(onTaxResources.get(player.getPlayerNumber() - 1) == lastResourcesCount.get(player.getPlayerNumber() - 1)/2){
                                                number.setText("It's done!");
                                                if(--onTax == 0){
                                                    btDone.setDisable(false);
                                                }
                                            }
                                            else
                                                number.setText("Player " + player.getPlayerNumber() + ": " + Integer.toString(onTaxResources.get(player.getPlayerNumber() - 1)));
                                            break;
                                        }
                                    }
                                    resourceTitle.setText("Capital: " + Long.toString(player.getCapitalCount()));
                                }
                                break;
                            case Cloud:
                                if(player.getCloudCount() > 0){
                                    for(ResourceCard resourceCard: player.getMyCards()){
                                        if(resourceCard instanceof Cloud){
                                            player.getMyCards().remove(resourceCard);
                                            onTaxResources.set(player.getPlayerNumber() - 1, onTaxResources.get(player.getPlayerNumber() - 1) + 1);
                                            if(onTaxResources.get(player.getPlayerNumber() - 1) == lastResourcesCount.get(player.getPlayerNumber() - 1)/2){
                                                number.setText("It's done!");
                                                if(--onTax == 0){
                                                    btDone.setDisable(false);
                                                }
                                            }
                                            else
                                                number.setText("Player " + player.getPlayerNumber() + ": " + Integer.toString(onTaxResources.get(player.getPlayerNumber() - 1)));
                                            break;
                                        }
                                    }
                                    resourceTitle.setText("Cloud: " + Long.toString(player.getCloudCount()));
                                }
                                break;
                            case Data:
                                if(player.getDataCount() > 0){
                                    for(ResourceCard resourceCard: player.getMyCards()){
                                        if(resourceCard instanceof Data){
                                            player.getMyCards().remove(resourceCard);
                                            onTaxResources.set(player.getPlayerNumber() - 1, onTaxResources.get(player.getPlayerNumber() - 1) + 1);
                                            if(onTaxResources.get(player.getPlayerNumber() - 1) == lastResourcesCount.get(player.getPlayerNumber() - 1)/2){
                                                number.setText("It's done!");
                                                if(--onTax == 0){
                                                    btDone.setDisable(false);
                                                }
                                            }
                                            else
                                                number.setText("Player " + player.getPlayerNumber() + ": " + Integer.toString(onTaxResources.get(player.getPlayerNumber() - 1)));
                                            break;
                                        }
                                    }
                                    resourceTitle.setText("Data: " + Long.toString(player.getDataCount()));
                                }
                                break;
                            case Patent:
                                if(player.getPatentCount() > 0){
                                    for(ResourceCard resourceCard: player.getMyCards()){
                                        if(resourceCard instanceof Patent){
                                            player.getMyCards().remove(resourceCard);
                                            onTaxResources.set(player.getPlayerNumber() - 1, onTaxResources.get(player.getPlayerNumber() - 1) + 1);
                                            if(onTaxResources.get(player.getPlayerNumber() - 1) == lastResourcesCount.get(player.getPlayerNumber() - 1)/2){
                                                number.setText("It's done!");
                                                if(--onTax == 0){
                                                    btDone.setDisable(false);
                                                }
                                            }
                                            else
                                                number.setText("Player " + player.getPlayerNumber() + ": " + Integer.toString(onTaxResources.get(player.getPlayerNumber() - 1)));
                                            break;
                                        }
                                    }
                                    resourceTitle.setText("Patent: " + Long.toString(player.getPatentCount()));
                                }
                                break;
                            case Talent:
                                if(player.getTalentCount() > 0){
                                    for(ResourceCard resourceCard: player.getMyCards()){
                                        if(resourceCard instanceof Talent){
                                            player.getMyCards().remove(resourceCard);
                                            onTaxResources.set(player.getPlayerNumber() - 1, onTaxResources.get(player.getPlayerNumber() - 1) + 1);
                                            if(onTaxResources.get(player.getPlayerNumber() - 1) == lastResourcesCount.get(player.getPlayerNumber() - 1)/2){
                                                number.setText("It's done!");
                                                if(--onTax == 0){
                                                    btDone.setDisable(false);
                                                }
                                            }
                                            else
                                                number.setText("Player " + player.getPlayerNumber() + ": " + Integer.toString(onTaxResources.get(player.getPlayerNumber() - 1)));
                                            break;
                                        }
                                    }
                                    resourceTitle.setText("Talent: " + Long.toString(player.getTalentCount()));
                                }
                                break;
                            case Null:
                                break;
                        }
                    }
                });
                myHBox.getChildren().addAll(minus, resourceTitle);
                paneForTitles.getChildren().add(myHBox);
            }
            paneForPlayers.getChildren().add(paneForTitles);
        }
        if(onTax == 0){
            btDone.setDisable(false);
        }
        Node oldLeft = board.getCurrentPane().getLeft();
        Node oldCenter = board.getCurrentPane().getCenter();
        Node oldTop = board.getCurrentPane().getTop();
        Node oldRight = board.getCurrentPane().getRight();
        Node oldBottom = board.getCurrentPane().getBottom();
        btDone.setOnAction(e -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                board.getCurrentPane().setLeft(oldLeft);
                board.getCurrentPane().setCenter(oldCenter);
                board.getCurrentPane().setTop(oldTop);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setRight(oldRight);
                board.getCurrentPane().setBottom(oldBottom);
                board.getCurrentPane().getChildren().add(auditor);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
                onTaxResources.clear();
                moveAuditor = false;
                onTax = 0;
                taxText.setText("");                
                if(handle_TurningGame.getCurrentPlayer().getOnTradeRequest())
                    Platform.runLater(() -> board.getTopSide().drawStatusPanel("Player " + handle_TurningGame.getCurrentPlayer().getPlayerNumber() + "! you have a Trade request. check trade panel"));
                else 
                    Platform.runLater(() -> board.getTopSide().drawStatusPanel("Player " + handle_TurningGame.getCurrentPlayer().getPlayerNumber() + "! Do your turn"));
                Platform.runLater(() -> board.getLeftSide().drawMyCards(handle_TurningGame.getCurrentPlayer()));
            });
        });
    }
    public void deleteAuditor(Sector sector){
        Platform.runLater(() -> {
            Bounds b = auditorBack.localToScene(auditorBack.getBoundsInLocal());
            Point2D p = board.getCurrentPane().sceneToLocal(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight() / 2);
            auditor.relocate(p.getX() - auditor.getFitWidth() / 2, p.getY() - auditor.getFitHeight() / 2);
        });
        auditor.setOpacity(1.0);
        sector.setHasAuditor(true);
        taxText.setText("");
        auditorBack.setOnMouseClicked(e -> {});
        Platform.runLater(() -> {
            board.getTopSide().drawStatusPanel("sum equals to 7. move auditor piece to any possible sector");
        });
    }
    public int getOnTrade(){
        return onTrade;
    }
    public ImageView getAuditor(){
        return auditor;
    }

    public boolean getMoveAuditor() {
        return moveAuditor;
    }

    public void setMoveAuditor(boolean moveAuditor) {
        this.moveAuditor = moveAuditor;
    }
}
