package util;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import type.*;
import graph.*; 
import card.*; 

public class drawMap extends Pane{
    private Node[][] nodes;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/bg.png")));
    private DoubleProperty hGap = new SimpleDoubleProperty(50);
    private DoubleProperty vGap = new SimpleDoubleProperty(50);
    FadeTransition fadeTransition = new FadeTransition();
    private int numberOfPlayers;
    private Handle_PreGame handle_preGame;
    private Handle_TurningGame handle_TurningGame;
    private ArrayList<Sector> sectors = new ArrayList<>();
    public drawMap(BorderPane currentPane, int numberOfPlayers, ArrayList<Player> players){
        this.numberOfPlayers = numberOfPlayers;
        this.handle_TurningGame = new Handle_TurningGame(players);
        handle_preGame = new Handle_PreGame(players, handle_TurningGame);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setDuration(Duration.millis(6000));
        fadeTransition.setNode(currentPane);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
        nodes = new Node[6][6];
        drawGraph();
    }
    public void drawGraph(){
        hGap.bind(widthProperty().divide(6.7));
        vGap.bind(heightProperty().divide(6.7));
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setSmooth(true);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.getChildren().add(background);
        List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Null(), new Patent(), new Talent());
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                Sector sector = new Sector(cards.get(new Random().nextInt(6)), j * 50 + 50, i * 50 + 50, j, i);
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                sectors.add(sector);
                this.getChildren().add(sector);
            }
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                nodes[i][j] = new Node(j, i, handle_preGame, handle_TurningGame, sectors);
                nodes[i][j].layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                nodes[i][j].layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                nodes[i][j].getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        ArrayList<Edge> edges = new ArrayList<>();
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                Edge e = new Edge(nodes[i][j], nodes[i][j+1], handle_preGame, handle_TurningGame, i*5+j);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<6; j++){
            for(int i=0; i<5; i++){
                Edge e = new Edge(nodes[i][j], nodes[i+1][j], handle_preGame, handle_TurningGame, 30 + j*5+i);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i+1)));
            }   
        }    
        for(Edge e: edges){
            e.setEdges(edges);
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                Node n = nodes[i][j];
                n.setNodes(nodes);
                this.getChildren().add(n);
            }
        }
    }
}
