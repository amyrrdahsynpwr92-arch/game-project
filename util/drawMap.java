package util;
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

public class drawMap extends Pane{
    private node[][] nodes;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/bg.png")));
    private DoubleProperty hGap = new SimpleDoubleProperty(50);
    private DoubleProperty vGap = new SimpleDoubleProperty(50);
    FadeTransition fadeTransition = new FadeTransition();
    public drawMap(BorderPane currentPane){
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setDuration(Duration.millis(4000));
        fadeTransition.setNode(currentPane);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
        nodes = new node[6][6];
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
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                Sector sector = new Sector(SectorType.values()[new Random().nextInt(6)], j * 50 + 50, i * 50 + 50);
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                this.getChildren().add(sector);
            }
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                nodes[i][j] = new node(j, i, 350, 350);
                nodes[i][j].layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                nodes[i][j].layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                nodes[i][j].getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                edge e = new edge(nodes[i][j], nodes[i][j+1]);
                this.getChildren().add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<6; j++){
            for(int i=0; i<5; i++){
                edge e = new edge(nodes[i][j], nodes[i+1][j]);
                this.getChildren().add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i+1)));
            }   
        }    
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                node n = nodes[i][j];
                this.getChildren().add(n);
            }
        }
    }
}
